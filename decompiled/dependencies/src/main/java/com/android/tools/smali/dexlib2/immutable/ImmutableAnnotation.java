package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.BaseAnnotation;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableAnnotation extends BaseAnnotation {
    private static final ImmutableConverter<ImmutableAnnotation, Annotation> CONVERTER = new ImmutableConverter<ImmutableAnnotation, Annotation>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableAnnotation.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull Annotation item) {
            return item instanceof ImmutableAnnotation;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableAnnotation makeImmutable(@Nonnull Annotation item) {
            return ImmutableAnnotation.of(item);
        }
    };

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotationElement> elements;

    @Nonnull
    protected final String type;
    protected final int visibility;

    public ImmutableAnnotation(int visibility, @Nonnull String type, @Nullable Collection<? extends AnnotationElement> elements) {
        this.visibility = visibility;
        this.type = type;
        this.elements = ImmutableAnnotationElement.immutableSetOf(elements);
    }

    public ImmutableAnnotation(int visibility, @Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotationElement> elements) {
        this.visibility = visibility;
        this.type = type;
        this.elements = ImmutableUtils.nullToEmptySet(elements);
    }

    public static ImmutableAnnotation of(Annotation annotation) {
        if (annotation instanceof ImmutableAnnotation) {
            return (ImmutableAnnotation) annotation;
        }
        return new ImmutableAnnotation(annotation.getVisibility(), annotation.getType(), annotation.getElements());
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation
    public int getVisibility() {
        return this.visibility;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    public ImmutableSet<? extends ImmutableAnnotationElement> getElements() {
        return this.elements;
    }

    @Nonnull
    public static ImmutableSet<ImmutableAnnotation> immutableSetOf(@Nullable Iterable<? extends Annotation> list) {
        return CONVERTER.toSet(list);
    }
}
