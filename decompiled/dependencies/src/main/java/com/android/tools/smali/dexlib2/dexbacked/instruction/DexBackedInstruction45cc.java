package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedReference;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.util.NibbleUtils;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction45cc extends DexBackedInstruction implements Instruction45cc {
    public DexBackedInstruction45cc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterC() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterD() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 4));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterE() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 5));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterF() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 5));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterG() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public Reference getReference() {
        return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType, this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return this.opcode.referenceType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
    public Reference getReference2() {
        return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType2, this.dexFile.getDataBuffer().readUshort(this.instructionStart + 6));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
    public int getReferenceType2() {
        return this.opcode.referenceType2;
    }
}
