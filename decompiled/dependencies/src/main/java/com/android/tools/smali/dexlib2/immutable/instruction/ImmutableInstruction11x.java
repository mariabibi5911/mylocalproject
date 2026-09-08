package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11x;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction11x extends ImmutableInstruction implements Instruction11x {
    public static final Format FORMAT = Format.Format11x;
    protected final int registerA;

    public ImmutableInstruction11x(@Nonnull Opcode opcode, int registerA) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
    }

    public static ImmutableInstruction11x of(Instruction11x instruction) {
        if (instruction instanceof ImmutableInstruction11x) {
            return (ImmutableInstruction11x) instruction;
        }
        return new ImmutableInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
