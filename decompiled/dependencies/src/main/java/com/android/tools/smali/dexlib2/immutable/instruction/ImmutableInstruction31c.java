package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReferenceFactory;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction31c extends ImmutableInstruction implements Instruction31c {
    public static final Format FORMAT = Format.Format31c;

    @Nonnull
    protected final ImmutableReference reference;
    protected final int registerA;

    public ImmutableInstruction31c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
    }

    public static ImmutableInstruction31c of(Instruction31c instruction) {
        if (instruction instanceof ImmutableInstruction31c) {
            return (ImmutableInstruction31c) instruction;
        }
        return new ImmutableInstruction31c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public ImmutableReference getReference() {
        return this.reference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return this.opcode.referenceType;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
