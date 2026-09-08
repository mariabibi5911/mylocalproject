"""
native_inventory.py - Feature 1d/1e/1f: native .so inventory, ABI detection,
and hashes/build-IDs.

Extracts every lib/<abi>/*.so from the APK into <project>/native/<abi>/,
and records per-library: size, hashes, ELF class (32/64-bit), machine
architecture, and the GNU build-id note if present.
"""

import hashlib
import json
import zipfile
from pathlib import Path

from elftools.elf.elffile import ELFFile


def _hash_bytes(data: bytes) -> dict:
    return {
        "md5": hashlib.md5(data).hexdigest(),
        "sha1": hashlib.sha1(data).hexdigest(),
        "sha256": hashlib.sha256(data).hexdigest(),
    }


def _elf_info(path: Path) -> dict:
    info = {"is_elf": False}
    try:
        with open(path, "rb") as f:
            elf = ELFFile(f)
            info["is_elf"] = True
            info["ei_class"] = elf.elfclass  # 32 or 64
            info["ei_data"] = elf.little_endian and "little" or "big"
            info["machine"] = elf["e_machine"]
            info["entry_point"] = hex(elf["e_entry"])
            info["is_pie"] = elf["e_type"] == "ET_DYN"

            build_id = None
            for section in elf.iter_sections():
                if section.name == ".note.gnu.build-id":
                    data = section.data()
                    # note format: namesz, descsz, type, name, desc
                    # build-id bytes start after the fixed header + name
                    try:
                        namesz = int.from_bytes(data[0:4], "little")
                        descsz = int.from_bytes(data[4:8], "little")
                        name_end = 12 + ((namesz + 3) // 4) * 4
                        build_id_bytes = data[name_end:name_end + descsz]
                        build_id = build_id_bytes.hex()
                    except Exception:
                        pass
            info["gnu_build_id"] = build_id

            info["section_count"] = elf.num_sections()
            info["segment_count"] = elf.num_segments()
    except Exception as e:
        info["error"] = str(e)
    return info


def extract_native_libs(apk_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)

    summary = {"abis": {}}

    with zipfile.ZipFile(apk_path) as z:
        so_entries = [n for n in z.namelist() if n.startswith("lib/") and n.endswith(".so")]
        for entry in sorted(so_entries):
            # entry looks like lib/arm64-v8a/libfoo.so
            parts = entry.split("/")
            if len(parts) != 3:
                continue
            _, abi, libname = parts
            data = z.read(entry)

            abi_dir = out_dir / abi
            abi_dir.mkdir(parents=True, exist_ok=True)
            out_path = abi_dir / libname
            out_path.write_bytes(data)

            lib_record = {
                "name": libname,
                "size_bytes": len(data),
                **_hash_bytes(data),
                **_elf_info(out_path),
            }
            summary["abis"].setdefault(abi, []).append(lib_record)

    summary["abi_list"] = sorted(summary["abis"].keys())
    summary["total_libraries"] = sum(len(v) for v in summary["abis"].values())
    return summary


def write_native_report(apk_path: str, out_dir: str, report_path: str) -> dict:
    summary = extract_native_libs(apk_path, out_dir)
    Path(report_path).write_text(json.dumps(summary, indent=2))
    return summary


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 4:
        print("usage: python native_inventory.py <apk> <out_native_dir> <report.json>")
        raise SystemExit(1)
    result = write_native_report(sys.argv[1], sys.argv[2], sys.argv[3])
    print(json.dumps(result, indent=2))
