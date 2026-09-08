package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.dexbacked.util.AnnotationsDirectory;
import com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBackedClassDef extends BaseTypeReference implements ClassDef {
    static final int NO_HIDDEN_API_RESTRICTIONS = 7;

    @Nullable
    private AnnotationsDirectory annotationsDirectory;
    private final int classDefOffset;

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int directMethodCount;

    @Nullable
    private final HiddenApiRestrictionsReader hiddenApiRestrictionsReader;
    private final int instanceFieldCount;
    private final int staticFieldCount;
    private final int staticFieldsOffset;
    private final int virtualMethodCount;
    private int instanceFieldsOffset = 0;
    private int directMethodsOffset = 0;
    private int virtualMethodsOffset = 0;

    public DexBackedClassDef(@Nonnull DexBackedDexFile dexFile, int classDefOffset, int hiddenApiRestrictionsOffset) {
        this.dexFile = dexFile;
        this.classDefOffset = classDefOffset;
        int classDataOffset = dexFile.getBuffer().readSmallUint(classDefOffset + 24);
        if (classDataOffset == 0) {
            this.staticFieldsOffset = -1;
            this.staticFieldCount = 0;
            this.instanceFieldCount = 0;
            this.directMethodCount = 0;
            this.virtualMethodCount = 0;
        } else {
            DexReader reader = dexFile.getDataBuffer().readerAt(classDataOffset);
            this.staticFieldCount = reader.readSmallUleb128();
            this.instanceFieldCount = reader.readSmallUleb128();
            this.directMethodCount = reader.readSmallUleb128();
            this.virtualMethodCount = reader.readSmallUleb128();
            this.staticFieldsOffset = reader.getOffset();
        }
        if (hiddenApiRestrictionsOffset != 0) {
            this.hiddenApiRestrictionsReader = new HiddenApiRestrictionsReader(hiddenApiRestrictionsOffset);
        } else {
            this.hiddenApiRestrictionsReader = null;
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 0));
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        return this.dexFile.getTypeSection().getOptional(this.dexFile.getBuffer().readOptionalUint(this.classDefOffset + 8));
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 4);
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        return this.dexFile.getStringSection().getOptional(this.dexFile.getBuffer().readOptionalUint(this.classDefOffset + 16));
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public List<String> getInterfaces() {
        final int interfacesOffset = this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 12);
        if (interfacesOffset > 0) {
            final int size = this.dexFile.getDataBuffer().readSmallUint(interfacesOffset);
            return new AbstractList<String>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.1
                @Override // java.util.AbstractList, java.util.List
                @Nonnull
                public String get(int index) {
                    return (String) DexBackedClassDef.this.dexFile.getTypeSection().get(DexBackedClassDef.this.dexFile.getDataBuffer().readUshort(interfacesOffset + 4 + (index * 2)));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return size;
                }
            };
        }
        return ImmutableList.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends DexBackedAnnotation> getAnnotations() {
        return getAnnotationsDirectory().getClassAnnotations();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedField> getStaticFields() {
        return getStaticFields(true);
    }

    @Nonnull
    public Iterable<? extends DexBackedField> getStaticFields(final boolean skipDuplicates) {
        if (this.staticFieldCount > 0) {
            DexReader<? extends DexBuffer> reader = this.dexFile.getDataBuffer().readerAt(this.staticFieldsOffset);
            final AnnotationsDirectory annotationsDirectory = getAnnotationsDirectory();
            final int staticInitialValuesOffset = this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 28);
            final int fieldsStartOffset = reader.getOffset();
            HiddenApiRestrictionsReader hiddenApiRestrictionsReader = this.hiddenApiRestrictionsReader;
            final Iterator<Integer> hiddenApiRestrictionIterator = hiddenApiRestrictionsReader == null ? null : hiddenApiRestrictionsReader.getRestrictionsForStaticFields();
            return new Iterable<DexBackedField>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.2
                @Override // java.lang.Iterable
                @Nonnull
                public Iterator<DexBackedField> iterator() {
                    final AnnotationsDirectory.AnnotationIterator annotationIterator = annotationsDirectory.getFieldAnnotationIterator();
                    final EncodedArrayItemIterator staticInitialValueIterator = EncodedArrayItemIterator.newOrEmpty(DexBackedClassDef.this.dexFile, staticInitialValuesOffset);
                    return new VariableSizeLookaheadIterator<DexBackedField>(DexBackedClassDef.this.dexFile.getDataBuffer(), fieldsStartOffset) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.2.1
                        private int count;

                        @Nullable
                        private FieldReference previousField;
                        private int previousIndex;

                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                        @Nullable
                        public DexBackedField readNextItem(@Nonnull DexReader reader2) {
                            DexBackedField item;
                            FieldReference currentField;
                            FieldReference nextField;
                            do {
                                int i = this.count + 1;
                                this.count = i;
                                if (i > DexBackedClassDef.this.staticFieldCount) {
                                    DexBackedClassDef.this.instanceFieldsOffset = reader2.getOffset();
                                    return endOfData();
                                }
                                int hiddenApiRestrictions = 7;
                                if (hiddenApiRestrictionIterator != null) {
                                    hiddenApiRestrictions = ((Integer) hiddenApiRestrictionIterator.next()).intValue();
                                }
                                item = new DexBackedField(DexBackedClassDef.this.dexFile, reader2, DexBackedClassDef.this, this.previousIndex, staticInitialValueIterator, annotationIterator, hiddenApiRestrictions);
                                currentField = this.previousField;
                                nextField = ImmutableFieldReference.of(item);
                                this.previousField = nextField;
                                this.previousIndex = item.fieldIndex;
                                if (!skipDuplicates || currentField == null) {
                                    break;
                                }
                            } while (currentField.equals(nextField));
                            return item;
                        }
                    };
                }
            };
        }
        this.instanceFieldsOffset = this.staticFieldsOffset;
        return ImmutableSet.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedField> getInstanceFields() {
        return getInstanceFields(true);
    }

    @Nonnull
    public Iterable<? extends DexBackedField> getInstanceFields(final boolean skipDuplicates) {
        if (this.instanceFieldCount > 0) {
            DexReader reader = this.dexFile.getDataBuffer().readerAt(getInstanceFieldsOffset());
            final AnnotationsDirectory annotationsDirectory = getAnnotationsDirectory();
            final int fieldsStartOffset = reader.getOffset();
            HiddenApiRestrictionsReader hiddenApiRestrictionsReader = this.hiddenApiRestrictionsReader;
            final Iterator<Integer> hiddenApiRestrictionIterator = hiddenApiRestrictionsReader == null ? null : hiddenApiRestrictionsReader.getRestrictionsForInstanceFields();
            return new Iterable<DexBackedField>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.3
                @Override // java.lang.Iterable
                @Nonnull
                public Iterator<DexBackedField> iterator() {
                    final AnnotationsDirectory.AnnotationIterator annotationIterator = annotationsDirectory.getFieldAnnotationIterator();
                    return new VariableSizeLookaheadIterator<DexBackedField>(DexBackedClassDef.this.dexFile.getDataBuffer(), fieldsStartOffset) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.3.1
                        private int count;

                        @Nullable
                        private FieldReference previousField;
                        private int previousIndex;

                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                        @Nullable
                        public DexBackedField readNextItem(@Nonnull DexReader reader2) {
                            DexBackedField item;
                            FieldReference currentField;
                            FieldReference nextField;
                            do {
                                int i = this.count + 1;
                                this.count = i;
                                if (i > DexBackedClassDef.this.instanceFieldCount) {
                                    DexBackedClassDef.this.directMethodsOffset = reader2.getOffset();
                                    return endOfData();
                                }
                                int hiddenApiRestrictions = 7;
                                if (hiddenApiRestrictionIterator != null) {
                                    hiddenApiRestrictions = ((Integer) hiddenApiRestrictionIterator.next()).intValue();
                                }
                                item = new DexBackedField(DexBackedClassDef.this.dexFile, reader2, DexBackedClassDef.this, this.previousIndex, annotationIterator, hiddenApiRestrictions);
                                currentField = this.previousField;
                                nextField = ImmutableFieldReference.of(item);
                                this.previousField = nextField;
                                this.previousIndex = item.fieldIndex;
                                if (!skipDuplicates || currentField == null) {
                                    break;
                                }
                            } while (currentField.equals(nextField));
                            return item;
                        }
                    };
                }
            };
        }
        int i = this.instanceFieldsOffset;
        if (i > 0) {
            this.directMethodsOffset = i;
        }
        return ImmutableSet.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedField> getFields() {
        return Iterables.concat(getStaticFields(), getInstanceFields());
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedMethod> getDirectMethods() {
        return getDirectMethods(true);
    }

    @Nonnull
    public Iterable<? extends DexBackedMethod> getDirectMethods(final boolean skipDuplicates) {
        if (this.directMethodCount > 0) {
            DexReader reader = this.dexFile.getDataBuffer().readerAt(getDirectMethodsOffset());
            final AnnotationsDirectory annotationsDirectory = getAnnotationsDirectory();
            final int methodsStartOffset = reader.getOffset();
            HiddenApiRestrictionsReader hiddenApiRestrictionsReader = this.hiddenApiRestrictionsReader;
            final Iterator<Integer> hiddenApiRestrictionIterator = hiddenApiRestrictionsReader == null ? null : hiddenApiRestrictionsReader.getRestrictionsForDirectMethods();
            return new Iterable<DexBackedMethod>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.4
                @Override // java.lang.Iterable
                @Nonnull
                public Iterator<DexBackedMethod> iterator() {
                    final AnnotationsDirectory.AnnotationIterator methodAnnotationIterator = annotationsDirectory.getMethodAnnotationIterator();
                    final AnnotationsDirectory.AnnotationIterator parameterAnnotationIterator = annotationsDirectory.getParameterAnnotationIterator();
                    return new VariableSizeLookaheadIterator<DexBackedMethod>(DexBackedClassDef.this.dexFile.getDataBuffer(), methodsStartOffset) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.4.1
                        private int count;
                        private int previousIndex;

                        @Nullable
                        private MethodReference previousMethod;

                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                        @Nullable
                        public DexBackedMethod readNextItem(@Nonnull DexReader reader2) {
                            DexBackedMethod item;
                            MethodReference currentMethod;
                            MethodReference nextMethod;
                            do {
                                int i = this.count + 1;
                                this.count = i;
                                if (i > DexBackedClassDef.this.directMethodCount) {
                                    DexBackedClassDef.this.virtualMethodsOffset = reader2.getOffset();
                                    return endOfData();
                                }
                                int hiddenApiRestrictions = 7;
                                if (hiddenApiRestrictionIterator != null) {
                                    hiddenApiRestrictions = ((Integer) hiddenApiRestrictionIterator.next()).intValue();
                                }
                                item = new DexBackedMethod(DexBackedClassDef.this.dexFile, reader2, DexBackedClassDef.this, this.previousIndex, methodAnnotationIterator, parameterAnnotationIterator, hiddenApiRestrictions);
                                currentMethod = this.previousMethod;
                                nextMethod = ImmutableMethodReference.of(item);
                                this.previousMethod = nextMethod;
                                this.previousIndex = item.methodIndex;
                                if (!skipDuplicates || currentMethod == null) {
                                    break;
                                }
                            } while (currentMethod.equals(nextMethod));
                            return item;
                        }
                    };
                }
            };
        }
        int i = this.directMethodsOffset;
        if (i > 0) {
            this.virtualMethodsOffset = i;
        }
        return ImmutableSet.of();
    }

    @Nonnull
    public Iterable<? extends DexBackedMethod> getVirtualMethods(boolean skipDuplicates) {
        if (this.virtualMethodCount > 0) {
            DexReader reader = this.dexFile.getDataBuffer().readerAt(getVirtualMethodsOffset());
            AnnotationsDirectory annotationsDirectory = getAnnotationsDirectory();
            int methodsStartOffset = reader.getOffset();
            HiddenApiRestrictionsReader hiddenApiRestrictionsReader = this.hiddenApiRestrictionsReader;
            Iterator<Integer> hiddenApiRestrictionIterator = hiddenApiRestrictionsReader == null ? null : hiddenApiRestrictionsReader.getRestrictionsForVirtualMethods();
            return new AnonymousClass5(annotationsDirectory, methodsStartOffset, hiddenApiRestrictionIterator, skipDuplicates);
        }
        return ImmutableSet.of();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements Iterable<DexBackedMethod> {
        final AnnotationsDirectory.AnnotationIterator methodAnnotationIterator;
        final AnnotationsDirectory.AnnotationIterator parameterAnnotationIterator;
        final /* synthetic */ AnnotationsDirectory val$annotationsDirectory;
        final /* synthetic */ Iterator val$hiddenApiRestrictionIterator;
        final /* synthetic */ int val$methodsStartOffset;
        final /* synthetic */ boolean val$skipDuplicates;

        AnonymousClass5(AnnotationsDirectory annotationsDirectory, int i, Iterator it, boolean z) {
            this.val$annotationsDirectory = annotationsDirectory;
            this.val$methodsStartOffset = i;
            this.val$hiddenApiRestrictionIterator = it;
            this.val$skipDuplicates = z;
            this.methodAnnotationIterator = annotationsDirectory.getMethodAnnotationIterator();
            this.parameterAnnotationIterator = annotationsDirectory.getParameterAnnotationIterator();
        }

        @Override // java.lang.Iterable
        @Nonnull
        public Iterator<DexBackedMethod> iterator() {
            return new VariableSizeLookaheadIterator<DexBackedMethod>(DexBackedClassDef.this.dexFile.getDataBuffer(), this.val$methodsStartOffset) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.5.1
                private int count;
                private int previousIndex;

                @Nullable
                private MethodReference previousMethod;

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                @Nullable
                public DexBackedMethod readNextItem(@Nonnull DexReader reader) {
                    DexBackedMethod item;
                    MethodReference currentMethod;
                    MethodReference nextMethod;
                    do {
                        int i = this.count + 1;
                        this.count = i;
                        if (i > DexBackedClassDef.this.virtualMethodCount) {
                            return endOfData();
                        }
                        int hiddenApiRestrictions = 7;
                        if (AnonymousClass5.this.val$hiddenApiRestrictionIterator != null) {
                            hiddenApiRestrictions = ((Integer) AnonymousClass5.this.val$hiddenApiRestrictionIterator.next()).intValue();
                        }
                        item = new DexBackedMethod(DexBackedClassDef.this.dexFile, reader, DexBackedClassDef.this, this.previousIndex, AnonymousClass5.this.methodAnnotationIterator, AnonymousClass5.this.parameterAnnotationIterator, hiddenApiRestrictions);
                        currentMethod = this.previousMethod;
                        nextMethod = ImmutableMethodReference.of(item);
                        this.previousMethod = nextMethod;
                        this.previousIndex = item.methodIndex;
                        if (!AnonymousClass5.this.val$skipDuplicates || currentMethod == null) {
                            break;
                        }
                    } while (currentMethod.equals(nextMethod));
                    return item;
                }
            };
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedMethod> getVirtualMethods() {
        return getVirtualMethods(true);
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends DexBackedMethod> getMethods() {
        return Iterables.concat(getDirectMethods(), getVirtualMethods());
    }

    private AnnotationsDirectory getAnnotationsDirectory() {
        if (this.annotationsDirectory == null) {
            int annotationsDirectoryOffset = this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 20);
            this.annotationsDirectory = AnnotationsDirectory.newOrEmpty(this.dexFile, annotationsDirectoryOffset);
        }
        return this.annotationsDirectory;
    }

    private int getInstanceFieldsOffset() {
        int i = this.instanceFieldsOffset;
        if (i > 0) {
            return i;
        }
        DexReader reader = this.dexFile.getDataBuffer().readerAt(this.staticFieldsOffset);
        DexBackedField.skipFields(reader, this.staticFieldCount);
        int offset = reader.getOffset();
        this.instanceFieldsOffset = offset;
        return offset;
    }

    private int getDirectMethodsOffset() {
        int i = this.directMethodsOffset;
        if (i > 0) {
            return i;
        }
        DexReader reader = this.dexFile.getDataBuffer().readerAt(getInstanceFieldsOffset());
        DexBackedField.skipFields(reader, this.instanceFieldCount);
        int offset = reader.getOffset();
        this.directMethodsOffset = offset;
        return offset;
    }

    private int getVirtualMethodsOffset() {
        int i = this.virtualMethodsOffset;
        if (i > 0) {
            return i;
        }
        DexReader reader = this.dexFile.getDataBuffer().readerAt(getDirectMethodsOffset());
        DexBackedMethod.skipMethods(reader, this.directMethodCount);
        int offset = reader.getOffset();
        this.virtualMethodsOffset = offset;
        return offset;
    }

    public int getSize() {
        int size = 32 + 4;
        int interfacesLength = getInterfaces().size();
        if (interfacesLength > 0) {
            size = size + 4 + (interfacesLength * 2);
        }
        AnnotationsDirectory directory = getAnnotationsDirectory();
        if (!AnnotationsDirectory.EMPTY.equals(directory)) {
            size += 16;
            Set<? extends DexBackedAnnotation> classAnnotations = directory.getClassAnnotations();
            if (!classAnnotations.isEmpty()) {
                size = size + 4 + (classAnnotations.size() * 4);
            }
        }
        int staticInitialValuesOffset = this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 28);
        if (staticInitialValuesOffset != 0) {
            size += this.dexFile.getDataBuffer().readerAt(staticInitialValuesOffset).peekSmallUleb128Size();
        }
        int classDataOffset = this.dexFile.getBuffer().readSmallUint(this.classDefOffset + 24);
        if (classDataOffset > 0) {
            DexReader reader = this.dexFile.getDataBuffer().readerAt(classDataOffset);
            reader.readSmallUleb128();
            reader.readSmallUleb128();
            reader.readSmallUleb128();
            reader.readSmallUleb128();
            size += reader.getOffset() - classDataOffset;
        }
        for (DexBackedField dexBackedField : getFields()) {
            size += dexBackedField.getSize();
        }
        for (DexBackedMethod dexBackedMethod : getMethods()) {
            size += dexBackedMethod.getSize();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class HiddenApiRestrictionsReader {
        private int directMethodsStartOffset;
        private int instanceFieldsStartOffset;
        private final int startOffset;
        private int virtualMethodsStartOffset;

        public HiddenApiRestrictionsReader(int startOffset) {
            this.startOffset = startOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public VariableSizeListIterator<Integer> getRestrictionsForStaticFields() {
            return new VariableSizeListIterator<Integer>(DexBackedClassDef.this.dexFile.getDataBuffer(), this.startOffset, DexBackedClassDef.this.staticFieldCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.HiddenApiRestrictionsReader.1
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected /* bridge */ /* synthetic */ Integer readNextItem(@Nonnull DexReader dexReader, int i) {
                    return readNextItem((DexReader<? extends DexBuffer>) dexReader, i);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected Integer readNextItem(@Nonnull DexReader<? extends DexBuffer> reader, int index) {
                    return Integer.valueOf(reader.readSmallUleb128());
                }

                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator, java.util.ListIterator, java.util.Iterator
                public Integer next() {
                    if (nextIndex() == DexBackedClassDef.this.staticFieldCount) {
                        HiddenApiRestrictionsReader.this.instanceFieldsStartOffset = getReaderOffset();
                    }
                    return (Integer) super.next();
                }
            };
        }

        private int getInstanceFieldsStartOffset() {
            if (this.instanceFieldsStartOffset == 0) {
                DexReader<? extends DexBuffer> reader = DexBackedClassDef.this.dexFile.getDataBuffer().readerAt(this.startOffset);
                for (int i = 0; i < DexBackedClassDef.this.staticFieldCount; i++) {
                    reader.readSmallUleb128();
                }
                int i2 = reader.getOffset();
                this.instanceFieldsStartOffset = i2;
            }
            return this.instanceFieldsStartOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Iterator<Integer> getRestrictionsForInstanceFields() {
            return new VariableSizeListIterator<Integer>(DexBackedClassDef.this.dexFile.getDataBuffer(), getInstanceFieldsStartOffset(), DexBackedClassDef.this.instanceFieldCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.HiddenApiRestrictionsReader.2
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected /* bridge */ /* synthetic */ Integer readNextItem(@Nonnull DexReader dexReader, int i) {
                    return readNextItem((DexReader<? extends DexBuffer>) dexReader, i);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected Integer readNextItem(@Nonnull DexReader<? extends DexBuffer> reader, int index) {
                    return Integer.valueOf(reader.readSmallUleb128());
                }

                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator, java.util.ListIterator, java.util.Iterator
                public Integer next() {
                    if (nextIndex() == DexBackedClassDef.this.instanceFieldCount) {
                        HiddenApiRestrictionsReader.this.directMethodsStartOffset = getReaderOffset();
                    }
                    return (Integer) super.next();
                }
            };
        }

        private int getDirectMethodsStartOffset() {
            if (this.directMethodsStartOffset == 0) {
                DexReader<? extends DexBuffer> reader = DexBackedClassDef.this.dexFile.getDataBuffer().readerAt(getInstanceFieldsStartOffset());
                for (int i = 0; i < DexBackedClassDef.this.instanceFieldCount; i++) {
                    reader.readSmallUleb128();
                }
                int i2 = reader.getOffset();
                this.directMethodsStartOffset = i2;
            }
            return this.directMethodsStartOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Iterator<Integer> getRestrictionsForDirectMethods() {
            return new VariableSizeListIterator<Integer>(DexBackedClassDef.this.dexFile.getDataBuffer(), getDirectMethodsStartOffset(), DexBackedClassDef.this.directMethodCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.HiddenApiRestrictionsReader.3
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected /* bridge */ /* synthetic */ Integer readNextItem(@Nonnull DexReader dexReader, int i) {
                    return readNextItem((DexReader<? extends DexBuffer>) dexReader, i);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected Integer readNextItem(@Nonnull DexReader<? extends DexBuffer> reader, int index) {
                    return Integer.valueOf(reader.readSmallUleb128());
                }

                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator, java.util.ListIterator, java.util.Iterator
                public Integer next() {
                    if (nextIndex() == DexBackedClassDef.this.directMethodCount) {
                        HiddenApiRestrictionsReader.this.virtualMethodsStartOffset = getReaderOffset();
                    }
                    return (Integer) super.next();
                }
            };
        }

        private int getVirtualMethodsStartOffset() {
            if (this.virtualMethodsStartOffset == 0) {
                DexReader<? extends DexBuffer> reader = DexBackedClassDef.this.dexFile.getDataBuffer().readerAt(getDirectMethodsStartOffset());
                for (int i = 0; i < DexBackedClassDef.this.directMethodCount; i++) {
                    reader.readSmallUleb128();
                }
                int i2 = reader.getOffset();
                this.virtualMethodsStartOffset = i2;
            }
            return this.virtualMethodsStartOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Iterator<Integer> getRestrictionsForVirtualMethods() {
            return new VariableSizeListIterator<Integer>(DexBackedClassDef.this.dexFile.getDataBuffer(), getVirtualMethodsStartOffset(), DexBackedClassDef.this.virtualMethodCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef.HiddenApiRestrictionsReader.4
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected /* bridge */ /* synthetic */ Integer readNextItem(@Nonnull DexReader dexReader, int i) {
                    return readNextItem((DexReader<? extends DexBuffer>) dexReader, i);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator
                protected Integer readNextItem(@Nonnull DexReader<? extends DexBuffer> reader, int index) {
                    return Integer.valueOf(reader.readSmallUleb128());
                }
            };
        }
    }
}
