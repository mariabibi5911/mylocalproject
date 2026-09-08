package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.VersionMap;
import com.android.tools.smali.dexlib2.dexbacked.CDexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.android.tools.smali.util.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class HeaderItem {
    public static final int BIG_ENDIAN_TAG = 2018915346;
    public static final int CHECKSUM_DATA_START_OFFSET = 12;
    public static final int CHECKSUM_OFFSET = 8;
    public static final int CLASS_COUNT_OFFSET = 96;
    public static final int CLASS_START_OFFSET = 100;
    public static final int DATA_SIZE_OFFSET = 104;
    public static final int DATA_START_OFFSET = 108;
    public static final int ENDIAN_TAG_OFFSET = 40;
    public static final int FIELD_COUNT_OFFSET = 80;
    public static final int FIELD_START_OFFSET = 84;
    public static final int FILE_SIZE_OFFSET = 32;
    public static final int HEADER_SIZE_OFFSET = 36;
    public static final int ITEM_SIZE = 112;
    public static final int LITTLE_ENDIAN_TAG = 305419896;
    private static final byte[] MAGIC_VALUE = {100, 101, 120, 10, 0, 0, 0, 0};
    public static final int MAP_OFFSET = 52;
    public static final int METHOD_COUNT_OFFSET = 88;
    public static final int METHOD_START_OFFSET = 92;
    public static final int PROTO_COUNT_OFFSET = 72;
    public static final int PROTO_START_OFFSET = 76;
    public static final int SIGNATURE_DATA_START_OFFSET = 32;
    public static final int SIGNATURE_OFFSET = 12;
    public static final int SIGNATURE_SIZE = 20;
    public static final int STRING_COUNT_OFFSET = 56;
    public static final int STRING_START_OFFSET = 60;
    public static final int TYPE_COUNT_OFFSET = 64;
    public static final int TYPE_START_OFFSET = 68;

    @Nonnull
    private DexBackedDexFile dexFile;

    public HeaderItem(@Nonnull DexBackedDexFile dexFile) {
        this.dexFile = dexFile;
    }

    public int getChecksum() {
        return this.dexFile.getBuffer().readSmallUint(8);
    }

    @Nonnull
    public byte[] getSignature() {
        return this.dexFile.getBuffer().readByteRange(12, 20);
    }

    public int getMapOffset() {
        return this.dexFile.getBuffer().readSmallUint(52);
    }

    public int getHeaderSize() {
        return this.dexFile.getBuffer().readSmallUint(36);
    }

    public int getStringCount() {
        return this.dexFile.getBuffer().readSmallUint(56);
    }

    public int getStringOffset() {
        return this.dexFile.getBuffer().readSmallUint(60);
    }

    public int getTypeCount() {
        return this.dexFile.getBuffer().readSmallUint(64);
    }

    public int getTypeOffset() {
        return this.dexFile.getBuffer().readSmallUint(68);
    }

    public int getProtoCount() {
        return this.dexFile.getBuffer().readSmallUint(72);
    }

    public int getProtoOffset() {
        return this.dexFile.getBuffer().readSmallUint(76);
    }

    public int getFieldCount() {
        return this.dexFile.getBuffer().readSmallUint(80);
    }

    public int getFieldOffset() {
        return this.dexFile.getBuffer().readSmallUint(84);
    }

    public int getMethodCount() {
        return this.dexFile.getBuffer().readSmallUint(88);
    }

    public int getMethodOffset() {
        return this.dexFile.getBuffer().readSmallUint(92);
    }

    public int getClassCount() {
        return this.dexFile.getBuffer().readSmallUint(96);
    }

    public int getClassOffset() {
        return this.dexFile.getBuffer().readSmallUint(100);
    }

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "header_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int startOffset = out.getCursor();
                StringBuilder magicBuilder = new StringBuilder();
                for (int i = 0; i < 8; i++) {
                    magicBuilder.append((char) this.dexFile.getBuffer().readUbyte(startOffset + i));
                }
                out.annotate(8, "magic: %s", StringUtils.escapeString(magicBuilder.toString()));
                out.annotate(4, "checksum", new Object[0]);
                out.annotate(20, "signature", new Object[0]);
                out.annotate(4, "file_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                int headerSize = this.dexFile.getBuffer().readInt(out.getCursor());
                out.annotate(4, "header_size: %d", Integer.valueOf(headerSize));
                int endianTag = this.dexFile.getBuffer().readInt(out.getCursor());
                out.annotate(4, "endian_tag: 0x%x (%s)", Integer.valueOf(endianTag), HeaderItem.getEndianText(endianTag));
                out.annotate(4, "link_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "link_offset: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "map_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "string_ids_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "string_ids_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "type_ids_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "type_ids_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "proto_ids_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "proto_ids_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "field_ids_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "field_ids_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "method_ids_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "method_ids_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "class_defs_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "class_defs_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "data_size: %d", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                out.annotate(4, "data_off: 0x%x", Integer.valueOf(this.dexFile.getBuffer().readInt(out.getCursor())));
                if (this.annotator.dexFile instanceof CDexBackedDexFile) {
                    CdexHeaderItem.annotateCdexHeaderFields(out, this.dexFile.getBuffer());
                }
                if (headerSize > 112) {
                    out.annotateTo(headerSize, "header padding", new Object[0]);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getEndianText(int endianTag) {
        if (endianTag == 305419896) {
            return "Little Endian";
        }
        if (endianTag == 2018915346) {
            return "Big Endian";
        }
        return "Invalid";
    }

    public static byte[] getMagicForApi(int api) {
        return getMagicForDexVersion(VersionMap.mapApiToDexVersion(api));
    }

    public static byte[] getMagicForDexVersion(int dexVersion) {
        byte[] magic = (byte[]) MAGIC_VALUE.clone();
        if (dexVersion < 0 || dexVersion > 999) {
            throw new IllegalArgumentException("dexVersion must be within [0, 999]");
        }
        for (int i = 6; i >= 4; i--) {
            int digit = dexVersion % 10;
            magic[i] = (byte) (digit + 48);
            dexVersion /= 10;
        }
        return magic;
    }

    public static boolean verifyMagic(byte[] buf, int offset) {
        if (buf.length - offset < 8) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (buf[offset + i] != MAGIC_VALUE[i]) {
                return false;
            }
        }
        for (int i2 = 4; i2 < 7; i2++) {
            if (buf[offset + i2] < 48 || buf[offset + i2] > 57) {
                return false;
            }
        }
        int i3 = offset + 7;
        return buf[i3] == MAGIC_VALUE[7];
    }

    public static int getVersion(byte[] buf, int offset) {
        if (!verifyMagic(buf, offset)) {
            return -1;
        }
        return getVersionUnchecked(buf, offset);
    }

    private static int getVersionUnchecked(byte[] buf, int offset) {
        int version = (buf[offset + 4] - 48) * 100;
        return version + ((buf[offset + 5] - 48) * 10) + (buf[offset + 6] - 48);
    }

    public static boolean isSupportedDexVersion(int version) {
        return VersionMap.mapDexVersionToApi(version) != -1;
    }

    public static int getEndian(byte[] buf, int offset) {
        DexBuffer bdb = new DexBuffer(buf);
        return bdb.readInt(offset + 40);
    }
}
