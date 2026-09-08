package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderAnnotationElement extends BaseAnnotationElement {

    @Nonnull
    final BuilderStringReference name;

    @Nonnull
    final BuilderEncodedValues.BuilderEncodedValue value;

    public BuilderAnnotationElement(@Nonnull BuilderStringReference name, @Nonnull BuilderEncodedValues.BuilderEncodedValue value) {
        this.name = name;
        this.value = value;
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return this.name.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }
}
