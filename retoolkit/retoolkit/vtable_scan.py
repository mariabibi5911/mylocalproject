"""
vtable_scan.py - generic vtable/function-pointer candidate scanning.

The first version only inspected already-relocated pointer bytes. Shared
libraries commonly store zero/addend values plus dynamic relocations, so this
version resolves relocation targets as well. It remains a heuristic: a
candidate may be a C++ vtable, a jump table, or another function-pointer
array and must be confirmed by the analyst.
"""

import json
import struct
from pathlib import Path

from elftools.elf.elffile import ELFFile


_EXECUTABLE_FLAG = 0x4
_DATA_SECTION_NAMES = (".data.rel.ro", ".rodata", ".data")


def _exec_ranges(elf: ELFFile):
    ranges = []
    for section in elf.iter_sections():
        try:
            if section["sh_flags"] & _EXECUTABLE_FLAG and section["sh_addr"]:
                start = section["sh_addr"]
                ranges.append((start, start + section["sh_size"]))
        except Exception:
            continue
    return ranges


def _in_exec(addr, ranges):
    return any(start <= addr < end for start, end in ranges)


def _relocation_targets(elf: ELFFile) -> dict:
    """Map relocated pointer slots to their link-time target addresses."""
    targets = {}
    for section in elf.iter_sections():
        if not hasattr(section, "iter_relocations"):
            continue
        try:
            symbol_table = elf.get_section(section["sh_link"]) if section["sh_link"] else None
        except Exception:
            symbol_table = None
        for relocation in section.iter_relocations():
            entry = relocation.entry
            addend = entry.get("r_addend", 0) or 0
            symbol_name = None
            target = addend
            symbol_index = entry.get("r_info_sym", 0)
            if symbol_table is not None and symbol_index:
                try:
                    symbol = symbol_table.get_symbol(symbol_index)
                    target = symbol["st_value"] + addend
                    symbol_name = symbol.name or None
                except Exception:
                    pass
            targets[entry["r_offset"]] = {
                "target": target,
                "relocation_type": entry.get("r_info_type"),
                "symbol": symbol_name,
            }
    return targets


def _candidate_for_slot(address, raw_value, relocation, exec_ranges):
    if _in_exec(raw_value, exec_ranges):
        return {
            "table_offset": hex(address),
            "target": hex(raw_value),
            "source": "raw_pointer",
        }
    if relocation and _in_exec(relocation["target"], exec_ranges):
        result = {
            "table_offset": hex(address),
            "target": hex(relocation["target"]),
            "source": "dynamic_relocation",
            "relocation_type": relocation["relocation_type"],
        }
        if relocation.get("symbol"):
            result["symbol"] = relocation["symbol"]
        return result
    return None


def _finish_run(candidates, section_name, run, min_run):
    if len(run) >= min_run:
        candidates.append({
            "section": section_name,
            "start": run[0]["table_offset"],
            "entry_count": len(run),
            "entries": run,
            "needs_manual_confirmation": True,
        })


def scan_vtable_candidates(so_path: str, min_run: int = 3) -> list:
    candidates = []
    with open(so_path, "rb") as stream:
        if stream.read(4) != b"\x7fELF":
            return candidates
        stream.seek(0)
        elf = ELFFile(stream)
        ptr_size = 8 if elf.elfclass == 64 else 4
        fmt = "<Q" if ptr_size == 8 else "<I"
        exec_ranges = _exec_ranges(elf)
        relocations = _relocation_targets(elf)

        for name in _DATA_SECTION_NAMES:
            section = elf.get_section_by_name(name)
            if section is None:
                continue
            data = section.data()
            base = section["sh_addr"]
            run = []
            for offset in range(0, len(data) - ptr_size + 1, ptr_size):
                (raw_value,) = struct.unpack_from(fmt, data, offset)
                address = base + offset
                slot = _candidate_for_slot(address, raw_value, relocations.get(address), exec_ranges)
                if slot is None:
                    _finish_run(candidates, name, run, min_run)
                    run = []
                else:
                    run.append(slot)
            _finish_run(candidates, name, run, min_run)
    return candidates


def write_vtable_report(so_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    lib_name = Path(so_path).name
    candidates = scan_vtable_candidates(so_path)
    out_path = out_dir / f"{lib_name}.vtable_candidates.json"
    out_path.write_text(json.dumps(candidates, indent=2) + "\n")
    return {
        "library": lib_name,
        "candidate_table_count": len(candidates),
        "output": str(out_path),
    }


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print("usage: python vtable_scan.py <lib.so> <out_dir>")
        raise SystemExit(1)
    result = write_vtable_report(sys.argv[1], sys.argv[2])
    print(json.dumps(result, indent=2))
