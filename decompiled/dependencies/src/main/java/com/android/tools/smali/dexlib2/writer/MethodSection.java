package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface MethodSection<StringKey, TypeKey, ProtoRefKey extends MethodProtoReference, MethodRefKey extends MethodReference, MethodKey> extends IndexSection<MethodRefKey> {
    @Nonnull
    TypeKey getDefiningClass(@Nonnull MethodRefKey methodrefkey);

    int getMethodIndex(@Nonnull MethodKey methodkey);

    @Nonnull
    MethodRefKey getMethodReference(@Nonnull MethodKey methodkey);

    @Nonnull
    StringKey getName(@Nonnull MethodRefKey methodrefkey);

    @Nonnull
    ProtoRefKey getPrototype(@Nonnull MethodRefKey methodrefkey);

    @Nonnull
    ProtoRefKey getPrototype(@Nonnull MethodKey methodkey);
}
