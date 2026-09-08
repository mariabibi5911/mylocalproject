"""
vtable_scan.py - Feature 2 (vtable/function-pointer candidates).

Generic heuristic: scans read-only data sections (.data.rel.ro, .rodata,
.data) for runs of consecutive pointer-sized values that each point into
an executable section. That pattern is what a C++ vtable (or any plain
function-pointer table) looks like in memory, regardless of what the
functions inside it actually do.

This is intentionally a structural scan only - it does not try to name,
classify, or filter tables by purpose.
"""

import json
import struct
from pathlib import Path

from elftools.elf.elffile import ELFFile


def _exec_ranges(elf: ELFFile):
    ranges = []
    for section in elf.iter_sections():
        try:
            if section["sh_flags"] & 0x4 and section["sh_addr"]:  # SHF_EXECINSTR
                start = section["sh_addr"]
                end = start + section["sh_size"]
                ranges.append((start, end))
        except Exception:
            continue
    return ranges


def _in_exec(addr, ranges):
    return any(start <= addr < end for start, end in ranges)


def scan_vtable_candidates(so_path: str, min_run: int = 3) -> list:
    candidates = []

    with open(so_path, "rb") as f:
        if f.read(4) != b"\x7fELF":
            return candidates
        f.seek(0)
        elf = ELFFile(f)
        ptr_size = 8 if elf.elfclass == 64 else 4
        fmt = "<Q" if ptr_size == 8 else "<I"
        exec_ranges = _exec_ranges(elf)

        data_section_names = (".data.rel.ro", ".rodata", ".data")
        for name in data_section_names:
            section = elf.get_section_by_name(name)
            if section is None:
                continue
            data = section.data()
            base = section["sh_addr"]

            run_start = None
            run_values = []

            for offset in range(0, len(data) - ptr_size + 1, ptr_size):
                (value,) = struct.unpack_from(fmt, data, offset)
                addr = base + offset

                if _in_exec(value, exec_ranges):
                    if run_start is None:
                        run_start = addr
                    run_values.append({"table_offset": hex(addr), "target": hex(value)})
                else:
                    if run_start is not None and len(run_values) >= min_run:
                        candidates.append({
                            "section": name,
                            "start": hex(run_start),
                            "entry_count": len(run_values),
                            "entries": run_values,
                        })
                    run_start = None
                    run_values = []

            if run_start is not None and len(run_values) >= min_run:
                candidates.append({
                    "section": name,
                    "start": hex(run_start),
                    "entry_count": len(run_values),
                    "entries": run_values,
                })

    return candidates


def write_vtable_report(so_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    lib_name = Path(so_path).name

    candidates = scan_vtable_candidates(so_path)
    out_path = out_dir / f"{lib_name}.vtable_candidates.json"
    out_path.write_text(json.dumps(candidates, indent=2))

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
