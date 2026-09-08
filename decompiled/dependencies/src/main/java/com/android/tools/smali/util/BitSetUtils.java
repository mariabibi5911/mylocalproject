package com.android.tools.smali.util;

import java.util.BitSet;

/* loaded from: classes.dex */
public class BitSetUtils {
    public static BitSet bitSetOfIndexes(int... indexes) {
        BitSet bitSet = new BitSet();
        for (int index : indexes) {
            bitSet.set(index);
        }
        return bitSet;
    }
}
