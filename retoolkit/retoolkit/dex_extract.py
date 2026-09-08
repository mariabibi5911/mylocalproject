"""
dex_extract.py - Feature 1c: DEX extraction (multi-dex aware).

Pulls every classes*.dex out of the APK zip into <project>/apk/dex/, and
records basic per-dex stats (size, sha256, declared class count) without
needing a JVM.
"""

import hashlib
import json
import zipfile
from pathlib import Path

from androguard.core.dex import DEX


def extract_dex_files(apk_path: str, out_dir: str) -> dict:
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)

    summary = {"dex_files": []}

    with zipfile.ZipFile(apk_path) as z:
        dex_names = sorted(
            n for n in z.namelist() if n.startswith("classes") and n.endswith(".dex")
        )
        for name in dex_names:
            data = z.read(name)
            out_path = out_dir / name
            out_path.write_bytes(data)

            sha256 = hashlib.sha256(data).hexdigest()
            class_count = None
            try:
                d = DEX(data)
                class_count = len(d.get_classes())
            except Exception:
                pass

            summary["dex_files"].append({
                "name": name,
                "size_bytes": len(data),
                "sha256": sha256,
                "class_count": class_count,
            })

    summary["total_dex_files"] = len(summary["dex_files"])
    summary["total_classes"] = sum(
        d["class_count"] for d in summary["dex_files"] if d["class_count"] is not None
    )
    return summary


def write_dex_report(apk_path: str, out_dir: str, report_path: str) -> dict:
    summary = extract_dex_files(apk_path, out_dir)
    Path(report_path).write_text(json.dumps(summary, indent=2))
    return summary


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 4:
        print("usage: python dex_extract.py <apk> <out_dex_dir> <report.json>")
        raise SystemExit(1)
    result = write_dex_report(sys.argv[1], sys.argv[2], sys.argv[3])
    print(json.dumps(result, indent=2))
