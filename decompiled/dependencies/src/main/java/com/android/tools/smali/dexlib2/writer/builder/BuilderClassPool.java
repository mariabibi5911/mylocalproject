package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import com.android.tools.smali.dexlib2.iface.debug.LineNumber;
import com.android.tools.smali.dexlib2.iface.debug.RestartLocal;
import com.android.tools.smali.dexlib2.iface.debug.SetSourceFile;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.util.EncodedValueUtils;
import com.android.tools.smali.dexlib2.writer.ClassSection;
import com.android.tools.smali.dexlib2.writer.DebugWriter;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import com.android.tools.smali.util.AbstractForwardSequentialList;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderClassPool extends BaseBuilderPool implements ClassSection<BuilderStringReference, BuilderTypeReference, BuilderTypeList, BuilderClassDef, BuilderField, BuilderMethod, BuilderAnnotationSet, BuilderEncodedValues.BuilderArrayEncodedValue> {

    @Nonnull
    private final ConcurrentMap<String, BuilderClassDef> internedItems;
    private ImmutableList<BuilderClassDef> sortedClasses;
    private static final Predicate<Field> HAS_INITIALIZER = new Predicate<Field>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.2
        @Override // com.google.common.base.Predicate
        public boolean apply(Field input) {
            EncodedValue encodedValue = input.getInitialValue();
            return (encodedValue == null || EncodedValueUtils.isDefaultValue(encodedValue)) ? false : true;
        }
    };
    private static final Function<BuilderField, BuilderEncodedValues.BuilderEncodedValue> GET_INITIAL_VALUE = new Function<BuilderField, BuilderEncodedValues.BuilderEncodedValue>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.3
        @Override // com.google.common.base.Function
        public BuilderEncodedValues.BuilderEncodedValue apply(BuilderField input) {
            BuilderEncodedValues.BuilderEncodedValue initialValue = input.getInitialValue();
            if (initialValue == null) {
                return BuilderEncodedValues.defaultValueForType(input.getType());
            }
            return initialValue;
        }
    };
    private static final Predicate<BuilderMethodParameter> HAS_PARAMETER_ANNOTATIONS = new Predicate<BuilderMethodParameter>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.4
        @Override // com.google.common.base.Predicate
        public boolean apply(BuilderMethodParameter input) {
            return input.getAnnotations().size() > 0;
        }
    };
    private static final Function<BuilderMethodParameter, BuilderAnnotationSet> PARAMETER_ANNOTATIONS = new Function<BuilderMethodParameter, BuilderAnnotationSet>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.5
        @Override // com.google.common.base.Function
        public BuilderAnnotationSet apply(BuilderMethodParameter input) {
            return input.getAnnotations();
        }
    };

    public BuilderClassPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
        this.sortedClasses = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nonnull
    public BuilderClassDef internClass(@Nonnull BuilderClassDef classDef) {
        BuilderClassDef prev = this.internedItems.put(classDef.getType(), classDef);
        if (prev != null) {
            throw new ExceptionWithContext("Class %s has already been interned", classDef.getType());
        }
        return classDef;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderClassDef> getSortedClasses() {
        if (this.sortedClasses == null) {
            this.sortedClasses = Ordering.natural().immutableSortedCopy(this.internedItems.values());
        }
        return this.sortedClasses;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Map.Entry<? extends BuilderClassDef, Integer> getClassEntryByType(@Nullable BuilderTypeReference type) {
        final BuilderClassDef classDef;
        if (type == null || (classDef = this.internedItems.get(type.getType())) == null) {
            return null;
        }
        return new Map.Entry<BuilderClassDef, Integer>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Map.Entry
            public BuilderClassDef getKey() {
                return classDef;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Map.Entry
            public Integer getValue() {
                return Integer.valueOf(classDef.classDefIndex);
            }

            @Override // java.util.Map.Entry
            public Integer setValue(Integer value) {
                BuilderClassDef builderClassDef = classDef;
                int intValue = value.intValue();
                builderClassDef.classDefIndex = intValue;
                return Integer.valueOf(intValue);
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public BuilderTypeReference getType(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.type;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAccessFlags(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderTypeReference getSuperclass(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.superclass;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderTypeList getInterfaces(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.interfaces;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderStringReference getSourceFile(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.sourceFile;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderEncodedValues.BuilderArrayEncodedValue getStaticInitializers(@Nonnull BuilderClassDef classDef) {
        return classDef.staticInitializers;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderField> getSortedStaticFields(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getStaticFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderField> getSortedInstanceFields(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getInstanceFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderField> getSortedFields(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getFields();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderMethod> getSortedDirectMethods(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getDirectMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderMethod> getSortedVirtualMethods(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getVirtualMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Collection<? extends BuilderMethod> getSortedMethods(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.getMethods();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getFieldAccessFlags(@Nonnull BuilderField builderField) {
        return builderField.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getMethodAccessFlags(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Set<HiddenApiRestriction> getFieldHiddenApiRestrictions(@Nonnull BuilderField builderField) {
        return builderField.getHiddenApiRestrictions();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public Set<HiddenApiRestriction> getMethodHiddenApiRestrictions(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.getHiddenApiRestrictions();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderAnnotationSet getClassAnnotations(@Nonnull BuilderClassDef builderClassDef) {
        if (builderClassDef.annotations.isEmpty()) {
            return null;
        }
        return builderClassDef.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderAnnotationSet getFieldAnnotations(@Nonnull BuilderField builderField) {
        if (builderField.annotations.isEmpty()) {
            return null;
        }
        return builderField.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderAnnotationSet getMethodAnnotations(@Nonnull BuilderMethod builderMethod) {
        if (builderMethod.annotations.isEmpty()) {
            return null;
        }
        return builderMethod.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public List<? extends BuilderAnnotationSet> getParameterAnnotations(@Nonnull BuilderMethod method) {
        final List<? extends BuilderMethodParameter> parameters = method.getParameters();
        boolean hasParameterAnnotations = Iterables.any(parameters, HAS_PARAMETER_ANNOTATIONS);
        if (hasParameterAnnotations) {
            return new AbstractForwardSequentialList<BuilderAnnotationSet>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.6
                @Override // com.android.tools.smali.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                @Nonnull
                public Iterator<BuilderAnnotationSet> iterator() {
                    return FluentIterable.from(parameters).transform(BuilderClassPool.PARAMETER_ANNOTATIONS).iterator();
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
    public Iterable<? extends DebugItem> getDebugItems(@Nonnull BuilderMethod builderMethod) {
        MethodImplementation impl = builderMethod.getImplementation();
        if (impl == null) {
            return null;
        }
        return impl.getDebugItems();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Iterable<? extends BuilderStringReference> getParameterNames(@Nonnull BuilderMethod method) {
        return Iterables.transform(method.getParameters(), new Function<BuilderMethodParameter, BuilderStringReference>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.7
            @Override // com.google.common.base.Function
            @Nullable
            public BuilderStringReference apply(BuilderMethodParameter input) {
                return input.name;
            }
        });
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getRegisterCount(@Nonnull BuilderMethod builderMethod) {
        MethodImplementation impl = builderMethod.getImplementation();
        if (impl == null) {
            return 0;
        }
        return impl.getRegisterCount();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public Iterable<? extends Instruction> getInstructions(@Nonnull BuilderMethod builderMethod) {
        MethodImplementation impl = builderMethod.getImplementation();
        if (impl == null) {
            return null;
        }
        return impl.getInstructions();
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull BuilderMethod builderMethod) {
        MethodImplementation impl = builderMethod.getImplementation();
        if (impl == null) {
            return ImmutableList.of();
        }
        return impl.getTryBlocks();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nullable
    public BuilderTypeReference getExceptionType(@Nonnull ExceptionHandler handler) {
        return checkTypeReference(handler.getExceptionTypeReference());
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    @Nonnull
    public MutableMethodImplementation makeMutableMethodImplementation(@Nonnull BuilderMethod builderMethod) {
        MethodImplementation impl = builderMethod.getImplementation();
        if (impl instanceof MutableMethodImplementation) {
            return (MutableMethodImplementation) impl;
        }
        return new MutableMethodImplementation(impl);
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setAnnotationDirectoryOffset(@Nonnull BuilderClassDef builderClassDef, int offset) {
        builderClassDef.annotationDirectoryOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAnnotationDirectoryOffset(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.annotationDirectoryOffset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setAnnotationSetRefListOffset(@Nonnull BuilderMethod builderMethod, int offset) {
        builderMethod.annotationSetRefListOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getAnnotationSetRefListOffset(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.annotationSetRefListOffset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void setCodeItemOffset(@Nonnull BuilderMethod builderMethod, int offset) {
        builderMethod.codeItemOffset = offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public int getCodeItemOffset(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.codeItemOffset;
    }

    @Nullable
    private BuilderStringReference checkStringReference(@Nullable StringReference stringReference) {
        if (stringReference == null) {
            return null;
        }
        try {
            return (BuilderStringReference) stringReference;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Only StringReference instances returned by DexBuilder.internStringReference or DexBuilder.internNullableStringReference may be used.");
        }
    }

    @Nullable
    private BuilderTypeReference checkTypeReference(@Nullable TypeReference typeReference) {
        if (typeReference == null) {
            return null;
        }
        try {
            return (BuilderTypeReference) typeReference;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Only TypeReference instances returned by DexBuilder.internTypeReference or DexBuilder.internNullableTypeReference may be used.");
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.ClassSection
    public void writeDebugItem(@Nonnull DebugWriter<BuilderStringReference, BuilderTypeReference> writer, DebugItem debugItem) throws IOException {
        switch (debugItem.getDebugItemType()) {
            case 3:
                StartLocal startLocal = (StartLocal) debugItem;
                writer.writeStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), checkStringReference(startLocal.getNameReference()), checkTypeReference(startLocal.getTypeReference()), checkStringReference(startLocal.getSignatureReference()));
                return;
            case 4:
            default:
                throw new ExceptionWithContext("Unexpected debug item type: %d", Integer.valueOf(debugItem.getDebugItemType()));
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
                writer.writeSetSourceFile(setSourceFile.getCodeAddress(), checkStringReference(setSourceFile.getSourceFileReference()));
                return;
            case 10:
                LineNumber lineNumber = (LineNumber) debugItem;
                writer.writeLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
                return;
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderClassDef builderClassDef) {
        return builderClassDef.classDefIndex;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderClassDef, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderClassDef>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassPool.8
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderClassDef key) {
                return key.classDefIndex;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderClassDef key, int value) {
                int prev = key.classDefIndex;
                key.classDefIndex = value;
                return prev;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }
}
