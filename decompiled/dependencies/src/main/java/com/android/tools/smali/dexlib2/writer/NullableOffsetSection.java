package com.android.tools.smali.dexlib2.writer;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface NullableOffsetSection<Key> extends OffsetSection<Key> {
    int getNullableItemOffset(@Nullable Key key);
}
