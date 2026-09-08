package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction31t extends DexBackedInstruction implements Instruction31t {
    public DexBackedInstruction31t(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction
    public int getCodeOffset() {
        return this.dexFile.getDataBuffer().readInt(this.instructionStart + 2);
    }
}
