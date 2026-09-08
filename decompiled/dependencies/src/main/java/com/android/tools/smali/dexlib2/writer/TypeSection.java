package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface TypeSection<StringKey, TypeKey, TypeRef extends TypeReference> extends NullableIndexSection<TypeKey> {
    int getItemIndex(@Nonnull TypeRef typeref);

    @Nonnull
    StringKey getString(@Nonnull TypeKey typekey);
}
