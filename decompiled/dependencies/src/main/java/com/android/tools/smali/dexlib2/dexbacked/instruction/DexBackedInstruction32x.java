package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction32x extends DexBackedInstruction implements Instruction32x {
    public DexBackedInstruction32x(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.dexFile.getDataBuffer().readUshort(this.instructionStart + 4);
    }
}
