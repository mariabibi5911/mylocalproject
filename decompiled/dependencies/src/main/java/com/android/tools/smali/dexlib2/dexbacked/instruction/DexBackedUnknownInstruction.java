package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.UnknownInstruction;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedUnknownInstruction extends DexBackedInstruction implements UnknownInstruction {
    public DexBackedUnknownInstruction(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, Opcode.NOP, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.UnknownInstruction
    public int getOriginalOpcode() {
        int opcode = this.dexFile.getDataBuffer().readUbyte(this.instructionStart);
        if (opcode == 0) {
            return this.dexFile.getDataBuffer().readUshort(this.instructionStart);
        }
        return opcode;
    }
}
