package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class Platform {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return CompactHashMap.createWithExpectedSize(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
        return CompactLinkedHashMap.createWithExpectedSize(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Set<E> newHashSetWithExpectedSize(int expectedSize) {
        return CompactHashSet.createWithExpectedSize(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return CompactLinkedHashSet.createWithExpectedSize(expectedSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return CompactHashMap.create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Set<E> preservesInsertionOrderOnAddsSet() {
        return CompactHashSet.create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] newArray(T[] tArr, int i) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] copy(Object[] objArr, int i, int i2, T[] tArr) {
        return (T[]) Arrays.copyOfRange(objArr, i, i2, tArr.getClass());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }

    static int reduceIterationsIfGwt(int iterations) {
        return iterations;
    }

    static int reduceExponentIfGwt(int exponent) {
        return exponent;
    }

    static void checkGwtRpcEnabled() {
    }

    private Platform() {
    }
}
