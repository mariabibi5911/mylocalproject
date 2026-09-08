package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedFieldReference extends BaseFieldReference {

    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int fieldIndex;

    public DexBackedFieldReference(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
        this.dexFile = dexFile;
        this.fieldIndex = fieldIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readUshort(this.dexFile.getFieldSection().getOffset(this.fieldIndex) + 0));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.dexFile.getBuffer().readSmallUint(this.dexFile.getFieldSection().getOffset(this.fieldIndex) + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readUshort(this.dexFile.getFieldSection().getOffset(this.fieldIndex) + 2));
    }

    public int getSize() {
        return 8;
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.fieldIndex;
        if (i < 0 || i >= this.dexFile.getFieldSection().size()) {
            throw new Reference.InvalidReferenceException("field@" + this.fieldIndex);
        }
    }
}
