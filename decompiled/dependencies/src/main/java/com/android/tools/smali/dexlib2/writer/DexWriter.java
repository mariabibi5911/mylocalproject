package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.base.BaseAnnotation;
import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.builder.BuilderTryBlock;
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31c;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.dexbacked.raw.ItemType;
import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.LineNumber;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11n;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction12x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21ih;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22b;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22cs;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction23x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction30t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31i;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35mi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35ms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction4rcc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction51l;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.util.InstructionUtil;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.dexlib2.writer.AnnotationSection;
import com.android.tools.smali.dexlib2.writer.AnnotationSetSection;
import com.android.tools.smali.dexlib2.writer.CallSiteSection;
import com.android.tools.smali.dexlib2.writer.ClassSection;
import com.android.tools.smali.dexlib2.writer.EncodedArraySection;
import com.android.tools.smali.dexlib2.writer.FieldSection;
import com.android.tools.smali.dexlib2.writer.MethodHandleSection;
import com.android.tools.smali.dexlib2.writer.MethodSection;
import com.android.tools.smali.dexlib2.writer.ProtoSection;
import com.android.tools.smali.dexlib2.writer.StringSection;
import com.android.tools.smali.dexlib2.writer.TypeListSection;
import com.android.tools.smali.dexlib2.writer.TypeSection;
import com.android.tools.smali.dexlib2.writer.io.DeferredOutputStream;
import com.android.tools.smali.dexlib2.writer.io.DeferredOutputStreamFactory;
import com.android.tools.smali.dexlib2.writer.io.DexDataStore;
import com.android.tools.smali.dexlib2.writer.io.MemoryDeferredOutputStream;
import com.android.tools.smali.dexlib2.writer.util.TryListBuilder;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.CharSequence;
import java.lang.Comparable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Adler32;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class DexWriter<StringKey extends CharSequence, StringRef extends StringReference, TypeKey extends CharSequence, TypeRef extends TypeReference, ProtoRefKey extends MethodProtoReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ClassKey extends Comparable<? super ClassKey>, CallSiteKey extends CallSiteReference, MethodHandleKey extends MethodHandleReference, AnnotationKey extends Annotation, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement extends AnnotationElement, StringSectionType extends StringSection<StringKey, StringRef>, TypeSectionType extends TypeSection<StringKey, TypeKey, TypeRef>, ProtoSectionType extends ProtoSection<StringKey, TypeKey, ProtoRefKey, TypeListKey>, FieldSectionType extends FieldSection<StringKey, TypeKey, FieldRefKey, FieldKey>, MethodSectionType extends MethodSection<StringKey, TypeKey, ProtoRefKey, MethodRefKey, MethodKey>, ClassSectionType extends ClassSection<StringKey, TypeKey, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedArrayKey>, CallSiteSectionType extends CallSiteSection<CallSiteKey, EncodedArrayKey>, MethodHandleSectionType extends MethodHandleSection<MethodHandleKey, FieldRefKey, MethodRefKey>, TypeListSectionType extends TypeListSection<TypeKey, TypeListKey>, AnnotationSectionType extends AnnotationSection<StringKey, TypeKey, AnnotationKey, AnnotationElement, EncodedValue>, AnnotationSetSectionType extends AnnotationSetSection<AnnotationKey, AnnotationSetKey>, EncodedArraySectionType extends EncodedArraySection<EncodedArrayKey, EncodedValue>> {
    public static final int MAX_POOL_SIZE = 65536;
    public static final int NO_INDEX = -1;
    public static final int NO_OFFSET = 0;
    private static Comparator<Map.Entry> toStringKeyComparator = new Comparator<Map.Entry>() { // from class: com.android.tools.smali.dexlib2.writer.DexWriter.2
        @Override // java.util.Comparator
        public int compare(Map.Entry o1, Map.Entry o2) {
            return o1.getKey().toString().compareTo(o2.getKey().toString());
        }
    };
    public final AnnotationSectionType annotationSection;
    public final AnnotationSetSectionType annotationSetSection;
    public final CallSiteSectionType callSiteSection;
    public final ClassSectionType classSection;
    public final EncodedArraySectionType encodedArraySection;
    public final FieldSectionType fieldSection;
    public final MethodHandleSectionType methodHandleSection;
    public final MethodSectionType methodSection;
    protected final Opcodes opcodes;
    private final IndexSection<?>[] overflowableSections;
    public final ProtoSectionType protoSection;
    public final StringSectionType stringSection;
    public final TypeListSectionType typeListSection;
    public final TypeSectionType typeSection;
    protected int stringIndexSectionOffset = 0;
    protected int typeSectionOffset = 0;
    protected int protoSectionOffset = 0;
    protected int fieldSectionOffset = 0;
    protected int methodSectionOffset = 0;
    protected int classIndexSectionOffset = 0;
    protected int callSiteSectionOffset = 0;
    protected int methodHandleSectionOffset = 0;
    protected int stringDataSectionOffset = 0;
    protected int classDataSectionOffset = 0;
    protected int typeListSectionOffset = 0;
    protected int encodedArraySectionOffset = 0;
    protected int annotationSectionOffset = 0;
    protected int annotationSetSectionOffset = 0;
    protected int annotationSetRefSectionOffset = 0;
    protected int annotationDirectorySectionOffset = 0;
    protected int debugSectionOffset = 0;
    protected int codeSectionOffset = 0;
    protected int hiddenApiRestrictionsOffset = 0;
    protected int mapSectionOffset = 0;
    protected boolean hasHiddenApiRestrictions = false;
    protected int numAnnotationSetRefItems = 0;
    protected int numAnnotationDirectoryItems = 0;
    protected int numDebugInfoItems = 0;
    protected int numCodeItemItems = 0;
    protected int numClassDataItems = 0;
    private Comparator<Map.Entry<? extends CallSiteKey, Integer>> callSiteComparator = (Comparator<Map.Entry<? extends CallSiteKey, Integer>>) new Comparator<Map.Entry<? extends CallSiteKey, Integer>>() { // from class: com.android.tools.smali.dexlib2.writer.DexWriter.1
        @Override // java.util.Comparator
        public int compare(Map.Entry<? extends CallSiteKey, Integer> entry, Map.Entry<? extends CallSiteKey, Integer> entry2) {
            return Ints.compare(DexWriter.this.encodedArraySection.getItemOffset(DexWriter.this.callSiteSection.getEncodedCallSite(entry.getKey())), DexWriter.this.encodedArraySection.getItemOffset(DexWriter.this.callSiteSection.getEncodedCallSite(entry2.getKey())));
        }
    };

    @Nonnull
    protected abstract DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.SectionProvider getSectionProvider();

    protected abstract void writeEncodedValue(@Nonnull DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.InternalEncodedValueWriter internalEncodedValueWriter, @Nonnull EncodedValue encodedvalue) throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public DexWriter(Opcodes opcodes) {
        this.opcodes = opcodes;
        DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.SectionProvider sectionProvider = getSectionProvider();
        this.stringSection = (StringSectionType) sectionProvider.getStringSection();
        TypeSectionType typesectiontype = (TypeSectionType) sectionProvider.getTypeSection();
        this.typeSection = typesectiontype;
        ProtoSectionType protosectiontype = (ProtoSectionType) sectionProvider.getProtoSection();
        this.protoSection = protosectiontype;
        FieldSectionType fieldsectiontype = (FieldSectionType) sectionProvider.getFieldSection();
        this.fieldSection = fieldsectiontype;
        MethodSectionType methodsectiontype = (MethodSectionType) sectionProvider.getMethodSection();
        this.methodSection = methodsectiontype;
        this.classSection = (ClassSectionType) sectionProvider.getClassSection();
        CallSiteSectionType callsitesectiontype = (CallSiteSectionType) sectionProvider.getCallSiteSection();
        this.callSiteSection = callsitesectiontype;
        MethodHandleSectionType methodhandlesectiontype = (MethodHandleSectionType) sectionProvider.getMethodHandleSection();
        this.methodHandleSection = methodhandlesectiontype;
        this.typeListSection = (TypeListSectionType) sectionProvider.getTypeListSection();
        this.annotationSection = (AnnotationSectionType) sectionProvider.getAnnotationSection();
        this.annotationSetSection = (AnnotationSetSectionType) sectionProvider.getAnnotationSetSection();
        this.encodedArraySection = (EncodedArraySectionType) sectionProvider.getEncodedArraySection();
        this.overflowableSections = new IndexSection[]{typesectiontype, protosectiontype, fieldsectiontype, methodsectiontype, callsitesectiontype, methodhandlesectiontype};
    }

    private static <T extends Comparable<? super T>> Comparator<Map.Entry<? extends T, ?>> comparableKeyComparator() {
        return (Comparator<Map.Entry<? extends T, ?>>) new Comparator<Map.Entry<? extends T, ?>>() { // from class: com.android.tools.smali.dexlib2.writer.DexWriter.3
            @Override // java.util.Comparator
            public int compare(Map.Entry<? extends T, ?> o1, Map.Entry<? extends T, ?> o2) {
                return ((Comparable) o1.getKey()).compareTo(o2.getKey());
            }
        };
    }

    private static <T extends Comparable<? super T>> Comparator<Map.Entry<?, ? extends T>> comparableValueComparator() {
        return (Comparator<Map.Entry<?, ? extends T>>) new Comparator<Map.Entry<?, ? extends T>>() { // from class: com.android.tools.smali.dexlib2.writer.DexWriter.4
            @Override // java.util.Comparator
            public int compare(Map.Entry<?, ? extends T> o1, Map.Entry<?, ? extends T> o2) {
                return ((Comparable) o1.getValue()).compareTo(o2.getValue());
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class InternalEncodedValueWriter extends EncodedValueWriter<StringKey, TypeKey, FieldRefKey, MethodRefKey, AnnotationElement, ProtoRefKey, MethodHandleKey, EncodedValue> {
        private InternalEncodedValueWriter(@Nonnull DexDataWriter writer) {
            super(writer, DexWriter.this.stringSection, DexWriter.this.typeSection, DexWriter.this.fieldSection, DexWriter.this.methodSection, DexWriter.this.protoSection, DexWriter.this.methodHandleSection, DexWriter.this.annotationSection);
        }

        @Override // com.android.tools.smali.dexlib2.writer.EncodedValueWriter
        protected void writeEncodedValue(@Nonnull EncodedValue encodedValue) throws IOException {
            DexWriter.this.writeEncodedValue(this, encodedValue);
        }
    }

    private int getDataSectionOffset() {
        return (this.stringSection.getItemCount() * 4) + 112 + (this.typeSection.getItemCount() * 4) + (this.protoSection.getItemCount() * 12) + (this.fieldSection.getItemCount() * 8) + (this.methodSection.getItemCount() * 8) + (this.classSection.getItemCount() * 32) + (this.callSiteSection.getItemCount() * 4) + (this.methodHandleSection.getItemCount() * 8);
    }

    @Nonnull
    public List<String> getMethodReferences() {
        List<String> methodReferences = Lists.newArrayList();
        for (Map.Entry<? extends MethodRefKey, Integer> methodReference : this.methodSection.getItems()) {
            methodReferences.add(DexFormatter.INSTANCE.getMethodDescriptor((MethodReference) methodReference.getKey()));
        }
        return methodReferences;
    }

    @Nonnull
    public List<String> getFieldReferences() {
        List<String> fieldReferences = Lists.newArrayList();
        for (Map.Entry<? extends FieldRefKey, Integer> fieldReference : this.fieldSection.getItems()) {
            fieldReferences.add(DexFormatter.INSTANCE.getFieldDescriptor((FieldReference) fieldReference.getKey()));
        }
        return fieldReferences;
    }

    @Nonnull
    public List<String> getTypeReferences() {
        List<String> classReferences = Lists.newArrayList();
        for (Map.Entry<? extends TypeKey, Integer> typeReference : this.typeSection.getItems()) {
            classReferences.add(((CharSequence) typeReference.getKey()).toString());
        }
        return classReferences;
    }

    public boolean hasOverflowed() {
        return hasOverflowed(65536);
    }

    public boolean hasOverflowed(int maxPoolSize) {
        for (IndexSection section : this.overflowableSections) {
            if (section.getItemCount() > maxPoolSize) {
                return true;
            }
        }
        return false;
    }

    public void writeTo(@Nonnull DexDataStore dest) throws IOException {
        writeTo(dest, MemoryDeferredOutputStream.getFactory());
    }

    /* JADX WARN: Finally extract failed */
    public void writeTo(@Nonnull DexDataStore dest, @Nonnull DeferredOutputStreamFactory tempFactory) throws IOException {
        try {
            int dataSectionOffset = getDataSectionOffset();
            DexDataWriter headerWriter = outputAt(dest, 0);
            DexDataWriter indexWriter = outputAt(dest, 112);
            DexDataWriter offsetWriter = outputAt(dest, dataSectionOffset);
            try {
                writeStrings(indexWriter, offsetWriter);
                writeTypes(indexWriter);
                writeTypeLists(offsetWriter);
                writeProtos(indexWriter);
                writeFields(indexWriter);
                writeMethods(indexWriter);
                DexDataWriter methodHandleWriter = outputAt(dest, indexWriter.getPosition() + (this.classSection.getItemCount() * 32) + (this.callSiteSection.getItemCount() * 4));
                try {
                    writeMethodHandles(methodHandleWriter);
                    methodHandleWriter.close();
                    writeEncodedArrays(offsetWriter);
                    DexDataWriter callSiteWriter = outputAt(dest, indexWriter.getPosition() + (this.classSection.getItemCount() * 32));
                    try {
                        writeCallSites(callSiteWriter);
                        callSiteWriter.close();
                        writeAnnotations(offsetWriter);
                        writeAnnotationSets(offsetWriter);
                        writeAnnotationSetRefs(offsetWriter);
                        writeAnnotationDirectories(offsetWriter);
                        writeDebugAndCodeItems(offsetWriter, tempFactory.makeDeferredOutputStream());
                        writeClasses(dest, indexWriter, offsetWriter);
                        writeMapItem(offsetWriter);
                        writeHeader(headerWriter, dataSectionOffset, offsetWriter.getPosition());
                        headerWriter.close();
                        indexWriter.close();
                        offsetWriter.close();
                        updateSignature(dest);
                        updateChecksum(dest);
                    } catch (Throwable th) {
                        callSiteWriter.close();
                        throw th;
                    }
                } catch (Throwable th2) {
                    methodHandleWriter.close();
                    throw th2;
                }
            } catch (Throwable th3) {
                headerWriter.close();
                indexWriter.close();
                offsetWriter.close();
                throw th3;
            }
        } finally {
            dest.close();
        }
    }

    private void updateSignature(@Nonnull DexDataStore dataStore) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[4096];
            InputStream input = dataStore.readAt(32);
            for (int bytesRead = input.read(buffer); bytesRead >= 0; bytesRead = input.read(buffer)) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] signature = md.digest();
            if (signature.length != 20) {
                throw new RuntimeException("unexpected digest write: " + signature.length + " bytes");
            }
            OutputStream output = dataStore.outputAt(12);
            output.write(signature);
            output.close();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateChecksum(@Nonnull DexDataStore dataStore) throws IOException {
        Adler32 a32 = new Adler32();
        byte[] buffer = new byte[4096];
        InputStream input = dataStore.readAt(12);
        for (int bytesRead = input.read(buffer); bytesRead >= 0; bytesRead = input.read(buffer)) {
            a32.update(buffer, 0, bytesRead);
        }
        OutputStream output = dataStore.outputAt(8);
        DexDataWriter.writeInt(output, (int) a32.getValue());
        output.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DexDataWriter outputAt(DexDataStore dataStore, int filePosition) throws IOException {
        return new DexDataWriter(dataStore.outputAt(filePosition), filePosition);
    }

    private void writeStrings(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
        this.stringIndexSectionOffset = indexWriter.getPosition();
        this.stringDataSectionOffset = offsetWriter.getPosition();
        int index = 0;
        List<Map.Entry<? extends StringKey, Integer>> stringEntries = Lists.newArrayList(this.stringSection.getItems());
        Collections.sort(stringEntries, toStringKeyComparator);
        for (Map.Entry<? extends StringKey, Integer> entry : stringEntries) {
            int index2 = index + 1;
            entry.setValue(Integer.valueOf(index));
            indexWriter.writeInt(offsetWriter.getPosition());
            String stringValue = ((CharSequence) entry.getKey()).toString();
            offsetWriter.writeUleb128(stringValue.length());
            offsetWriter.writeString(stringValue);
            offsetWriter.write(0);
            index = index2;
        }
    }

    private void writeTypes(@Nonnull DexDataWriter writer) throws IOException {
        this.typeSectionOffset = writer.getPosition();
        int index = 0;
        List<Map.Entry<? extends TypeKey, Integer>> typeEntries = Lists.newArrayList(this.typeSection.getItems());
        Collections.sort(typeEntries, toStringKeyComparator);
        for (Map.Entry<? extends TypeKey, Integer> entry : typeEntries) {
            entry.setValue(Integer.valueOf(index));
            writer.writeInt(this.stringSection.getItemIndex(this.typeSection.getString(entry.getKey())));
            index++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeProtos(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        this.protoSectionOffset = dexDataWriter.getPosition();
        int i = 0;
        ArrayList<Map.Entry> newArrayList = Lists.newArrayList(this.protoSection.getItems());
        Collections.sort(newArrayList, comparableKeyComparator());
        for (Map.Entry entry : newArrayList) {
            int i2 = i + 1;
            entry.setValue(Integer.valueOf(i));
            MethodProtoReference methodProtoReference = (MethodProtoReference) entry.getKey();
            dexDataWriter.writeInt(this.stringSection.getItemIndex(this.protoSection.getShorty(methodProtoReference)));
            dexDataWriter.writeInt(this.typeSection.getItemIndex(this.protoSection.getReturnType(methodProtoReference)));
            dexDataWriter.writeInt(this.typeListSection.getNullableItemOffset(this.protoSection.getParameters(methodProtoReference)));
            i = i2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeFields(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        this.fieldSectionOffset = dexDataWriter.getPosition();
        int i = 0;
        ArrayList<Map.Entry> newArrayList = Lists.newArrayList(this.fieldSection.getItems());
        Collections.sort(newArrayList, comparableKeyComparator());
        for (Map.Entry entry : newArrayList) {
            int i2 = i + 1;
            entry.setValue(Integer.valueOf(i));
            FieldReference fieldReference = (FieldReference) entry.getKey();
            dexDataWriter.writeUshort(this.typeSection.getItemIndex(this.fieldSection.getDefiningClass(fieldReference)));
            dexDataWriter.writeUshort(this.typeSection.getItemIndex(this.fieldSection.getFieldType(fieldReference)));
            dexDataWriter.writeInt(this.stringSection.getItemIndex(this.fieldSection.getName(fieldReference)));
            i = i2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeMethods(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        this.methodSectionOffset = dexDataWriter.getPosition();
        int i = 0;
        ArrayList<Map.Entry> newArrayList = Lists.newArrayList(this.methodSection.getItems());
        Collections.sort(newArrayList, comparableKeyComparator());
        for (Map.Entry entry : newArrayList) {
            int i2 = i + 1;
            entry.setValue(Integer.valueOf(i));
            MethodReference methodReference = (MethodReference) entry.getKey();
            dexDataWriter.writeUshort(this.typeSection.getItemIndex(this.methodSection.getDefiningClass(methodReference)));
            dexDataWriter.writeUshort(this.protoSection.getItemIndex(this.methodSection.getPrototype(methodReference)));
            dexDataWriter.writeInt(this.stringSection.getItemIndex(this.methodSection.getName(methodReference)));
            i = i2;
        }
    }

    private void writeClasses(@Nonnull DexDataStore dataStore, @Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
        this.classIndexSectionOffset = indexWriter.getPosition();
        this.classDataSectionOffset = offsetWriter.getPosition();
        List<Map.Entry<? extends ClassKey, Integer>> classEntriesKeySorted = Lists.newArrayList(this.classSection.getItems());
        Collections.sort(classEntriesKeySorted, comparableKeyComparator());
        int index = 0;
        Iterator<Map.Entry<? extends ClassKey, Integer>> it = classEntriesKeySorted.iterator();
        while (it.hasNext()) {
            index = writeClass(indexWriter, offsetWriter, index, it.next());
        }
        if (!shouldWriteHiddenApiRestrictions()) {
            return;
        }
        offsetWriter.align();
        this.hiddenApiRestrictionsOffset = offsetWriter.getPosition();
        List<Map.Entry<? extends ClassKey, Integer>> classEntriesValueSorted = Lists.newArrayList(this.classSection.getItems());
        Collections.sort(classEntriesValueSorted, comparableValueComparator());
        RestrictionsWriter restrictionsWriter = new RestrictionsWriter(dataStore, offsetWriter, classEntriesValueSorted.size());
        try {
            for (Map.Entry<? extends ClassKey, Integer> key : classEntriesValueSorted) {
                for (FieldKey fieldKey : this.classSection.getSortedStaticFields(key.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getFieldHiddenApiRestrictions(fieldKey));
                }
                for (FieldKey fieldKey2 : this.classSection.getSortedInstanceFields(key.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getFieldHiddenApiRestrictions(fieldKey2));
                }
                for (MethodKey methodKey : this.classSection.getSortedDirectMethods(key.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getMethodHiddenApiRestrictions(methodKey));
                }
                for (MethodKey methodKey2 : this.classSection.getSortedVirtualMethods(key.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getMethodHiddenApiRestrictions(methodKey2));
                }
                restrictionsWriter.finishClass();
            }
        } finally {
            restrictionsWriter.close();
        }
    }

    private boolean shouldWriteHiddenApiRestrictions() {
        return this.hasHiddenApiRestrictions && this.opcodes.api >= 29;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RestrictionsWriter {
        private final DexDataStore dataStore;
        private final DexDataWriter offsetsWriter;
        private final DexDataWriter restrictionsWriter;
        private final int startOffset;
        private boolean writeRestrictionsForClass = false;
        private int pendingBlankEntries = 0;

        public RestrictionsWriter(DexDataStore dataStore, DexDataWriter offsetWriter, int numClasses) throws IOException {
            this.startOffset = offsetWriter.getPosition();
            this.dataStore = dataStore;
            this.restrictionsWriter = offsetWriter;
            int offsetsSize = numClasses * 4;
            offsetWriter.writeInt(0);
            this.offsetsWriter = DexWriter.outputAt(dataStore, offsetWriter.getPosition());
            for (int i = 0; i < offsetsSize; i++) {
                this.restrictionsWriter.write(0);
            }
            this.restrictionsWriter.flush();
        }

        public void finishClass() throws IOException {
            if (!this.writeRestrictionsForClass) {
                this.offsetsWriter.writeInt(0);
            }
            this.writeRestrictionsForClass = false;
            this.pendingBlankEntries = 0;
        }

        private void addBlankEntry() throws IOException {
            if (this.writeRestrictionsForClass) {
                this.restrictionsWriter.writeUleb128(HiddenApiRestriction.WHITELIST.getValue());
            } else {
                this.pendingBlankEntries++;
            }
        }

        public void writeRestriction(@Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions) throws IOException {
            if (hiddenApiRestrictions.isEmpty()) {
                addBlankEntry();
                return;
            }
            if (!this.writeRestrictionsForClass) {
                this.writeRestrictionsForClass = true;
                this.offsetsWriter.writeInt(this.restrictionsWriter.getPosition() - this.startOffset);
                for (int i = 0; i < this.pendingBlankEntries; i++) {
                    this.restrictionsWriter.writeUleb128(HiddenApiRestriction.WHITELIST.getValue());
                }
                this.pendingBlankEntries = 0;
            }
            this.restrictionsWriter.writeUleb128(HiddenApiRestriction.combineFlags(hiddenApiRestrictions));
        }

        public void close() throws IOException {
            DexDataWriter writer = null;
            this.offsetsWriter.close();
            try {
                writer = DexWriter.outputAt(this.dataStore, this.startOffset);
                writer.writeInt(this.restrictionsWriter.getPosition() - this.startOffset);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int writeClass(@Nonnull DexDataWriter dexDataWriter, @Nonnull DexDataWriter dexDataWriter2, int i, @Nullable Map.Entry<? extends ClassKey, Integer> entry) throws IOException {
        if (entry == null) {
            return i;
        }
        if (entry.getValue().intValue() != -1) {
            return i;
        }
        ClassKey key = entry.getKey();
        entry.setValue(0);
        ClassSectionType classsectiontype = this.classSection;
        int writeClass = writeClass(dexDataWriter, dexDataWriter2, i, classsectiontype.getClassEntryByType(classsectiontype.getSuperclass(key)));
        Iterator it = this.typeListSection.getTypes(this.classSection.getInterfaces(key)).iterator();
        while (it.hasNext()) {
            writeClass = writeClass(dexDataWriter, dexDataWriter2, writeClass, this.classSection.getClassEntryByType((CharSequence) it.next()));
        }
        int i2 = writeClass + 1;
        entry.setValue(Integer.valueOf(writeClass));
        dexDataWriter.writeInt(this.typeSection.getItemIndex(this.classSection.getType(key)));
        dexDataWriter.writeInt(this.classSection.getAccessFlags(key));
        dexDataWriter.writeInt(this.typeSection.getNullableItemIndex(this.classSection.getSuperclass(key)));
        dexDataWriter.writeInt(this.typeListSection.getNullableItemOffset(this.classSection.getInterfaces(key)));
        dexDataWriter.writeInt(this.stringSection.getNullableItemIndex(this.classSection.getSourceFile(key)));
        dexDataWriter.writeInt(this.classSection.getAnnotationDirectoryOffset(key));
        Collection<? extends FieldKey> sortedStaticFields = this.classSection.getSortedStaticFields(key);
        Collection<? extends FieldKey> sortedInstanceFields = this.classSection.getSortedInstanceFields(key);
        Collection<? extends MethodKey> sortedDirectMethods = this.classSection.getSortedDirectMethods(key);
        Collection<? extends MethodKey> sortedVirtualMethods = this.classSection.getSortedVirtualMethods(key);
        boolean z = sortedStaticFields.size() > 0 || sortedInstanceFields.size() > 0 || sortedDirectMethods.size() > 0 || sortedVirtualMethods.size() > 0;
        if (z) {
            dexDataWriter.writeInt(dexDataWriter2.getPosition());
        } else {
            dexDataWriter.writeInt(0);
        }
        Object staticInitializers = this.classSection.getStaticInitializers(key);
        if (staticInitializers != null) {
            dexDataWriter.writeInt(this.encodedArraySection.getItemOffset(staticInitializers));
        } else {
            dexDataWriter.writeInt(0);
        }
        if (z) {
            this.numClassDataItems++;
            dexDataWriter2.writeUleb128(sortedStaticFields.size());
            dexDataWriter2.writeUleb128(sortedInstanceFields.size());
            dexDataWriter2.writeUleb128(sortedDirectMethods.size());
            dexDataWriter2.writeUleb128(sortedVirtualMethods.size());
            writeEncodedFields(dexDataWriter2, sortedStaticFields);
            writeEncodedFields(dexDataWriter2, sortedInstanceFields);
            writeEncodedMethods(dexDataWriter2, sortedDirectMethods);
            writeEncodedMethods(dexDataWriter2, sortedVirtualMethods);
        }
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeCallSites(DexDataWriter dexDataWriter) throws IOException {
        this.callSiteSectionOffset = dexDataWriter.getPosition();
        ArrayList<Map.Entry> newArrayList = Lists.newArrayList(this.callSiteSection.getItems());
        Collections.sort(newArrayList, this.callSiteComparator);
        int i = 0;
        for (Map.Entry entry : newArrayList) {
            entry.setValue(Integer.valueOf(i));
            dexDataWriter.writeInt(this.encodedArraySection.getItemOffset(this.callSiteSection.getEncodedCallSite((CallSiteReference) entry.getKey())));
            i++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeMethodHandles(DexDataWriter dexDataWriter) throws IOException {
        int itemIndex;
        this.methodHandleSectionOffset = dexDataWriter.getPosition();
        int i = 0;
        for (Map.Entry entry : this.methodHandleSection.getItems()) {
            int i2 = i + 1;
            entry.setValue(Integer.valueOf(i));
            MethodHandleReference methodHandleReference = (MethodHandleReference) entry.getKey();
            dexDataWriter.writeUshort(methodHandleReference.getMethodHandleType());
            dexDataWriter.writeUshort(0);
            switch (methodHandleReference.getMethodHandleType()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    itemIndex = this.fieldSection.getItemIndex(this.methodHandleSection.getFieldReference(methodHandleReference));
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    itemIndex = this.methodSection.getItemIndex(this.methodHandleSection.getMethodReference(methodHandleReference));
                    break;
                default:
                    throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleReference.getMethodHandleType()));
            }
            dexDataWriter.writeUshort(itemIndex);
            dexDataWriter.writeUshort(0);
            i = i2;
        }
    }

    private void writeEncodedFields(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends FieldKey> fields) throws IOException {
        int prevIndex = 0;
        for (FieldKey key : fields) {
            int index = this.fieldSection.getFieldIndex(key);
            if (!this.classSection.getFieldHiddenApiRestrictions(key).isEmpty()) {
                this.hasHiddenApiRestrictions = true;
            }
            writer.writeUleb128(index - prevIndex);
            writer.writeUleb128(this.classSection.getFieldAccessFlags(key));
            prevIndex = index;
        }
    }

    private void writeEncodedMethods(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends MethodKey> methods) throws IOException {
        int prevIndex = 0;
        for (MethodKey key : methods) {
            int index = this.methodSection.getMethodIndex(key);
            if (!this.classSection.getMethodHiddenApiRestrictions(key).isEmpty()) {
                this.hasHiddenApiRestrictions = true;
            }
            writer.writeUleb128(index - prevIndex);
            writer.writeUleb128(this.classSection.getMethodAccessFlags(key));
            writer.writeUleb128(this.classSection.getCodeItemOffset(key));
            prevIndex = index;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeTypeLists(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        dexDataWriter.align();
        this.typeListSectionOffset = dexDataWriter.getPosition();
        for (Map.Entry entry : this.typeListSection.getItems()) {
            dexDataWriter.align();
            entry.setValue(Integer.valueOf(dexDataWriter.getPosition()));
            Collection types = this.typeListSection.getTypes(entry.getKey());
            dexDataWriter.writeInt(types.size());
            Iterator it = types.iterator();
            while (it.hasNext()) {
                dexDataWriter.writeUshort(this.typeSection.getItemIndex((CharSequence) it.next()));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeEncodedArrays(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        InternalEncodedValueWriter internalEncodedValueWriter = new InternalEncodedValueWriter(dexDataWriter);
        this.encodedArraySectionOffset = dexDataWriter.getPosition();
        for (Map.Entry entry : this.encodedArraySection.getItems()) {
            entry.setValue(Integer.valueOf(dexDataWriter.getPosition()));
            List encodedValueList = this.encodedArraySection.getEncodedValueList(entry.getKey());
            dexDataWriter.writeUleb128(encodedValueList.size());
            Iterator it = encodedValueList.iterator();
            while (it.hasNext()) {
                writeEncodedValue(internalEncodedValueWriter, it.next());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeAnnotations(@Nonnull DexDataWriter writer) throws IOException {
        DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.InternalEncodedValueWriter encodedValueWriter = new InternalEncodedValueWriter(writer);
        this.annotationSectionOffset = writer.getPosition();
        for (Map.Entry<? extends AnnotationKey, Integer> entry : this.annotationSection.getItems()) {
            entry.setValue(Integer.valueOf(writer.getPosition()));
            Annotation annotation = (Annotation) entry.getKey();
            writer.writeUbyte(this.annotationSection.getVisibility(annotation));
            writer.writeUleb128(this.typeSection.getItemIndex(this.annotationSection.getType(annotation)));
            Collection<? extends AnnotationElement> elements = Ordering.from(BaseAnnotationElement.BY_NAME).immutableSortedCopy(this.annotationSection.getElements(annotation));
            writer.writeUleb128(elements.size());
            Iterator<? extends AnnotationElement> it = elements.iterator();
            while (it.hasNext()) {
                AnnotationElement annotationElement = (AnnotationElement) it.next();
                writer.writeUleb128(this.stringSection.getItemIndex(this.annotationSection.getElementName(annotationElement)));
                writeEncodedValue(encodedValueWriter, this.annotationSection.getElementValue(annotationElement));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeAnnotationSets(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        dexDataWriter.align();
        this.annotationSetSectionOffset = dexDataWriter.getPosition();
        if (shouldCreateEmptyAnnotationSet()) {
            dexDataWriter.writeInt(0);
        }
        for (Map.Entry entry : this.annotationSetSection.getItems()) {
            ImmutableList immutableSortedCopy = Ordering.from(BaseAnnotation.BY_TYPE).immutableSortedCopy(this.annotationSetSection.getAnnotations(entry.getKey()));
            dexDataWriter.align();
            entry.setValue(Integer.valueOf(dexDataWriter.getPosition()));
            dexDataWriter.writeInt(immutableSortedCopy.size());
            Iterator<E> it = immutableSortedCopy.iterator();
            while (it.hasNext()) {
                dexDataWriter.writeInt(this.annotationSection.getItemOffset((Annotation) it.next()));
            }
        }
    }

    private void writeAnnotationSetRefs(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.annotationSetRefSectionOffset = writer.getPosition();
        HashMap<List<? extends AnnotationSetKey>, Integer> internedItems = Maps.newHashMap();
        Iterator it = this.classSection.getSortedClasses().iterator();
        while (it.hasNext()) {
            for (MethodKey methodKey : this.classSection.getSortedMethods((Comparable) it.next())) {
                List<? extends AnnotationSetKey> parameterAnnotations = this.classSection.getParameterAnnotations(methodKey);
                if (parameterAnnotations != null) {
                    Integer prev = internedItems.get(parameterAnnotations);
                    if (prev != null) {
                        this.classSection.setAnnotationSetRefListOffset(methodKey, prev.intValue());
                    } else {
                        writer.align();
                        int position = writer.getPosition();
                        this.classSection.setAnnotationSetRefListOffset(methodKey, position);
                        internedItems.put(parameterAnnotations, Integer.valueOf(position));
                        this.numAnnotationSetRefItems++;
                        writer.writeInt(parameterAnnotations.size());
                        for (AnnotationSetKey annotationSetKey : parameterAnnotations) {
                            if (this.annotationSetSection.getAnnotations(annotationSetKey).size() > 0) {
                                writer.writeInt(this.annotationSetSection.getItemOffset(annotationSetKey));
                            } else if (shouldCreateEmptyAnnotationSet()) {
                                writer.writeInt(this.annotationSetSectionOffset);
                            } else {
                                writer.writeInt(0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeAnnotationDirectories(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.annotationDirectorySectionOffset = writer.getPosition();
        HashMap newHashMap = Maps.newHashMap();
        ByteBuffer tempBuffer = ByteBuffer.allocate(65536);
        tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
        for (Comparable comparable : this.classSection.getSortedClasses()) {
            Collection<? extends FieldKey> fields = this.classSection.getSortedFields(comparable);
            Collection<? extends MethodKey> methods = this.classSection.getSortedMethods(comparable);
            int maxSize = (fields.size() * 8) + (methods.size() * 16);
            if (maxSize > tempBuffer.capacity()) {
                tempBuffer = ByteBuffer.allocate(maxSize);
                tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }
            tempBuffer.clear();
            int fieldAnnotations = 0;
            int methodAnnotations = 0;
            int parameterAnnotations = 0;
            for (FieldKey field : fields) {
                Object fieldAnnotations2 = this.classSection.getFieldAnnotations(field);
                if (fieldAnnotations2 != null) {
                    fieldAnnotations++;
                    tempBuffer.putInt(this.fieldSection.getFieldIndex(field));
                    tempBuffer.putInt(this.annotationSetSection.getItemOffset(fieldAnnotations2));
                }
            }
            for (MethodKey method : methods) {
                Object methodAnnotations2 = this.classSection.getMethodAnnotations(method);
                if (methodAnnotations2 != null) {
                    methodAnnotations++;
                    tempBuffer.putInt(this.methodSection.getMethodIndex(method));
                    tempBuffer.putInt(this.annotationSetSection.getItemOffset(methodAnnotations2));
                }
            }
            for (MethodKey method2 : methods) {
                int offset = this.classSection.getAnnotationSetRefListOffset(method2);
                if (offset != 0) {
                    parameterAnnotations++;
                    tempBuffer.putInt(this.methodSection.getMethodIndex(method2));
                    tempBuffer.putInt(offset);
                }
            }
            Object classAnnotations = this.classSection.getClassAnnotations(comparable);
            if (fieldAnnotations == 0 && methodAnnotations == 0 && parameterAnnotations == 0) {
                if (classAnnotations != null) {
                    Integer directoryOffset = (Integer) newHashMap.get(classAnnotations);
                    if (directoryOffset != null) {
                        this.classSection.setAnnotationDirectoryOffset(comparable, directoryOffset.intValue());
                    } else {
                        newHashMap.put(classAnnotations, Integer.valueOf(writer.getPosition()));
                    }
                }
            }
            this.numAnnotationDirectoryItems++;
            this.classSection.setAnnotationDirectoryOffset(comparable, writer.getPosition());
            writer.writeInt(this.annotationSetSection.getNullableItemOffset(classAnnotations));
            writer.writeInt(fieldAnnotations);
            writer.writeInt(methodAnnotations);
            writer.writeInt(parameterAnnotations);
            writer.write(tempBuffer.array(), 0, tempBuffer.position());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CodeItemOffset<MethodKey> {
        int codeOffset;

        @Nonnull
        MethodKey method;

        private CodeItemOffset(@Nonnull MethodKey method, int codeOffset) {
            this.codeOffset = codeOffset;
            this.method = method;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00fc A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeDebugAndCodeItems(@Nonnull DexDataWriter dexDataWriter, @Nonnull DeferredOutputStream deferredOutputStream) throws IOException {
        Iterator it;
        List<? extends TryBlock<? extends ExceptionHandler>> list;
        Iterable<? extends Instruction> iterable;
        int writeCodeItem;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.debugSectionOffset = dexDataWriter.getPosition();
        DebugWriter debugWriter = new DebugWriter(this.stringSection, this.typeSection, dexDataWriter);
        DexDataWriter dexDataWriter2 = new DexDataWriter(deferredOutputStream, 0);
        ArrayList<CodeItemOffset> newArrayList = Lists.newArrayList();
        Iterator it2 = this.classSection.getSortedClasses().iterator();
        while (it2.hasNext()) {
            Comparable comparable = (Comparable) it2.next();
            Collection<? extends MethodKey> sortedDirectMethods = this.classSection.getSortedDirectMethods(comparable);
            Collection<? extends MethodKey> sortedVirtualMethods = this.classSection.getSortedVirtualMethods(comparable);
            for (Object obj : Iterables.concat(sortedDirectMethods, sortedVirtualMethods)) {
                List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks = this.classSection.getTryBlocks(obj);
                Iterable<? extends Instruction> instructions = this.classSection.getInstructions(obj);
                Iterable<? extends DebugItem> debugItems = this.classSection.getDebugItems(obj);
                try {
                    if (instructions == null || !this.stringSection.hasJumboIndexes()) {
                        it = it2;
                        list = tryBlocks;
                    } else {
                        boolean z = false;
                        Iterator<? extends Instruction> it3 = instructions.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                it = it2;
                                list = tryBlocks;
                                break;
                            }
                            Instruction next = it3.next();
                            it = it2;
                            list = tryBlocks;
                            if (next.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference) ((ReferenceInstruction) next).getReference()) >= 65536) {
                                z = true;
                                break;
                            } else {
                                it2 = it;
                                tryBlocks = list;
                            }
                        }
                        if (z) {
                            MutableMethodImplementation makeMutableMethodImplementation = this.classSection.makeMutableMethodImplementation(obj);
                            fixInstructions(makeMutableMethodImplementation);
                            List<BuilderInstruction> instructions2 = makeMutableMethodImplementation.getInstructions();
                            List<BuilderTryBlock> tryBlocks2 = makeMutableMethodImplementation.getTryBlocks();
                            debugItems = makeMutableMethodImplementation.getDebugItems();
                            list = tryBlocks2;
                            iterable = instructions2;
                            DebugWriter debugWriter2 = debugWriter;
                            Collection<? extends MethodKey> collection = sortedVirtualMethods;
                            writeCodeItem = writeCodeItem(dexDataWriter2, byteArrayOutputStream, obj, list, iterable, writeDebugItem(dexDataWriter, debugWriter, this.classSection.getParameterNames(obj), debugItems));
                            if (writeCodeItem == -1) {
                                newArrayList.add(new CodeItemOffset(obj, writeCodeItem));
                            }
                            it2 = it;
                            debugWriter = debugWriter2;
                            sortedVirtualMethods = collection;
                        }
                    }
                    writeCodeItem = writeCodeItem(dexDataWriter2, byteArrayOutputStream, obj, list, iterable, writeDebugItem(dexDataWriter, debugWriter, this.classSection.getParameterNames(obj), debugItems));
                    if (writeCodeItem == -1) {
                    }
                    it2 = it;
                    debugWriter = debugWriter2;
                    sortedVirtualMethods = collection;
                } catch (RuntimeException e) {
                    throw new ExceptionWithContext(e, "Exception occurred while writing code_item for method %s", this.methodSection.getMethodReference(obj));
                }
                iterable = instructions;
                DebugWriter debugWriter22 = debugWriter;
                Collection<? extends MethodKey> collection2 = sortedVirtualMethods;
            }
        }
        dexDataWriter.align();
        this.codeSectionOffset = dexDataWriter.getPosition();
        dexDataWriter2.close();
        deferredOutputStream.writeTo(dexDataWriter);
        deferredOutputStream.close();
        for (CodeItemOffset codeItemOffset : newArrayList) {
            this.classSection.setCodeItemOffset(codeItemOffset.method, this.codeSectionOffset + codeItemOffset.codeOffset);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void fixInstructions(@Nonnull MutableMethodImplementation mutableMethodImplementation) {
        List<BuilderInstruction> instructions = mutableMethodImplementation.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference) ((ReferenceInstruction) instruction).getReference()) >= 65536) {
                mutableMethodImplementation.replaceInstruction(i, new BuilderInstruction31c(Opcode.CONST_STRING_JUMBO, ((OneRegisterInstruction) instruction).getRegisterA(), ((ReferenceInstruction) instruction).getReference()));
            }
        }
    }

    private int writeDebugItem(@Nonnull DexDataWriter writer, @Nonnull DebugWriter<StringKey, TypeKey> debugWriter, @Nullable Iterable<? extends StringKey> parameterNames, @Nullable Iterable<? extends DebugItem> debugItems) throws IOException {
        int parameterCount = 0;
        int lastNamedParameterIndex = -1;
        if (parameterNames != null) {
            parameterCount = Iterables.size(parameterNames);
            int index = 0;
            for (StringKey parameterName : parameterNames) {
                if (parameterName != null) {
                    lastNamedParameterIndex = index;
                }
                index++;
            }
        }
        if (lastNamedParameterIndex == -1 && (debugItems == null || Iterables.isEmpty(debugItems))) {
            return 0;
        }
        this.numDebugInfoItems++;
        int debugItemOffset = writer.getPosition();
        int startingLineNumber = 0;
        if (debugItems != null) {
            Iterator<? extends DebugItem> it = debugItems.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DebugItem debugItem = it.next();
                if (debugItem instanceof LineNumber) {
                    startingLineNumber = ((LineNumber) debugItem).getLineNumber();
                    break;
                }
            }
        }
        writer.writeUleb128(startingLineNumber);
        writer.writeUleb128(parameterCount);
        if (parameterNames != null) {
            int index2 = 0;
            for (StringKey parameterName2 : parameterNames) {
                if (index2 == parameterCount) {
                    break;
                }
                index2++;
                writer.writeUleb128(this.stringSection.getNullableItemIndex(parameterName2) + 1);
            }
        }
        if (debugItems != null) {
            debugWriter.reset(startingLineNumber);
            Iterator<? extends DebugItem> it2 = debugItems.iterator();
            while (it2.hasNext()) {
                this.classSection.writeDebugItem(debugWriter, it2.next());
            }
        }
        writer.write(0);
        return debugItemOffset;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:32:0x00f5. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    private int writeCodeItem(@Nonnull DexDataWriter dexDataWriter, @Nonnull ByteArrayOutputStream byteArrayOutputStream, @Nonnull MethodKey methodkey, @Nonnull List<? extends TryBlock<? extends ExceptionHandler>> list, @Nullable Iterable<? extends Instruction> iterable, int i) throws IOException {
        HashMap hashMap;
        InstructionWriter instructionWriter;
        Iterator it;
        int parameterRegisterCount;
        if (iterable != null || i != 0) {
            this.numCodeItemItems++;
            dexDataWriter.align();
            int position = dexDataWriter.getPosition();
            dexDataWriter.writeUshort(this.classSection.getRegisterCount(methodkey));
            dexDataWriter.writeUshort(MethodUtil.getParameterRegisterCount((Collection<? extends CharSequence>) this.typeListSection.getTypes(this.protoSection.getParameters(this.methodSection.getPrototype(methodkey))), AccessFlags.STATIC.isSet(this.classSection.getMethodAccessFlags(methodkey))));
            if (iterable != null) {
                List massageTryBlocks = TryListBuilder.massageTryBlocks(list);
                int i2 = 0;
                int i3 = 0;
                for (Instruction instruction : iterable) {
                    i3 += instruction.getCodeUnits();
                    if (instruction.getOpcode().referenceType == 3) {
                        MethodReference methodReference = (MethodReference) ((ReferenceInstruction) instruction).getReference();
                        Opcode opcode = instruction.getOpcode();
                        if (InstructionUtil.isInvokePolymorphic(opcode)) {
                            parameterRegisterCount = ((VariableRegisterInstruction) instruction).getRegisterCount();
                        } else {
                            parameterRegisterCount = MethodUtil.getParameterRegisterCount(methodReference, InstructionUtil.isInvokeStatic(opcode));
                        }
                        if (parameterRegisterCount > i2) {
                            i2 = parameterRegisterCount;
                        }
                    }
                }
                dexDataWriter.writeUshort(i2);
                dexDataWriter.writeUshort(massageTryBlocks.size());
                dexDataWriter.writeInt(i);
                InstructionWriter makeInstructionWriter = InstructionWriter.makeInstructionWriter(this.opcodes, dexDataWriter, this.stringSection, this.typeSection, this.fieldSection, this.methodSection, this.protoSection, this.methodHandleSection, this.callSiteSection);
                dexDataWriter.writeInt(i3);
                int i4 = 0;
                for (Instruction instruction2 : iterable) {
                    try {
                        switch (AnonymousClass5.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction2.getOpcode().format.ordinal()]) {
                            case 1:
                                makeInstructionWriter.write((Instruction10t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 2:
                                makeInstructionWriter.write((Instruction10x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 3:
                                makeInstructionWriter.write((Instruction11n) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 4:
                                makeInstructionWriter.write((Instruction11x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 5:
                                makeInstructionWriter.write((Instruction12x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 6:
                                makeInstructionWriter.write((Instruction20bc) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 7:
                                makeInstructionWriter.write((Instruction20t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 8:
                                makeInstructionWriter.write((Instruction21c) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 9:
                                makeInstructionWriter.write((Instruction21ih) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 10:
                                makeInstructionWriter.write((Instruction21lh) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 11:
                                makeInstructionWriter.write((Instruction21s) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 12:
                                makeInstructionWriter.write((Instruction21t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 13:
                                makeInstructionWriter.write((Instruction22b) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 14:
                                makeInstructionWriter.write((Instruction22c) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 15:
                                makeInstructionWriter.write((Instruction22cs) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 16:
                                makeInstructionWriter.write((Instruction22s) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 17:
                                makeInstructionWriter.write((Instruction22t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 18:
                                makeInstructionWriter.write((Instruction22x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 19:
                                makeInstructionWriter.write((Instruction23x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 20:
                                makeInstructionWriter.write((Instruction30t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 21:
                                makeInstructionWriter.write((Instruction31c) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 22:
                                makeInstructionWriter.write((Instruction31i) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 23:
                                makeInstructionWriter.write((Instruction31t) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 24:
                                makeInstructionWriter.write((Instruction32x) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 25:
                                makeInstructionWriter.write((Instruction35c) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 26:
                                makeInstructionWriter.write((Instruction35mi) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 27:
                                makeInstructionWriter.write((Instruction35ms) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 28:
                                makeInstructionWriter.write((Instruction3rc) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 29:
                                makeInstructionWriter.write((Instruction3rmi) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 30:
                                makeInstructionWriter.write((Instruction3rms) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 31:
                                makeInstructionWriter.write((Instruction45cc) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 32:
                                makeInstructionWriter.write((Instruction4rcc) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 33:
                                makeInstructionWriter.write((Instruction51l) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 34:
                                makeInstructionWriter.write((ArrayPayload) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 35:
                                makeInstructionWriter.write((PackedSwitchPayload) instruction2);
                                i4 += instruction2.getCodeUnits();
                            case 36:
                                makeInstructionWriter.write((SparseSwitchPayload) instruction2);
                                i4 += instruction2.getCodeUnits();
                            default:
                                throw new ExceptionWithContext("Unsupported instruction format: %s", instruction2.getOpcode().format);
                        }
                    } catch (RuntimeException e) {
                        throw new ExceptionWithContext(e, "Error while writing instruction at code offset 0x%x", Integer.valueOf(i4));
                    }
                }
                if (massageTryBlocks.size() > 0) {
                    dexDataWriter.align();
                    HashMap newHashMap = Maps.newHashMap();
                    Iterator it2 = massageTryBlocks.iterator();
                    while (it2.hasNext()) {
                        newHashMap.put(((TryBlock) it2.next()).getExceptionHandlers(), 0);
                    }
                    DexDataWriter.writeUleb128(byteArrayOutputStream, newHashMap.size());
                    Iterator it3 = massageTryBlocks.iterator();
                    while (it3.hasNext()) {
                        TryBlock tryBlock = (TryBlock) it3.next();
                        int startCodeAddress = tryBlock.getStartCodeAddress();
                        int codeUnitCount = (tryBlock.getCodeUnitCount() + startCodeAddress) - startCodeAddress;
                        dexDataWriter.writeInt(startCodeAddress);
                        dexDataWriter.writeUshort(codeUnitCount);
                        if (tryBlock.getExceptionHandlers().size() == 0) {
                            throw new ExceptionWithContext("No exception handlers for the try block!", new Object[0]);
                        }
                        Integer num = (Integer) newHashMap.get(tryBlock.getExceptionHandlers());
                        if (num.intValue() != 0) {
                            dexDataWriter.writeUshort(num.intValue());
                            hashMap = newHashMap;
                            instructionWriter = makeInstructionWriter;
                            it = it3;
                        } else {
                            Integer valueOf = Integer.valueOf(byteArrayOutputStream.size());
                            dexDataWriter.writeUshort(valueOf.intValue());
                            newHashMap.put(tryBlock.getExceptionHandlers(), valueOf);
                            int size = tryBlock.getExceptionHandlers().size();
                            hashMap = newHashMap;
                            ExceptionHandler exceptionHandler = (ExceptionHandler) tryBlock.getExceptionHandlers().get(size - 1);
                            if (exceptionHandler.getExceptionType() == null) {
                                size = (size * (-1)) + 1;
                            }
                            DexDataWriter.writeSleb128(byteArrayOutputStream, size);
                            for (ExceptionHandler exceptionHandler2 : tryBlock.getExceptionHandlers()) {
                                ExceptionHandler exceptionHandler3 = exceptionHandler;
                                InstructionWriter instructionWriter2 = makeInstructionWriter;
                                CharSequence exceptionType = this.classSection.getExceptionType(exceptionHandler2);
                                Iterator it4 = it3;
                                int handlerCodeAddress = exceptionHandler2.getHandlerCodeAddress();
                                if (exceptionType != null) {
                                    DexDataWriter.writeUleb128(byteArrayOutputStream, this.typeSection.getItemIndex(exceptionType));
                                    DexDataWriter.writeUleb128(byteArrayOutputStream, handlerCodeAddress);
                                } else {
                                    DexDataWriter.writeUleb128(byteArrayOutputStream, handlerCodeAddress);
                                }
                                makeInstructionWriter = instructionWriter2;
                                exceptionHandler = exceptionHandler3;
                                it3 = it4;
                            }
                            instructionWriter = makeInstructionWriter;
                            it = it3;
                        }
                        newHashMap = hashMap;
                        makeInstructionWriter = instructionWriter;
                        it3 = it;
                    }
                    if (byteArrayOutputStream.size() > 0) {
                        byteArrayOutputStream.writeTo(dexDataWriter);
                        byteArrayOutputStream.reset();
                    }
                }
            } else {
                dexDataWriter.writeUshort(0);
                dexDataWriter.writeUshort(0);
                dexDataWriter.writeInt(i);
                dexDataWriter.writeInt(0);
            }
            return position;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.writer.DexWriter$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;

        static {
            int[] iArr = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr;
            try {
                iArr[Format.Format10t.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format10x.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11n.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11x.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format12x.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20bc.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20t.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21c.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21ih.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21lh.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21s.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21t.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22b.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22c.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22cs.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22s.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22t.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22x.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format23x.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format30t.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31c.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31i.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31t.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format32x.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35mi.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35ms.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rmi.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rms.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format45cc.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format4rcc.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format51l.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.ArrayPayload.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.PackedSwitchPayload.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.SparseSwitchPayload.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
        }
    }

    private int calcNumItems() {
        int numItems = 0 + 1;
        if (this.stringSection.getItems().size() > 0) {
            numItems += 2;
        }
        if (this.typeSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.protoSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.fieldSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.methodSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.callSiteSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.methodHandleSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.typeListSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.encodedArraySection.getItems().size() > 0) {
            numItems++;
        }
        if (this.annotationSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.annotationSetSection.getItems().size() > 0 || shouldCreateEmptyAnnotationSet()) {
            numItems++;
        }
        if (this.numAnnotationSetRefItems > 0) {
            numItems++;
        }
        if (this.numAnnotationDirectoryItems > 0) {
            numItems++;
        }
        if (this.numDebugInfoItems > 0) {
            numItems++;
        }
        if (this.numCodeItemItems > 0) {
            numItems++;
        }
        if (this.classSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.numClassDataItems > 0) {
            numItems++;
        }
        if (shouldWriteHiddenApiRestrictions()) {
            numItems++;
        }
        return numItems + 1;
    }

    private void writeMapItem(@Nonnull DexDataWriter dexDataWriter) throws IOException {
        dexDataWriter.align();
        this.mapSectionOffset = dexDataWriter.getPosition();
        dexDataWriter.writeInt(calcNumItems());
        writeMapItem(dexDataWriter, 0, 1, 0);
        writeMapItem(dexDataWriter, 1, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
        writeMapItem(dexDataWriter, 2, this.typeSection.getItems().size(), this.typeSectionOffset);
        writeMapItem(dexDataWriter, 3, this.protoSection.getItems().size(), this.protoSectionOffset);
        writeMapItem(dexDataWriter, 4, this.fieldSection.getItems().size(), this.fieldSectionOffset);
        writeMapItem(dexDataWriter, 5, this.methodSection.getItems().size(), this.methodSectionOffset);
        writeMapItem(dexDataWriter, 6, this.classSection.getItems().size(), this.classIndexSectionOffset);
        writeMapItem(dexDataWriter, 7, this.callSiteSection.getItems().size(), this.callSiteSectionOffset);
        writeMapItem(dexDataWriter, 8, this.methodHandleSection.getItems().size(), this.methodHandleSectionOffset);
        writeMapItem(dexDataWriter, 8194, this.stringSection.getItems().size(), this.stringDataSectionOffset);
        writeMapItem(dexDataWriter, 4097, this.typeListSection.getItems().size(), this.typeListSectionOffset);
        writeMapItem(dexDataWriter, 8197, this.encodedArraySection.getItems().size(), this.encodedArraySectionOffset);
        writeMapItem(dexDataWriter, ItemType.ANNOTATION_ITEM, this.annotationSection.getItems().size(), this.annotationSectionOffset);
        writeMapItem(dexDataWriter, 4099, this.annotationSetSection.getItems().size() + (shouldCreateEmptyAnnotationSet() ? 1 : 0), this.annotationSetSectionOffset);
        writeMapItem(dexDataWriter, 4098, this.numAnnotationSetRefItems, this.annotationSetRefSectionOffset);
        writeMapItem(dexDataWriter, ItemType.ANNOTATION_DIRECTORY_ITEM, this.numAnnotationDirectoryItems, this.annotationDirectorySectionOffset);
        writeMapItem(dexDataWriter, ItemType.DEBUG_INFO_ITEM, this.numDebugInfoItems, this.debugSectionOffset);
        writeMapItem(dexDataWriter, ItemType.CODE_ITEM, this.numCodeItemItems, this.codeSectionOffset);
        writeMapItem(dexDataWriter, 8192, this.numClassDataItems, this.classDataSectionOffset);
        if (shouldWriteHiddenApiRestrictions()) {
            writeMapItem(dexDataWriter, ItemType.HIDDENAPI_CLASS_DATA_ITEM, 1, this.hiddenApiRestrictionsOffset);
        }
        writeMapItem(dexDataWriter, 4096, 1, this.mapSectionOffset);
    }

    private void writeMapItem(@Nonnull DexDataWriter writer, int type, int size, int offset) throws IOException {
        if (size > 0) {
            writer.writeUshort(type);
            writer.writeUshort(0);
            writer.writeInt(size);
            writer.writeInt(offset);
        }
    }

    private void writeHeader(@Nonnull DexDataWriter writer, int dataOffset, int fileSize) throws IOException {
        writer.write(HeaderItem.getMagicForApi(this.opcodes.api));
        writer.writeInt(0);
        writer.write(new byte[20]);
        writer.writeInt(fileSize);
        writer.writeInt(112);
        writer.writeInt(HeaderItem.LITTLE_ENDIAN_TAG);
        writer.writeInt(0);
        writer.writeInt(0);
        writer.writeInt(this.mapSectionOffset);
        writeSectionInfo(writer, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
        writeSectionInfo(writer, this.typeSection.getItems().size(), this.typeSectionOffset);
        writeSectionInfo(writer, this.protoSection.getItems().size(), this.protoSectionOffset);
        writeSectionInfo(writer, this.fieldSection.getItems().size(), this.fieldSectionOffset);
        writeSectionInfo(writer, this.methodSection.getItems().size(), this.methodSectionOffset);
        writeSectionInfo(writer, this.classSection.getItems().size(), this.classIndexSectionOffset);
        writer.writeInt(fileSize - dataOffset);
        writer.writeInt(dataOffset);
    }

    private void writeSectionInfo(DexDataWriter writer, int numItems, int offset) throws IOException {
        writer.writeInt(numItems);
        if (numItems > 0) {
            writer.writeInt(offset);
        } else {
            writer.writeInt(0);
        }
    }

    private boolean shouldCreateEmptyAnnotationSet() {
        return this.opcodes.api < 17;
    }

    /* loaded from: classes.dex */
    public abstract class SectionProvider {
        @Nonnull
        public abstract AnnotationSectionType getAnnotationSection();

        @Nonnull
        public abstract AnnotationSetSectionType getAnnotationSetSection();

        @Nonnull
        public abstract CallSiteSectionType getCallSiteSection();

        @Nonnull
        public abstract ClassSectionType getClassSection();

        @Nonnull
        public abstract EncodedArraySectionType getEncodedArraySection();

        @Nonnull
        public abstract FieldSectionType getFieldSection();

        @Nonnull
        public abstract MethodHandleSectionType getMethodHandleSection();

        @Nonnull
        public abstract MethodSectionType getMethodSection();

        @Nonnull
        public abstract ProtoSectionType getProtoSection();

        @Nonnull
        public abstract StringSectionType getStringSection();

        @Nonnull
        public abstract TypeListSectionType getTypeListSection();

        @Nonnull
        public abstract TypeSectionType getTypeSection();

        public SectionProvider() {
        }
    }
}
