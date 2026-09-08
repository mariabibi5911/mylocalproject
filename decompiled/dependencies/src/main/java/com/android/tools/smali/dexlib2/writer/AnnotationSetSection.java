package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.iface.Annotation;
import java.util.Collection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface AnnotationSetSection<AnnotationKey extends Annotation, AnnotationSetKey> extends NullableOffsetSection<AnnotationSetKey> {
    @Nonnull
    Collection<? extends AnnotationKey> getAnnotations(@Nonnull AnnotationSetKey annotationsetkey);
}
