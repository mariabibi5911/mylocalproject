package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderInstruction35c extends BuilderInstruction implements Instruction35c {
    public static final Format FORMAT = Format.Format35c;

    @Nonnull
    protected final Reference reference;
    protected final int registerC;
    protected final int registerCount;
    protected final int registerD;
    protected final int registerE;
    protected final int registerF;
    protected final int registerG;

    public BuilderInstruction35c(@Nonnull Opcode opcode, int registerCount, int registerC, int registerD, int registerE, int registerF, int registerG, @Nonnull Reference reference) {
        super(opcode);
        this.registerCount = Preconditions.check35cAnd45ccRegisterCount(registerCount);
        this.registerC = registerCount > 0 ? Preconditions.checkNibbleRegister(registerC) : 0;
        this.registerD = registerCount > 1 ? Preconditions.checkNibbleRegister(registerD) : 0;
        this.registerE = registerCount > 2 ? Preconditions.checkNibbleRegister(registerE) : 0;
        this.registerF = registerCount > 3 ? Preconditions.checkNibbleRegister(registerF) : 0;
        this.registerG = registerCount > 4 ? Preconditions.checkNibbleRegister(registerG) : 0;
        this.reference = reference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterC() {
        return this.registerC;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterD() {
        return this.registerD;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterE() {
        return this.registerE;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterF() {
        return this.registerF;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterG() {
        return this.registerG;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public Reference getReference() {
        return this.reference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return this.opcode.referenceType;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
