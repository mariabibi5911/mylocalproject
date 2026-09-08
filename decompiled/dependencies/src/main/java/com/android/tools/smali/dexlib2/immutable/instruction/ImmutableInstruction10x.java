package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction10x extends ImmutableInstruction implements Instruction10x {
    public static final Format FORMAT = Format.Format10x;

    public ImmutableInstruction10x(@Nonnull Opcode opcode) {
        super(opcode);
    }

    public static ImmutableInstruction10x of(Instruction10x instruction) {
        if (instruction instanceof ImmutableInstruction10x) {
            return (ImmutableInstruction10x) instruction;
        }
        return new ImmutableInstruction10x(instruction.getOpcode());
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
