# retoolkit

A generic, PC-side companion toolkit for APK / native-library reverse
engineering. Built to sit next to your Android app (NGI PRO) and pick up
where a phone screen runs out of room: full-project file management,
call-graph exploration, and global search across everything the
pipeline extracts.

This toolkit is intentionally **generic** — it inventories, disassembles,
and cross-references whatever binary you point it at. It does not search
for, tag, or extract named "features" from a specific target binary; it
builds the structural analysis (functions, strings, xrefs, call graph)
and leaves interpretation to you.

## Install

```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

## Quick start — run the full pipeline on an APK

```bash
python3 -m retoolkit.pipeline /path/to/app.apk /path/to/project_dir
```

This creates the following structure under `project_dir/` (matching the
layout you specified, minus any feature-tagging folders):

```
project_dir/
├── apk/                    original APK, extracted dex files
│   └── dex/
├── native/                 extracted .so libraries, per ABI
│   ├── arm64-v8a/
│   ├── armeabi-v7a/
│   ├── x86/
│   └── x86_64/
├── analysis/
│   ├── functions/          function list + imports/exports per library
│   ├── strings/            strings dump per library
│   ├── calls/              call graph (edges + indirect call sites)
│   └── vtables/            function-pointer table candidates
├── reports/
│   ├── metadata.json       APK metadata, hashes
│   ├── manifest_report.json  permissions, exported components, url schemes
│   ├── dex_report.json     per-dex hash + class count
│   ├── native_report.json  per-.so hash, ELF info, build-id
│   └── summary.json        top-level index of everything produced
├── notes/                  (empty — yours to fill in)
└── scripts/                copies of these helper scripts
```

By default it runs deep native analysis (functions/strings/callgraph/
vtable-scan) against `arm64-v8a` if present, else the first available
ABI. Pass a preference list to override:

```bash
python3 -m retoolkit.pipeline app.apk project_dir armeabi-v7a,x86
```

## Individual modules (all also runnable standalone via `python -m`)

| Module | What it does |
|---|---|
| `retoolkit.apk_metadata` | package name, version, SDK levels, signing info, hashes |
| `retoolkit.manifest_report` | decoded manifest, permission risk flags, exported components, custom URL schemes |
| `retoolkit.dex_extract` | pulls every `classes*.dex`, hashes + class counts |
| `retoolkit.native_inventory` | extracts every `.so` per ABI, ELF class/machine/build-id |
| `retoolkit.native_analysis` | function list, strings, imports/exports, PLT/GOT for one `.so` |
| `retoolkit.callgraph` | builds + queries the call graph (see below) |
| `retoolkit.vtable_scan` | scans for function-pointer table candidates |
| `retoolkit.search` | search strings/functions/imports/manifest across a whole project |
| `retoolkit.pc_bridge` | local file server + zip export + "copy as" formatting helpers |

## Call-graph explorer (the "address → manual search → suffering" fix)

Build once per library (the pipeline already does this for you):

```bash
python3 -m retoolkit.callgraph build native/arm64-v8a/libfoo.so analysis/calls
```

Then query instantly, forward or backward, from any address:

```bash
# what does this function call?
python3 -m retoolkit.callgraph query analysis/calls/libfoo.so.callgraph.json callees 0x3ef834

# what calls this function? (reverse xref)
python3 -m retoolkit.callgraph query analysis/calls/libfoo.so.callgraph.json callers 0x4307e4

# expand 2 hops out as a tree
python3 -m retoolkit.callgraph query analysis/calls/libfoo.so.callgraph.json expand 0x3ef834 2
```

Or from Python, for building your own UI on top:

```python
from retoolkit.callgraph import CallGraphExplorer
g = CallGraphExplorer("analysis/calls/libfoo.so.callgraph.json")
g.callees("0x3ef834")
g.callers("0x4307e4")
g.expand("0x3ef834", depth=3, direction="callers")
g.indirect_sites_near()   # BLR/CALL-reg sites capstone couldn't resolve statically
```

Indirect calls (`BLR`, `CALL reg`, etc.) can't be resolved to a target
address just from disassembly — those are recorded separately in
`indirect_call_sites` so you know exactly where to point a vtable/
function-pointer analysis, instead of finding them one at a time while
scrolling.

## Global search

```bash
python3 -m retoolkit.search project_dir "auth" --regex
```

Searches strings, function/symbol names, imports/exports, and manifest
component names in one pass across the whole project.

## Mobile ⇄ PC bridge

```bash
# On the phone (e.g. via Termux/adb shell), serve a finished project:
python3 -m retoolkit.pc_bridge serve project_dir 8000
# On the PC, browse to http://<phone-ip>:8000/ and download anything.

# Or just zip it for the share sheet / adb pull:
python3 -m retoolkit.pc_bridge zip project_dir out.zip
```

**Security note:** `serve_project()` binds an unauthenticated HTTP server to
`0.0.0.0`. Use it only on a trusted network, stop it when finished, and do
not expose it to the public internet; analysis projects can contain the APK,
native binaries, strings, and sensitive reports.

`retoolkit/pc_bridge.py` also exposes `copy_as_hex_string`,
`copy_as_c_array`, `copy_as_python_bytes`, and `copy_as_hexdump` — wire
these into your Android hex editor's "copy" button to get the same
formats on both ends.

## Notes on porting pieces into the Android app

- `search.py` and `callgraph.py`'s `CallGraphExplorer` are the two
  modules most worth porting into Kotlin/Java for in-app use — they're
  pure data-structure logic (dict/JSON in, JSON out) with no OS-specific
  dependencies, so the algorithm translates directly even though the
  language won't.
- `native_analysis.py` and `vtable_scan.py` depend on `pyelftools` and
  `capstone`; capstone has an Android/ARM build and could run in-app via
  Chaquopy (Python-on-Android) if you want the deep analysis on-device
  too, rather than PC-only.
- Everything writes plain JSON, so the Android app and this PC toolkit
  can share a project folder over the file bridge and both read/write
  the same `analysis/` files without needing a shared codebase.
