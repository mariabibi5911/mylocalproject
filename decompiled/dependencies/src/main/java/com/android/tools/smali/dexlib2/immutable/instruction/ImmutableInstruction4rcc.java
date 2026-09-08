package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction4rcc;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReferenceFactory;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction4rcc extends ImmutableInstruction implements Instruction4rcc {
    private static final Format FORMAT = Format.Format4rcc;

    @Nonnull
    protected final ImmutableReference reference;

    @Nonnull
    protected final ImmutableReference reference2;
    protected final int registerCount;
    protected final int startRegister;

    public ImmutableInstruction4rcc(@Nonnull Opcode opcode, int startRegister, int registerCount, @Nonnull Reference reference, @Nonnull Reference reference2) {
        super(opcode);
        this.startRegister = Preconditions.checkShortRegister(startRegister);
        this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
        this.reference = ImmutableReferenceFactory.of(reference);
        this.reference2 = ImmutableReferenceFactory.of(reference2);
    }

    public static ImmutableInstruction4rcc of(Instruction4rcc instruction) {
        if (instruction instanceof ImmutableInstruction4rcc) {
            return (ImmutableInstruction4rcc) instruction;
        }
        return new ImmutableInstruction4rcc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getReference(), instruction.getReference2());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
    public int getStartRegister() {
        return this.startRegister;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public Reference getReference() {
        return this.reference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return this.opcode.referenceType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
    public Reference getReference2() {
        return this.reference2;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
    public int getReferenceType2() {
        return this.opcode.referenceType2;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
