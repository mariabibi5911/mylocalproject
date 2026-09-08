package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction22s extends BuilderInstruction implements Instruction22s {
    public static final Format FORMAT = Format.Format22s;
    protected final int literal;
    protected final int registerA;
    protected final int registerB;

    public BuilderInstruction22s(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.registerB = Preconditions.checkNibbleRegister(registerB);
        this.literal = Preconditions.checkShortLiteral(literal);
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

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
