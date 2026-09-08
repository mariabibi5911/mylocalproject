package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableFieldReference extends BaseFieldReference implements ImmutableReference {

    @Nonnull
    protected final String definingClass;

    @Nonnull
    protected final String name;

    @Nonnull
    protected final String type;

    public ImmutableFieldReference(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type) {
        this.definingClass = definingClass;
        this.name = name;
        this.type = type;
    }

    @Nonnull
    public static ImmutableFieldReference of(@Nonnull FieldReference fieldReference) {
        if (fieldReference instanceof ImmutableFieldReference) {
            return (ImmutableFieldReference) fieldReference;
        }
        return new ImmutableFieldReference(fieldReference.getDefiningClass(), fieldReference.getName(), fieldReference.getType());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.type;
    }
}
