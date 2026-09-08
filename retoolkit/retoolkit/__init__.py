"""
retoolkit - a generic APK / native-library reverse engineering toolkit.

This package is intentionally generic: it inventories, disassembles, and
cross-references whatever binary you point it at. It contains no
game-specific, cheat-specific, or feature-tagging logic of any kind.
"""

__version__ = "0.1.0"

# androguard (via loguru) is very chatty at DEBUG level by default;
# keep pipeline output readable.
try:
    from loguru import logger as _loguru_logger
    import sys as _sys
    _loguru_logger.remove()
    _loguru_logger.add(_sys.stderr, level="WARNING")
except Exception:
    pass
