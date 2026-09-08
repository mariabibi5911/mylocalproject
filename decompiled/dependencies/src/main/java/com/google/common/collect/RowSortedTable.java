package com.google.common.collect;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {
    @Override // com.google.common.collect.Table
    SortedSet<R> rowKeySet();

    @Override // com.google.common.collect.Table
    SortedMap<R, Map<C, V>> rowMap();

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.google.common.collect.RowSortedTable$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC<R, C, V> {
    }
}
