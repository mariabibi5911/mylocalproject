package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableTypeReference extends BaseTypeReference implements ImmutableReference {
    private static final ImmutableConverter<ImmutableTypeReference, TypeReference> CONVERTER = new ImmutableConverter<ImmutableTypeReference, TypeReference>() { // from class: com.android.tools.smali.dexlib2.immutable.reference.ImmutableTypeReference.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull TypeReference item) {
            return item instanceof ImmutableTypeReference;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableTypeReference makeImmutable(@Nonnull TypeReference item) {
            return ImmutableTypeReference.of(item);
        }
    };

    @Nonnull
    protected final String type;

    public ImmutableTypeReference(String type) {
        this.type = type;
    }

    @Nonnull
    public static ImmutableTypeReference of(@Nonnull TypeReference typeReference) {
        if (typeReference instanceof ImmutableTypeReference) {
            return (ImmutableTypeReference) typeReference;
        }
        return new ImmutableTypeReference(typeReference.getType());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Nonnull
    public static ImmutableList<ImmutableTypeReference> immutableListOf(@Nullable List<? extends TypeReference> list) {
        return CONVERTER.toList(list);
    }
}
