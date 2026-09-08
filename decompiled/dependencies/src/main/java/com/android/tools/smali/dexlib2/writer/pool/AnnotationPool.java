package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.writer.AnnotationSection;
import java.util.Collection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class AnnotationPool extends BaseOffsetPool<Annotation> implements AnnotationSection<CharSequence, CharSequence, Annotation, AnnotationElement, EncodedValue> {
    public AnnotationPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull Annotation annotation) {
        Integer prev = (Integer) this.internedItems.put(annotation, 0);
        if (prev == null) {
            ((TypePool) this.dexPool.typeSection).intern(annotation.getType());
            for (AnnotationElement element : annotation.getElements()) {
                ((StringPool) this.dexPool.stringSection).intern(element.getName());
                this.dexPool.internEncodedValue(element.getValue());
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSection
    public int getVisibility(@Nonnull Annotation annotation) {
        return annotation.getVisibility();
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSection
    @Nonnull
    public CharSequence getType(@Nonnull Annotation annotation) {
        return annotation.getType();
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSection
    @Nonnull
    public Collection<? extends AnnotationElement> getElements(@Nonnull Annotation annotation) {
        return annotation.getElements();
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSection
    @Nonnull
    public CharSequence getElementName(@Nonnull AnnotationElement annotationElement) {
        return annotationElement.getName();
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSection
    @Nonnull
    public EncodedValue getElementValue(@Nonnull AnnotationElement annotationElement) {
        return annotationElement.getValue();
    }
}
