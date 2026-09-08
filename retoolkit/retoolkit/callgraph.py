"""
callgraph.py - Feature 2 (call relationships) + Feature 5 (call-graph explorer).

Disassembles every executable section of a .so with Capstone, records
every direct branch/call instruction (BL/B on ARM64, BL/BLX on ARM32,
CALL/JMP on x86/x86_64) as a graph edge, and lets you query forward
(callees) and reverse (callers) from any address - the generic version
of "item 5" from the discussion, with no game-specific filtering.

Indirect calls (BLR/CALL reg, BLX reg) are recorded separately as
"indirect_call_sites" since their target isn't known statically - this
is exactly the set of locations a vtable/function-pointer analysis
would want to resolve by hand.
"""

import json
from collections import defaultdict
from pathlib import Path

import capstone
from elftools.elf.elffile import ELFFile


_ARCH_MAP = {
    "EM_AARCH64": (capstone.CS_ARCH_ARM64, capstone.CS_MODE_ARM),
    "EM_ARM": (capstone.CS_ARCH_ARM, capstone.CS_MODE_ARM),
    "EM_386": (capstone.CS_ARCH_X86, capstone.CS_MODE_32),
    "EM_X86_64": (capstone.CS_ARCH_X86, capstone.CS_MODE_64),
}

_DIRECT_CALL_MNEMONICS = {
    "bl", "bx", "b", "blx",       # arm/arm64 direct branch-with-link / branch
    "call", "jmp",                 # x86/x86_64
}
_INDIRECT_CALL_MNEMONICS = {
    "blr", "br",                   # arm64 indirect
    "blx", "bx",                   # arm32 indirect (register operand case, filtered below)
}


def _executable_sections(elf: ELFFile):
    for section in elf.iter_sections():
        try:
            if section["sh_flags"] & 0x4:  # SHF_EXECINSTR
                yield section
        except Exception:
            continue


def build_callgraph(so_path: str) -> dict:
    with open(so_path, "rb") as f:
        if f.read(4) != b"\x7fELF":
            return {"error": "not a valid ELF file (magic number mismatch) - skipped"}
        f.seek(0)
        elf = ELFFile(f)
        machine = elf["e_machine"]
        if machine not in _ARCH_MAP:
            return {"error": f"unsupported machine type: {machine}"}

        arch, mode = _ARCH_MAP[machine]
        md = capstone.Cs(arch, mode)
        md.detail = True

        edges = []                       # list of {"from": hex, "to": hex, "mnemonic": ...}
        indirect_sites = []               # list of {"address": hex, "mnemonic": ...}
        instructions_indexed = 0

        for section in _executable_sections(elf):
            code = section.data()
            base = section["sh_addr"]
            if base == 0:
                continue
            for insn in md.disasm(code, base):
                instructions_indexed += 1
                mnem = insn.mnemonic.lower()

                if mnem in _DIRECT_CALL_MNEMONICS:
                    # operand is typically an immediate target address for direct branches
                    ops = insn.op_str.strip()
                    if ops.startswith("#"):
                        ops = ops[1:]
                    try:
                        target = int(ops, 16) if ops.lower().startswith("0x") else int(ops)
                        edges.append({
                            "from": hex(insn.address),
                            "to": hex(target),
                            "mnemonic": mnem,
                        })
                        continue
                    except ValueError:
                        pass  # falls through - register operand, so it's indirect

                if mnem in _INDIRECT_CALL_MNEMONICS or mnem in _DIRECT_CALL_MNEMONICS:
                    indirect_sites.append({
                        "address": hex(insn.address),
                        "mnemonic": mnem,
                        "op_str": insn.op_str,
                    })

    return {
        "instructions_indexed": instructions_indexed,
        "edges": edges,
        "indirect_call_sites": indirect_sites,
        "edge_count": len(edges),
        "indirect_call_site_count": len(indirect_sites),
    }


def write_callgraph(so_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    lib_name = Path(so_path).name

    graph = build_callgraph(so_path)
    out_path = out_dir / f"{lib_name}.callgraph.json"
    out_path.write_text(json.dumps(graph, indent=2))
    if "error" not in graph:
        graph["output"] = str(out_path)
    return graph


# ---------- explorer: forward/backward queries over a built graph ----------

class CallGraphExplorer:
    """
    Load a .callgraph.json produced above and answer:
      - callees(addr): what does this address call directly
      - callers(addr): what calls this address directly (reverse xref)
      - expand(addr, depth): BFS a few hops out in either direction
    """

    def __init__(self, callgraph_json_path: str):
        data = json.loads(Path(callgraph_json_path).read_text())
        self._forward = defaultdict(list)   # from -> [to, ...]
        self._reverse = defaultdict(list)   # to -> [from, ...]
        for edge in data.get("edges", []):
            self._forward[edge["from"]].append(edge["to"])
            self._reverse[edge["to"]].append(edge["from"])
        self.indirect_sites = data.get("indirect_call_sites", [])

    def callees(self, addr: str) -> list:
        return sorted(set(self._forward.get(addr, [])))

    def callers(self, addr: str) -> list:
        return sorted(set(self._reverse.get(addr, [])))

    def expand(self, addr: str, depth: int = 2, direction: str = "callees") -> dict:
        """Return a nested tree {addr: {child: {...}}} up to `depth` hops."""
        adjacency = self._forward if direction == "callees" else self._reverse

        def _walk(node, remaining, seen):
            if remaining == 0 or node in seen:
                return {}
            seen = seen | {node}
            children = sorted(set(adjacency.get(node, [])))
            return {child: _walk(child, remaining - 1, seen) for child in children}

        return {addr: _walk(addr, depth, set())}

    def indirect_sites_near(self, addr_range=None) -> list:
        """List indirect call sites (BLR/CALL reg etc) - the ones a vtable
        analysis would need to resolve by hand, surfaced up front instead
        of discovered one at a time while scrolling."""
        return self.indirect_sites


if __name__ == "__main__":
    import sys

    if len(sys.argv) < 3:
        print("usage:")
        print("  build:  python callgraph.py build <lib.so> <out_dir>")
        print("  query:  python callgraph.py query <callgraph.json> <callees|callers|expand> <addr> [depth]")
        raise SystemExit(1)

    if sys.argv[1] == "build":
        result = write_callgraph(sys.argv[2], sys.argv[3])
        print(json.dumps({k: v for k, v in result.items() if k != "edges"}, indent=2))
    elif sys.argv[1] == "query":
        explorer = CallGraphExplorer(sys.argv[2])
        mode = sys.argv[3]
        addr = sys.argv[4]
        if mode == "callees":
            print(json.dumps(explorer.callees(addr), indent=2))
        elif mode == "callers":
            print(json.dumps(explorer.callers(addr), indent=2))
        elif mode == "expand":
            depth = int(sys.argv[5]) if len(sys.argv) > 5 else 2
            print(json.dumps(explorer.expand(addr, depth), indent=2))
