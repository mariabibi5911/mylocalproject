package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction3rmi extends BuilderInstruction implements Instruction3rmi {
    public static final Format FORMAT = Format.Format3rmi;
    protected final int inlineIndex;
    protected final int registerCount;
    protected final int startRegister;

    public BuilderInstruction3rmi(@Nonnull Opcode opcode, int startRegister, int registerCount, int inlineIndex) {
        super(opcode);
        this.startRegister = Preconditions.checkShortRegister(startRegister);
        this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
        this.inlineIndex = inlineIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
    public int getStartRegister() {
        return this.startRegister;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.InlineIndexInstruction
    public int getInlineIndex() {
        return this.inlineIndex;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
