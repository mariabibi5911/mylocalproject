package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.ReferenceType;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedReference;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction20bc extends DexBackedInstruction implements Instruction20bc {
    public DexBackedInstruction20bc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VerificationErrorInstruction
    public int getVerificationError() {
        return this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1) & 63;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public Reference getReference() {
        final int referenceIndex = this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2);
        try {
            int referenceType = getReferenceType();
            return DexBackedReference.makeReference(this.dexFile, referenceType, referenceIndex);
        } catch (ReferenceType.InvalidReferenceTypeException ex) {
            return new Reference() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction20bc.1
                @Override // com.android.tools.smali.dexlib2.iface.reference.Reference
                public void validateReference() throws Reference.InvalidReferenceException {
                    throw new Reference.InvalidReferenceException(String.format("%d@%d", Integer.valueOf(ex.getReferenceType()), Integer.valueOf(referenceIndex)), ex);
                }
            };
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        int referenceType = (this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1) >>> 6) + 1;
        ReferenceType.validateReferenceType(referenceType);
        return referenceType;
    }
}
