package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.reference.DexBackedReference;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.util.NibbleUtils;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction22c extends DexBackedInstruction implements Instruction22c {
    public DexBackedInstruction22c(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readByte(this.instructionStart + 1));
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readByte(this.instructionStart + 1));
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
}
