#!/system/bin/sh

LIB="$1"
OUT="/sdcard/NGI/lib-dump"
mkdir -p "$OUT"

if [ -z "$LIB" ] || [ ! -f "$LIB" ]; then
    echo "❌ Usage: DUMP-LIB.SH libName.so"
    exit 1
fi

echo "🚀 Start Analysis for $LIB ..."

# ELF basic info
readelf -h "$LIB" > "$OUT/elf_header.txt"
readelf -S "$LIB" > "$OUT/sections.txt"
readelf -d "$LIB" > "$OUT/dynamic.txt"

# Symbols and exports
nm -D "$LIB" > "$OUT/symbols.txt"
objdump -T "$LIB" > "$OUT/exports.txt"

# Strings
strings "$LIB" > "$OUT/strings.txt"
grep -Ei 'key|token|pass|auth|secret|flag' "$OUT/strings.txt" > "$OUT/sensitive_strings.txt"

# Dump common sections (if exist)
for sec in .text .data .rodata .bss .init_array .got .plt; do
    objcopy --dump-section $sec="$OUT/$sec.bin" "$LIB" 2>/dev/null
done

# Simple report
echo "# libDump Report for $LIB" > "$OUT/report.md"
echo "## ELF Header" >> "$OUT/report.md"
head -n 20 "$OUT/elf_header.txt" >> "$OUT/report.md"
echo "## Sensitive Strings" >> "$OUT/report.md"
cat "$OUT/sensitive_strings.txt" >> "$OUT/report.md"
echo "## Symbols" >> "$OUT/report.md"
head -n 50 "$OUT/symbols.txt" >> "$OUT/report.md"

echo "✅ Dump finished. Report saved in $OUT/report.md"