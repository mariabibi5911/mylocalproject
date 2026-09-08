package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
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
import com.android.tools.smali.dexlib2.util.FieldUtil;
import com.android.tools.smali.dexlib2.writer.DexWriter;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBuilder extends DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderCallSiteReference, BuilderMethodHandleReference, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderArrayEncodedValue, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderCallSitePool, BuilderMethodHandlePool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool, BuilderEncodedArrayPool> {
    @Override // com.android.tools.smali.dexlib2.writer.DexWriter
    protected /* bridge */ /* synthetic */ void writeEncodedValue(@Nonnull DexWriter.InternalEncodedValueWriter internalEncodedValueWriter, @Nonnull BuilderEncodedValues.BuilderEncodedValue builderEncodedValue) throws IOException {
        writeEncodedValue2((DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderCallSiteReference, BuilderMethodHandleReference, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderArrayEncodedValue, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderCallSitePool, BuilderMethodHandlePool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool, BuilderEncodedArrayPool>.InternalEncodedValueWriter) internalEncodedValueWriter, builderEncodedValue);
    }

    public DexBuilder(@Nonnull Opcodes opcodes) {
        super(opcodes);
    }

    @Override // com.android.tools.smali.dexlib2.writer.DexWriter
    @Nonnull
    protected DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderCallSiteReference, BuilderMethodHandleReference, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderArrayEncodedValue, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderCallSitePool, BuilderMethodHandlePool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool, BuilderEncodedArrayPool>.SectionProvider getSectionProvider() {
        return new DexBuilderSectionProvider();
    }

    @Nonnull
    public BuilderField internField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable EncodedValue initialValue, @Nonnull Set<? extends Annotation> annotations, @Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions) {
        return new BuilderField(((BuilderFieldPool) this.fieldSection).internField(definingClass, name, type), accessFlags, internNullableEncodedValue(initialValue), ((BuilderAnnotationSetPool) this.annotationSetSection).internAnnotationSet(annotations), hiddenApiRestrictions);
    }

    @Nonnull
    public BuilderMethod internMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable List<? extends MethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nonnull Set<? extends Annotation> annotations, @Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions, @Nullable MethodImplementation methodImplementation) {
        List<? extends MethodParameter> parameters2;
        if (parameters != null) {
            parameters2 = parameters;
        } else {
            parameters2 = ImmutableList.of();
        }
        return new BuilderMethod(((BuilderMethodPool) this.methodSection).internMethod(definingClass, name, parameters2, returnType), internMethodParameters(parameters2), accessFlags, ((BuilderAnnotationSetPool) this.annotationSetSection).internAnnotationSet(annotations), hiddenApiRestrictions, methodImplementation);
    }

    @Nonnull
    public BuilderClassDef internClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable List<String> interfaces, @Nullable String sourceFile, @Nonnull Set<? extends Annotation> annotations, @Nullable Iterable<? extends BuilderField> fields, @Nullable Iterable<? extends BuilderMethod> methods) {
        List<String> interfaces2;
        if (interfaces == null) {
            interfaces2 = ImmutableList.of();
        } else {
            Set<String> interfaces_copy = Sets.newHashSet(interfaces);
            Iterator<String> interfaceIterator = interfaces.iterator();
            while (interfaceIterator.hasNext()) {
                String iface = interfaceIterator.next();
                if (!interfaces_copy.contains(iface)) {
                    interfaceIterator.remove();
                } else {
                    interfaces_copy.remove(iface);
                }
            }
            interfaces2 = interfaces;
        }
        ImmutableSortedSet<BuilderField> staticFields = null;
        ImmutableSortedSet<BuilderField> instanceFields = null;
        BuilderEncodedValues.BuilderArrayEncodedValue internedStaticInitializers = null;
        if (fields != null) {
            staticFields = ImmutableSortedSet.copyOf(Iterables.filter(fields, FieldUtil.FIELD_IS_STATIC));
            instanceFields = ImmutableSortedSet.copyOf(Iterables.filter(fields, FieldUtil.FIELD_IS_INSTANCE));
            ArrayEncodedValue staticInitializers = StaticInitializerUtil.getStaticInitializers(staticFields);
            if (staticInitializers != null) {
                internedStaticInitializers = ((BuilderEncodedArrayPool) this.encodedArraySection).internArrayEncodedValue(staticInitializers);
            }
        }
        return ((BuilderClassPool) this.classSection).internClass(new BuilderClassDef(((BuilderTypePool) this.typeSection).internType(type), accessFlags, ((BuilderTypePool) this.typeSection).internNullableType(superclass), ((BuilderTypeListPool) this.typeListSection).internTypeList(interfaces2), ((BuilderStringPool) this.stringSection).internNullableString(sourceFile), ((BuilderAnnotationSetPool) this.annotationSetSection).internAnnotationSet(annotations), staticFields, instanceFields, methods, internedStaticInitializers));
    }

    public BuilderCallSiteReference internCallSite(@Nonnull CallSiteReference callSiteReference) {
        return ((BuilderCallSitePool) this.callSiteSection).internCallSite(callSiteReference);
    }

    public BuilderMethodHandleReference internMethodHandle(@Nonnull MethodHandleReference methodHandleReference) {
        return ((BuilderMethodHandlePool) this.methodHandleSection).internMethodHandle(methodHandleReference);
    }

    @Nonnull
    public BuilderStringReference internStringReference(@Nonnull String string) {
        return ((BuilderStringPool) this.stringSection).internString(string);
    }

    @Nullable
    public BuilderStringReference internNullableStringReference(@Nullable String string) {
        if (string != null) {
            return internStringReference(string);
        }
        return null;
    }

    @Nonnull
    public BuilderTypeReference internTypeReference(@Nonnull String type) {
        return ((BuilderTypePool) this.typeSection).internType(type);
    }

    @Nullable
    public BuilderTypeReference internNullableTypeReference(@Nullable String type) {
        if (type != null) {
            return internTypeReference(type);
        }
        return null;
    }

    @Nonnull
    public BuilderFieldReference internFieldReference(@Nonnull FieldReference field) {
        return ((BuilderFieldPool) this.fieldSection).internField(field);
    }

    @Nonnull
    public BuilderMethodReference internMethodReference(@Nonnull MethodReference method) {
        return ((BuilderMethodPool) this.methodSection).internMethod(method);
    }

    @Nonnull
    public BuilderMethodProtoReference internMethodProtoReference(@Nonnull MethodProtoReference methodProto) {
        return ((BuilderProtoPool) this.protoSection).internMethodProto(methodProto);
    }

    @Nonnull
    public BuilderReference internReference(@Nonnull Reference reference) {
        if (reference instanceof StringReference) {
            return internStringReference(((StringReference) reference).getString());
        }
        if (reference instanceof TypeReference) {
            return internTypeReference(((TypeReference) reference).getType());
        }
        if (reference instanceof MethodReference) {
            return internMethodReference((MethodReference) reference);
        }
        if (reference instanceof FieldReference) {
            return internFieldReference((FieldReference) reference);
        }
        if (reference instanceof MethodProtoReference) {
            return internMethodProtoReference((MethodProtoReference) reference);
        }
        if (reference instanceof CallSiteReference) {
            return internCallSite((CallSiteReference) reference);
        }
        if (reference instanceof MethodHandleReference) {
            return internMethodHandle((MethodHandleReference) reference);
        }
        throw new IllegalArgumentException("Could not determine type of reference");
    }

    @Nonnull
    private List<BuilderMethodParameter> internMethodParameters(@Nullable List<? extends MethodParameter> methodParameters) {
        if (methodParameters == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(Iterators.transform(methodParameters.iterator(), new Function<MethodParameter, BuilderMethodParameter>() { // from class: com.android.tools.smali.dexlib2.writer.builder.DexBuilder.1
            @Override // com.google.common.base.Function
            @Nullable
            public BuilderMethodParameter apply(MethodParameter input) {
                return DexBuilder.this.internMethodParameter(input);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public BuilderMethodParameter internMethodParameter(@Nonnull MethodParameter methodParameter) {
        return new BuilderMethodParameter(((BuilderTypePool) this.typeSection).internType(methodParameter.getType()), ((BuilderStringPool) this.stringSection).internNullableString(methodParameter.getName()), ((BuilderAnnotationSetPool) this.annotationSetSection).internAnnotationSet(methodParameter.getAnnotations()));
    }

    /* renamed from: writeEncodedValue, reason: avoid collision after fix types in other method */
    protected void writeEncodedValue2(@Nonnull DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderCallSiteReference, BuilderMethodHandleReference, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderArrayEncodedValue, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderCallSitePool, BuilderMethodHandlePool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool, BuilderEncodedArrayPool>.InternalEncodedValueWriter writer, @Nonnull BuilderEncodedValues.BuilderEncodedValue encodedValue) throws IOException {
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
                writer.writeMethodType(((BuilderEncodedValues.BuilderMethodTypeEncodedValue) encodedValue).methodProtoReference);
                return;
            case 22:
                writer.writeMethodHandle(((BuilderEncodedValues.BuilderMethodHandleEncodedValue) encodedValue).methodHandleReference);
                return;
            case 23:
                writer.writeString(((BuilderEncodedValues.BuilderStringEncodedValue) encodedValue).stringReference);
                return;
            case 24:
                writer.writeType(((BuilderEncodedValues.BuilderTypeEncodedValue) encodedValue).typeReference);
                return;
            case 25:
                writer.writeField(((BuilderEncodedValues.BuilderFieldEncodedValue) encodedValue).fieldReference);
                return;
            case 26:
                writer.writeMethod(((BuilderEncodedValues.BuilderMethodEncodedValue) encodedValue).methodReference);
                return;
            case 27:
                writer.writeEnum(((BuilderEncodedValues.BuilderEnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                BuilderEncodedValues.BuilderArrayEncodedValue arrayEncodedValue = (BuilderEncodedValues.BuilderArrayEncodedValue) encodedValue;
                writer.writeArray(arrayEncodedValue.elements);
                return;
            case 29:
                BuilderEncodedValues.BuilderAnnotationEncodedValue annotationEncodedValue = (BuilderEncodedValues.BuilderAnnotationEncodedValue) encodedValue;
                writer.writeAnnotation(annotationEncodedValue.typeReference, annotationEncodedValue.elements);
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
    @Nonnull
    public Set<? extends BuilderAnnotationElement> internAnnotationElements(@Nonnull Set<? extends AnnotationElement> elements) {
        return ImmutableSet.copyOf(Iterators.transform(elements.iterator(), new Function<AnnotationElement, BuilderAnnotationElement>() { // from class: com.android.tools.smali.dexlib2.writer.builder.DexBuilder.2
            @Override // com.google.common.base.Function
            @Nullable
            public BuilderAnnotationElement apply(AnnotationElement input) {
                return DexBuilder.this.internAnnotationElement(input);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public BuilderAnnotationElement internAnnotationElement(@Nonnull AnnotationElement annotationElement) {
        return new BuilderAnnotationElement(((BuilderStringPool) this.stringSection).internString(annotationElement.getName()), internEncodedValue(annotationElement.getValue()));
    }

    @Nullable
    BuilderEncodedValues.BuilderEncodedValue internNullableEncodedValue(@Nullable EncodedValue encodedValue) {
        if (encodedValue == null) {
            return null;
        }
        return internEncodedValue(encodedValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nonnull
    public BuilderEncodedValues.BuilderEncodedValue internEncodedValue(@Nonnull EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 0:
                return new BuilderEncodedValues.BuilderByteEncodedValue(((ByteEncodedValue) encodedValue).getValue());
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
                throw new ExceptionWithContext("Unexpected encoded value type: %d", Integer.valueOf(encodedValue.getValueType()));
            case 2:
                return new BuilderEncodedValues.BuilderShortEncodedValue(((ShortEncodedValue) encodedValue).getValue());
            case 3:
                return new BuilderEncodedValues.BuilderCharEncodedValue(((CharEncodedValue) encodedValue).getValue());
            case 4:
                return new BuilderEncodedValues.BuilderIntEncodedValue(((IntEncodedValue) encodedValue).getValue());
            case 6:
                return new BuilderEncodedValues.BuilderLongEncodedValue(((LongEncodedValue) encodedValue).getValue());
            case 16:
                return new BuilderEncodedValues.BuilderFloatEncodedValue(((FloatEncodedValue) encodedValue).getValue());
            case 17:
                return new BuilderEncodedValues.BuilderDoubleEncodedValue(((DoubleEncodedValue) encodedValue).getValue());
            case 21:
                return internMethodTypeEncodedValue((MethodTypeEncodedValue) encodedValue);
            case 22:
                return internMethodHandleEncodedValue((MethodHandleEncodedValue) encodedValue);
            case 23:
                return internStringEncodedValue((StringEncodedValue) encodedValue);
            case 24:
                return internTypeEncodedValue((TypeEncodedValue) encodedValue);
            case 25:
                return internFieldEncodedValue((FieldEncodedValue) encodedValue);
            case 26:
                return internMethodEncodedValue((MethodEncodedValue) encodedValue);
            case 27:
                return internEnumEncodedValue((EnumEncodedValue) encodedValue);
            case 28:
                return internArrayEncodedValue((ArrayEncodedValue) encodedValue);
            case 29:
                return internAnnotationEncodedValue((AnnotationEncodedValue) encodedValue);
            case 30:
                return BuilderEncodedValues.BuilderNullEncodedValue.INSTANCE;
            case 31:
                boolean value = ((BooleanEncodedValue) encodedValue).getValue();
                return value ? BuilderEncodedValues.BuilderBooleanEncodedValue.TRUE_VALUE : BuilderEncodedValues.BuilderBooleanEncodedValue.FALSE_VALUE;
        }
    }

    @Nonnull
    private BuilderEncodedValues.BuilderAnnotationEncodedValue internAnnotationEncodedValue(@Nonnull AnnotationEncodedValue value) {
        return new BuilderEncodedValues.BuilderAnnotationEncodedValue(((BuilderTypePool) this.typeSection).internType(value.getType()), internAnnotationElements(value.getElements()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderArrayEncodedValue internArrayEncodedValue(@Nonnull ArrayEncodedValue value) {
        return new BuilderEncodedValues.BuilderArrayEncodedValue(ImmutableList.copyOf(Iterators.transform(value.getValue().iterator(), new Function<EncodedValue, BuilderEncodedValues.BuilderEncodedValue>() { // from class: com.android.tools.smali.dexlib2.writer.builder.DexBuilder.3
            @Override // com.google.common.base.Function
            @Nullable
            public BuilderEncodedValues.BuilderEncodedValue apply(EncodedValue input) {
                return DexBuilder.this.internEncodedValue(input);
            }
        })));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderEnumEncodedValue internEnumEncodedValue(@Nonnull EnumEncodedValue value) {
        return new BuilderEncodedValues.BuilderEnumEncodedValue(((BuilderFieldPool) this.fieldSection).internField(value.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderFieldEncodedValue internFieldEncodedValue(@Nonnull FieldEncodedValue value) {
        return new BuilderEncodedValues.BuilderFieldEncodedValue(((BuilderFieldPool) this.fieldSection).internField(value.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderMethodEncodedValue internMethodEncodedValue(@Nonnull MethodEncodedValue value) {
        return new BuilderEncodedValues.BuilderMethodEncodedValue(((BuilderMethodPool) this.methodSection).internMethod(value.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderStringEncodedValue internStringEncodedValue(@Nonnull StringEncodedValue string) {
        return new BuilderEncodedValues.BuilderStringEncodedValue(((BuilderStringPool) this.stringSection).internString(string.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderTypeEncodedValue internTypeEncodedValue(@Nonnull TypeEncodedValue type) {
        return new BuilderEncodedValues.BuilderTypeEncodedValue(((BuilderTypePool) this.typeSection).internType(type.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderMethodTypeEncodedValue internMethodTypeEncodedValue(@Nonnull MethodTypeEncodedValue methodType) {
        return new BuilderEncodedValues.BuilderMethodTypeEncodedValue(((BuilderProtoPool) this.protoSection).internMethodProto(methodType.getValue()));
    }

    @Nonnull
    private BuilderEncodedValues.BuilderMethodHandleEncodedValue internMethodHandleEncodedValue(@Nonnull MethodHandleEncodedValue methodHandle) {
        return new BuilderEncodedValues.BuilderMethodHandleEncodedValue(((BuilderMethodHandlePool) this.methodHandleSection).internMethodHandle(methodHandle.getValue()));
    }

    /* loaded from: classes.dex */
    protected class DexBuilderSectionProvider extends DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderCallSiteReference, BuilderMethodHandleReference, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderArrayEncodedValue, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderCallSitePool, BuilderMethodHandlePool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool, BuilderEncodedArrayPool>.SectionProvider {
        protected DexBuilderSectionProvider() {
            super();
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderStringPool getStringSection() {
            return new BuilderStringPool();
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderTypePool getTypeSection() {
            return new BuilderTypePool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderProtoPool getProtoSection() {
            return new BuilderProtoPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderFieldPool getFieldSection() {
            return new BuilderFieldPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderMethodPool getMethodSection() {
            return new BuilderMethodPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderClassPool getClassSection() {
            return new BuilderClassPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderCallSitePool getCallSiteSection() {
            return new BuilderCallSitePool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderMethodHandlePool getMethodHandleSection() {
            return new BuilderMethodHandlePool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderTypeListPool getTypeListSection() {
            return new BuilderTypeListPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderAnnotationPool getAnnotationSection() {
            return new BuilderAnnotationPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderAnnotationSetPool getAnnotationSetSection() {
            return new BuilderAnnotationSetPool(DexBuilder.this);
        }

        @Override // com.android.tools.smali.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public BuilderEncodedArrayPool getEncodedArraySection() {
            return new BuilderEncodedArrayPool(DexBuilder.this);
        }
    }
}
