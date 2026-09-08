"""
command_discovery.py - generic static command/string reference discovery.

Given analyst-supplied regular expressions, this module finds matching
strings in an ELF shared library, maps them to virtual addresses, and looks
for PC-relative references plus nearby direct branches/calls. It emits
candidate records only; it does not claim that a string is a handler and it
does not modify or execute the target binary.

This is intentionally target-neutral. Pass the command families relevant to
a binary you are authorized to analyze, for example a project-specific
prefix regex from the command line.
"""

import argparse
import json
import re
from pathlib import Path
from typing import Dict, List, Optional, Sequence, Tuple

import capstone
from elftools.elf.elffile import ELFFile
from elftools.elf.sections import SymbolTableSection


_ARCH_MAP = {
    "EM_AARCH64": (capstone.CS_ARCH_ARM64, capstone.CS_MODE_ARM),
    "EM_ARM": (capstone.CS_ARCH_ARM, capstone.CS_MODE_ARM),
    "EM_386": (capstone.CS_ARCH_X86, capstone.CS_MODE_32),
    "EM_X86_64": (capstone.CS_ARCH_X86, capstone.CS_MODE_64),
}
_EXECUTABLE_FLAG = 0x4
_ASCII_RUN = re.compile(rb"[\x20-\x7e]{4,}")
_BRANCH_MNEMONICS = {
    "b", "bl", "br", "blr", "bx", "blx", "call", "jmp", "jal", "jalr"
}


def _is_executable(section) -> bool:
    try:
        return bool(section["sh_flags"] & _EXECUTABLE_FLAG)
    except Exception:
        return False


def _compile_patterns(patterns: Sequence[str], ignore_case: bool) -> List[re.Pattern]:
    flags = re.IGNORECASE if ignore_case else 0
    return [re.compile(pattern, flags) for pattern in patterns]


def _collect_matching_strings(elf: ELFFile, patterns: Sequence[re.Pattern]) -> List[dict]:
    matches = []
    for section in elf.iter_sections():
        try:
            if section["sh_type"] == "SHT_NOBITS" or not section["sh_size"]:
                continue
            data = section.data()
        except Exception:
            continue
        for match in _ASCII_RUN.finditer(data):
            value = match.group().decode("ascii", errors="replace")
            if not any(pattern.search(value) for pattern in patterns):
                continue
            offset = int(section["sh_offset"]) + match.start()
            address = int(section["sh_addr"]) + match.start()
            matches.append({
                "command": value,
                "file_offset": hex(offset),
                "address": hex(address),
                "section": section.name,
            })
    return matches


def _function_names(elf: ELFFile) -> Dict[int, str]:
    names = {}
    for section in elf.iter_sections():
        if not isinstance(section, SymbolTableSection):
            continue
        for symbol in section.iter_symbols():
            if symbol.name and symbol["st_info"].type == "STT_FUNC":
                names.setdefault(int(symbol["st_value"]), symbol.name)
    return names


def _immediate_target(insn) -> Optional[int]:
    try:
        for operand in insn.operands:
            if operand.type == capstone.arm64.ARM64_OP_IMM:
                return int(operand.imm)
            if operand.type == capstone.x86.X86_OP_IMM:
                return int(operand.imm)
    except AttributeError:
        # Architectures without detail constants still get a conservative
        # fallback from the operand text below.
        pass
    text = insn.op_str.strip().split(",", 1)[0].strip().lstrip("#")
    try:
        return int(text, 0)
    except ValueError:
        return None


def _aarch64_resolved_reference(instructions: Sequence, index: int) -> Tuple[Optional[int], str]:
    """Resolve the common ADRP plus ADD/LDR literal-address sequence."""
    adrp = instructions[index]
    page = _immediate_target(adrp)
    if page is None or not adrp.operands:
        return page, "adrp-page" if page is not None else ""
    try:
        destination = adrp.operands[0].reg
    except AttributeError:
        return page, "adrp-page"

    # A compiler normally consumes the page base within a handful of
    # instructions. Stop at a control-flow instruction or a write to the
    # tracked register so we do not associate an unrelated later value.
    for following in instructions[index + 1:index + 9]:
        mnemonic = following.mnemonic.lower()
        if mnemonic in _BRANCH_MNEMONICS or mnemonic in {"ret", "brk"}:
            break
        try:
            if mnemonic == "add" and len(following.operands) >= 3:
                dst, src, immediate = following.operands[:3]
                if (dst.reg == destination and src.reg == destination and
                        immediate.type == capstone.arm64.ARM64_OP_IMM):
                    return page + int(immediate.imm), "adrp+add"
            if mnemonic in {"ldr", "ldrsw"} and len(following.operands) >= 2:
                destination_operand, memory = following.operands[:2]
                if (memory.type == capstone.arm64.ARM64_OP_MEM and
                        memory.mem.base == destination):
                    return page + int(memory.mem.disp), "adrp+ldr"
            # If the first operand writes the tracked register, its old page
            # value is no longer reliable.
            if following.operands and following.operands[0].type == capstone.arm64.ARM64_OP_REG:
                if following.operands[0].reg == destination and mnemonic not in {"add", "ldr", "ldrsw"}:
                    break
        except AttributeError:
            break
    return page, "adrp-page"


def _reference_target(insn, machine: str, instructions: Sequence = (), index: int = -1) -> Tuple[Optional[int], str]:
    """Return (target, kind) for common literal-address instructions."""
    mnemonic = insn.mnemonic.lower()
    if machine == "EM_AARCH64":
        if mnemonic == "adr":
            target = _immediate_target(insn)
            return target, "adr" if target is not None else ""
        if mnemonic == "adrp":
            if index >= 0 and instructions:
                return _aarch64_resolved_reference(instructions, index)
            target = _immediate_target(insn)
            return target, "adrp-page" if target is not None else ""
    elif machine in ("EM_386", "EM_X86_64") and mnemonic == "lea":
        try:
            for operand in insn.operands:
                if operand.type == capstone.x86.X86_OP_MEM:
                    mem = operand.mem
                    if mem.base == (capstone.x86.X86_REG_RIP if machine == "EM_X86_64" else capstone.x86.X86_REG_EIP):
                        return insn.address + insn.size + mem.disp, "rip-relative"
        except AttributeError:
            pass
    elif machine == "EM_ARM" and mnemonic == "adr":
        target = _immediate_target(insn)
        return target, "adr" if target is not None else ""
    return None, ""


def _branch_target(insn) -> Optional[int]:
    if insn.mnemonic.lower() not in _BRANCH_MNEMONICS:
        return None
    return _immediate_target(insn)


def _disassemble(elf: ELFFile):
    machine = elf["e_machine"]
    if machine not in _ARCH_MAP:
        return machine, []
    arch, mode = _ARCH_MAP[machine]
    decoder = capstone.Cs(arch, mode)
    decoder.detail = True
    instructions = []
    for section in elf.iter_sections():
        if not _is_executable(section):
            continue
        try:
            code = section.data()
            base = int(section["sh_addr"])
        except Exception:
            continue
        instructions.extend(decoder.disasm(code, base))
    instructions.sort(key=lambda instruction: instruction.address)
    return machine, instructions


def _matches_reference(target: int, string_address: int, machine: str, kind: str) -> bool:
    if kind == "adrp-page" and machine == "EM_AARCH64":
        return (target & ~0xFFF) == (string_address & ~0xFFF)
    return target == string_address


def _nearby_branches(instructions: Sequence, index: int, window: int,
                     names: Dict[int, str]) -> List[dict]:
    start = max(0, index - window)
    end = min(len(instructions), index + window + 1)
    branches = []
    for instruction in instructions[start:end]:
        target = _branch_target(instruction)
        if target is None:
            continue
        record = {
            "address": hex(instruction.address),
            "mnemonic": instruction.mnemonic,
            "operands": instruction.op_str,
            "target": hex(target),
        }
        if target in names:
            record["symbol"] = names[target]
        branches.append(record)
    return branches


def _make_record(command: dict, references: List[dict]) -> dict:
    branch_targets = []
    for reference in references:
        for branch in reference["nearby_branches"]:
            if branch not in branch_targets:
                branch_targets.append(branch)
    exact = any(ref["reference_kind"] in ("adr", "adrp+add", "adrp+ldr", "rip-relative")
                for ref in references)
    confidence = "high" if exact and branch_targets else "medium" if references else "low"
    entry = references[0]["entry"] if references else None
    return {
        "Feature": command["command"],
        "Entry": entry,
        "Command": command["command"],
        "Branch": branch_targets,
        "Lookup": references,
        "Handler": None,
        "Important globals": [],
        "Vtable calls": [],
        "Game-state reads": [],
        "Game-state writes": [],
        "Confidence": confidence,
        "Status": "candidate - manual verification required",
    }


def discover_commands(so_path: str, patterns: Sequence[str], window: int = 96,
                      ignore_case: bool = False) -> dict:
    """Find matching strings and code references in one ELF library."""
    path = Path(so_path)
    patterns_compiled = _compile_patterns(patterns, ignore_case)
    try:
        with path.open("rb") as stream:
            if stream.read(4) != b"\x7fELF":
                return {"library": path.name, "error": "not a valid ELF file"}
            stream.seek(0)
            elf = ELFFile(stream)
            machine, instructions = _disassemble(elf)
            if machine not in _ARCH_MAP:
                return {"library": path.name, "error": f"unsupported machine type: {machine}"}
            strings = _collect_matching_strings(elf, patterns_compiled)
            names = _function_names(elf)

            commands = []
            for command in strings:
                exact_refs = []
                page_refs = []
                command_address = int(command["address"], 16)
                for index, instruction in enumerate(instructions):
                    target, kind = _reference_target(instruction, machine, instructions, index)
                    if target is None or not _matches_reference(target, command_address, machine, kind):
                        continue
                    reference = {
                        "entry": hex(instruction.address),
                        "reference_kind": kind,
                        "instruction": instruction.mnemonic,
                        "operands": instruction.op_str,
                        "nearby_branches": _nearby_branches(instructions, index, window, names),
                    }
                    if kind == "adrp-page":
                        page_refs.append(reference)
                    else:
                        exact_refs.append(reference)
                # A page-only ADRP is ambiguous when several strings share a
                # page. Prefer exact ADRP+ADD/ADR/RIP-relative evidence and
                # retain page evidence only when no exact reference exists.
                refs = exact_refs or page_refs
                commands.append({**command, "references": refs, "record": _make_record(command, refs)})

            return {
                "library": path.name,
                "machine": machine,
                "patterns": list(patterns),
                "command_count": len(commands),
                "commands": commands,
            }
    except Exception as exc:
        return {"library": path.name, "error": f"analysis failed: {exc}"}


def write_command_report(so_path: str, out_dir: str, patterns: Sequence[str],
                         window: int = 96, ignore_case: bool = False) -> dict:
    out = Path(out_dir)
    out.mkdir(parents=True, exist_ok=True)
    report = discover_commands(so_path, patterns, window, ignore_case)
    output = out / f"{Path(so_path).name}.command_discovery.json"
    output.write_text(json.dumps(report, indent=2) + "\n")
    report["output"] = str(output)
    return report


def _cli() -> None:
    parser = argparse.ArgumentParser(description="discover analyst-supplied command strings in an ELF")
    parser.add_argument("library")
    parser.add_argument("out_dir")
    parser.add_argument("--pattern", action="append", required=True,
                        help="regular expression to match a string; repeat for multiple families")
    parser.add_argument("--window", type=int, default=96,
                        help="instructions before/after a reference to inspect")
    parser.add_argument("--ignore-case", action="store_true")
    args = parser.parse_args()
    result = write_command_report(args.library, args.out_dir, args.pattern,
                                  args.window, args.ignore_case)
    print(json.dumps({key: value for key, value in result.items() if key != "commands"}, indent=2))


if __name__ == "__main__":
    _cli()
