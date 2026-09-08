package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction32x extends ImmutableInstruction implements Instruction32x {
    public static final Format FORMAT = Format.Format32x;
    protected final int registerA;
    protected final int registerB;

    public ImmutableInstruction32x(@Nonnull Opcode opcode, int registerA, int registerB) {
        super(opcode);
        this.registerA = Preconditions.checkShortRegister(registerA);
        this.registerB = Preconditions.checkShortRegister(registerB);
    }

    public static ImmutableInstruction32x of(Instruction32x instruction) {
        if (instruction instanceof ImmutableInstruction32x) {
            return (ImmutableInstruction32x) instruction;
        }
        return new ImmutableInstruction32x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
