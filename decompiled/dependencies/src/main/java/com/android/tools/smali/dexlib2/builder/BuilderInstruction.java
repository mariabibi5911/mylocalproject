package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BuilderInstruction implements Instruction {

    @Nullable
    MethodLocation location;

    @Nonnull
    protected final Opcode opcode;

    public abstract Format getFormat();

    /* JADX INFO: Access modifiers changed from: protected */
    public BuilderInstruction(@Nonnull Opcode opcode) {
        Preconditions.checkFormat(opcode, getFormat());
        this.opcode = opcode;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    @Nonnull
    public Opcode getOpcode() {
        return this.opcode;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return getFormat().size / 2;
    }

    @Nonnull
    public MethodLocation getLocation() {
        MethodLocation methodLocation = this.location;
        if (methodLocation == null) {
            throw new IllegalStateException("Cannot get the location of an instruction that hasn't been added to a method.");
        }
        return methodLocation;
    }
}
