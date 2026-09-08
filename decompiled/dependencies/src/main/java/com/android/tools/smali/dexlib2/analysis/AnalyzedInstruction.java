package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnalyzedInstruction implements Comparable<AnalyzedInstruction> {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    @Nonnull
    protected Instruction instruction;
    protected final int instructionIndex;

    @Nonnull
    protected final MethodAnalyzer methodAnalyzer;
    protected final Instruction originalInstruction;

    @Nonnull
    protected final RegisterType[] postRegisterMap;

    @Nonnull
    protected final RegisterType[] preRegisterMap;

    @Nonnull
    protected final TreeSet<AnalyzedInstruction> predecessors = new TreeSet<>();

    @Nonnull
    protected final LinkedList<AnalyzedInstruction> successors = new LinkedList<>();

    @Nullable
    protected Map<PredecessorOverrideKey, RegisterType> predecessorRegisterOverrides = null;

    public AnalyzedInstruction(@Nonnull MethodAnalyzer methodAnalyzer, @Nonnull Instruction instruction, int instructionIndex, int registerCount) {
        this.methodAnalyzer = methodAnalyzer;
        this.instruction = instruction;
        this.originalInstruction = instruction;
        this.instructionIndex = instructionIndex;
        this.postRegisterMap = new RegisterType[registerCount];
        this.preRegisterMap = new RegisterType[registerCount];
        RegisterType unknown = RegisterType.getRegisterType((byte) 0, (TypeProto) null);
        for (int i = 0; i < registerCount; i++) {
            this.preRegisterMap[i] = unknown;
            this.postRegisterMap[i] = unknown;
        }
    }

    public int getInstructionIndex() {
        return this.instructionIndex;
    }

    public int getPredecessorCount() {
        return this.predecessors.size();
    }

    public SortedSet<AnalyzedInstruction> getPredecessors() {
        return Collections.unmodifiableSortedSet(this.predecessors);
    }

    public RegisterType getPredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber) {
        RegisterType override;
        Map<PredecessorOverrideKey, RegisterType> map = this.predecessorRegisterOverrides;
        if (map != null && (override = map.get(new PredecessorOverrideKey(predecessor, registerNumber))) != null) {
            return override;
        }
        return predecessor.postRegisterMap[registerNumber];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addPredecessor(AnalyzedInstruction predecessor) {
        return this.predecessors.add(predecessor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addSuccessor(AnalyzedInstruction successor) {
        this.successors.add(successor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDeodexedInstruction(Instruction instruction) {
        if (!this.originalInstruction.getOpcode().odexOnly()) {
            throw new AssertionError();
        }
        this.instruction = instruction;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void restoreOdexedInstruction() {
        if (!this.originalInstruction.getOpcode().odexOnly()) {
            throw new AssertionError();
        }
        this.instruction = this.originalInstruction;
    }

    @Nonnull
    public List<AnalyzedInstruction> getSuccessors() {
        return Collections.unmodifiableList(this.successors);
    }

    @Nonnull
    public Instruction getInstruction() {
        return this.instruction;
    }

    @Nonnull
    public Instruction getOriginalInstruction() {
        return this.originalInstruction;
    }

    public boolean isBeginningInstruction() {
        return this.predecessors.size() != 0 && this.predecessors.first().instructionIndex == -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean mergeRegister(int registerNumber, RegisterType registerType, BitSet verifiedInstructions, boolean override) {
        RegisterType mergedRegisterType;
        if (registerNumber < 0 || registerNumber >= this.postRegisterMap.length) {
            throw new AssertionError();
        }
        if (registerType == null) {
            throw new AssertionError();
        }
        RegisterType oldRegisterType = this.preRegisterMap[registerNumber];
        if (override) {
            mergedRegisterType = getMergedPreRegisterTypeFromPredecessors(registerNumber);
        } else {
            mergedRegisterType = oldRegisterType.merge(registerType);
        }
        if (mergedRegisterType.equals(oldRegisterType)) {
            return false;
        }
        this.preRegisterMap[registerNumber] = mergedRegisterType;
        verifiedInstructions.clear(this.instructionIndex);
        if (setsRegister(registerNumber)) {
            return false;
        }
        this.postRegisterMap[registerNumber] = mergedRegisterType;
        return true;
    }

    @Nonnull
    protected RegisterType getMergedPreRegisterTypeFromPredecessors(int registerNumber) {
        RegisterType mergedRegisterType = null;
        Iterator<AnalyzedInstruction> it = this.predecessors.iterator();
        while (it.hasNext()) {
            AnalyzedInstruction predecessor = it.next();
            RegisterType predecessorRegisterType = getPredecessorRegisterType(predecessor, registerNumber);
            if (predecessorRegisterType != null) {
                if (mergedRegisterType == null) {
                    mergedRegisterType = predecessorRegisterType;
                } else {
                    mergedRegisterType = predecessorRegisterType.merge(mergedRegisterType);
                }
            }
        }
        if (mergedRegisterType == null) {
            throw new IllegalStateException();
        }
        return mergedRegisterType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setPostRegisterType(int registerNumber, RegisterType registerType) {
        if (registerNumber >= 0) {
            RegisterType[] registerTypeArr = this.postRegisterMap;
            if (registerNumber < registerTypeArr.length) {
                if (registerType == null) {
                    throw new AssertionError();
                }
                RegisterType oldRegisterType = registerTypeArr[registerNumber];
                if (oldRegisterType.equals(registerType)) {
                    return false;
                }
                this.postRegisterMap[registerNumber] = registerType;
                return true;
            }
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean overridePredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber, @Nonnull RegisterType registerType, BitSet verifiedInstructions) {
        if (this.predecessorRegisterOverrides == null) {
            this.predecessorRegisterOverrides = Maps.newHashMap();
        }
        this.predecessorRegisterOverrides.put(new PredecessorOverrideKey(predecessor, registerNumber), registerType);
        RegisterType mergedType = getMergedPreRegisterTypeFromPredecessors(registerNumber);
        if (this.preRegisterMap[registerNumber].equals(mergedType)) {
            return false;
        }
        this.preRegisterMap[registerNumber] = mergedType;
        verifiedInstructions.clear(this.instructionIndex);
        if (setsRegister(registerNumber) || this.postRegisterMap[registerNumber].equals(mergedType)) {
            return false;
        }
        this.postRegisterMap[registerNumber] = mergedType;
        return true;
    }

    public boolean isInvokeInit() {
        if (!this.instruction.getOpcode().canInitializeReference()) {
            return false;
        }
        ReferenceInstruction instruction = (ReferenceInstruction) this.instruction;
        Reference reference = instruction.getReference();
        if (reference instanceof MethodReference) {
            return ((MethodReference) reference).getName().equals("<init>");
        }
        return false;
    }

    public boolean setsRegister(int registerNumber) {
        int destinationRegister;
        if (isInvokeInit()) {
            Instruction instruction = this.instruction;
            if (instruction instanceof FiveRegisterInstruction) {
                if (((FiveRegisterInstruction) instruction).getRegisterCount() <= 0) {
                    throw new AssertionError();
                }
                destinationRegister = ((FiveRegisterInstruction) this.instruction).getRegisterC();
            } else {
                if (!(instruction instanceof RegisterRangeInstruction)) {
                    throw new AssertionError();
                }
                RegisterRangeInstruction rangeInstruction = (RegisterRangeInstruction) instruction;
                if (rangeInstruction.getRegisterCount() <= 0) {
                    throw new AssertionError();
                }
                destinationRegister = rangeInstruction.getStartRegister();
            }
            RegisterType preInstructionDestRegisterType = getPreInstructionRegisterType(destinationRegister);
            if (preInstructionDestRegisterType.category == 0) {
                RegisterType preInstructionRegisterType = getPreInstructionRegisterType(registerNumber);
                if (preInstructionRegisterType.category == 16 || preInstructionRegisterType.category == 17) {
                    return true;
                }
            }
            if (preInstructionDestRegisterType.category != 16 && preInstructionDestRegisterType.category != 17) {
                return false;
            }
            if (registerNumber == destinationRegister) {
                return true;
            }
            return preInstructionDestRegisterType.equals(getPreInstructionRegisterType(registerNumber));
        }
        if (this.instructionIndex > 0 && this.methodAnalyzer.getClassPath().isArt() && getPredecessorCount() == 1 && (this.instruction.getOpcode() == Opcode.IF_EQZ || this.instruction.getOpcode() == Opcode.IF_NEZ)) {
            AnalyzedInstruction prevInstruction = this.predecessors.first();
            if (prevInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF && MethodAnalyzer.canPropagateTypeAfterInstanceOf(prevInstruction, this, this.methodAnalyzer.getClassPath())) {
                Instruction22c instanceOfInstruction = (Instruction22c) prevInstruction.instruction;
                if (registerNumber == instanceOfInstruction.getRegisterB()) {
                    return true;
                }
                if (this.instructionIndex > 1) {
                    int originalSourceRegister = -1;
                    RegisterType newType = null;
                    Iterator<AnalyzedInstruction> it = prevInstruction.predecessors.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        AnalyzedInstruction prevPrevAnalyzedInstruction = it.next();
                        Opcode opcode = prevPrevAnalyzedInstruction.instruction.getOpcode();
                        if (opcode == Opcode.MOVE_OBJECT || opcode == Opcode.MOVE_OBJECT_16 || opcode == Opcode.MOVE_OBJECT_FROM16) {
                            TwoRegisterInstruction moveInstruction = (TwoRegisterInstruction) prevPrevAnalyzedInstruction.instruction;
                            RegisterType originalType = prevPrevAnalyzedInstruction.getPostInstructionRegisterType(moveInstruction.getRegisterB());
                            if (moveInstruction.getRegisterA() != instanceOfInstruction.getRegisterB()) {
                                originalSourceRegister = -1;
                                break;
                            }
                            if (originalType.type == null) {
                                originalSourceRegister = -1;
                                break;
                            }
                            if (newType == null) {
                                newType = RegisterType.getRegisterType(this.methodAnalyzer.getClassPath(), (TypeReference) instanceOfInstruction.getReference());
                            }
                            if (MethodAnalyzer.isNotWideningConversion(originalType, newType)) {
                                if (originalSourceRegister != -1) {
                                    if (originalSourceRegister != moveInstruction.getRegisterB()) {
                                        originalSourceRegister = -1;
                                        break;
                                    }
                                } else {
                                    originalSourceRegister = moveInstruction.getRegisterB();
                                }
                            }
                        } else {
                            originalSourceRegister = -1;
                            break;
                        }
                    }
                    if (originalSourceRegister != -1 && registerNumber == originalSourceRegister) {
                        return true;
                    }
                }
            }
        }
        if (!this.instruction.getOpcode().setsRegister()) {
            return false;
        }
        int destinationRegister2 = getDestinationRegister();
        if (registerNumber == destinationRegister2) {
            return true;
        }
        return this.instruction.getOpcode().setsWideRegister() && registerNumber == destinationRegister2 + 1;
    }

    public List<Integer> getSetRegisters() {
        int destinationRegister;
        List<Integer> setRegisters = Lists.newArrayList();
        if (this.instruction.getOpcode().setsRegister()) {
            setRegisters.add(Integer.valueOf(getDestinationRegister()));
        }
        if (this.instruction.getOpcode().setsWideRegister()) {
            setRegisters.add(Integer.valueOf(getDestinationRegister() + 1));
        }
        if (isInvokeInit()) {
            Instruction instruction = this.instruction;
            if (instruction instanceof FiveRegisterInstruction) {
                destinationRegister = ((FiveRegisterInstruction) instruction).getRegisterC();
                if (((FiveRegisterInstruction) this.instruction).getRegisterCount() <= 0) {
                    throw new AssertionError();
                }
            } else {
                if (!(instruction instanceof RegisterRangeInstruction)) {
                    throw new AssertionError();
                }
                RegisterRangeInstruction rangeInstruction = (RegisterRangeInstruction) instruction;
                if (rangeInstruction.getRegisterCount() <= 0) {
                    throw new AssertionError();
                }
                destinationRegister = rangeInstruction.getStartRegister();
            }
            RegisterType preInstructionDestRegisterType = getPreInstructionRegisterType(destinationRegister);
            if (preInstructionDestRegisterType.category == 16 || preInstructionDestRegisterType.category == 17) {
                setRegisters.add(Integer.valueOf(destinationRegister));
                RegisterType objectRegisterType = this.preRegisterMap[destinationRegister];
                int i = 0;
                while (true) {
                    RegisterType[] registerTypeArr = this.preRegisterMap;
                    if (i >= registerTypeArr.length) {
                        break;
                    }
                    if (i != destinationRegister) {
                        RegisterType preInstructionRegisterType = registerTypeArr[i];
                        if (preInstructionRegisterType.equals(objectRegisterType)) {
                            setRegisters.add(Integer.valueOf(i));
                        } else if (preInstructionRegisterType.category == 16 || preInstructionRegisterType.category == 17) {
                            RegisterType postInstructionRegisterType = this.postRegisterMap[i];
                            if (postInstructionRegisterType.category == 0) {
                                setRegisters.add(Integer.valueOf(i));
                            }
                        }
                    }
                    i++;
                }
            } else if (preInstructionDestRegisterType.category == 0) {
                int i2 = 0;
                while (true) {
                    RegisterType[] registerTypeArr2 = this.preRegisterMap;
                    if (i2 >= registerTypeArr2.length) {
                        break;
                    }
                    RegisterType registerType = registerTypeArr2[i2];
                    if (registerType.category == 16 || registerType.category == 17) {
                        setRegisters.add(Integer.valueOf(i2));
                    }
                    i2++;
                }
            }
        }
        if (this.instructionIndex > 0 && this.methodAnalyzer.getClassPath().isArt() && getPredecessorCount() == 1 && (this.instruction.getOpcode() == Opcode.IF_EQZ || this.instruction.getOpcode() == Opcode.IF_NEZ)) {
            AnalyzedInstruction prevInstruction = this.predecessors.first();
            if (prevInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF && MethodAnalyzer.canPropagateTypeAfterInstanceOf(prevInstruction, this, this.methodAnalyzer.getClassPath())) {
                Instruction22c instanceOfInstruction = (Instruction22c) prevInstruction.instruction;
                setRegisters.add(Integer.valueOf(instanceOfInstruction.getRegisterB()));
                if (this.instructionIndex > 1) {
                    int originalSourceRegister = -1;
                    RegisterType newType = null;
                    Iterator<AnalyzedInstruction> it = prevInstruction.predecessors.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        AnalyzedInstruction prevPrevAnalyzedInstruction = it.next();
                        Opcode opcode = prevPrevAnalyzedInstruction.instruction.getOpcode();
                        if (opcode == Opcode.MOVE_OBJECT || opcode == Opcode.MOVE_OBJECT_16 || opcode == Opcode.MOVE_OBJECT_FROM16) {
                            TwoRegisterInstruction moveInstruction = (TwoRegisterInstruction) prevPrevAnalyzedInstruction.instruction;
                            RegisterType originalType = prevPrevAnalyzedInstruction.getPostInstructionRegisterType(moveInstruction.getRegisterB());
                            if (moveInstruction.getRegisterA() != instanceOfInstruction.getRegisterB()) {
                                originalSourceRegister = -1;
                                break;
                            }
                            if (originalType.type == null) {
                                originalSourceRegister = -1;
                                break;
                            }
                            if (newType == null) {
                                newType = RegisterType.getRegisterType(this.methodAnalyzer.getClassPath(), (TypeReference) instanceOfInstruction.getReference());
                            }
                            if (MethodAnalyzer.isNotWideningConversion(originalType, newType)) {
                                if (originalSourceRegister != -1) {
                                    if (originalSourceRegister != moveInstruction.getRegisterB()) {
                                        originalSourceRegister = -1;
                                        break;
                                    }
                                } else {
                                    originalSourceRegister = moveInstruction.getRegisterB();
                                }
                            }
                        } else {
                            originalSourceRegister = -1;
                            break;
                        }
                    }
                    if (originalSourceRegister != -1) {
                        setRegisters.add(Integer.valueOf(originalSourceRegister));
                    }
                }
            }
        }
        return setRegisters;
    }

    public int getDestinationRegister() {
        if (!this.instruction.getOpcode().setsRegister()) {
            throw new ExceptionWithContext("Cannot call getDestinationRegister() for an instruction that doesn't store a value", new Object[0]);
        }
        return ((OneRegisterInstruction) this.instruction).getRegisterA();
    }

    public int getRegisterCount() {
        return this.postRegisterMap.length;
    }

    @Nonnull
    public RegisterType getPostInstructionRegisterType(int registerNumber) {
        return this.postRegisterMap[registerNumber];
    }

    @Nonnull
    public RegisterType getPreInstructionRegisterType(int registerNumber) {
        return this.preRegisterMap[registerNumber];
    }

    @Override // java.lang.Comparable
    public int compareTo(@Nonnull AnalyzedInstruction analyzedInstruction) {
        int i = this.instructionIndex;
        int i2 = analyzedInstruction.instructionIndex;
        if (i < i2) {
            return -1;
        }
        if (i == i2) {
            return 0;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PredecessorOverrideKey {
        public final AnalyzedInstruction analyzedInstruction;
        public final int registerNumber;

        public PredecessorOverrideKey(AnalyzedInstruction analyzedInstruction, int registerNumber) {
            this.analyzedInstruction = analyzedInstruction;
            this.registerNumber = registerNumber;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PredecessorOverrideKey that = (PredecessorOverrideKey) o;
            return Objects.equal(Integer.valueOf(this.registerNumber), Integer.valueOf(that.registerNumber)) && Objects.equal(this.analyzedInstruction, that.analyzedInstruction);
        }

        public int hashCode() {
            return Objects.hashCode(this.analyzedInstruction, Integer.valueOf(this.registerNumber));
        }
    }
}
