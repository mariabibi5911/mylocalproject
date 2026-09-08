package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.CharEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.LongEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue;
import com.android.tools.smali.dexlib2.writer.DexWriter;
import com.android.tools.smali.dexlib2.writer.io.DexDataStore;
import com.android.tools.smali.dexlib2.writer.io.FileDataStore;
import com.android.tools.smali.dexlib2.writer.pool.TypeListPool;
import com.android.tools.smali.util.ExceptionWithContext;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexPool extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool> {
    private final BasePool<?, ?>[] sections;

    @Override // com.android.tools.smali.dexlib2.writer.DexWriter
    protected /* bridge */ /* synthetic */ void writeEncodedValue(@Nonnull DexWriter.InternalEncodedValueWriter internalEncodedValueWriter, @Nonnull EncodedValue encodedValue) throws IOException {
        writeEncodedValue2((DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.InternalEncodedValueWriter) internalEncodedValueWriter, encodedValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DexPool(Opcodes opcodes) {
        super(opcodes);
        this.sections = new BasePool[]{(BasePool) this.stringSection, (BasePool) this.typeSection, (BasePool) this.protoSection, (BasePool) this.fieldSection, (BasePool) this.methodSection, (BasePool) this.classSection, (BasePool) this.callSiteSection, (BasePool) this.methodHandleSection, (BasePool) this.typeListSection, (BasePool) this.annotationSection, (BasePool) this.annotationSetSection, (BasePool) this.encodedArraySection};
    }

    @Override // com.android.tools.smali.dexlib2.writer.DexWriter
    @Nonnull
    protected DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.SectionProvider getSectionProvider() {
        return new DexPoolSectionProvider();
    }

    public static void writeTo(@Nonnull DexDataStore dataStore, @Nonnull DexFile input) throws IOException {
        DexPool dexPool = new DexPool(input.getOpcodes());
        for (ClassDef classDef : input.getClasses()) {
            dexPool.internClass(classDef);
        }
        dexPool.writeTo(dataStore);
    }

    public static void writeTo(@Nonnull String path, @Nonnull DexFile input) throws IOException {
        DexPool dexPool = new DexPool(input.getOpcodes());
        for (ClassDef classDef : input.getClasses()) {
            dexPool.internClass(classDef);
        }
        dexPool.writeTo(new FileDataStore(new File(path)));
    }

    public void internClass(ClassDef classDef) {
        ((ClassPool) this.classSection).intern(classDef);
    }

    public void mark() {
        for (Markable section : this.sections) {
            section.mark();
        }
    }

    public void reset() {
        for (Markable section : this.sections) {
            section.reset();
        }
    }

    /* renamed from: writeEncodedValue, reason: avoid collision after fix types in other method */
    protected void writeEncodedValue2(@Nonnull DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.InternalEncodedValueWriter writer, @Nonnull EncodedValue encodedValue) throws IOException {
        switch (encodedValue.getValueType()) {
            case 0:
                writer.writeByte(((ByteEncodedValue) encodedValue).getValue());
                return;
            case 1:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            case 20:
            default:
                throw new ExceptionWithContext("Unrecognized value type: %d", Integer.valueOf(encodedValue.getValueType()));
            case 2:
                writer.writeShort(((ShortEncodedValue) encodedValue).getValue());
                return;
            case 3:
                writer.writeChar(((CharEncodedValue) encodedValue).getValue());
                return;
            case 4:
                writer.writeInt(((IntEncodedValue) encodedValue).getValue());
                return;
            case 6:
                writer.writeLong(((LongEncodedValue) encodedValue).getValue());
                return;
            case 16:
                writer.writeFloat(((FloatEncodedValue) encodedValue).getValue());
                return;
            case 17:
                writer.writeDouble(((DoubleEncodedValue) encodedValue).getValue());
                return;
            case 21:
                writer.writeMethodType(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                writer.writeMethodHandle(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                writer.writeString(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                writer.writeType(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                writer.writeField(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                writer.writeMethod(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                writer.writeEnum(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                ArrayEncodedValue arrayEncodedValue = (ArrayEncodedValue) encodedValue;
                writer.writeArray(arrayEncodedValue.getValue());
                return;
            case 29:
                AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue) encodedValue;
                writer.writeAnnotation(annotationEncodedValue.getType(), annotationEncodedValue.getElements());
                return;
            case 30:
                writer.writeNull();
                return;
            case 31:
                writer.writeBoolean(((BooleanEncodedValue) encodedValue).getValue());
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void internEncodedValue(@Nonnull EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 21:
                ((ProtoPool) this.protoSection).intern(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                ((MethodHandlePool) this.methodHandleSection).intern(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                ((StringPool) this.stringSection).intern(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                ((TypePool) this.typeSection).intern(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                ((FieldPool) this.fieldSection).intern(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                ((MethodPool) this.methodSection).intern(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                ((FieldPool) this.fieldSection).intern(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                Iterator<? extends EncodedValue> it = ((ArrayEncodedValue) encodedValue).getValue().iterator();
                while (it.hasNext()) {
                    internEncodedValue(it.next());
                }
                return;
            case 29:
                AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue) encodedValue;
                ((TypePool) this.typeSection).intern(annotationEncodedValue.getType());
                for (AnnotationElement element : annotationEncodedValue.getElements()) {
                    ((StringPool) this.stringSection).intern(element.getName());
                    internEncodedValue(element.getValue());
                }
                return;
            default:
                return;
        }
    }

    /* loaded from: classes.dex */
    protected class DexPoolSectionProvider extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.SectionProvider {
        protected DexPoolSectionProvider() {
            super();
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public StringPool getStringSection() {
            return new StringPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public TypePool getTypeSection() {
            return new TypePool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public ProtoPool getProtoSection() {
            return new ProtoPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public FieldPool getFieldSection() {
            return new FieldPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public MethodPool getMethodSection() {
            return new MethodPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public ClassPool getClassSection() {
            return new ClassPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public CallSitePool getCallSiteSection() {
            return new CallSitePool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public MethodHandlePool getMethodHandleSection() {
            return new MethodHandlePool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public TypeListPool getTypeListSection() {
            return new TypeListPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public AnnotationPool getAnnotationSection() {
            return new AnnotationPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public AnnotationSetPool getAnnotationSetSection() {
            return new AnnotationSetPool(DexPool.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public EncodedArrayPool getEncodedArraySection() {
            return new EncodedArrayPool(DexPool.this);
        }
    }
}
