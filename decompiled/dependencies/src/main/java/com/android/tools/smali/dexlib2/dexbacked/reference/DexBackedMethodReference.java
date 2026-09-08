package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedMethodReference extends BaseMethodReference {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodIndex;
    private int protoIdItemOffset;

    public DexBackedMethodReference(@Nonnull DexBackedDexFile dexFile, int methodIndex) {
        this.dexFile = dexFile;
        this.methodIndex = methodIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readUshort(this.dexFile.getMethodSection().getOffset(this.methodIndex) + 0));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.dexFile.getBuffer().readSmallUint(this.dexFile.getMethodSection().getOffset(this.methodIndex) + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public List<String> getParameterTypes() {
        int protoIdItemOffset = getProtoIdItemOffset();
        int parametersOffset = this.dexFile.getBuffer().readSmallUint(protoIdItemOffset + 8);
        if (parametersOffset > 0) {
            final int parameterCount = this.dexFile.getDataBuffer().readSmallUint(parametersOffset + 0);
            final int paramListStart = parametersOffset + 4;
            return new FixedSizeList<String>() { // from class: com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedMethodReference.1
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public String readItem(int index) {
                    return (String) DexBackedMethodReference.this.dexFile.getTypeSection().get(DexBackedMethodReference.this.dexFile.getDataBuffer().readUshort(paramListStart + (index * 2)));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameterCount;
                }
            };
        }
        return ImmutableList.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        int protoIdItemOffset = getProtoIdItemOffset();
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readSmallUint(protoIdItemOffset + 4));
    }

    private int getProtoIdItemOffset() {
        if (this.protoIdItemOffset == 0) {
            this.protoIdItemOffset = this.dexFile.getProtoSection().getOffset(this.dexFile.getBuffer().readUshort(this.dexFile.getMethodSection().getOffset(this.methodIndex) + 2));
        }
        return this.protoIdItemOffset;
    }

    public int getSize() {
        return 8;
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.methodIndex;
        if (i < 0 || i >= this.dexFile.getMethodSection().size()) {
            throw new Reference.InvalidReferenceException("method@" + this.methodIndex);
        }
    }
}
