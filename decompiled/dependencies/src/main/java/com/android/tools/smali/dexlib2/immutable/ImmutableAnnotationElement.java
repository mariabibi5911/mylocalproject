package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableAnnotationElement extends BaseAnnotationElement {
    private static final ImmutableConverter<ImmutableAnnotationElement, AnnotationElement> CONVERTER = new ImmutableConverter<ImmutableAnnotationElement, AnnotationElement>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableAnnotationElement.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull AnnotationElement item) {
            return item instanceof ImmutableAnnotationElement;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableAnnotationElement makeImmutable(@Nonnull AnnotationElement item) {
            return ImmutableAnnotationElement.of(item);
        }
    };

    @Nonnull
    protected final String name;

    @Nonnull
    protected final ImmutableEncodedValue value;

    public ImmutableAnnotationElement(@Nonnull String name, @Nonnull EncodedValue value) {
        this.name = name;
        this.value = ImmutableEncodedValueFactory.of(value);
    }

    public ImmutableAnnotationElement(@Nonnull String name, @Nonnull ImmutableEncodedValue value) {
        this.name = name;
        this.value = value;
    }

    public static ImmutableAnnotationElement of(AnnotationElement annotationElement) {
        if (annotationElement instanceof ImmutableAnnotationElement) {
            return (ImmutableAnnotationElement) annotationElement;
        }
        return new ImmutableAnnotationElement(annotationElement.getName(), annotationElement.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }

    @Nonnull
    public static ImmutableSet<ImmutableAnnotationElement> immutableSetOf(@Nullable Iterable<? extends AnnotationElement> list) {
        return CONVERTER.toSet(list);
    }
}
