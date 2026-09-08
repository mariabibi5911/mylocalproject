package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction21lh extends ImmutableInstruction implements Instruction21lh {
    public static final Format FORMAT = Format.Format21lh;
    protected final long literal;
    protected final int registerA;

    public ImmutableInstruction21lh(@Nonnull Opcode opcode, int registerA, long literal) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.literal = Preconditions.checkLongHatLiteral(literal);
    }

    public static ImmutableInstruction21lh of(Instruction21lh instruction) {
        if (instruction instanceof ImmutableInstruction21lh) {
            return (ImmutableInstruction21lh) instruction;
        }
        return new ImmutableInstruction21lh(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction
    public long getWideLiteral() {
        return this.literal;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.HatLiteralInstruction
    public short getHatLiteral() {
        return (short) (this.literal >>> 48);
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
