package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;

/* loaded from: classes.dex */
public class OdexHeaderItem {
    public static final int AUX_LENGTH_OFFSET = 28;
    public static final int AUX_OFFSET = 24;
    public static final int DEPENDENCIES_LENGTH_OFFSET = 20;
    public static final int DEPENDENCIES_OFFSET = 16;
    public static final int DEX_LENGTH_OFFSET = 12;
    public static final int DEX_OFFSET = 8;
    public static final int FLAGS_OFFSET = 32;
    public static final int ITEM_SIZE = 40;
    public static final int MAGIC_LENGTH = 8;
    public static final int MAGIC_OFFSET = 0;
    private static final byte[] MAGIC_VALUE = {100, 101, 121, 10, 0, 0, 0, 0};
    private static final int[] SUPPORTED_ODEX_VERSIONS = {35, 36};

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

    public static boolean isSupportedOdexVersion(int version) {
        int i = 0;
        while (true) {
            int[] iArr = SUPPORTED_ODEX_VERSIONS;
            if (i < iArr.length) {
                if (iArr[i] != version) {
                    i++;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static int getDexOffset(byte[] buf) {
        DexBuffer bdb = new DexBuffer(buf);
        return bdb.readSmallUint(8);
    }

    public static int getDependenciesOffset(byte[] buf) {
        DexBuffer bdb = new DexBuffer(buf);
        return bdb.readSmallUint(16);
    }
}
