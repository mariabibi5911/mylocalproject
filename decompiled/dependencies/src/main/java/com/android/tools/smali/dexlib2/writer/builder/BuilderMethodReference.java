package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderMethodReference extends BaseMethodReference implements BuilderReference {

    @Nonnull
    final BuilderTypeReference definingClass;
    int index = -1;

    @Nonnull
    final BuilderStringReference name;

    @Nonnull
    final BuilderMethodProtoReference proto;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderMethodReference(@Nonnull BuilderTypeReference definingClass, @Nonnull BuilderStringReference name, @Nonnull BuilderMethodProtoReference proto) {
        this.definingClass = definingClass;
        this.name = name;
        this.proto = proto;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public BuilderTypeList getParameterTypes() {
        return this.proto.parameterTypes;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.proto.returnType.getType();
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
