package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.util.NibbleUtils;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedInstruction22s extends DexBackedInstruction implements Instruction22s {
    public DexBackedInstruction22s(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
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

    @Override // com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
    public int getNarrowLiteral() {
        return this.dexFile.getDataBuffer().readShort(this.instructionStart + 2);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction
    public long getWideLiteral() {
        return getNarrowLiteral();
    }
}
