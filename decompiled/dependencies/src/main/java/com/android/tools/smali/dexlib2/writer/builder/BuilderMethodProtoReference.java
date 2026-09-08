package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderMethodProtoReference extends BaseMethodProtoReference implements MethodProtoReference, BuilderReference {
    int index = -1;

    @Nonnull
    final BuilderTypeList parameterTypes;

    @Nonnull
    final BuilderTypeReference returnType;

    @Nonnull
    final BuilderStringReference shorty;

    public BuilderMethodProtoReference(@Nonnull BuilderStringReference shorty, @Nonnull BuilderTypeList parameterTypes, @Nonnull BuilderTypeReference returnType) {
        this.shorty = shorty;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public List<? extends CharSequence> getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public String getReturnType() {
        return this.returnType.getType();
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
