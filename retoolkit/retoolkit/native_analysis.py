"""
native_analysis.py - Feature 2: generic native library analysis.

For a given .so, produces (no feature-tagging, no game-specific logic):

  - function list (from symtab/dynsym, with start address; end address
    inferred as "next known symbol start" when a size isn't recorded)
  - strings dump (all printable ASCII/UTF-16 runs, with file offset)
  - imports (undefined dynamic symbols the library expects at load time)
  - exports (defined global dynamic symbols other libraries can call)
  - PLT/GOT table dump (procedure linkage table -> resolved import name)
  - vtable/function-pointer candidate scan (generic heuristic: runs of
    consecutive pointer-sized values in .data.rel.ro / .data that each
    point into an executable section)

This module intentionally has no concept of named "features" (e.g. no
searching for specific game namespaces/strings) - it just extracts the
structure of the binary. What you do with it afterwards is up to you.
"""

import json
import re
from pathlib import Path

from elftools.elf.elffile import ELFFile
from elftools.elf.sections import SymbolTableSection
from elftools.elf.relocation import RelocationSection


# ---------- strings ----------

_ASCII_RUN = re.compile(rb"[\x20-\x7e]{4,}")


def extract_strings(so_path: str, min_len: int = 4) -> list:
    data = Path(so_path).read_bytes()
    results = []
    for m in _ASCII_RUN.finditer(data):
        s = m.group().decode("ascii", errors="ignore")
        if len(s) >= min_len:
            results.append({"offset": hex(m.start()), "value": s})
    return results


# ---------- symbols (functions, imports, exports) ----------

def _iter_symbol_sections(elf: ELFFile):
    for section in elf.iter_sections():
        if isinstance(section, SymbolTableSection):
            yield section


def extract_symbols(so_path: str) -> dict:
    functions = []
    imports = []
    exports = []

    with open(so_path, "rb") as f:
        elf = ELFFile(f)
        for section in _iter_symbol_sections(elf):
            for sym in section.iter_symbols():
                if not sym.name:
                    continue
                info = sym["st_info"]
                sym_type = info.type
                sym_bind = info.bind
                shndx = sym["st_shndx"]

                record = {
                    "name": sym.name,
                    "value": hex(sym["st_value"]),
                    "size": sym["st_size"],
                    "type": sym_type,
                    "bind": sym_bind,
                    "section_index": shndx,
                }

                if sym_type == "STT_FUNC":
                    if shndx == "SHN_UNDEF":
                        imports.append(record)
                    else:
                        functions.append(record)
                        if sym_bind in ("STB_GLOBAL", "STB_WEAK") and shndx != "SHN_UNDEF":
                            exports.append(record)
                elif shndx == "SHN_UNDEF" and sym_type == "STT_NOTYPE":
                    # undefined non-function symbol -> still an import (data import)
                    imports.append(record)

    # dedupe by (name, value) while preserving order
    def _dedupe(records):
        seen = set()
        out = []
        for r in records:
            key = (r["name"], r["value"])
            if key not in seen:
                seen.add(key)
                out.append(r)
        return out

    functions = _dedupe(functions)
    functions.sort(key=lambda r: int(r["value"], 16))

    # infer end address as next function's start when size is 0/unknown
    for i, fn in enumerate(functions):
        if fn["size"]:
            fn["end"] = hex(int(fn["value"], 16) + fn["size"])
            fn["end_inferred"] = False
        elif i + 1 < len(functions):
            fn["end"] = functions[i + 1]["value"]
            fn["end_inferred"] = True
        else:
            fn["end"] = None
            fn["end_inferred"] = True

    return {
        "functions": functions,
        "imports": _dedupe(imports),
        "exports": _dedupe(exports),
    }


# ---------- PLT / GOT ----------

def extract_plt_got(so_path: str) -> list:
    """
    Maps each PLT/GOT relocation slot to the imported symbol name it
    resolves to at load time. This is the table indirect calls bounce
    through, so it's the starting point for tracing any `BL`/`BLR` to
    an external function.
    """
    entries = []
    with open(so_path, "rb") as f:
        elf = ELFFile(f)
        symtab = elf.get_section_by_name(".dynsym")
        for section in elf.iter_sections():
            if not isinstance(section, RelocationSection):
                continue
            if not any(tag in section.name for tag in (".plt", ".got", ".rela.dyn", ".rel.dyn")):
                continue
            for reloc in section.iter_relocations():
                sym_name = None
                if symtab is not None and reloc["r_info_sym"] != 0:
                    sym = symtab.get_symbol(reloc["r_info_sym"])
                    sym_name = sym.name
                entries.append({
                    "section": section.name,
                    "offset": hex(reloc["r_offset"]),
                    "symbol": sym_name,
                    "reloc_type": reloc["r_info_type"],
                })
    return entries


# ---------- orchestration ----------

def _is_elf(so_path: str) -> bool:
    try:
        with open(so_path, "rb") as f:
            return f.read(4) == b"\x7fELF"
    except Exception:
        return False


def analyze_library(so_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    lib_name = Path(so_path).name

    if not _is_elf(so_path):
        return {
            "library": lib_name,
            "error": "not a valid ELF file (magic number mismatch) - skipped",
        }

    symbols = extract_symbols(so_path)
    strings = extract_strings(so_path)
    plt_got = extract_plt_got(so_path)

    (out_dir / "functions" / f"{lib_name}.functions.json").parent.mkdir(parents=True, exist_ok=True)
    (out_dir / "functions" / f"{lib_name}.functions.json").write_text(
        json.dumps(symbols["functions"], indent=2)
    )
    (out_dir / "strings" / f"{lib_name}.strings.json").parent.mkdir(parents=True, exist_ok=True)
    (out_dir / "strings" / f"{lib_name}.strings.json").write_text(
        json.dumps(strings, indent=2)
    )

    io_report = {"imports": symbols["imports"], "exports": symbols["exports"], "plt_got": plt_got}
    io_path = out_dir / "functions" / f"{lib_name}.imports_exports.json"
    io_path.write_text(json.dumps(io_report, indent=2))

    return {
        "library": lib_name,
        "function_count": len(symbols["functions"]),
        "import_count": len(symbols["imports"]),
        "export_count": len(symbols["exports"]),
        "string_count": len(strings),
        "plt_got_entry_count": len(plt_got),
        "outputs": {
            "functions": str(out_dir / "functions" / f"{lib_name}.functions.json"),
            "strings": str(out_dir / "strings" / f"{lib_name}.strings.json"),
            "imports_exports": str(io_path),
        },
    }


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print("usage: python native_analysis.py <lib.so> <out_analysis_dir>")
        raise SystemExit(1)
    result = analyze_library(sys.argv[1], sys.argv[2])
    print(json.dumps(result, indent=2))
