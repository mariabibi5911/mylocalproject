package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface FieldSection<StringKey, TypeKey, FieldRefKey extends FieldReference, FieldKey> extends IndexSection<FieldRefKey> {
    @Nonnull
    TypeKey getDefiningClass(@Nonnull FieldRefKey fieldrefkey);

    int getFieldIndex(@Nonnull FieldKey fieldkey);

    @Nonnull
    TypeKey getFieldType(@Nonnull FieldRefKey fieldrefkey);

    @Nonnull
    StringKey getName(@Nonnull FieldRefKey fieldrefkey);
}
