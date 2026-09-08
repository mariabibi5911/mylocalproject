package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22t;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction22t extends ImmutableInstruction implements Instruction22t {
    public static final Format FORMAT = Format.Format22t;
    protected final int codeOffset;
    protected final int registerA;
    protected final int registerB;

    public ImmutableInstruction22t(@Nonnull Opcode opcode, int registerA, int registerB, int codeOffset) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.registerB = Preconditions.checkNibbleRegister(registerB);
        this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
    }

    public static ImmutableInstruction22t of(Instruction22t instruction) {
        if (instruction instanceof ImmutableInstruction22t) {
            return (ImmutableInstruction22t) instruction;
        }
        return new ImmutableInstruction22t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getCodeOffset());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
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
