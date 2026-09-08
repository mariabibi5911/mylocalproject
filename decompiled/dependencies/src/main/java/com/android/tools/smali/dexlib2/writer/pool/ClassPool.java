package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation;
import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import com.android.tools.smali.dexlib2.iface.debug.LineNumber;
import com.android.tools.smali.dexlib2.iface.debug.RestartLocal;
import com.android.tools.smali.dexlib2.iface.debug.SetSourceFile;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.writer.ClassSection;
import com.android.tools.smali.dexlib2.writer.DebugWriter;
import com.android.tools.smali.dexlib2.writer.pool.TypeListPool;
import com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil;
import com.android.tools.smali.util.AbstractForwardSequentialList;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ClassPool extends BasePool<String, PoolClassDef> implements ClassSection<CharSequence, CharSequence, TypeListPool.Key<? extends Collection<? extends CharSequence>>, PoolClassDef, Field, PoolMethod, Set<? extends Annotation>, ArrayEncodedValue> {
    private static final Predicate<MethodParameter> HAS_PARAMETER_ANNOTATIONS = new Predicate<MethodParameter>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.2
        @Override // com.google.common.base.Predicate
        public boolean apply(MethodParameter input) {
            return input.getAnnotations().size() > 0;
        }
    };
    private static final Function<MethodParameter, Set<? extends Annotation>> PARAMETER_ANNOTATIONS = new Function<MethodParameter, Set<? extends Annotation>>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.3
        @Override // com.google.common.base.Function
        public Set<? extends Annotation> apply(MethodParameter input) {
            return input.getAnnotations();
        }
    };
    private ImmutableList<PoolClassDef> sortedClasses;

    public ClassPool(@Nonnull DexPool dexPool) {
        super(dexPool);
        this.sortedClasses = null;
    }

    public void intern(@Nonnull ClassDef classDef) {
        PoolClassDef poolClassDef = new PoolClassDef(classDef);
        PoolClassDef prev = (PoolClassDef) this.internedItems.put(poolClassDef.getType(), poolClassDef);
        if (prev != null) {
            throw new ExceptionWithContext("Class %s has already been interned", poolClassDef.getType());
        }
        ((TypePool) this.dexPool.typeSection).intern(poolClassDef.getType());
        ((TypePool) this.dexPool.typeSection).internNullable(poolClassDef.getSuperclass());
        ((TypeListPool) this.dexPool.typeListSection).intern(poolClassDef.getInterfaces());
        ((StringPool) this.dexPool.stringSection).internNullable(poolClassDef.getSourceFile());
        HashSet<String> fields = new HashSet<>();
        for (Field field : poolClassDef.getFields()) {
            String fieldDescriptor = DexFormatter.INSTANCE.getShortFieldDescriptor(field);
            if (!fields.add(fieldDescriptor)) {
                throw new ExceptionWithContext("Multiple definitions for field %s->%s", poolClassDef.getType(), fieldDescriptor);
            }
            ((FieldPool) this.dexPool.fieldSection).intern(field);
            EncodedValue initialValue = field.getInitialValue();
            if (initialValue != null) {
                this.dexPool.internEncodedValue(initialValue);
            }
            ((AnnotationSetPool) this.dexPool.annotationSetSection).intern(field.getAnnotations());
            ArrayEncodedValue staticInitializers = getStaticInitializers(poolClassDef);
            if (staticInitializers != null) {
                ((EncodedArrayPool) this.dexPool.encodedArraySection).intern(staticInitializers);
            }
        }
        HashSet<String> methods = new HashSet<>();
        for (PoolMethod method : poolClassDef.getMethods()) {
            String methodDescriptor = DexFormatter.INSTANCE.getShortMethodDescriptor(method);
            if (!methods.add(methodDescriptor)) {
                throw new ExceptionWithContext("Multiple definitions for method %s->%s", poolClassDef.getType(), methodDescriptor);
            }
            ((MethodPool) this.dexPool.methodSection).intern(method);
            internCode(method);
            internDebug(method);
            ((AnnotationSetPool) this.dexPool.annotationSetSection).intern(method.getAnnotations());
            for (MethodParameter parameter : method.getParameters()) {
                ((AnnotationSetPool) this.dexPool.annotationSetSection).intern(parameter.getAnnotations());
            }
        }
        ((AnnotationSetPool) this.dexPool.annotationSetSection).intern(poolClassDef.getAnnotations());
    }

    private void internCode(@Nonnull Method method) {
        boolean hasInstruction = false;
        MethodImplementation methodImpl = method.getImplementation();
        if (methodImpl != null) {
            for (Instruction instruction : methodImpl.getInstructions()) {
                hasInstruction = true;
                if (instruction instanceof ReferenceInstruction) {
                    ReferenceInstruction refInst = (ReferenceInstruction) instruction;
                    internReference(refInst.getReference(), refInst.getReferenceType());
                }
                if (instruction instanceof DualReferenceInstruction) {
                    DualReferenceInstruction dualRefInst = (DualReferenceInstruction) instruction;
                    internReference(dualRefInst.getReference2(), dualRefInst.getReferenceType2());
                }
            }
            List<? extends TryBlock> tryBlocks = methodImpl.getTryBlocks();
            if (!hasInstruction && tryBlocks.size() > 0) {
                throw new ExceptionWithContext("Method %s has no instructions, but has try blocks.", method);
            }
            for (TryBlock<? extends ExceptionHandler> tryBlock : methodImpl.getTryBlocks()) {
                Iterator<? extends Object> it = tryBlock.getExceptionHandlers().iterator();
                while (it.hasNext()) {
                    ExceptionHandler handler = (ExceptionHandler) it.next();
                    ((TypePool) this.dexPool.typeSection).internNullable(handler.getExceptionType());
                }
            }
        }
    }

    private void internReference(@Nonnull Reference reference, int referenceType) {
        switch (referenceType) {
            case 0:
                ((StringPool) this.dexPool.stringSection).intern((StringReference) reference);
                return;
            case 1:
                ((TypePool) this.dexPool.typeSection).intern(((TypeReference) reference).getType());
                return;
            case 2:
                ((FieldPool) this.dexPool.fieldSection).intern((FieldReference) reference);
                return;
            case 3:
                ((MethodPool) this.dexPool.methodSection).intern((MethodReference) reference);
                return;
            case 4:
                ((ProtoPool) this.dexPool.protoSection).intern((MethodProtoReference) reference);
                return;
            case 5:
                ((CallSitePool) this.dexPool.callSiteSection).intern((CallSiteReference) reference);
                return;
            case 6:
                ((MethodHandlePool) this.dexPool.methodHandleSection).intern((MethodHandleReference) reference);
                return;
            default:
                throw new ExceptionWithContext("Unrecognized reference type: %d", Integer.valueOf(referenceType));
        }
    }

    private void internDebug(@Nonnull Method method) {
        for (MethodParameter param : method.getParameters()) {
            String paramName = param.getName();
            if (paramName != null) {
                ((StringPool) this.dexPool.stringSection).intern(paramName);
            }
        }
        MethodImplementation methodImpl = method.getImplementation();
        if (methodImpl != null) {
            for (DebugItem debugItem : methodImpl.getDebugItems()) {
                switch (debugItem.getDebugItemType()) {
                    case 3:
                        StartLocal startLocal = (StartLocal) debugItem;
                        ((StringPool) this.dexPool.stringSection).internNullable(startLocal.getName());
                        ((TypePool) this.dexPool.typeSection).internNullable(startLocal.getType());
                        ((StringPool) this.dexPool.stringSection).internNullable(startLocal.getSignature());
                        break;
                    case 9:
                        ((StringPool) this.dexPool.stringSection).internNullable(((SetSourceFile) debugItem).getSourceFile());
                        break;
                }
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends PoolClassDef> getSortedClasses() {
        if (this.sortedClasses == null) {
            this.sortedClasses = Ordering.natural().immutableSortedCopy(this.internedItems.values());
        }
        return this.sortedClasses;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Map.Entry<? extends PoolClassDef, Integer> getClassEntryByType(@Nullable CharSequence name) {
        final PoolClassDef classDef;
        if (name == null || (classDef = (PoolClassDef) this.internedItems.get(name.toString())) == null) {
            return null;
        }
        return new Map.Entry<PoolClassDef, Integer>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Map.Entry
            public PoolClassDef getKey() {
                return classDef;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Map.Entry
            public Integer getValue() {
                return Integer.valueOf(classDef.classDefIndex);
            }

            @Override // java.util.Map.Entry
            public Integer setValue(Integer value) {
                PoolClassDef poolClassDef = classDef;
                int intValue = value.intValue();
                poolClassDef.classDefIndex = intValue;
                return Integer.valueOf(intValue);
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public CharSequence getType(@Nonnull PoolClassDef classDef) {
        return classDef.getType();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAccessFlags(@Nonnull PoolClassDef classDef) {
        return classDef.getAccessFlags();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public CharSequence getSuperclass(@Nonnull PoolClassDef classDef) {
        return classDef.getSuperclass();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public TypeListPool.Key<List<String>> getInterfaces(@Nonnull PoolClassDef classDef) {
        return classDef.interfaces;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public CharSequence getSourceFile(@Nonnull PoolClassDef classDef) {
        return classDef.getSourceFile();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public ArrayEncodedValue getStaticInitializers(@Nonnull PoolClassDef classDef) {
        return StaticInitializerUtil.getStaticInitializers(classDef.getStaticFields());
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends Field> getSortedStaticFields(@Nonnull PoolClassDef classDef) {
        return classDef.getStaticFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends Field> getSortedInstanceFields(@Nonnull PoolClassDef classDef) {
        return classDef.getInstanceFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends Field> getSortedFields(@Nonnull PoolClassDef classDef) {
        return classDef.getFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<PoolMethod> getSortedDirectMethods(@Nonnull PoolClassDef classDef) {
        return classDef.getDirectMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<PoolMethod> getSortedVirtualMethods(@Nonnull PoolClassDef classDef) {
        return classDef.getVirtualMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends PoolMethod> getSortedMethods(@Nonnull PoolClassDef classDef) {
        return classDef.getMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getFieldAccessFlags(@Nonnull Field field) {
        return field.getAccessFlags();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getMethodAccessFlags(@Nonnull PoolMethod method) {
        return method.getAccessFlags();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Set<HiddenApiRestriction> getFieldHiddenApiRestrictions(@Nonnull Field field) {
        return field.getHiddenApiRestrictions();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Set<HiddenApiRestriction> getMethodHiddenApiRestrictions(@Nonnull PoolMethod poolMethod) {
        return poolMethod.getHiddenApiRestrictions();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Set<? extends Annotation> getClassAnnotations(@Nonnull PoolClassDef classDef) {
        Set<? extends Annotation> annotations = classDef.getAnnotations();
        if (annotations.size() == 0) {
            return null;
        }
        return annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Set<? extends Annotation> getFieldAnnotations(@Nonnull Field field) {
        Set<? extends Annotation> annotations = field.getAnnotations();
        if (annotations.size() == 0) {
            return null;
        }
        return annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Set<? extends Annotation> getMethodAnnotations(@Nonnull PoolMethod method) {
        Set<? extends Annotation> annotations = method.getAnnotations();
        if (annotations.size() == 0) {
            return null;
        }
        return annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public List<? extends Set<? extends Annotation>> getParameterAnnotations(@Nonnull PoolMethod method) {
        final List<? extends MethodParameter> parameters = method.getParameters();
        boolean hasParameterAnnotations = Iterables.any(parameters, HAS_PARAMETER_ANNOTATIONS);
        if (hasParameterAnnotations) {
            return new AbstractForwardSequentialList<Set<? extends Annotation>>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.4
                @Override // com.android.tools.smali.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                @Nonnull
                public Iterator<Set<? extends Annotation>> iterator() {
                    return FluentIterable.from(parameters).transform(ClassPool.PARAMETER_ANNOTATIONS).iterator();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameters.size();
                }
            };
        }
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Iterable<? extends DebugItem> getDebugItems(@Nonnull PoolMethod method) {
        MethodImplementation impl = method.getImplementation();
        if (impl != null) {
            return impl.getDebugItems();
        }
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Iterable<CharSequence> getParameterNames(@Nonnull PoolMethod method) {
        return Iterables.transform(method.getParameters(), new Function<MethodParameter, CharSequence>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.5
            @Override // com.google.common.base.Function
            @Nullable
            public CharSequence apply(MethodParameter input) {
                return input.getName();
            }
        });
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getRegisterCount(@Nonnull PoolMethod method) {
        MethodImplementation impl = method.getImplementation();
        if (impl != null) {
            return impl.getRegisterCount();
        }
        return 0;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Iterable<? extends Instruction> getInstructions(@Nonnull PoolMethod method) {
        MethodImplementation impl = method.getImplementation();
        if (impl != null) {
            return impl.getInstructions();
        }
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull PoolMethod method) {
        MethodImplementation impl = method.getImplementation();
        if (impl != null) {
            return impl.getTryBlocks();
        }
        return ImmutableList.of();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public CharSequence getExceptionType(@Nonnull ExceptionHandler handler) {
        return handler.getExceptionType();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public MutableMethodImplementation makeMutableMethodImplementation(@Nonnull PoolMethod poolMethod) {
        return new MutableMethodImplementation(poolMethod.getImplementation());
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setAnnotationDirectoryOffset(@Nonnull PoolClassDef classDef, int offset) {
        classDef.annotationDirectoryOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAnnotationDirectoryOffset(@Nonnull PoolClassDef classDef) {
        return classDef.annotationDirectoryOffset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setAnnotationSetRefListOffset(@Nonnull PoolMethod method, int offset) {
        method.annotationSetRefListOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAnnotationSetRefListOffset(@Nonnull PoolMethod method) {
        return method.annotationSetRefListOffset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setCodeItemOffset(@Nonnull PoolMethod method, int offset) {
        method.codeItemOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getCodeItemOffset(@Nonnull PoolMethod method) {
        return method.codeItemOffset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void writeDebugItem(@Nonnull DebugWriter<CharSequence, CharSequence> writer, DebugItem debugItem) throws IOException {
        switch (debugItem.getDebugItemType()) {
            case 3:
                StartLocal startLocal = (StartLocal) debugItem;
                writer.writeStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), startLocal.getName(), startLocal.getType(), startLocal.getSignature());
                return;
            case 5:
                EndLocal endLocal = (EndLocal) debugItem;
                writer.writeEndLocal(endLocal.getCodeAddress(), endLocal.getRegister());
                return;
            case 6:
                RestartLocal restartLocal = (RestartLocal) debugItem;
                writer.writeRestartLocal(restartLocal.getCodeAddress(), restartLocal.getRegister());
                return;
            case 7:
                writer.writePrologueEnd(debugItem.getCodeAddress());
                return;
            case 8:
                writer.writeEpilogueBegin(debugItem.getCodeAddress());
                return;
            case 9:
                SetSourceFile setSourceFile = (SetSourceFile) debugItem;
                writer.writeSetSourceFile(setSourceFile.getCodeAddress(), setSourceFile.getSourceFile());
                break;
            case 10:
                LineNumber lineNumber = (LineNumber) debugItem;
                writer.writeLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
                return;
        }
        throw new ExceptionWithContext("Unexpected debug item type: %d", Integer.valueOf(debugItem.getDebugItemType()));
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull PoolClassDef classDef) {
        return classDef.classDefIndex;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<PoolClassDef, Integer>> getItems() {
        return new AbstractCollection<Map.Entry<PoolClassDef, Integer>>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.6
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<Map.Entry<PoolClassDef, Integer>> iterator() {
                return new Iterator<Map.Entry<PoolClassDef, Integer>>() { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.6.1
                    Iterator<PoolClassDef> iter;

                    {
                        this.iter = ClassPool.this.internedItems.values().iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Map.Entry<PoolClassDef, Integer> next() {
                        return new Map.Entry<PoolClassDef, Integer>(this.iter.next()) { // from class: com.android.tools.smali.dexlib2.writer.pool.ClassPool.1MapEntry

                            @Nonnull
                            private final PoolClassDef classDef;

                            {
                                this.classDef = classDef;
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.Map.Entry
                            public PoolClassDef getKey() {
                                return this.classDef;
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.Map.Entry
                            public Integer getValue() {
                                return Integer.valueOf(this.classDef.classDefIndex);
                            }

                            @Override // java.util.Map.Entry
                            public Integer setValue(Integer value) {
                                int prev = this.classDef.classDefIndex;
                                this.classDef.classDefIndex = value.intValue();
                                return Integer.valueOf(prev);
                            }
                        };
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return ClassPool.this.internedItems.size();
            }
        };
    }
}
