package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction3rms extends DexBackedInstruction implements Instruction3rms {
    public DexBackedInstruction3rms(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
    public int getStartRegister() {
        return this.dexFile.getDataBuffer().readUshort(this.instructionStart + 4);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VtableIndexInstruction
    public int getVtableIndex() {
        return this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2);
    }
}
