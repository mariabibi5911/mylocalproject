package com.android.tools.smali.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableUtils {
    @Nonnull
    public static <T> ImmutableList<T> nullToEmptyList(@Nullable ImmutableList<T> list) {
        if (list == null) {
            return ImmutableList.of();
        }
        return list;
    }

    @Nonnull
    public static <T> ImmutableSet<T> nullToEmptySet(@Nullable ImmutableSet<T> set) {
        if (set == null) {
            return ImmutableSet.of();
        }
        return set;
    }

    @Nonnull
    public static <T> ImmutableSortedSet<T> nullToEmptySortedSet(@Nullable ImmutableSortedSet<T> set) {
        if (set == null) {
            return ImmutableSortedSet.of();
        }
        return set;
    }
}
