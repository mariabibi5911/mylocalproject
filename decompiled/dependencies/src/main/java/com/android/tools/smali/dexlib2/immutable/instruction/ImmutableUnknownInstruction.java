package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.UnknownInstruction;

/* loaded from: classes.dex */
public class ImmutableUnknownInstruction extends ImmutableInstruction implements UnknownInstruction {
    public static final Format FORMAT = Format.Format10x;
    protected final int originalOpcode;

    public ImmutableUnknownInstruction(int originalOpcode) {
        super(Opcode.NOP);
        this.originalOpcode = originalOpcode;
    }

    public static ImmutableUnknownInstruction of(UnknownInstruction instruction) {
        if (instruction instanceof ImmutableUnknownInstruction) {
            return (ImmutableUnknownInstruction) instruction;
        }
        return new ImmutableUnknownInstruction(instruction.getOriginalOpcode());
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.UnknownInstruction
    public int getOriginalOpcode() {
        return this.originalOpcode;
    }
}
