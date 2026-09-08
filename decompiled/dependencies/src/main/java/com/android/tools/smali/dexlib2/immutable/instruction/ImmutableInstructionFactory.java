package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.writer.InstructionFactory;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableInstructionFactory implements InstructionFactory<Reference> {
    public static final ImmutableInstructionFactory INSTANCE = new ImmutableInstructionFactory();

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public /* bridge */ /* synthetic */ Instruction makeArrayPayload(int i, @Nullable List list) {
        return makeArrayPayload(i, (List<Number>) list);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public /* bridge */ /* synthetic */ Instruction makePackedSwitchPayload(@Nullable List list) {
        return makePackedSwitchPayload((List<? extends SwitchElement>) list);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public /* bridge */ /* synthetic */ Instruction makeSparseSwitchPayload(@Nullable List list) {
        return makeSparseSwitchPayload((List<? extends SwitchElement>) list);
    }

    private ImmutableInstructionFactory() {
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction10t makeInstruction10t(@Nonnull Opcode opcode, int codeOffset) {
        return new ImmutableInstruction10t(opcode, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction10x makeInstruction10x(@Nonnull Opcode opcode) {
        return new ImmutableInstruction10x(opcode);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction11n makeInstruction11n(@Nonnull Opcode opcode, int registerA, int literal) {
        return new ImmutableInstruction11n(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction11x makeInstruction11x(@Nonnull Opcode opcode, int registerA) {
        return new ImmutableInstruction11x(opcode, registerA);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction12x makeInstruction12x(@Nonnull Opcode opcode, int registerA, int registerB) {
        return new ImmutableInstruction12x(opcode, registerA, registerB);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction20bc makeInstruction20bc(@Nonnull Opcode opcode, int verificationError, @Nonnull Reference reference) {
        return new ImmutableInstruction20bc(opcode, verificationError, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction20t makeInstruction20t(@Nonnull Opcode opcode, int codeOffset) {
        return new ImmutableInstruction20t(opcode, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction21c makeInstruction21c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
        return new ImmutableInstruction21c(opcode, registerA, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction21ih makeInstruction21ih(@Nonnull Opcode opcode, int registerA, int literal) {
        return new ImmutableInstruction21ih(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction21lh makeInstruction21lh(@Nonnull Opcode opcode, int registerA, long literal) {
        return new ImmutableInstruction21lh(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction21s makeInstruction21s(@Nonnull Opcode opcode, int registerA, int literal) {
        return new ImmutableInstruction21s(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction21t makeInstruction21t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
        return new ImmutableInstruction21t(opcode, registerA, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction22b makeInstruction22b(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
        return new ImmutableInstruction22b(opcode, registerA, registerB, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction22c makeInstruction22c(@Nonnull Opcode opcode, int registerA, int registerB, @Nonnull Reference reference) {
        return new ImmutableInstruction22c(opcode, registerA, registerB, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction22s makeInstruction22s(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
        return new ImmutableInstruction22s(opcode, registerA, registerB, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction22t makeInstruction22t(@Nonnull Opcode opcode, int registerA, int registerB, int codeOffset) {
        return new ImmutableInstruction22t(opcode, registerA, registerB, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction22x makeInstruction22x(@Nonnull Opcode opcode, int registerA, int registerB) {
        return new ImmutableInstruction22x(opcode, registerA, registerB);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction23x makeInstruction23x(@Nonnull Opcode opcode, int registerA, int registerB, int registerC) {
        return new ImmutableInstruction23x(opcode, registerA, registerB, registerC);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction30t makeInstruction30t(@Nonnull Opcode opcode, int codeOffset) {
        return new ImmutableInstruction30t(opcode, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction31c makeInstruction31c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
        return new ImmutableInstruction31c(opcode, registerA, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction31i makeInstruction31i(@Nonnull Opcode opcode, int registerA, int literal) {
        return new ImmutableInstruction31i(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction31t makeInstruction31t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
        return new ImmutableInstruction31t(opcode, registerA, codeOffset);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction32x makeInstruction32x(@Nonnull Opcode opcode, int registerA, int registerB) {
        return new ImmutableInstruction32x(opcode, registerA, registerB);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction35c makeInstruction35c(@Nonnull Opcode opcode, int registerCount, int registerC, int registerD, int registerE, int registerF, int registerG, @Nonnull Reference reference) {
        return new ImmutableInstruction35c(opcode, registerCount, registerC, registerD, registerE, registerF, registerG, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction3rc makeInstruction3rc(@Nonnull Opcode opcode, int startRegister, int registerCount, @Nonnull Reference reference) {
        return new ImmutableInstruction3rc(opcode, startRegister, registerCount, reference);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableInstruction51l makeInstruction51l(@Nonnull Opcode opcode, int registerA, long literal) {
        return new ImmutableInstruction51l(opcode, registerA, literal);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableSparseSwitchPayload makeSparseSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
        return new ImmutableSparseSwitchPayload(switchElements);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutablePackedSwitchPayload makePackedSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
        return new ImmutablePackedSwitchPayload(switchElements);
    }

    @Override // com.android.tools.smali.dexlib2.writer.InstructionFactory
    public ImmutableArrayPayload makeArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
        return new ImmutableArrayPayload(elementWidth, arrayElements);
    }
}
