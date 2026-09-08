package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21s;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction21s extends ImmutableInstruction implements Instruction21s {
    public static final Format FORMAT = Format.Format21s;
    protected final int literal;
    protected final int registerA;

    public ImmutableInstruction21s(@Nonnull Opcode opcode, int registerA, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.literal = Preconditions.checkShortLiteral(literal);
    }

    public static ImmutableInstruction21s of(Instruction21s instruction) {
        if (instruction instanceof ImmutableInstruction21s) {
            return (ImmutableInstruction21s) instruction;
        }
        return new ImmutableInstruction21s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
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
