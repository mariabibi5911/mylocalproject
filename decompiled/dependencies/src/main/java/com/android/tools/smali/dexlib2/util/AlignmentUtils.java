package com.android.tools.smali.dexlib2.util;

/* loaded from: classes.dex */
public abstract class AlignmentUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static int alignOffset(int offset, int alignment) {
        int mask = alignment - 1;
        if (alignment < 0 || (mask & alignment) != 0) {
            throw new AssertionError();
        }
        return (offset + mask) & (~mask);
    }

    public static boolean isAligned(int offset, int alignment) {
        return offset % alignment == 0;
    }
}
