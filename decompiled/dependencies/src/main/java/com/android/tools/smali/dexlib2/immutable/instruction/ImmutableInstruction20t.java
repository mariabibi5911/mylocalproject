package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20t;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction20t extends ImmutableInstruction implements Instruction20t {
    public static final Format FORMAT = Format.Format20t;
    protected final int codeOffset;

    public ImmutableInstruction20t(@Nonnull Opcode opcode, int codeOffset) {
        super(opcode);
        this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
    }

    public static ImmutableInstruction20t of(Instruction20t instruction) {
        if (instruction instanceof ImmutableInstruction20t) {
            return (ImmutableInstruction20t) instruction;
        }
        return new ImmutableInstruction20t(instruction.getOpcode(), instruction.getCodeOffset());
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
