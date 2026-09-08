package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction23x;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction23x extends BuilderInstruction implements Instruction23x {
    public static final Format FORMAT = Format.Format23x;
    protected final int registerA;
    protected final int registerB;
    protected final int registerC;

    public BuilderInstruction23x(@Nonnull Opcode opcode, int registerA, int registerB, int registerC) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.registerB = Preconditions.checkByteRegister(registerB);
        this.registerC = Preconditions.checkByteRegister(registerC);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ThreeRegisterInstruction
    public int getRegisterC() {
        return this.registerC;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
