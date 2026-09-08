package com.android.tools.smali.dexlib2.writer;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface NullableIndexSection<Key> extends IndexSection<Key> {
    int getNullableItemIndex(@Nullable Key key);
}
