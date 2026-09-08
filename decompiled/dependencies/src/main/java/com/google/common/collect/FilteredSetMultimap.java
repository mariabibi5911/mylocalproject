package com.google.common.collect;

import com.android.tools.r8.annotations.SynthesizedClassV2;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
interface FilteredSetMultimap<K, V> extends FilteredMultimap<K, V>, SetMultimap<K, V> {
    @Override // com.google.common.collect.FilteredMultimap
    SetMultimap<K, V> unfiltered();

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.google.common.collect.FilteredSetMultimap$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC<K, V> {
    }
}
