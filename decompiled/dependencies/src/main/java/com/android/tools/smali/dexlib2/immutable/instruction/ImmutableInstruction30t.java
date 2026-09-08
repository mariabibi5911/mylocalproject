package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction30t;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction30t extends ImmutableInstruction implements Instruction30t {
    public static final Format FORMAT = Format.Format30t;
    protected final int codeOffset;

    public ImmutableInstruction30t(@Nonnull Opcode opcode, int codeOffset) {
        super(opcode);
        this.codeOffset = codeOffset;
    }

    public static ImmutableInstruction30t of(Instruction30t instruction) {
        if (instruction instanceof ImmutableInstruction30t) {
            return (ImmutableInstruction30t) instruction;
        }
        return new ImmutableInstruction30t(instruction.getOpcode(), instruction.getCodeOffset());
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
