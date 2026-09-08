package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReferenceFactory;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction22c extends ImmutableInstruction implements Instruction22c {
    public static final Format FORMAT = Format.Format22c;

    @Nonnull
    protected final ImmutableReference reference;
    protected final int registerA;
    protected final int registerB;

    public ImmutableInstruction22c(@Nonnull Opcode opcode, int registerA, int registerB, @Nonnull Reference reference) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.registerB = Preconditions.checkNibbleRegister(registerB);
        this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
    }

    public static ImmutableInstruction22c of(Instruction22c instruction) {
        if (instruction instanceof ImmutableInstruction22c) {
            return (ImmutableInstruction22c) instruction;
        }
        return new ImmutableInstruction22c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getReference());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
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
