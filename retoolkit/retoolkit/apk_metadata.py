"""
apk_metadata.py - Feature 1a: APK metadata extractor.

Pulls package name, version, SDK levels, signing info, and basic file stats
out of an APK, with no dependency on Java (uses androguard, pure Python).
"""

import hashlib
import json
import zipfile
from pathlib import Path

from androguard.core.apk import APK


def _hash_file(path: Path) -> dict:
    md5 = hashlib.md5()
    sha1 = hashlib.sha1()
    sha256 = hashlib.sha256()
    with open(path, "rb") as f:
        for chunk in iter(lambda: f.read(1 << 20), b""):
            md5.update(chunk)
            sha1.update(chunk)
            sha256.update(chunk)
    return {"md5": md5.hexdigest(), "sha1": sha1.hexdigest(), "sha256": sha256.hexdigest()}


def extract_metadata(apk_path: str) -> dict:
    apk_path = Path(apk_path)
    a = APK(str(apk_path))

    signing_info = []
    try:
        for cert in a.get_certificates_der_v2() or a.get_certificates_der_v1():
            signing_info.append({"length_bytes": len(cert)})
    except Exception:
        pass

    with zipfile.ZipFile(apk_path) as z:
        entries = z.infolist()
        entry_summary = {
            "total_entries": len(entries),
            "total_uncompressed_bytes": sum(e.file_size for e in entries),
        }

    metadata = {
        "file": {
            "name": apk_path.name,
            "size_bytes": apk_path.stat().st_size,
            **_hash_file(apk_path),
        },
        "package": {
            "package_name": a.get_package(),
            "version_code": a.get_androidversion_code(),
            "version_name": a.get_androidversion_name(),
            "min_sdk_version": a.get_min_sdk_version(),
            "target_sdk_version": a.get_target_sdk_version(),
            "max_sdk_version": a.get_max_sdk_version(),
            "app_name": a.get_app_name(),
        },
        "signing": {
            "is_signed_v1": a.is_signed_v1(),
            "is_signed_v2": a.is_signed_v2(),
            "is_signed_v3": a.is_signed_v3(),
            "certificates": signing_info,
        },
        "zip_summary": entry_summary,
    }
    return metadata


def write_metadata_report(apk_path: str, out_path: str) -> dict:
    metadata = extract_metadata(apk_path)
    Path(out_path).write_text(json.dumps(metadata, indent=2))
    return metadata


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print("usage: python apk_metadata.py <apk> <out.json>")
        raise SystemExit(1)
    result = write_metadata_report(sys.argv[1], sys.argv[2])
    print(json.dumps(result, indent=2))
