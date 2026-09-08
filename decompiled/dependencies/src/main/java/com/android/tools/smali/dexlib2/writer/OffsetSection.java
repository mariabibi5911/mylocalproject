package com.android.tools.smali.dexlib2.writer;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface OffsetSection<Key> {
    int getItemOffset(@Nonnull Key key);

    @Nonnull
    Collection<? extends Map.Entry<? extends Key, Integer>> getItems();
}
