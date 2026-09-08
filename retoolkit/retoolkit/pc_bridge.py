"""
pc_bridge.py - Feature: mobile <-> PC bridge.

Two pieces:

1. serve_project(): spins up a plain HTTP file server rooted at a project
   directory, so a PC on the same network/USB-tether can browse to
   http://<phone-ip>:<port>/ and download any file with a normal browser -
   no cable-specific tooling needed on the PC side.

2. copy_as(): formats a chunk of bytes (e.g. a hex selection) as one of
   several text representations useful for pasting into PC-side tools:
   hex string, C byte array, Python bytes literal, or a plain hexdump.
"""

import http.server
import socketserver
import functools
import zipfile
from pathlib import Path


def serve_project(project_dir: str, port: int = 8000):
    """Blocking call - runs a simple HTTP file server rooted at project_dir.
    On the phone: run this from the app (e.g. via Chaquopy/Termux) or from
    the scripts/ folder over adb shell. On the PC: open
    http://<phone-ip>:<port>/ in any browser to browse and download files.
    """
    handler = functools.partial(
        http.server.SimpleHTTPRequestHandler, directory=str(project_dir)
    )
    with socketserver.TCPServer(("0.0.0.0", port), handler) as httpd:
        print(f"Serving {project_dir} at http://0.0.0.0:{port}/  (Ctrl+C to stop)")
        httpd.serve_forever()


def export_zip(project_dir: str, out_zip_path: str, include=("reports", "analysis", "notes")) -> str:
    """Zips the requested subfolders of a project for one-tap sharing
    (e.g. via Android's share sheet) or for `adb pull`."""
    project_dir = Path(project_dir)
    out_zip_path = Path(out_zip_path)

    with zipfile.ZipFile(out_zip_path, "w", zipfile.ZIP_DEFLATED) as zf:
        for sub in include:
            sub_path = project_dir / sub
            if not sub_path.exists():
                continue
            for file_path in sub_path.rglob("*"):
                if file_path.is_file():
                    zf.write(file_path, file_path.relative_to(project_dir))

    return str(out_zip_path)


# ---------- copy-as formats ----------

def copy_as_hex_string(data: bytes) -> str:
    return data.hex()


def copy_as_c_array(data: bytes, var_name: str = "data") -> str:
    body = ", ".join(f"0x{b:02X}" for b in data)
    return f"unsigned char {var_name}[] = {{ {body} }};"


def copy_as_python_bytes(data: bytes) -> str:
    return repr(data)


def copy_as_hexdump(data: bytes, base_addr: int = 0, width: int = 16) -> str:
    lines = []
    for i in range(0, len(data), width):
        chunk = data[i:i + width]
        hex_part = " ".join(f"{b:02x}" for b in chunk)
        ascii_part = "".join(chr(b) if 32 <= b < 127 else "." for b in chunk)
        lines.append(f"{base_addr + i:08x}  {hex_part:<{width * 3}}  {ascii_part}")
    return "\n".join(lines)


COPY_FORMATS = {
    "hex": copy_as_hex_string,
    "c_array": copy_as_c_array,
    "python_bytes": copy_as_python_bytes,
    "hexdump": copy_as_hexdump,
}


if __name__ == "__main__":
    import sys

    if len(sys.argv) < 2:
        print("usage:")
        print("  serve:  python pc_bridge.py serve <project_dir> [port]")
        print("  zip:    python pc_bridge.py zip <project_dir> <out.zip>")
        raise SystemExit(1)

    if sys.argv[1] == "serve":
        port = int(sys.argv[3]) if len(sys.argv) > 3 else 8000
        serve_project(sys.argv[2], port)
    elif sys.argv[1] == "zip":
        path = export_zip(sys.argv[2], sys.argv[3])
        print(f"wrote {path}")
