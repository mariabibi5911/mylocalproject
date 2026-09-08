package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.ReferenceType;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableReferenceFactory;
import com.android.tools.smali.dexlib2.util.Preconditions;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableInstruction20bc extends ImmutableInstruction implements Instruction20bc {
    public static final Format FORMAT = Format.Format20bc;

    @Nonnull
    protected final ImmutableReference reference;
    protected final int verificationError;

    public ImmutableInstruction20bc(@Nonnull Opcode opcode, int verificationError, @Nonnull Reference reference) {
        super(opcode);
        this.verificationError = Preconditions.checkVerificationError(verificationError);
        this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
    }

    public static ImmutableInstruction20bc of(Instruction20bc instruction) {
        if (instruction instanceof ImmutableInstruction20bc) {
            return (ImmutableInstruction20bc) instruction;
        }
        return new ImmutableInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.VerificationErrorInstruction
    public int getVerificationError() {
        return this.verificationError;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public ImmutableReference getReference() {
        return this.reference;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return ReferenceType.getReferenceType(this.reference);
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
