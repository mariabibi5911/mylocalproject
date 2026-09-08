"""
project.py - creates the standard analysis folder structure for a project.

Layout (generic, no feature/cheat-specific folders):

<PROJECT>/
├── apk/                  original APK + extracted contents
├── native/               extracted .so libraries, one subfolder per ABI
├── analysis/
│   ├── functions/        per-library function list JSON
│   ├── xrefs/            per-library cross-reference database
│   ├── strings/          per-library strings dump + regex search index
│   ├── calls/            per-library call graph (edges) JSON
│   └── vtables/          per-library vtable/function-pointer candidates
├── reports/              metadata.json, manifest_report.json, hashes.json
├── notes/                free-form analyst notes (never auto-written)
└── scripts/              copies of the helper scripts used to produce this
"""

from pathlib import Path


SUBDIRS = [
    "apk",
    "native",
    "analysis/functions",
    "analysis/xrefs",
    "analysis/strings",
    "analysis/calls",
    "analysis/vtables",
    "reports",
    "notes",
    "scripts",
]


def init_project(root: str) -> Path:
    """Create the standard folder structure under `root` and return its Path."""
    root_path = Path(root)
    for sub in SUBDIRS:
        (root_path / sub).mkdir(parents=True, exist_ok=True)
    return root_path
