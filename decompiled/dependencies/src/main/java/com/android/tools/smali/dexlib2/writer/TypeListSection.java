package com.android.tools.smali.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface TypeListSection<TypeKey, TypeListKey> extends NullableOffsetSection<TypeListKey> {
    @Override // com.android.tools.smali.dexlib2.writer.NullableOffsetSection
    int getNullableItemOffset(@Nullable TypeListKey typelistkey);

    @Nonnull
    Collection<? extends TypeKey> getTypes(@Nullable TypeListKey typelistkey);
}
