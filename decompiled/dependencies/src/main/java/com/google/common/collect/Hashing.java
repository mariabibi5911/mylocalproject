package com.google.common.collect;

import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
final class Hashing {
    private static final long C1 = -862048943;
    private static final long C2 = 461845907;
    private static final int MAX_TABLE_SIZE = 1073741824;

    private Hashing() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int smear(int hashCode) {
        return (int) (Integer.rotateLeft((int) (hashCode * C1), 15) * C2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int smearedHash(@CheckForNull Object o) {
        return smear(o == null ? 0 : o.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int closedTableSize(int expectedEntries, double loadFactor) {
        int expectedEntries2 = Math.max(expectedEntries, 2);
        int tableSize = Integer.highestOneBit(expectedEntries2);
        if (expectedEntries2 > ((int) (tableSize * loadFactor))) {
            int tableSize2 = tableSize << 1;
            if (tableSize2 > 0) {
                return tableSize2;
            }
            return 1073741824;
        }
        return tableSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean needsResizing(int size, int tableSize, double loadFactor) {
        return ((double) size) > ((double) tableSize) * loadFactor && tableSize < 1073741824;
    }
}
