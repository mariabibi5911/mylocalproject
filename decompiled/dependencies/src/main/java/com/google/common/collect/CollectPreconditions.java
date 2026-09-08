package com.google.common.collect;

import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
final class CollectPreconditions {
    CollectPreconditions() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkEntryNotNull(Object key, Object value) {
        if (key == null) {
            String valueOf = String.valueOf(value);
            throw new NullPointerException(new StringBuilder(String.valueOf(valueOf).length() + 24).append("null key in entry: null=").append(valueOf).toString());
        }
        if (value == null) {
            String valueOf2 = String.valueOf(key);
            throw new NullPointerException(new StringBuilder(String.valueOf(valueOf2).length() + 26).append("null value in entry: ").append(valueOf2).append("=null").toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkNonnegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(name).length() + 40).append(name).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long checkNonnegative(long value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(name).length() + 49).append(name).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkPositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(name).length() + 38).append(name).append(" must be positive but was: ").append(value).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRemove(boolean canRemove) {
        Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
    }
}
