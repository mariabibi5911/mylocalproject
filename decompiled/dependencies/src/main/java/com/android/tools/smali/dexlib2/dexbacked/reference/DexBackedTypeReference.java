package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedTypeReference extends BaseTypeReference {

    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int typeIndex;

    public DexBackedTypeReference(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
        this.dexFile = dexFile;
        this.typeIndex = typeIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }

    public int getSize() {
        return 4;
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.typeIndex;
        if (i < 0 || i >= this.dexFile.getTypeSection().size()) {
            throw new Reference.InvalidReferenceException("type@" + this.typeIndex);
        }
    }
}
