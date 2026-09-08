package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseStringReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderStringReference extends BaseStringReference implements BuilderReference {
    int index = -1;

    @Nonnull
    final String string;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderStringReference(@Nonnull String string) {
        this.string = string;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return this.string;
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
