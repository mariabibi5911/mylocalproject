package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedMethodReference;
import com.android.tools.smali.dexlib2.dexbacked.util.AnnotationsDirectory;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.dexbacked.util.ParameterIterator;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.util.AbstractForwardSequentialList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBackedMethod extends BaseMethodReference implements Method {
    public final int accessFlags;

    @Nonnull
    public final DexBackedClassDef classDef;
    private final int codeOffset;

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int hiddenApiRestrictions;
    private final int methodAnnotationSetOffset;
    private int methodIdItemOffset;
    public final int methodIndex;
    private final int parameterAnnotationSetListOffset;
    private int parametersOffset = -1;
    private int protoIdItemOffset;
    private final int startOffset;

    public DexBackedMethod(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousMethodIndex, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int methodIndexDiff = reader.readLargeUleb128();
        this.methodIndex = methodIndexDiff + previousMethodIndex;
        this.accessFlags = reader.readSmallUleb128();
        this.codeOffset = reader.readSmallUleb128();
        this.hiddenApiRestrictions = hiddenApiRestrictions;
        this.methodAnnotationSetOffset = 0;
        this.parameterAnnotationSetListOffset = 0;
    }

    public DexBackedMethod(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousMethodIndex, @Nonnull AnnotationsDirectory.AnnotationIterator methodAnnotationIterator, @Nonnull AnnotationsDirectory.AnnotationIterator paramaterAnnotationIterator, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int methodIndexDiff = reader.readLargeUleb128();
        int i = methodIndexDiff + previousMethodIndex;
        this.methodIndex = i;
        this.accessFlags = reader.readSmallUleb128();
        this.codeOffset = reader.readSmallUleb128();
        this.hiddenApiRestrictions = hiddenApiRestrictions;
        this.methodAnnotationSetOffset = methodAnnotationIterator.seekTo(i);
        this.parameterAnnotationSetListOffset = paramaterAnnotationIterator.seekTo(i);
    }

    public int getMethodIndex() {
        return this.methodIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.classDef.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.dexFile.getBuffer().readSmallUint(getMethodIdItemOffset() + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readSmallUint(getProtoIdItemOffset() + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public List<? extends MethodParameter> getParameters() {
        int parametersOffset = getParametersOffset();
        if (parametersOffset > 0) {
            final List<String> parameterTypes = getParameterTypes();
            return new AbstractForwardSequentialList<MethodParameter>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedMethod.1
                @Override // com.android.tools.smali.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                @Nonnull
                public Iterator<MethodParameter> iterator() {
                    return new ParameterIterator(parameterTypes, DexBackedMethod.this.getParameterAnnotations(), DexBackedMethod.this.getParameterNames());
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameterTypes.size();
                }
            };
        }
        List<String> parameterTypes2 = ImmutableList.of();
        return parameterTypes2;
    }

    @Nonnull
    public List<? extends Set<? extends DexBackedAnnotation>> getParameterAnnotations() {
        return AnnotationsDirectory.getParameterAnnotations(this.dexFile, this.parameterAnnotationSetListOffset);
    }

    @Nonnull
    public Iterator<String> getParameterNames() {
        DexBackedMethodImplementation methodImpl = getImplementation();
        if (methodImpl != null) {
            return methodImpl.getParameterNames(null);
        }
        return ImmutableSet.of().iterator();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public List<String> getParameterTypes() {
        int parametersOffset = getParametersOffset();
        if (parametersOffset > 0) {
            final int parameterCount = this.dexFile.getDataBuffer().readSmallUint(parametersOffset + 0);
            final int paramListStart = parametersOffset + 4;
            return new FixedSizeList<String>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedMethod.2
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public String readItem(int index) {
                    return (String) DexBackedMethod.this.dexFile.getTypeSection().get(DexBackedMethod.this.dexFile.getDataBuffer().readUshort(paramListStart + (index * 2)));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameterCount;
                }
            };
        }
        return ImmutableList.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return AnnotationsDirectory.getAnnotations(this.dexFile, this.methodAnnotationSetOffset);
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        int i = this.hiddenApiRestrictions;
        if (i == 7) {
            return ImmutableSet.of();
        }
        return EnumSet.copyOf((Collection) HiddenApiRestriction.getAllFlags(i));
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nullable
    public DexBackedMethodImplementation getImplementation() {
        int i = this.codeOffset;
        if (i > 0) {
            DexBackedDexFile dexBackedDexFile = this.dexFile;
            return dexBackedDexFile.createMethodImplementation(dexBackedDexFile, this, i);
        }
        return null;
    }

    private int getMethodIdItemOffset() {
        if (this.methodIdItemOffset == 0) {
            this.methodIdItemOffset = this.dexFile.getMethodSection().getOffset(this.methodIndex);
        }
        return this.methodIdItemOffset;
    }

    private int getProtoIdItemOffset() {
        if (this.protoIdItemOffset == 0) {
            int protoIndex = this.dexFile.getBuffer().readUshort(getMethodIdItemOffset() + 2);
            this.protoIdItemOffset = this.dexFile.getProtoSection().getOffset(protoIndex);
        }
        int protoIndex2 = this.protoIdItemOffset;
        return protoIndex2;
    }

    private int getParametersOffset() {
        if (this.parametersOffset == -1) {
            this.parametersOffset = this.dexFile.getBuffer().readSmallUint(getProtoIdItemOffset() + 8);
        }
        return this.parametersOffset;
    }

    public static void skipMethods(@Nonnull DexReader reader, int count) {
        for (int i = 0; i < count; i++) {
            reader.skipUleb128();
            reader.skipUleb128();
            reader.skipUleb128();
        }
    }

    public int getSize() {
        DexReader reader = this.dexFile.getDataBuffer().readerAt(this.startOffset);
        reader.readLargeUleb128();
        reader.readSmallUleb128();
        reader.readSmallUleb128();
        int size = 0 + (reader.getOffset() - this.startOffset);
        DexBackedMethodImplementation impl = getImplementation();
        if (impl != null) {
            size += impl.getSize();
        }
        DexBackedMethodReference methodRef = new DexBackedMethodReference(this.dexFile, this.methodIndex);
        return size + methodRef.getSize();
    }
}
