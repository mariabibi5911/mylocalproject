package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseAnnotationEncodedValue;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import com.android.tools.smali.dexlib2.immutable.ImmutableAnnotationElement;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableAnnotationEncodedValue extends BaseAnnotationEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotationElement> elements;

    @Nonnull
    protected final String type;

    public ImmutableAnnotationEncodedValue(@Nonnull String type, @Nullable Collection<? extends AnnotationElement> elements) {
        this.type = type;
        this.elements = ImmutableAnnotationElement.immutableSetOf(elements);
    }

    public ImmutableAnnotationEncodedValue(@Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotationElement> elements) {
        this.type = type;
        this.elements = ImmutableUtils.nullToEmptySet(elements);
    }

    public static ImmutableAnnotationEncodedValue of(AnnotationEncodedValue annotationEncodedValue) {
        if (annotationEncodedValue instanceof ImmutableAnnotationEncodedValue) {
            return (ImmutableAnnotationEncodedValue) annotationEncodedValue;
        }
        return new ImmutableAnnotationEncodedValue(annotationEncodedValue.getType(), annotationEncodedValue.getElements());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public ImmutableSet<? extends ImmutableAnnotationElement> getElements() {
        return this.elements;
    }
}
