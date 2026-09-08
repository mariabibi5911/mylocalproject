package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction10x extends BuilderInstruction implements Instruction10x {
    public static final Format FORMAT = Format.Format10x;

    public BuilderInstruction10x(@Nonnull Opcode opcode) {
        super(opcode);
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
