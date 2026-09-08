package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction31t extends ImmutableInstruction implements Instruction31t {
    public static final Format FORMAT = Format.Format31t;
    protected final int codeOffset;
    protected final int registerA;

    public ImmutableInstruction31t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.codeOffset = codeOffset;
    }

    public static ImmutableInstruction31t of(Instruction31t instruction) {
        if (instruction instanceof ImmutableInstruction31t) {
            return (ImmutableInstruction31t) instruction;
        }
        return new ImmutableInstruction31t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getCodeOffset());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction
    public int getCodeOffset() {
        return this.codeOffset;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
