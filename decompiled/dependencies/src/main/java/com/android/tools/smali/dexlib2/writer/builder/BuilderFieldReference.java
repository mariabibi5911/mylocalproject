package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderFieldReference extends BaseFieldReference implements BuilderReference {

    @Nonnull
    final BuilderTypeReference definingClass;

    @Nonnull
    final BuilderTypeReference fieldType;
    int index = -1;

    @Nonnull
    final BuilderStringReference name;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderFieldReference(@Nonnull BuilderTypeReference definingClass, @Nonnull BuilderStringReference name, @Nonnull BuilderTypeReference fieldType) {
        this.definingClass = definingClass;
        this.name = name;
        this.fieldType = fieldType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.fieldType.getType();
    }

    @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderReference
    public int getIndex() {
        return this.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderReference
    public void setIndex(int index) {
        this.index = index;
    }
}
