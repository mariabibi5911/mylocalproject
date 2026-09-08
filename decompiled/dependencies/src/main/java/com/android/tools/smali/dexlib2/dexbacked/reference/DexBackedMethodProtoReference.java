package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodProtoReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedMethodProtoReference extends BaseMethodProtoReference {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int protoIndex;

    public DexBackedMethodProtoReference(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
        this.dexFile = dexFile;
        this.protoIndex = protoIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public List<String> getParameterTypes() {
        int parametersOffset = this.dexFile.getBuffer().readSmallUint(this.dexFile.getProtoSection().getOffset(this.protoIndex) + 8);
        if (parametersOffset > 0) {
            final int parameterCount = this.dexFile.getDataBuffer().readSmallUint(parametersOffset + 0);
            final int paramListStart = parametersOffset + 4;
            return new FixedSizeList<String>() { // from class: com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedMethodProtoReference.1
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public String readItem(int index) {
                    return (String) DexBackedMethodProtoReference.this.dexFile.getTypeSection().get(DexBackedMethodProtoReference.this.dexFile.getDataBuffer().readUshort(paramListStart + (index * 2)));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameterCount;
                }
            };
        }
        return ImmutableList.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public String getReturnType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readSmallUint(this.dexFile.getProtoSection().getOffset(this.protoIndex) + 4));
    }

    public int getSize() {
        List<String> parameters = getParameterTypes();
        if (!parameters.isEmpty()) {
            int size = 12 + (parameters.size() * 2) + 4;
            return size;
        }
        return 12;
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.protoIndex;
        if (i < 0 || i >= this.dexFile.getProtoSection().size()) {
            throw new Reference.InvalidReferenceException("proto@" + this.protoIndex);
        }
    }
}
