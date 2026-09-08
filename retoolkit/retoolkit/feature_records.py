"""
feature_records.py - analyst-owned feature record storage.

The toolkit discovers binary structure; it does not infer what a command
means or change program behavior. This module provides a stable JSON record
for an analyst to fill in while tracing a command through a native library.

The field names intentionally mirror the investigation worksheet:
Feature, Entry, Command, Branch, Lookup, Handler, Important globals,
Vtable calls, Game-state reads, Game-state writes, Confidence, and Status.
"""

import argparse
import json
import re
from pathlib import Path
from typing import Any, Dict, List


SCHEMA_FIELDS = (
    "Feature",
    "Entry",
    "Command",
    "Branch",
    "Lookup",
    "Handler",
    "Important globals",
    "Vtable calls",
    "Game-state reads",
    "Game-state writes",
    "Confidence",
    "Status",
)

DEFAULT_RECORD = {
    "Feature": "",
    "Entry": None,
    "Command": None,
    "Branch": [],
    "Lookup": [],
    "Handler": None,
    "Important globals": [],
    "Vtable calls": [],
    "Game-state reads": [],
    "Game-state writes": [],
    "Confidence": "unknown",
    "Status": "unresolved",
}


def make_record(feature: str, **values: Any) -> Dict[str, Any]:
    """Create a record with every schema field present.

    Unknown fields are rejected so a typo cannot silently create a second,
    incompatible schema. Values are deliberately not interpreted: addresses,
    names, notes, and lists remain analyst-controlled data.
    """
    record = dict(DEFAULT_RECORD)
    record["Feature"] = str(feature)
    unknown = set(values) - set(SCHEMA_FIELDS)
    if unknown:
        raise ValueError(f"unknown feature record fields: {sorted(unknown)}")
    record.update(values)
    return record


def validate_record(record: Dict[str, Any]) -> List[str]:
    """Return validation errors without changing the record."""
    errors = []
    missing = [field for field in SCHEMA_FIELDS if field not in record]
    if missing:
        errors.append(f"missing fields: {', '.join(missing)}")
    if not str(record.get("Feature", "")).strip():
        errors.append("Feature must not be empty")
    for field in ("Branch", "Lookup", "Important globals", "Vtable calls",
                  "Game-state reads", "Game-state writes"):
        value = record.get(field)
        if value is not None and not isinstance(value, list):
            errors.append(f"{field} must be a list or null")
    return errors


def _slug(value: str) -> str:
    value = re.sub(r"[^A-Za-z0-9._-]+", "_", value.strip())
    return value.strip("._") or "unnamed_feature"


def write_record(project_dir: str, record: Dict[str, Any], overwrite: bool = False) -> Path:
    errors = validate_record(record)
    if errors:
        raise ValueError("invalid feature record: " + "; ".join(errors))
    output_dir = Path(project_dir) / "features"
    output_dir.mkdir(parents=True, exist_ok=True)
    output = output_dir / f"{_slug(record['Feature'])}.json"
    if output.exists() and not overwrite:
        raise FileExistsError(f"feature record already exists: {output}")
    output.write_text(json.dumps(record, indent=2) + "\n")
    return output


def load_records(project_dir: str) -> List[Dict[str, Any]]:
    records = []
    for path in sorted((Path(project_dir) / "features").glob("*.json")):
        record = json.loads(path.read_text())
        errors = validate_record(record)
        if errors:
            raise ValueError(f"{path}: {'; '.join(errors)}")
        records.append(record)
    return records


def _cli() -> None:
    parser = argparse.ArgumentParser(description="manage analyst feature records")
    sub = parser.add_subparsers(dest="action", required=True)

    create = sub.add_parser("create", help="create an empty record")
    create.add_argument("project_dir")
    create.add_argument("feature")
    create.add_argument("--overwrite", action="store_true")
    create.add_argument("--status", default="unresolved")
    create.add_argument("--confidence", default="unknown")
    create.add_argument("--entry")
    create.add_argument("--command")

    listing = sub.add_parser("list", help="list records in a project")
    listing.add_argument("project_dir")

    validate = sub.add_parser("validate", help="validate all records")
    validate.add_argument("project_dir")

    args = parser.parse_args()
    if args.action == "create":
        record = make_record(
            args.feature,
            Entry=args.entry,
            Command=args.command or args.feature,
            Confidence=args.confidence,
            Status=args.status,
        )
        print(write_record(args.project_dir, record, overwrite=args.overwrite))
    elif args.action == "list":
        print(json.dumps(load_records(args.project_dir), indent=2))
    else:
        records = load_records(args.project_dir)
        print(json.dumps({"valid": True, "count": len(records)}, indent=2))


if __name__ == "__main__":
    _cli()
