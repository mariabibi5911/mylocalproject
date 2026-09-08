package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11n;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction11n extends BuilderInstruction implements Instruction11n {
    public static final Format FORMAT = Format.Format11n;
    protected final int literal;
    protected final int registerA;

    public BuilderInstruction11n(@Nonnull Opcode opcode, int registerA, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.literal = Preconditions.checkNibbleLiteral(literal);
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

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
