"""
search.py - Feature: search everywhere.

A single entry point that searches across everything the pipeline has
already extracted for a project: strings, function/symbol names, and
manifest components. Supports plain substring or regex, case-sensitive
or not. This is the generic search layer - point it at a project
directory produced by pipeline.py and query it however you like.
"""

import json
import re
from pathlib import Path


def _load_json(path: Path):
    if not path.exists():
        return None
    try:
        return json.loads(path.read_text())
    except Exception:
        return None


def search_project(project_dir: str, query: str, regex: bool = False,
                    case_sensitive: bool = False) -> dict:
    project_dir = Path(project_dir)
    flags = 0 if case_sensitive else re.IGNORECASE
    pattern = re.compile(query if regex else re.escape(query), flags)

    results = {"strings": [], "functions": [], "imports_exports": [], "manifest": []}

    for strings_file in (project_dir / "analysis" / "strings").glob("*.strings.json"):
        data = _load_json(strings_file) or []
        for entry in data:
            if pattern.search(entry.get("value", "")):
                results["strings"].append({
                    "library": strings_file.name.replace(".strings.json", ""),
                    "offset": entry["offset"],
                    "value": entry["value"],
                })

    for func_file in (project_dir / "analysis" / "functions").glob("*.functions.json"):
        data = _load_json(func_file) or []
        for entry in data:
            if pattern.search(entry.get("name", "")):
                results["functions"].append({
                    "library": func_file.name.replace(".functions.json", ""),
                    "name": entry["name"],
                    "address": entry["value"],
                    "end": entry.get("end"),
                })

    for io_file in (project_dir / "analysis" / "functions").glob("*.imports_exports.json"):
        data = _load_json(io_file) or {}
        lib = io_file.name.replace(".imports_exports.json", "")
        for kind in ("imports", "exports"):
            for entry in data.get(kind, []):
                if pattern.search(entry.get("name", "")):
                    results["imports_exports"].append({
                        "library": lib, "kind": kind,
                        "name": entry["name"], "address": entry.get("value"),
                    })

    manifest_report = _load_json(project_dir / "reports" / "manifest_report.json")
    if manifest_report:
        for key in ("activities", "services", "receivers", "providers"):
            for entry in manifest_report.get(key, []):
                if pattern.search(entry.get("name", "")):
                    results["manifest"].append({"component_type": key, **entry})

    results["total_matches"] = sum(len(v) for v in results.values() if isinstance(v, list))
    return results


if __name__ == "__main__":
    import sys

    if len(sys.argv) < 3:
        print("usage: python search.py <project_dir> <query> [--regex] [--case-sensitive]")
        raise SystemExit(1)
    project_dir, query = sys.argv[1], sys.argv[2]
    use_regex = "--regex" in sys.argv
    case_sensitive = "--case-sensitive" in sys.argv
    print(json.dumps(search_project(project_dir, query, use_regex, case_sensitive), indent=2))
