package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class EncodedValueWriter<StringKey, TypeKey, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, AnnotationElement extends AnnotationElement, ProtoRefKey, MethodHandleKey extends MethodHandleReference, EncodedValue> {

    @Nonnull
    private final AnnotationSection<StringKey, TypeKey, ?, AnnotationElement, EncodedValue> annotationSection;

    @Nonnull
    private final FieldSection<?, ?, FieldRefKey, ?> fieldSection;

    @Nonnull
    private final MethodHandleSection<MethodHandleKey, ?, ?> methodHandleSection;

    @Nonnull
    private final MethodSection<?, ?, ?, MethodRefKey, ?> methodSection;

    @Nonnull
    private final ProtoSection<?, ?, ProtoRefKey, ?> protoSection;

    @Nonnull
    private final StringSection<StringKey, ?> stringSection;

    @Nonnull
    private final TypeSection<?, TypeKey, ?> typeSection;

    @Nonnull
    private final DexDataWriter writer;

    protected abstract void writeEncodedValue(@Nonnull EncodedValue encodedvalue) throws IOException;

    public EncodedValueWriter(@Nonnull DexDataWriter writer, @Nonnull StringSection<StringKey, ?> stringSection, @Nonnull TypeSection<?, TypeKey, ?> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, ProtoSection<?, ?, ProtoRefKey, ?> protoSection, MethodHandleSection<MethodHandleKey, ?, ?> methodHandleSection, @Nonnull AnnotationSection<StringKey, TypeKey, ?, AnnotationElement, EncodedValue> annotationSection) {
        this.writer = writer;
        this.stringSection = stringSection;
        this.typeSection = typeSection;
        this.fieldSection = fieldSection;
        this.methodSection = methodSection;
        this.protoSection = protoSection;
        this.methodHandleSection = methodHandleSection;
        this.annotationSection = annotationSection;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeAnnotation(TypeKey typekey, Collection<? extends AnnotationElement> collection) throws IOException {
        this.writer.writeEncodedValueHeader(29, 0);
        this.writer.writeUleb128(this.typeSection.getItemIndex((TypeSection<?, TypeKey, ?>) typekey));
        this.writer.writeUleb128(collection.size());
        for (AnnotationElement annotationElement : Ordering.from(BaseAnnotationElement.BY_NAME).immutableSortedCopy(collection)) {
            this.writer.writeUleb128(this.stringSection.getItemIndex((StringSection<StringKey, ?>) this.annotationSection.getElementName(annotationElement)));
            writeEncodedValue(this.annotationSection.getElementValue(annotationElement));
        }
    }

    public void writeArray(Collection<? extends EncodedValue> elements) throws IOException {
        this.writer.writeEncodedValueHeader(28, 0);
        this.writer.writeUleb128(elements.size());
        for (EncodedValue element : elements) {
            writeEncodedValue(element);
        }
    }

    public void writeBoolean(boolean z) throws IOException {
        this.writer.writeEncodedValueHeader(31, z ? 1 : 0);
    }

    public void writeByte(byte value) throws IOException {
        this.writer.writeEncodedInt(0, value);
    }

    public void writeChar(char value) throws IOException {
        this.writer.writeEncodedUint(3, value);
    }

    public void writeDouble(double value) throws IOException {
        this.writer.writeEncodedDouble(17, value);
    }

    public void writeEnum(@Nonnull FieldRefKey value) throws IOException {
        this.writer.writeEncodedUint(27, this.fieldSection.getItemIndex(value));
    }

    public void writeField(@Nonnull FieldRefKey value) throws IOException {
        this.writer.writeEncodedUint(25, this.fieldSection.getItemIndex(value));
    }

    public void writeFloat(float value) throws IOException {
        this.writer.writeEncodedFloat(16, value);
    }

    public void writeInt(int value) throws IOException {
        this.writer.writeEncodedInt(4, value);
    }

    public void writeLong(long value) throws IOException {
        this.writer.writeEncodedLong(6, value);
    }

    public void writeMethod(@Nonnull MethodRefKey value) throws IOException {
        this.writer.writeEncodedUint(26, this.methodSection.getItemIndex(value));
    }

    public void writeNull() throws IOException {
        this.writer.write(30);
    }

    public void writeShort(int value) throws IOException {
        this.writer.writeEncodedInt(2, value);
    }

    public void writeString(@Nonnull StringKey value) throws IOException {
        this.writer.writeEncodedUint(23, this.stringSection.getItemIndex((StringSection<StringKey, ?>) value));
    }

    public void writeType(@Nonnull TypeKey value) throws IOException {
        this.writer.writeEncodedUint(24, this.typeSection.getItemIndex((TypeSection<?, TypeKey, ?>) value));
    }

    public void writeMethodType(@Nonnull ProtoRefKey value) throws IOException {
        this.writer.writeEncodedUint(21, this.protoSection.getItemIndex(value));
    }

    public void writeMethodHandle(@Nonnull MethodHandleKey value) throws IOException {
        this.writer.writeEncodedUint(22, this.methodHandleSection.getItemIndex(value));
    }
}
