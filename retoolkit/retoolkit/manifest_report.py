"""
manifest_report.py - Feature 1b: manifest / permission report.

Decodes AndroidManifest.xml and produces a structured report: permissions
(with a basic risk tag), exported components, intent filters, and any
custom URL schemes (useful for spotting OAuth/deep-link callbacks).
"""

import json
from pathlib import Path

from androguard.core.apk import APK

# A small, generic list of permissions worth flagging as "sensitive" -
# this is just common Android permission knowledge, not app-specific.
SENSITIVE_PERMISSIONS = {
    "android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.WRITE_EXTERNAL_STORAGE",
    "android.permission.MANAGE_EXTERNAL_STORAGE",
    "android.permission.CAMERA",
    "android.permission.RECORD_AUDIO",
    "android.permission.ACCESS_FINE_LOCATION",
    "android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.READ_CONTACTS",
    "android.permission.READ_SMS",
    "android.permission.SEND_SMS",
    "android.permission.READ_PHONE_STATE",
    "android.permission.SYSTEM_ALERT_WINDOW",
    "android.permission.REQUEST_INSTALL_PACKAGES",
    "android.permission.QUERY_ALL_PACKAGES",
}


def _exported(a: APK, tag: str, name: str):
    try:
        return a.get_attribute_value(tag, "exported", name=name)
    except Exception:
        return None


def build_report(apk_path: str) -> dict:
    a = APK(apk_path)

    permissions = []
    for p in a.get_permissions():
        permissions.append({"name": p, "sensitive": p in SENSITIVE_PERMISSIONS})

    activities = []
    for name in a.get_activities():
        activities.append({
            "name": name,
            "exported": _exported(a, "activity", name),
        })

    services = [{"name": n, "exported": _exported(a, "service", n)}
                for n in a.get_services()]
    receivers = [{"name": n, "exported": _exported(a, "receiver", n)}
                 for n in a.get_receivers()]
    providers = [{"name": n, "exported": _exported(a, "provider", n)}
                 for n in a.get_providers()]

    # custom url schemes declared via intent-filter <data android:scheme=...>
    schemes = set()
    try:
        root = a.get_android_manifest_xml()
        for data_tag in root.iter("data"):
            scheme = data_tag.get(
                "{http://schemas.android.com/apk/res/android}scheme"
            )
            if scheme:
                schemes.add(scheme)
    except Exception:
        pass

    try:
        debuggable = a.get_attribute_value("application", "debuggable")
    except Exception:
        debuggable = None
    try:
        cleartext = a.get_attribute_value("application", "usesCleartextTraffic")
    except Exception:
        cleartext = None

    report = {
        "package": a.get_package(),
        "debuggable": debuggable,
        "uses_cleartext_traffic": cleartext,
        "permissions": permissions,
        "sensitive_permission_count": sum(1 for p in permissions if p["sensitive"]),
        "activities": activities,
        "services": services,
        "receivers": receivers,
        "providers": providers,
        "custom_url_schemes": sorted(schemes),
        "exported_component_count": sum(
            1 for c in activities + services + receivers + providers
            if str(c.get("exported")).lower() == "true"
        ),
    }
    return report


def write_manifest_report(apk_path: str, out_path: str) -> dict:
    report = build_report(apk_path)
    Path(out_path).write_text(json.dumps(report, indent=2))
    return report


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print("usage: python manifest_report.py <apk> <out.json>")
        raise SystemExit(1)
    result = write_manifest_report(sys.argv[1], sys.argv[2])
    print(json.dumps(result, indent=2))
