package com.android.tools.smali.dexlib2.writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ProtoSection<StringKey, TypeKey, ProtoKey, TypeListKey> extends IndexSection<ProtoKey> {
    @Nullable
    TypeListKey getParameters(@Nonnull ProtoKey protokey);

    @Nonnull
    TypeKey getReturnType(@Nonnull ProtoKey protokey);

    @Nonnull
    StringKey getShorty(@Nonnull ProtoKey protokey);
}
