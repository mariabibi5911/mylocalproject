package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction21lh extends DexBackedInstruction implements Instruction21lh {
    public DexBackedInstruction21lh(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction
    public long getWideLiteral() {
        return getHatLiteral() << 48;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.HatLiteralInstruction
    public short getHatLiteral() {
        return (short) this.dexFile.getDataBuffer().readShort(this.instructionStart + 2);
    }
}
