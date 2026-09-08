"""
pipeline.py - runs the full generic analysis pipeline end-to-end:

  1. create project folder structure
  2. APK metadata extraction        -> reports/metadata.json
  3. Manifest/permission report      -> reports/manifest_report.json
  4. DEX extraction                  -> apk/dex/, reports/dex_report.json
  5. Native .so inventory            -> native/<abi>/, reports/native_report.json
  6. Per-library native analysis     -> analysis/functions, analysis/strings
  7. Per-library call graph          -> analysis/calls
  8. Per-library vtable candidates   -> analysis/vtables
  9. reports/summary.json            -> top-level index of everything produced

No feature-tagging / cheat-specific logic anywhere in this file.
"""

import json
import shutil
import sys
from pathlib import Path

from . import project, apk_metadata, manifest_report, dex_extract, native_inventory
from . import native_analysis, callgraph, vtable_scan


def run_pipeline(apk_path: str, project_root: str, arches=None) -> dict:
    apk_path = Path(apk_path)
    root = project.init_project(project_root)

    # copy the source APK into apk/
    apk_copy = root / "apk" / apk_path.name
    shutil.copy2(apk_path, apk_copy)

    summary = {"project_root": str(root), "source_apk": str(apk_copy)}

    print("[1/6] APK metadata ...")
    summary["metadata"] = apk_metadata.write_metadata_report(
        str(apk_copy), str(root / "reports" / "metadata.json")
    )

    print("[2/6] Manifest / permission report ...")
    summary["manifest"] = manifest_report.write_manifest_report(
        str(apk_copy), str(root / "reports" / "manifest_report.json")
    )

    print("[3/6] DEX extraction ...")
    summary["dex"] = dex_extract.write_dex_report(
        str(apk_copy), str(root / "apk" / "dex"), str(root / "reports" / "dex_report.json")
    )

    print("[4/6] Native library inventory ...")
    native_summary = native_inventory.write_native_report(
        str(apk_copy), str(root / "native"), str(root / "reports" / "native_report.json")
    )
    summary["native_inventory"] = native_summary

    # pick which ABI to run deep analysis on (prefer arm64-v8a, else first available)
    abis = native_summary.get("abi_list", [])
    target_abi = None
    if arches:
        target_abi = next((a for a in arches if a in abis), None)
    if target_abi is None:
        target_abi = "arm64-v8a" if "arm64-v8a" in abis else (abis[0] if abis else None)

    per_library_results = []
    if target_abi:
        print(f"[5/6] Deep native analysis for ABI '{target_abi}' ...")
        for lib_record in native_summary["abis"][target_abi]:
            lib_path = root / "native" / target_abi / lib_record["name"]
            print(f"      -> {lib_record['name']}")

            lib_result = {"library": lib_record["name"], "abi": target_abi}
            lib_result["analysis"] = native_analysis.analyze_library(
                str(lib_path), str(root / "analysis")
            )
            lib_result["callgraph"] = callgraph.write_callgraph(
                str(lib_path), str(root / "analysis" / "calls")
            )
            # drop the raw edge list from the summary (kept in the json file)
            lib_result["callgraph"] = {
                k: v for k, v in lib_result["callgraph"].items() if k != "edges"
            }
            lib_result["vtables"] = vtable_scan.write_vtable_report(
                str(lib_path), str(root / "analysis" / "vtables")
            )
            per_library_results.append(lib_result)
    else:
        print("[5/6] No native libraries found - skipping deep analysis.")

    summary["target_abi"] = target_abi
    summary["per_library"] = per_library_results

    print("[6/6] Writing summary ...")
    (root / "reports" / "summary.json").write_text(json.dumps(summary, indent=2))

    return summary


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("usage: python -m retoolkit.pipeline <apk_path> <project_root> [abi1,abi2,...]")
        raise SystemExit(1)
    arches = sys.argv[3].split(",") if len(sys.argv) > 3 else None
    result = run_pipeline(sys.argv[1], sys.argv[2], arches)
    print("\nDone. Summary written to:", Path(sys.argv[2]) / "reports" / "summary.json")
