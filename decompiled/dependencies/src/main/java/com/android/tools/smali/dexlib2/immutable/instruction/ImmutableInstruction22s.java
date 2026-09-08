package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction22s extends ImmutableInstruction implements Instruction22s {
    public static final Format FORMAT = Format.Format22s;
    protected final int literal;
    protected final int registerA;
    protected final int registerB;

    public ImmutableInstruction22s(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.registerB = Preconditions.checkNibbleRegister(registerB);
        this.literal = Preconditions.checkShortLiteral(literal);
    }

    public static ImmutableInstruction22s of(Instruction22s instruction) {
        if (instruction instanceof ImmutableInstruction22s) {
            return (ImmutableInstruction22s) instruction;
        }
        return new ImmutableInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
    public int getNarrowLiteral() {
        return this.literal;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction
    public long getWideLiteral() {
        return this.literal;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
