package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodHandleReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderMethodHandleReference extends BaseMethodHandleReference implements BuilderReference {
    int index = -1;

    @Nonnull
    final BuilderReference memberReference;
    final int methodHandleType;

    public BuilderMethodHandleReference(int methodHandleType, @Nonnull BuilderReference memberReference) {
        this.methodHandleType = methodHandleType;
        this.memberReference = memberReference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
    public int getMethodHandleType() {
        return this.methodHandleType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
    @Nonnull
    public BuilderReference getMemberReference() {
        return this.memberReference;
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
