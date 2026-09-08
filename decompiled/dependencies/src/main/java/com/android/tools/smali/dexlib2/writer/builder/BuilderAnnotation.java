package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.BaseAnnotation;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
class BuilderAnnotation extends BaseAnnotation {

    @Nonnull
    final Set<? extends BuilderAnnotationElement> elements;
    int offset = 0;

    @Nonnull
    final BuilderTypeReference type;
    int visibility;

    public BuilderAnnotation(int visibility, @Nonnull BuilderTypeReference type, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
        this.visibility = visibility;
        this.type = type;
        this.elements = elements;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation
    public int getVisibility() {
        return this.visibility;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends BuilderAnnotationElement> getElements() {
        return this.elements;
    }
}
