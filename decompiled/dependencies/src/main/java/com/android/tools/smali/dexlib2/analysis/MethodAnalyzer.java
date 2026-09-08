package com.android.tools.smali.dexlib2.analysis;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.location.LocationRequestCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.dexbacked.raw.CdexHeaderItem;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.ThreeRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22cs;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35mi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35ms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction10x;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction21c;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction22c;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction35c;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction3rc;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.dexlib2.util.TypeUtils;
import com.android.tools.smali.dexlib2.writer.util.TryListBuilder;
import com.android.tools.smali.util.BitSetUtils;
import com.android.tools.smali.util.ExceptionWithContext;
import com.android.tools.smali.util.SparseArray;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* loaded from: classes.dex */
public class MethodAnalyzer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    @Nullable
    private AnalysisException analysisException;

    @Nonnull
    private final SparseArray<AnalyzedInstruction> analyzedInstructions;

    @Nonnull
    private final BitSet analyzedState;

    @Nonnull
    private final ClassPath classPath;

    @Nullable
    private final InlineMethodResolver inlineResolver;

    @Nonnull
    private final Method method;

    @Nonnull
    private final MethodImplementation methodImpl;
    private final boolean normalizeVirtualMethods;
    private final int paramRegisterCount;
    private final AnalyzedInstruction startOfMethod;
    private static final BitSet Primitive32BitCategories = BitSetUtils.bitSetOfIndexes(2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    private static final BitSet WideLowCategories = BitSetUtils.bitSetOfIndexes(12, 14);
    private static final BitSet WideHighCategories = BitSetUtils.bitSetOfIndexes(13, 15);
    private static final BitSet ReferenceOrUninitCategories = BitSetUtils.bitSetOfIndexes(2, 16, 17, 18);
    private static final BitSet BooleanCategories = BitSetUtils.bitSetOfIndexes(2, 3, 4);

    public MethodAnalyzer(@Nonnull ClassPath classPath, @Nonnull Method method, @Nullable InlineMethodResolver inlineResolver, boolean normalizeVirtualMethods) {
        SparseArray<AnalyzedInstruction> sparseArray = new SparseArray<>(0);
        this.analyzedInstructions = sparseArray;
        this.analysisException = null;
        this.classPath = classPath;
        this.inlineResolver = inlineResolver;
        this.normalizeVirtualMethods = normalizeVirtualMethods;
        this.method = method;
        MethodImplementation methodImpl = method.getImplementation();
        if (methodImpl == null) {
            throw new IllegalArgumentException("The method has no implementation");
        }
        this.methodImpl = methodImpl;
        this.startOfMethod = new AnalyzedInstruction(this, new ImmutableInstruction10x(Opcode.NOP), -1, methodImpl.getRegisterCount()) { // from class: com.android.tools.smali.dexlib2.analysis.MethodAnalyzer.1
            @Override // com.android.tools.smali.dexlib2.analysis.AnalyzedInstruction
            protected boolean addPredecessor(AnalyzedInstruction predecessor) {
                throw new UnsupportedOperationException();
            }

            @Override // com.android.tools.smali.dexlib2.analysis.AnalyzedInstruction
            @Nonnull
            public RegisterType getPredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber) {
                throw new UnsupportedOperationException();
            }
        };
        buildInstructionList();
        this.analyzedState = new BitSet(sparseArray.size());
        this.paramRegisterCount = MethodUtil.getParameterRegisterCount(method);
        analyze();
    }

    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    private void analyze() {
        int objectRegisterNumber;
        Method method = this.method;
        MethodImplementation methodImpl = this.methodImpl;
        int totalRegisters = methodImpl.getRegisterCount();
        int parameterRegisters = this.paramRegisterCount;
        int nonParameterRegisters = totalRegisters - parameterRegisters;
        int i = 1;
        if (MethodUtil.isStatic(method)) {
            propagateParameterTypes(totalRegisters - parameterRegisters);
        } else {
            int thisRegister = totalRegisters - parameterRegisters;
            if (MethodUtil.isConstructor(method)) {
                setPostRegisterTypeAndPropagateChanges(this.startOfMethod, thisRegister, RegisterType.getRegisterType((byte) 17, this.classPath.getClass(method.getDefiningClass())));
            } else {
                setPostRegisterTypeAndPropagateChanges(this.startOfMethod, thisRegister, RegisterType.getRegisterType((byte) 18, this.classPath.getClass(method.getDefiningClass())));
            }
            propagateParameterTypes((totalRegisters - parameterRegisters) + 1);
        }
        RegisterType uninit = RegisterType.getRegisterType((byte) 1, (TypeProto) null);
        for (int i2 = 0; i2 < nonParameterRegisters; i2++) {
            setPostRegisterTypeAndPropagateChanges(this.startOfMethod, i2, uninit);
        }
        BitSet instructionsToAnalyze = new BitSet(this.analyzedInstructions.size());
        Iterator<AnalyzedInstruction> it = this.startOfMethod.successors.iterator();
        while (it.hasNext()) {
            AnalyzedInstruction successor = it.next();
            instructionsToAnalyze.set(successor.instructionIndex);
        }
        BitSet undeodexedInstructions = new BitSet(this.analyzedInstructions.size());
        while (true) {
            boolean didSomething = false;
            while (!instructionsToAnalyze.isEmpty()) {
                boolean didSomething2 = didSomething;
                for (int i3 = instructionsToAnalyze.nextSetBit(0); i3 >= 0; i3 = instructionsToAnalyze.nextSetBit(i3 + 1)) {
                    instructionsToAnalyze.clear(i3);
                    if (!this.analyzedState.get(i3)) {
                        AnalyzedInstruction instructionToAnalyze = this.analyzedInstructions.valueAt(i3);
                        try {
                            if (instructionToAnalyze.originalInstruction.getOpcode().odexOnly()) {
                                instructionToAnalyze.restoreOdexedInstruction();
                            }
                            if (!analyzeInstruction(instructionToAnalyze)) {
                                undeodexedInstructions.set(i3);
                            } else {
                                didSomething2 = true;
                                undeodexedInstructions.clear(i3);
                                this.analyzedState.set(instructionToAnalyze.getInstructionIndex());
                                Iterator<AnalyzedInstruction> it2 = instructionToAnalyze.successors.iterator();
                                while (it2.hasNext()) {
                                    AnalyzedInstruction successor2 = it2.next();
                                    instructionsToAnalyze.set(successor2.getInstructionIndex());
                                }
                            }
                        } catch (AnalysisException ex) {
                            this.analysisException = ex;
                            int codeAddress = getInstructionAddress(instructionToAnalyze);
                            ex.codeAddress = codeAddress;
                            Object[] objArr = new Object[i];
                            objArr[0] = instructionToAnalyze.instruction.getOpcode().name;
                            ex.addContext(String.format("opcode: %s", objArr));
                            ex.addContext(String.format("code address: %d", Integer.valueOf(codeAddress)));
                            ex.addContext(String.format("method: %s", method));
                            didSomething = didSomething2;
                        }
                    }
                }
                didSomething = didSomething2;
                if (this.analysisException != null) {
                    break;
                } else {
                    i = 1;
                }
            }
            if (!didSomething) {
                break;
            }
            if (!undeodexedInstructions.isEmpty()) {
                for (int i4 = undeodexedInstructions.nextSetBit(0); i4 >= 0; i4 = undeodexedInstructions.nextSetBit(i4 + 1)) {
                    instructionsToAnalyze.set(i4);
                }
            }
            i = 1;
        }
        for (int i5 = 0; i5 < this.analyzedInstructions.size(); i5++) {
            AnalyzedInstruction analyzedInstruction = this.analyzedInstructions.valueAt(i5);
            Instruction instruction = analyzedInstruction.getInstruction();
            if (instruction.getOpcode().odexOnly()) {
                switch (AnonymousClass3.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction.getOpcode().format.ordinal()]) {
                    case 1:
                        analyzeOdexReturnVoid(analyzedInstruction, false);
                        continue;
                    case 2:
                    case 3:
                        analyzePutGetVolatile(analyzedInstruction, false);
                        continue;
                    case 4:
                        analyzeInvokeDirectEmpty(analyzedInstruction, false);
                        continue;
                    case 5:
                        analyzeInvokeObjectInitRange(analyzedInstruction, false);
                        continue;
                    case 6:
                        objectRegisterNumber = ((Instruction22cs) instruction).getRegisterB();
                        break;
                    case 7:
                    case 8:
                        objectRegisterNumber = ((FiveRegisterInstruction) instruction).getRegisterC();
                        break;
                    case 9:
                    case 10:
                        objectRegisterNumber = ((RegisterRangeInstruction) instruction).getStartRegister();
                        break;
                }
                analyzedInstruction.setDeodexedInstruction(new UnresolvedOdexInstruction(instruction, objectRegisterNumber));
            }
        }
    }

    private void propagateParameterTypes(int parameterStartRegister) {
        int i = 0;
        for (MethodParameter parameter : this.method.getParameters()) {
            if (TypeUtils.isWideType(parameter)) {
                int i2 = i + 1;
                setPostRegisterTypeAndPropagateChanges(this.startOfMethod, i + parameterStartRegister, RegisterType.getWideRegisterType(parameter, true));
                setPostRegisterTypeAndPropagateChanges(this.startOfMethod, i2 + parameterStartRegister, RegisterType.getWideRegisterType(parameter, false));
                i = i2 + 1;
            } else {
                setPostRegisterTypeAndPropagateChanges(this.startOfMethod, i + parameterStartRegister, RegisterType.getRegisterType(this.classPath, parameter));
                i++;
            }
        }
    }

    public List<AnalyzedInstruction> getAnalyzedInstructions() {
        return this.analyzedInstructions.getValues();
    }

    public List<Instruction> getInstructions() {
        return Lists.transform(this.analyzedInstructions.getValues(), new Function<AnalyzedInstruction, Instruction>() { // from class: com.android.tools.smali.dexlib2.analysis.MethodAnalyzer.2
            @Override // com.google.common.base.Function
            @Nullable
            public Instruction apply(@Nullable AnalyzedInstruction input) {
                if (input == null) {
                    return null;
                }
                return input.instruction;
            }
        });
    }

    @Nullable
    public AnalysisException getAnalysisException() {
        return this.analysisException;
    }

    public int getParamRegisterCount() {
        return this.paramRegisterCount;
    }

    public int getInstructionAddress(@Nonnull AnalyzedInstruction instruction) {
        return this.analyzedInstructions.keyAt(instruction.instructionIndex);
    }

    private void setDestinationRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
        setPostRegisterTypeAndPropagateChanges(analyzedInstruction, analyzedInstruction.getDestinationRegister(), registerType);
    }

    private void propagateChanges(@Nonnull BitSet changedInstructions, int registerNumber, boolean override) {
        while (!changedInstructions.isEmpty()) {
            int instructionIndex = changedInstructions.nextSetBit(0);
            while (instructionIndex >= 0) {
                changedInstructions.clear(instructionIndex);
                propagateRegisterToSuccessors(this.analyzedInstructions.valueAt(instructionIndex), registerNumber, changedInstructions, override);
                instructionIndex = changedInstructions.nextSetBit(instructionIndex + 1);
            }
        }
    }

    private void overridePredecessorRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull AnalyzedInstruction predecessor, int registerNumber, @Nonnull RegisterType registerType) {
        BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
        if (!analyzedInstruction.overridePredecessorRegisterType(predecessor, registerNumber, registerType, this.analyzedState)) {
            return;
        }
        changedInstructions.set(analyzedInstruction.instructionIndex);
        propagateChanges(changedInstructions, registerNumber, true);
        if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            overridePredecessorRegisterTypeAndPropagateChanges(analyzedInstruction, predecessor, registerNumber + 1, RegisterType.LONG_HI_TYPE);
        } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            overridePredecessorRegisterTypeAndPropagateChanges(analyzedInstruction, predecessor, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
        }
    }

    private void initializeRefAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, @Nonnull RegisterType registerType) {
        BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
        if (!analyzedInstruction.setPostRegisterType(registerNumber, registerType)) {
            return;
        }
        propagateRegisterToSuccessors(analyzedInstruction, registerNumber, changedInstructions, false);
        propagateChanges(changedInstructions, registerNumber, false);
        if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.LONG_HI_TYPE);
        } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
        }
    }

    private void setPostRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, @Nonnull RegisterType registerType) {
        BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
        if (!analyzedInstruction.setPostRegisterType(registerNumber, registerType)) {
            return;
        }
        propagateRegisterToSuccessors(analyzedInstruction, registerNumber, changedInstructions, false);
        propagateChanges(changedInstructions, registerNumber, false);
        if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.LONG_HI_TYPE);
        } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
        }
    }

    private void propagateRegisterToSuccessors(@Nonnull AnalyzedInstruction instruction, int registerNumber, @Nonnull BitSet changedInstructions, boolean override) {
        RegisterType postRegisterType = instruction.getPostInstructionRegisterType(registerNumber);
        Iterator<AnalyzedInstruction> it = instruction.successors.iterator();
        while (it.hasNext()) {
            AnalyzedInstruction successor = it.next();
            if (successor.mergeRegister(registerNumber, postRegisterType, this.analyzedState, override)) {
                changedInstructions.set(successor.instructionIndex);
            }
        }
    }

    private void buildInstructionList() {
        int registerCount;
        ImmutableList<Instruction> instructions;
        MethodAnalyzer methodAnalyzer = this;
        int registerCount2 = methodAnalyzer.methodImpl.getRegisterCount();
        ImmutableList<Instruction> instructions2 = ImmutableList.copyOf(methodAnalyzer.methodImpl.getInstructions());
        methodAnalyzer.analyzedInstructions.ensureCapacity(instructions2.size());
        int currentCodeAddress = 0;
        for (int i = 0; i < instructions2.size(); i++) {
            Instruction instruction = instructions2.get(i);
            methodAnalyzer.analyzedInstructions.append(currentCodeAddress, new AnalyzedInstruction(methodAnalyzer, instruction, i, registerCount2));
            if (methodAnalyzer.analyzedInstructions.indexOfKey(currentCodeAddress) != i) {
                throw new AssertionError();
            }
            currentCodeAddress += instruction.getCodeUnits();
        }
        List<? extends TryBlock<? extends ExceptionHandler>> tries = TryListBuilder.massageTryBlocks(methodAnalyzer.methodImpl.getTryBlocks());
        int triesIndex = 0;
        TryBlock currentTry = null;
        AnalyzedInstruction[] currentExceptionHandlers = null;
        AnalyzedInstruction[][] exceptionHandlers = new AnalyzedInstruction[instructions2.size()];
        if (tries != null) {
            for (int i2 = 0; i2 < methodAnalyzer.analyzedInstructions.size(); i2++) {
                AnalyzedInstruction instruction2 = methodAnalyzer.analyzedInstructions.valueAt(i2);
                Opcode instructionOpcode = instruction2.instruction.getOpcode();
                int currentCodeAddress2 = methodAnalyzer.getInstructionAddress(instruction2);
                if (currentTry != null && currentTry.getStartCodeAddress() + currentTry.getCodeUnitCount() <= currentCodeAddress2) {
                    currentTry = null;
                    triesIndex++;
                }
                if (currentTry == null && triesIndex < tries.size()) {
                    TryBlock tryBlock = tries.get(triesIndex);
                    if (tryBlock.getStartCodeAddress() <= currentCodeAddress2) {
                        if (tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount() <= currentCodeAddress2) {
                            throw new AssertionError();
                        }
                        currentTry = tryBlock;
                        currentExceptionHandlers = methodAnalyzer.buildExceptionHandlerArray(tryBlock);
                    }
                }
                if (currentTry != null && instructionOpcode.canThrow()) {
                    exceptionHandlers[i2] = currentExceptionHandlers;
                }
            }
        }
        if (methodAnalyzer.analyzedInstructions.size() <= 0) {
            throw new AssertionError();
        }
        BitSet instructionsToProcess = new BitSet(instructions2.size());
        int i3 = 0;
        methodAnalyzer.addPredecessorSuccessor(methodAnalyzer.startOfMethod, methodAnalyzer.analyzedInstructions.valueAt(0), exceptionHandlers, instructionsToProcess);
        while (!instructionsToProcess.isEmpty()) {
            int currentInstructionIndex = instructionsToProcess.nextSetBit(i3);
            instructionsToProcess.clear(currentInstructionIndex);
            AnalyzedInstruction instruction3 = methodAnalyzer.analyzedInstructions.valueAt(currentInstructionIndex);
            Opcode instructionOpcode2 = instruction3.instruction.getOpcode();
            int instructionCodeAddress = methodAnalyzer.getInstructionAddress(instruction3);
            if (!instruction3.instruction.getOpcode().canContinue()) {
                registerCount = registerCount2;
            } else {
                if (currentInstructionIndex == methodAnalyzer.analyzedInstructions.size() - 1) {
                    throw new AnalysisException("Execution can continue past the last instruction", new Object[0]);
                }
                AnalyzedInstruction nextInstruction = methodAnalyzer.analyzedInstructions.valueAt(currentInstructionIndex + 1);
                methodAnalyzer.addPredecessorSuccessor(instruction3, nextInstruction, exceptionHandlers, instructionsToProcess);
                registerCount = registerCount2;
            }
            if (!(instruction3.instruction instanceof OffsetInstruction)) {
                instructions = instructions2;
            } else {
                OffsetInstruction offsetInstruction = (OffsetInstruction) instruction3.instruction;
                if (instructionOpcode2 == Opcode.PACKED_SWITCH) {
                    instructions = instructions2;
                } else if (instructionOpcode2 == Opcode.SPARSE_SWITCH) {
                    instructions = instructions2;
                } else if (instructionOpcode2 == Opcode.FILL_ARRAY_DATA) {
                    instructions = instructions2;
                } else {
                    int targetAddressOffset = offsetInstruction.getCodeOffset();
                    instructions = instructions2;
                    methodAnalyzer.addPredecessorSuccessor(instruction3, methodAnalyzer.analyzedInstructions.get(instructionCodeAddress + targetAddressOffset), exceptionHandlers, instructionsToProcess);
                }
                AnalyzedInstruction analyzedSwitchPayload = methodAnalyzer.analyzedInstructions.get(offsetInstruction.getCodeOffset() + instructionCodeAddress);
                if (analyzedSwitchPayload == null) {
                    throw new AnalysisException("Invalid switch payload offset", new Object[0]);
                }
                SwitchPayload switchPayload = (SwitchPayload) analyzedSwitchPayload.instruction;
                for (SwitchElement switchElement : switchPayload.getSwitchElements()) {
                    OffsetInstruction offsetInstruction2 = offsetInstruction;
                    AnalyzedInstruction analyzedSwitchPayload2 = analyzedSwitchPayload;
                    AnalyzedInstruction targetInstruction = methodAnalyzer.analyzedInstructions.get(instructionCodeAddress + switchElement.getOffset());
                    if (targetInstruction == null) {
                        throw new AnalysisException("Invalid switch target offset", new Object[0]);
                    }
                    methodAnalyzer.addPredecessorSuccessor(instruction3, targetInstruction, exceptionHandlers, instructionsToProcess);
                    offsetInstruction = offsetInstruction2;
                    analyzedSwitchPayload = analyzedSwitchPayload2;
                }
            }
            methodAnalyzer = this;
            registerCount2 = registerCount;
            instructions2 = instructions;
            i3 = 0;
        }
    }

    private void addPredecessorSuccessor(@Nonnull AnalyzedInstruction predecessor, @Nonnull AnalyzedInstruction successor, @Nonnull AnalyzedInstruction[][] exceptionHandlers, @Nonnull BitSet instructionsToProcess) {
        addPredecessorSuccessor(predecessor, successor, exceptionHandlers, instructionsToProcess, false);
    }

    private void addPredecessorSuccessor(@Nonnull AnalyzedInstruction predecessor, @Nonnull AnalyzedInstruction successor, @Nonnull AnalyzedInstruction[][] exceptionHandlers, @Nonnull BitSet instructionsToProcess, boolean allowMoveException) {
        if (!allowMoveException && successor.instruction.getOpcode() == Opcode.MOVE_EXCEPTION) {
            throw new AnalysisException("Execution can pass from the " + predecessor.instruction.getOpcode().name + " instruction at code address 0x" + Integer.toHexString(getInstructionAddress(predecessor)) + " to the move-exception instruction at address 0x" + Integer.toHexString(getInstructionAddress(successor)), new Object[0]);
        }
        if (!successor.addPredecessor(predecessor)) {
            return;
        }
        predecessor.addSuccessor(successor);
        instructionsToProcess.set(successor.getInstructionIndex());
        AnalyzedInstruction[] exceptionHandlersForSuccessor = exceptionHandlers[successor.instructionIndex];
        if (exceptionHandlersForSuccessor != null) {
            if (!successor.instruction.getOpcode().canThrow()) {
                throw new AssertionError();
            }
            for (AnalyzedInstruction exceptionHandler : exceptionHandlersForSuccessor) {
                addPredecessorSuccessor(predecessor, exceptionHandler, exceptionHandlers, instructionsToProcess, true);
            }
        }
    }

    @Nonnull
    private AnalyzedInstruction[] buildExceptionHandlerArray(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
        List<? extends Object> exceptionHandlers = tryBlock.getExceptionHandlers();
        AnalyzedInstruction[] handlerInstructions = new AnalyzedInstruction[exceptionHandlers.size()];
        for (int i = 0; i < exceptionHandlers.size(); i++) {
            handlerInstructions[i] = this.analyzedInstructions.get(((ExceptionHandler) exceptionHandlers.get(i)).getHandlerCodeAddress());
        }
        return handlerInstructions;
    }

    private boolean analyzeInstruction(@Nonnull AnalyzedInstruction analyzedInstruction) {
        Instruction instruction = analyzedInstruction.instruction;
        switch (AnonymousClass3.$SwitchMap$com$android$tools$smali$dexlib2$Opcode[instruction.getOpcode().ordinal()]) {
            case 1:
                return true;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                analyzeMove(analyzedInstruction);
                return true;
            case 11:
            case 12:
            case 13:
                analyzeMoveResult(analyzedInstruction);
                return true;
            case 14:
                analyzeMoveException(analyzedInstruction);
                return true;
            case 15:
            case 16:
            case 17:
            case 18:
                return true;
            case 19:
            case 20:
                analyzeOdexReturnVoid(analyzedInstruction);
                return true;
            case 21:
            case 22:
            case 23:
            case 24:
                analyzeConst(analyzedInstruction);
                return true;
            case 25:
            case 26:
            case 27:
            case 28:
                analyzeWideConst(analyzedInstruction);
                return true;
            case 29:
            case 30:
                analyzeConstString(analyzedInstruction);
                return true;
            case 31:
                analyzeConstClass(analyzedInstruction);
                return true;
            case 32:
            case 33:
                return true;
            case 34:
                analyzeCheckCast(analyzedInstruction);
                return true;
            case 35:
                analyzeInstanceOf(analyzedInstruction);
                return true;
            case 36:
                analyzeArrayLength(analyzedInstruction);
                return true;
            case 37:
                analyzeNewInstance(analyzedInstruction);
                return true;
            case 38:
                analyzeNewArray(analyzedInstruction);
                return true;
            case 39:
            case 40:
                return true;
            case 41:
                return true;
            case 42:
            case 43:
            case 44:
            case 45:
                return true;
            case 46:
            case 47:
                return true;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /* 48 */:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_EDITOR_ABSOLUTEX /* 49 */:
            case 50:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_TAG /* 51 */:
            case 52:
                analyzeFloatWideCmp(analyzedInstruction);
                return true;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_BASELINE_TO_BOTTOM_OF /* 53 */:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_MARGIN_BASELINE /* 54 */:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_GONE_MARGIN_BASELINE /* 55 */:
            case HeaderItem.STRING_COUNT_OFFSET /* 56 */:
            case 57:
            case 58:
            case 59:
            case 60:
            case LockFreeTaskQueueCore.CLOSED_SHIFT /* 61 */:
            case 62:
                return true;
            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
            case 64:
                analyzeIfEqzNez(analyzedInstruction);
                return true;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_HEIGHT /* 65 */:
                analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
                return true;
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.BYTE_TYPE);
                return true;
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.CHAR_TYPE);
                return true;
            case 69:
                analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.SHORT_TYPE);
                return true;
            case 70:
                analyzeAgetWide(analyzedInstruction);
                return true;
            case 71:
                analyzeAgetObject(analyzedInstruction);
                return true;
            case HeaderItem.PROTO_COUNT_OFFSET /* 72 */:
            case 73:
            case 74:
            case 75:
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case 77:
            case 78:
                return true;
            case 79:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case HeaderItem.FIELD_COUNT_OFFSET /* 80 */:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
                return true;
            case 81:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BYTE_TYPE);
                return true;
            case 82:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.CHAR_TYPE);
                return true;
            case 83:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.SHORT_TYPE);
                return true;
            case HeaderItem.FIELD_START_OFFSET /* 84 */:
            case 85:
                analyzeIgetSgetWideObject(analyzedInstruction);
                return true;
            case 86:
            case 87:
            case HeaderItem.METHOD_COUNT_OFFSET /* 88 */:
            case 89:
            case 90:
            case 91:
            case HeaderItem.METHOD_START_OFFSET /* 92 */:
                return true;
            case 93:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case 94:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
                return true;
            case 95:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BYTE_TYPE);
                return true;
            case HeaderItem.CLASS_COUNT_OFFSET /* 96 */:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.CHAR_TYPE);
                return true;
            case 97:
                analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.SHORT_TYPE);
                return true;
            case 98:
            case 99:
                analyzeIgetSgetWideObject(analyzedInstruction);
                return true;
            case 100:
            case TypedValues.TYPE_TARGET /* 101 */:
            case LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY /* 102 */:
            case 103:
            case 104:
            case 105:
            case 106:
                return true;
            case 107:
                analyzeInvokeVirtual(analyzedInstruction, false);
                return true;
            case 108:
                analyzeInvokeVirtual(analyzedInstruction, false);
                return true;
            case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY /* 109 */:
                analyzeInvokeDirect(analyzedInstruction);
                return true;
            case 110:
                return true;
            case 111:
                return true;
            case 112:
                analyzeInvokeVirtual(analyzedInstruction, true);
                return true;
            case 113:
                analyzeInvokeVirtual(analyzedInstruction, true);
                return true;
            case 114:
                analyzeInvokeDirectRange(analyzedInstruction);
                return true;
            case 115:
                return true;
            case CdexHeaderItem.DEBUG_INFO_OFFSETS_POS_OFFSET /* 116 */:
                return true;
            case 117:
            case 118:
                analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case 119:
            case CdexHeaderItem.DEBUG_INFO_OFFSETS_TABLE_OFFSET /* 120 */:
                analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
                return true;
            case 121:
                analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
                return true;
            case 122:
                analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
                return true;
            case 123:
                analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
                return true;
            case CdexHeaderItem.DEBUG_INFO_BASE /* 124 */:
                analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
                return true;
            case 125:
                analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
                return true;
            case 126:
            case WorkQueueKt.MASK /* 127 */:
                analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case 128:
            case 129:
                analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
                return true;
            case 130:
                analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
                return true;
            case 131:
                analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
                return true;
            case 132:
                analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
                return true;
            case 133:
                analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
                return true;
            case 134:
                analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
                return true;
            case 135:
                analyzeUnaryOp(analyzedInstruction, RegisterType.BYTE_TYPE);
                return true;
            case 136:
                analyzeUnaryOp(analyzedInstruction, RegisterType.CHAR_TYPE);
                return true;
            case 137:
                analyzeUnaryOp(analyzedInstruction, RegisterType.SHORT_TYPE);
                return true;
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
                analyzeBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
                return true;
            case 146:
            case 147:
            case 148:
                analyzeBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
                return true;
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
                analyzeBinaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE, false);
                return true;
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
                analyzeBinaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE, false);
                return true;
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
                analyzeBinaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE, false);
                return true;
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
                analyzeBinary2AddrOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
                return true;
            case 178:
            case 179:
            case 180:
                analyzeBinary2AddrOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
                return true;
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
                analyzeBinary2AddrOp(analyzedInstruction, RegisterType.LONG_LO_TYPE, false);
                return true;
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
                analyzeBinary2AddrOp(analyzedInstruction, RegisterType.FLOAT_TYPE, false);
                return true;
            case 197:
            case 198:
            case 199:
            case ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION /* 200 */:
            case 201:
                analyzeBinary2AddrOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE, false);
                return true;
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
                analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
                return true;
            case 207:
            case 208:
            case 209:
                analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
                return true;
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
                analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
                return true;
            case 216:
            case 217:
            case 218:
                analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
                return true;
            case 219:
                analyzeLiteralBinaryOp(analyzedInstruction, getDestTypeForLiteralShiftRight(analyzedInstruction, true), false);
                return true;
            case 220:
                analyzeLiteralBinaryOp(analyzedInstruction, getDestTypeForLiteralShiftRight(analyzedInstruction, false), false);
                return true;
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
                analyzePutGetVolatile(analyzedInstruction);
                return true;
            case 230:
                return true;
            case 231:
                analyzeExecuteInline(analyzedInstruction);
                return true;
            case 232:
                analyzeExecuteInlineRange(analyzedInstruction);
                return true;
            case 233:
                analyzeInvokeDirectEmpty(analyzedInstruction);
                return true;
            case 234:
                analyzeInvokeObjectInitRange(analyzedInstruction);
                return true;
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            case 241:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 247:
            case 248:
                return analyzeIputIgetQuick(analyzedInstruction);
            case 249:
                return analyzeInvokeVirtualQuick(analyzedInstruction, false, false);
            case ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION /* 250 */:
                return analyzeInvokeVirtualQuick(analyzedInstruction, true, false);
            case 251:
                return analyzeInvokeVirtualQuick(analyzedInstruction, false, true);
            case 252:
                return analyzeInvokeVirtualQuick(analyzedInstruction, true, true);
            case 253:
            case 254:
            case 255:
                analyzePutGetVolatile(analyzedInstruction);
                return true;
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.analysis.MethodAnalyzer$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Opcode;

        static {
            int[] iArr = new int[Opcode.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Opcode = iArr;
            try {
                iArr[Opcode.NOP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_FROM16.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_16.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_WIDE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_WIDE_FROM16.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_WIDE_16.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_OBJECT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_OBJECT_FROM16.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_OBJECT_16.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_RESULT.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_RESULT_WIDE.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_RESULT_OBJECT.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MOVE_EXCEPTION.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN_VOID.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN_WIDE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN_OBJECT.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN_VOID_BARRIER.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RETURN_VOID_NO_BARRIER.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_4.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_16.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_HIGH16.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_WIDE_16.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_WIDE_32.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_WIDE.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_WIDE_HIGH16.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_STRING.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_STRING_JUMBO.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CONST_CLASS.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MONITOR_ENTER.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MONITOR_EXIT.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CHECK_CAST.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INSTANCE_OF.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ARRAY_LENGTH.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEW_INSTANCE.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEW_ARRAY.ordinal()] = 38;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FILLED_NEW_ARRAY.ordinal()] = 39;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FILLED_NEW_ARRAY_RANGE.ordinal()] = 40;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FILL_ARRAY_DATA.ordinal()] = 41;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.THROW.ordinal()] = 42;
            } catch (NoSuchFieldError e42) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.GOTO.ordinal()] = 43;
            } catch (NoSuchFieldError e43) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.GOTO_16.ordinal()] = 44;
            } catch (NoSuchFieldError e44) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.GOTO_32.ordinal()] = 45;
            } catch (NoSuchFieldError e45) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.PACKED_SWITCH.ordinal()] = 46;
            } catch (NoSuchFieldError e46) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPARSE_SWITCH.ordinal()] = 47;
            } catch (NoSuchFieldError e47) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CMPL_FLOAT.ordinal()] = 48;
            } catch (NoSuchFieldError e48) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CMPG_FLOAT.ordinal()] = 49;
            } catch (NoSuchFieldError e49) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CMPL_DOUBLE.ordinal()] = 50;
            } catch (NoSuchFieldError e50) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CMPG_DOUBLE.ordinal()] = 51;
            } catch (NoSuchFieldError e51) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.CMP_LONG.ordinal()] = 52;
            } catch (NoSuchFieldError e52) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_EQ.ordinal()] = 53;
            } catch (NoSuchFieldError e53) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_NE.ordinal()] = 54;
            } catch (NoSuchFieldError e54) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_LT.ordinal()] = 55;
            } catch (NoSuchFieldError e55) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_GE.ordinal()] = 56;
            } catch (NoSuchFieldError e56) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_GT.ordinal()] = 57;
            } catch (NoSuchFieldError e57) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_LE.ordinal()] = 58;
            } catch (NoSuchFieldError e58) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_LTZ.ordinal()] = 59;
            } catch (NoSuchFieldError e59) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_GEZ.ordinal()] = 60;
            } catch (NoSuchFieldError e60) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_GTZ.ordinal()] = 61;
            } catch (NoSuchFieldError e61) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_LEZ.ordinal()] = 62;
            } catch (NoSuchFieldError e62) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_EQZ.ordinal()] = 63;
            } catch (NoSuchFieldError e63) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IF_NEZ.ordinal()] = 64;
            } catch (NoSuchFieldError e64) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET.ordinal()] = 65;
            } catch (NoSuchFieldError e65) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_BOOLEAN.ordinal()] = 66;
            } catch (NoSuchFieldError e66) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_BYTE.ordinal()] = 67;
            } catch (NoSuchFieldError e67) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_CHAR.ordinal()] = 68;
            } catch (NoSuchFieldError e68) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_SHORT.ordinal()] = 69;
            } catch (NoSuchFieldError e69) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_WIDE.ordinal()] = 70;
            } catch (NoSuchFieldError e70) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AGET_OBJECT.ordinal()] = 71;
            } catch (NoSuchFieldError e71) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT.ordinal()] = 72;
            } catch (NoSuchFieldError e72) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_BOOLEAN.ordinal()] = 73;
            } catch (NoSuchFieldError e73) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_BYTE.ordinal()] = 74;
            } catch (NoSuchFieldError e74) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_CHAR.ordinal()] = 75;
            } catch (NoSuchFieldError e75) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_SHORT.ordinal()] = 76;
            } catch (NoSuchFieldError e76) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_WIDE.ordinal()] = 77;
            } catch (NoSuchFieldError e77) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.APUT_OBJECT.ordinal()] = 78;
            } catch (NoSuchFieldError e78) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET.ordinal()] = 79;
            } catch (NoSuchFieldError e79) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_BOOLEAN.ordinal()] = 80;
            } catch (NoSuchFieldError e80) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_BYTE.ordinal()] = 81;
            } catch (NoSuchFieldError e81) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_CHAR.ordinal()] = 82;
            } catch (NoSuchFieldError e82) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_SHORT.ordinal()] = 83;
            } catch (NoSuchFieldError e83) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_WIDE.ordinal()] = 84;
            } catch (NoSuchFieldError e84) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_OBJECT.ordinal()] = 85;
            } catch (NoSuchFieldError e85) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT.ordinal()] = 86;
            } catch (NoSuchFieldError e86) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_BOOLEAN.ordinal()] = 87;
            } catch (NoSuchFieldError e87) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_BYTE.ordinal()] = 88;
            } catch (NoSuchFieldError e88) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_CHAR.ordinal()] = 89;
            } catch (NoSuchFieldError e89) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_SHORT.ordinal()] = 90;
            } catch (NoSuchFieldError e90) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_WIDE.ordinal()] = 91;
            } catch (NoSuchFieldError e91) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_OBJECT.ordinal()] = 92;
            } catch (NoSuchFieldError e92) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET.ordinal()] = 93;
            } catch (NoSuchFieldError e93) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_BOOLEAN.ordinal()] = 94;
            } catch (NoSuchFieldError e94) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_BYTE.ordinal()] = 95;
            } catch (NoSuchFieldError e95) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_CHAR.ordinal()] = 96;
            } catch (NoSuchFieldError e96) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_SHORT.ordinal()] = 97;
            } catch (NoSuchFieldError e97) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_WIDE.ordinal()] = 98;
            } catch (NoSuchFieldError e98) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_OBJECT.ordinal()] = 99;
            } catch (NoSuchFieldError e99) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT.ordinal()] = 100;
            } catch (NoSuchFieldError e100) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_BOOLEAN.ordinal()] = 101;
            } catch (NoSuchFieldError e101) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_BYTE.ordinal()] = 102;
            } catch (NoSuchFieldError e102) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_CHAR.ordinal()] = 103;
            } catch (NoSuchFieldError e103) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_SHORT.ordinal()] = 104;
            } catch (NoSuchFieldError e104) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_WIDE.ordinal()] = 105;
            } catch (NoSuchFieldError e105) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_OBJECT.ordinal()] = 106;
            } catch (NoSuchFieldError e106) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_VIRTUAL.ordinal()] = 107;
            } catch (NoSuchFieldError e107) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_SUPER.ordinal()] = 108;
            } catch (NoSuchFieldError e108) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_DIRECT.ordinal()] = 109;
            } catch (NoSuchFieldError e109) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_STATIC.ordinal()] = 110;
            } catch (NoSuchFieldError e110) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_INTERFACE.ordinal()] = 111;
            } catch (NoSuchFieldError e111) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_VIRTUAL_RANGE.ordinal()] = 112;
            } catch (NoSuchFieldError e112) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_SUPER_RANGE.ordinal()] = 113;
            } catch (NoSuchFieldError e113) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_DIRECT_RANGE.ordinal()] = 114;
            } catch (NoSuchFieldError e114) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_STATIC_RANGE.ordinal()] = 115;
            } catch (NoSuchFieldError e115) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_INTERFACE_RANGE.ordinal()] = 116;
            } catch (NoSuchFieldError e116) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEG_INT.ordinal()] = 117;
            } catch (NoSuchFieldError e117) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NOT_INT.ordinal()] = 118;
            } catch (NoSuchFieldError e118) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEG_LONG.ordinal()] = 119;
            } catch (NoSuchFieldError e119) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NOT_LONG.ordinal()] = 120;
            } catch (NoSuchFieldError e120) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEG_FLOAT.ordinal()] = 121;
            } catch (NoSuchFieldError e121) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.NEG_DOUBLE.ordinal()] = 122;
            } catch (NoSuchFieldError e122) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_LONG.ordinal()] = 123;
            } catch (NoSuchFieldError e123) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_FLOAT.ordinal()] = 124;
            } catch (NoSuchFieldError e124) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_DOUBLE.ordinal()] = 125;
            } catch (NoSuchFieldError e125) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.LONG_TO_INT.ordinal()] = 126;
            } catch (NoSuchFieldError e126) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DOUBLE_TO_INT.ordinal()] = 127;
            } catch (NoSuchFieldError e127) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.LONG_TO_FLOAT.ordinal()] = 128;
            } catch (NoSuchFieldError e128) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DOUBLE_TO_FLOAT.ordinal()] = 129;
            } catch (NoSuchFieldError e129) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.LONG_TO_DOUBLE.ordinal()] = 130;
            } catch (NoSuchFieldError e130) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FLOAT_TO_INT.ordinal()] = 131;
            } catch (NoSuchFieldError e131) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FLOAT_TO_LONG.ordinal()] = 132;
            } catch (NoSuchFieldError e132) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.FLOAT_TO_DOUBLE.ordinal()] = 133;
            } catch (NoSuchFieldError e133) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DOUBLE_TO_LONG.ordinal()] = 134;
            } catch (NoSuchFieldError e134) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_BYTE.ordinal()] = 135;
            } catch (NoSuchFieldError e135) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_CHAR.ordinal()] = 136;
            } catch (NoSuchFieldError e136) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INT_TO_SHORT.ordinal()] = 137;
            } catch (NoSuchFieldError e137) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_INT.ordinal()] = 138;
            } catch (NoSuchFieldError e138) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_INT.ordinal()] = 139;
            } catch (NoSuchFieldError e139) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_INT.ordinal()] = 140;
            } catch (NoSuchFieldError e140) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_INT.ordinal()] = 141;
            } catch (NoSuchFieldError e141) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_INT.ordinal()] = 142;
            } catch (NoSuchFieldError e142) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHL_INT.ordinal()] = 143;
            } catch (NoSuchFieldError e143) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHR_INT.ordinal()] = 144;
            } catch (NoSuchFieldError e144) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.USHR_INT.ordinal()] = 145;
            } catch (NoSuchFieldError e145) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_INT.ordinal()] = 146;
            } catch (NoSuchFieldError e146) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_INT.ordinal()] = 147;
            } catch (NoSuchFieldError e147) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_INT.ordinal()] = 148;
            } catch (NoSuchFieldError e148) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_LONG.ordinal()] = 149;
            } catch (NoSuchFieldError e149) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_LONG.ordinal()] = 150;
            } catch (NoSuchFieldError e150) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_LONG.ordinal()] = 151;
            } catch (NoSuchFieldError e151) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_LONG.ordinal()] = 152;
            } catch (NoSuchFieldError e152) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_LONG.ordinal()] = 153;
            } catch (NoSuchFieldError e153) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_LONG.ordinal()] = 154;
            } catch (NoSuchFieldError e154) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_LONG.ordinal()] = 155;
            } catch (NoSuchFieldError e155) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_LONG.ordinal()] = 156;
            } catch (NoSuchFieldError e156) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHL_LONG.ordinal()] = 157;
            } catch (NoSuchFieldError e157) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHR_LONG.ordinal()] = 158;
            } catch (NoSuchFieldError e158) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.USHR_LONG.ordinal()] = 159;
            } catch (NoSuchFieldError e159) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_FLOAT.ordinal()] = 160;
            } catch (NoSuchFieldError e160) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_FLOAT.ordinal()] = 161;
            } catch (NoSuchFieldError e161) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_FLOAT.ordinal()] = 162;
            } catch (NoSuchFieldError e162) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_FLOAT.ordinal()] = 163;
            } catch (NoSuchFieldError e163) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_FLOAT.ordinal()] = 164;
            } catch (NoSuchFieldError e164) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_DOUBLE.ordinal()] = 165;
            } catch (NoSuchFieldError e165) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_DOUBLE.ordinal()] = 166;
            } catch (NoSuchFieldError e166) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_DOUBLE.ordinal()] = 167;
            } catch (NoSuchFieldError e167) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_DOUBLE.ordinal()] = 168;
            } catch (NoSuchFieldError e168) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_DOUBLE.ordinal()] = 169;
            } catch (NoSuchFieldError e169) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_INT_2ADDR.ordinal()] = 170;
            } catch (NoSuchFieldError e170) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_INT_2ADDR.ordinal()] = 171;
            } catch (NoSuchFieldError e171) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_INT_2ADDR.ordinal()] = 172;
            } catch (NoSuchFieldError e172) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_INT_2ADDR.ordinal()] = 173;
            } catch (NoSuchFieldError e173) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_INT_2ADDR.ordinal()] = 174;
            } catch (NoSuchFieldError e174) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHL_INT_2ADDR.ordinal()] = 175;
            } catch (NoSuchFieldError e175) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHR_INT_2ADDR.ordinal()] = 176;
            } catch (NoSuchFieldError e176) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.USHR_INT_2ADDR.ordinal()] = 177;
            } catch (NoSuchFieldError e177) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_INT_2ADDR.ordinal()] = 178;
            } catch (NoSuchFieldError e178) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_INT_2ADDR.ordinal()] = 179;
            } catch (NoSuchFieldError e179) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_INT_2ADDR.ordinal()] = 180;
            } catch (NoSuchFieldError e180) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_LONG_2ADDR.ordinal()] = 181;
            } catch (NoSuchFieldError e181) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_LONG_2ADDR.ordinal()] = 182;
            } catch (NoSuchFieldError e182) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_LONG_2ADDR.ordinal()] = 183;
            } catch (NoSuchFieldError e183) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_LONG_2ADDR.ordinal()] = 184;
            } catch (NoSuchFieldError e184) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_LONG_2ADDR.ordinal()] = 185;
            } catch (NoSuchFieldError e185) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_LONG_2ADDR.ordinal()] = 186;
            } catch (NoSuchFieldError e186) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_LONG_2ADDR.ordinal()] = 187;
            } catch (NoSuchFieldError e187) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_LONG_2ADDR.ordinal()] = 188;
            } catch (NoSuchFieldError e188) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHL_LONG_2ADDR.ordinal()] = 189;
            } catch (NoSuchFieldError e189) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHR_LONG_2ADDR.ordinal()] = 190;
            } catch (NoSuchFieldError e190) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.USHR_LONG_2ADDR.ordinal()] = 191;
            } catch (NoSuchFieldError e191) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_FLOAT_2ADDR.ordinal()] = 192;
            } catch (NoSuchFieldError e192) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_FLOAT_2ADDR.ordinal()] = 193;
            } catch (NoSuchFieldError e193) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_FLOAT_2ADDR.ordinal()] = 194;
            } catch (NoSuchFieldError e194) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_FLOAT_2ADDR.ordinal()] = 195;
            } catch (NoSuchFieldError e195) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_FLOAT_2ADDR.ordinal()] = 196;
            } catch (NoSuchFieldError e196) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_DOUBLE_2ADDR.ordinal()] = 197;
            } catch (NoSuchFieldError e197) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SUB_DOUBLE_2ADDR.ordinal()] = 198;
            } catch (NoSuchFieldError e198) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_DOUBLE_2ADDR.ordinal()] = 199;
            } catch (NoSuchFieldError e199) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_DOUBLE_2ADDR.ordinal()] = 200;
            } catch (NoSuchFieldError e200) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_DOUBLE_2ADDR.ordinal()] = 201;
            } catch (NoSuchFieldError e201) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_INT_LIT16.ordinal()] = 202;
            } catch (NoSuchFieldError e202) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RSUB_INT.ordinal()] = 203;
            } catch (NoSuchFieldError e203) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_INT_LIT16.ordinal()] = 204;
            } catch (NoSuchFieldError e204) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_INT_LIT16.ordinal()] = 205;
            } catch (NoSuchFieldError e205) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_INT_LIT16.ordinal()] = 206;
            } catch (NoSuchFieldError e206) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_INT_LIT16.ordinal()] = 207;
            } catch (NoSuchFieldError e207) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_INT_LIT16.ordinal()] = 208;
            } catch (NoSuchFieldError e208) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_INT_LIT16.ordinal()] = 209;
            } catch (NoSuchFieldError e209) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ADD_INT_LIT8.ordinal()] = 210;
            } catch (NoSuchFieldError e210) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.RSUB_INT_LIT8.ordinal()] = 211;
            } catch (NoSuchFieldError e211) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.MUL_INT_LIT8.ordinal()] = 212;
            } catch (NoSuchFieldError e212) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.DIV_INT_LIT8.ordinal()] = 213;
            } catch (NoSuchFieldError e213) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.REM_INT_LIT8.ordinal()] = 214;
            } catch (NoSuchFieldError e214) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHL_INT_LIT8.ordinal()] = 215;
            } catch (NoSuchFieldError e215) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.AND_INT_LIT8.ordinal()] = 216;
            } catch (NoSuchFieldError e216) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.OR_INT_LIT8.ordinal()] = 217;
            } catch (NoSuchFieldError e217) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.XOR_INT_LIT8.ordinal()] = 218;
            } catch (NoSuchFieldError e218) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SHR_INT_LIT8.ordinal()] = 219;
            } catch (NoSuchFieldError e219) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.USHR_INT_LIT8.ordinal()] = 220;
            } catch (NoSuchFieldError e220) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_VOLATILE.ordinal()] = 221;
            } catch (NoSuchFieldError e221) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_VOLATILE.ordinal()] = 222;
            } catch (NoSuchFieldError e222) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_VOLATILE.ordinal()] = 223;
            } catch (NoSuchFieldError e223) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_VOLATILE.ordinal()] = 224;
            } catch (NoSuchFieldError e224) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_OBJECT_VOLATILE.ordinal()] = 225;
            } catch (NoSuchFieldError e225) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_WIDE_VOLATILE.ordinal()] = 226;
            } catch (NoSuchFieldError e226) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_WIDE_VOLATILE.ordinal()] = 227;
            } catch (NoSuchFieldError e227) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_WIDE_VOLATILE.ordinal()] = 228;
            } catch (NoSuchFieldError e228) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_WIDE_VOLATILE.ordinal()] = 229;
            } catch (NoSuchFieldError e229) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.THROW_VERIFICATION_ERROR.ordinal()] = 230;
            } catch (NoSuchFieldError e230) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.EXECUTE_INLINE.ordinal()] = 231;
            } catch (NoSuchFieldError e231) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.EXECUTE_INLINE_RANGE.ordinal()] = 232;
            } catch (NoSuchFieldError e232) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_DIRECT_EMPTY.ordinal()] = 233;
            } catch (NoSuchFieldError e233) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_OBJECT_INIT_RANGE.ordinal()] = 234;
            } catch (NoSuchFieldError e234) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_QUICK.ordinal()] = 235;
            } catch (NoSuchFieldError e235) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_WIDE_QUICK.ordinal()] = 236;
            } catch (NoSuchFieldError e236) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_OBJECT_QUICK.ordinal()] = 237;
            } catch (NoSuchFieldError e237) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_QUICK.ordinal()] = 238;
            } catch (NoSuchFieldError e238) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_WIDE_QUICK.ordinal()] = 239;
            } catch (NoSuchFieldError e239) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_OBJECT_QUICK.ordinal()] = 240;
            } catch (NoSuchFieldError e240) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_BOOLEAN_QUICK.ordinal()] = 241;
            } catch (NoSuchFieldError e241) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_BYTE_QUICK.ordinal()] = 242;
            } catch (NoSuchFieldError e242) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_CHAR_QUICK.ordinal()] = 243;
            } catch (NoSuchFieldError e243) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_SHORT_QUICK.ordinal()] = 244;
            } catch (NoSuchFieldError e244) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_BOOLEAN_QUICK.ordinal()] = 245;
            } catch (NoSuchFieldError e245) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_BYTE_QUICK.ordinal()] = 246;
            } catch (NoSuchFieldError e246) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_CHAR_QUICK.ordinal()] = 247;
            } catch (NoSuchFieldError e247) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IGET_SHORT_QUICK.ordinal()] = 248;
            } catch (NoSuchFieldError e248) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_VIRTUAL_QUICK.ordinal()] = 249;
            } catch (NoSuchFieldError e249) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_SUPER_QUICK.ordinal()] = 250;
            } catch (NoSuchFieldError e250) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_VIRTUAL_QUICK_RANGE.ordinal()] = 251;
            } catch (NoSuchFieldError e251) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.INVOKE_SUPER_QUICK_RANGE.ordinal()] = 252;
            } catch (NoSuchFieldError e252) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.IPUT_OBJECT_VOLATILE.ordinal()] = 253;
            } catch (NoSuchFieldError e253) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SGET_OBJECT_VOLATILE.ordinal()] = 254;
            } catch (NoSuchFieldError e254) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPUT_OBJECT_VOLATILE.ordinal()] = 255;
            } catch (NoSuchFieldError e255) {
            }
            int[] iArr2 = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr2;
            try {
                iArr2[Format.Format10x.ordinal()] = 1;
            } catch (NoSuchFieldError e256) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21c.ordinal()] = 2;
            } catch (NoSuchFieldError e257) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22c.ordinal()] = 3;
            } catch (NoSuchFieldError e258) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 4;
            } catch (NoSuchFieldError e259) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 5;
            } catch (NoSuchFieldError e260) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22cs.ordinal()] = 6;
            } catch (NoSuchFieldError e261) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35mi.ordinal()] = 7;
            } catch (NoSuchFieldError e262) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35ms.ordinal()] = 8;
            } catch (NoSuchFieldError e263) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rmi.ordinal()] = 9;
            } catch (NoSuchFieldError e264) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rms.ordinal()] = 10;
            } catch (NoSuchFieldError e265) {
            }
        }
    }

    private void analyzeMove(@Nonnull AnalyzedInstruction analyzedInstruction) {
        TwoRegisterInstruction instruction = (TwoRegisterInstruction) analyzedInstruction.instruction;
        RegisterType sourceRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, sourceRegisterType);
    }

    private void analyzeMoveResult(@Nonnull AnalyzedInstruction analyzedInstruction) {
        RegisterType resultRegisterType;
        AnalyzedInstruction previousInstruction = null;
        if (analyzedInstruction.instructionIndex > 0) {
            AnalyzedInstruction previousInstruction2 = this.analyzedInstructions.valueAt(analyzedInstruction.instructionIndex - 1);
            previousInstruction = previousInstruction2;
        }
        if (previousInstruction == null || !previousInstruction.instruction.getOpcode().setsResult()) {
            throw new AnalysisException(analyzedInstruction.instruction.getOpcode().name + " must occur after an invoke-*/fill-new-array instruction", new Object[0]);
        }
        ReferenceInstruction invokeInstruction = (ReferenceInstruction) previousInstruction.instruction;
        Reference reference = invokeInstruction.getReference();
        if (reference instanceof MethodReference) {
            resultRegisterType = RegisterType.getRegisterType(this.classPath, ((MethodReference) reference).getReturnType());
        } else {
            resultRegisterType = RegisterType.getRegisterType(this.classPath, (TypeReference) reference);
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, resultRegisterType);
    }

    private void analyzeMoveException(@Nonnull AnalyzedInstruction analyzedInstruction) {
        int instructionAddress = getInstructionAddress(analyzedInstruction);
        RegisterType exceptionType = RegisterType.UNKNOWN_TYPE;
        for (TryBlock<? extends ExceptionHandler> tryBlock : this.methodImpl.getTryBlocks()) {
            Iterator<? extends Object> it = tryBlock.getExceptionHandlers().iterator();
            while (it.hasNext()) {
                ExceptionHandler handler = (ExceptionHandler) it.next();
                if (handler.getHandlerCodeAddress() == instructionAddress) {
                    String type = handler.getExceptionType();
                    if (type == null) {
                        exceptionType = RegisterType.getRegisterType((byte) 18, this.classPath.getClass("Ljava/lang/Throwable;"));
                    } else {
                        exceptionType = RegisterType.getRegisterType((byte) 18, this.classPath.getClass(type)).merge(exceptionType);
                    }
                }
            }
        }
        if (exceptionType.category == 0) {
            throw new AnalysisException("move-exception must be the first instruction in an exception handler block", new Object[0]);
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, exceptionType);
    }

    private void analyzeOdexReturnVoid(AnalyzedInstruction analyzedInstruction) {
        analyzeOdexReturnVoid(analyzedInstruction, true);
    }

    private void analyzeOdexReturnVoid(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
        Instruction10x deodexedInstruction = new ImmutableInstruction10x(Opcode.RETURN_VOID);
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        if (analyzeResult) {
            analyzeInstruction(analyzedInstruction);
        }
    }

    private void analyzeConst(@Nonnull AnalyzedInstruction analyzedInstruction) {
        NarrowLiteralInstruction instruction = (NarrowLiteralInstruction) analyzedInstruction.instruction;
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterTypeForLiteral(instruction.getNarrowLiteral()));
    }

    private void analyzeWideConst(@Nonnull AnalyzedInstruction analyzedInstruction) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
    }

    private void analyzeConstString(@Nonnull AnalyzedInstruction analyzedInstruction) {
        TypeProto stringClass = this.classPath.getClass("Ljava/lang/String;");
        RegisterType stringType = RegisterType.getRegisterType((byte) 18, stringClass);
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, stringType);
    }

    private void analyzeConstClass(@Nonnull AnalyzedInstruction analyzedInstruction) {
        TypeProto classClass = this.classPath.getClass("Ljava/lang/Class;");
        RegisterType classType = RegisterType.getRegisterType((byte) 18, classClass);
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, classType);
    }

    private void analyzeCheckCast(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ReferenceInstruction instruction = (ReferenceInstruction) analyzedInstruction.instruction;
        TypeReference reference = (TypeReference) instruction.getReference();
        RegisterType castRegisterType = RegisterType.getRegisterType(this.classPath, reference);
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, castRegisterType);
    }

    public static boolean isNotWideningConversion(RegisterType originalType, RegisterType newType) {
        if (originalType.type == null || newType.type == null) {
            return true;
        }
        if (originalType.type.isInterface()) {
            return newType.type.implementsInterface(originalType.type.getType());
        }
        TypeProto commonSuperclass = newType.type.getCommonSuperclass(originalType.type);
        return commonSuperclass.getType().equals(originalType.type.getType()) || !commonSuperclass.getType().equals(newType.type.getType());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canPropagateTypeAfterInstanceOf(AnalyzedInstruction analyzedInstanceOfInstruction, AnalyzedInstruction analyzedIfInstruction, ClassPath classPath) {
        if (!classPath.isArt()) {
            return false;
        }
        Instruction ifInstruction = analyzedIfInstruction.instruction;
        if (((Instruction21t) ifInstruction).getRegisterA() == analyzedInstanceOfInstruction.getDestinationRegister()) {
            Reference reference = ((Instruction22c) analyzedInstanceOfInstruction.getInstruction()).getReference();
            RegisterType registerType = RegisterType.getRegisterType(classPath, (TypeReference) reference);
            try {
                if (registerType.type != null && !registerType.type.isInterface()) {
                    int objectRegister = ((TwoRegisterInstruction) analyzedInstanceOfInstruction.getInstruction()).getRegisterB();
                    RegisterType originalType = analyzedIfInstruction.getPreInstructionRegisterType(objectRegister);
                    return isNotWideningConversion(originalType, registerType);
                }
            } catch (UnresolvedClassException e) {
                return false;
            }
        }
        return false;
    }

    private void analyzeIfEqzNez(@Nonnull AnalyzedInstruction analyzedInstruction) {
        if (this.classPath.isArt()) {
            int instructionIndex = analyzedInstruction.getInstructionIndex();
            if (instructionIndex <= 0 || analyzedInstruction.getPredecessorCount() != 1) {
                return;
            }
            AnalyzedInstruction prevAnalyzedInstruction = analyzedInstruction.getPredecessors().first();
            if (prevAnalyzedInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF) {
                AnalyzedInstruction fallthroughInstruction = this.analyzedInstructions.valueAt(analyzedInstruction.getInstructionIndex() + 1);
                int nextAddress = getInstructionAddress(analyzedInstruction) + ((Instruction21t) analyzedInstruction.instruction).getCodeOffset();
                AnalyzedInstruction branchInstruction = this.analyzedInstructions.get(nextAddress);
                int narrowingRegister = ((Instruction22c) prevAnalyzedInstruction.instruction).getRegisterB();
                RegisterType originalType = analyzedInstruction.getPreInstructionRegisterType(narrowingRegister);
                Instruction22c instanceOfInstruction = (Instruction22c) prevAnalyzedInstruction.instruction;
                RegisterType newType = RegisterType.getRegisterType(this.classPath, (TypeReference) instanceOfInstruction.getReference());
                Iterator<Integer> it = analyzedInstruction.getSetRegisters().iterator();
                while (it.hasNext()) {
                    int register = it.next().intValue();
                    if (analyzedInstruction.instruction.getOpcode() == Opcode.IF_EQZ) {
                        overridePredecessorRegisterTypeAndPropagateChanges(fallthroughInstruction, analyzedInstruction, register, newType);
                        overridePredecessorRegisterTypeAndPropagateChanges(branchInstruction, analyzedInstruction, register, originalType);
                    } else {
                        overridePredecessorRegisterTypeAndPropagateChanges(fallthroughInstruction, analyzedInstruction, register, originalType);
                        overridePredecessorRegisterTypeAndPropagateChanges(branchInstruction, analyzedInstruction, register, newType);
                    }
                }
            }
        }
    }

    private void analyzeInstanceOf(@Nonnull AnalyzedInstruction analyzedInstruction) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
    }

    private void analyzeArrayLength(@Nonnull AnalyzedInstruction analyzedInstruction) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.INTEGER_TYPE);
    }

    private void analyzeNewInstance(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ReferenceInstruction instruction = (ReferenceInstruction) analyzedInstruction.instruction;
        int register = ((OneRegisterInstruction) analyzedInstruction.instruction).getRegisterA();
        RegisterType destRegisterType = analyzedInstruction.getPostInstructionRegisterType(register);
        if (destRegisterType.category != 0) {
            if (destRegisterType.category != 16) {
                throw new AssertionError();
            }
        } else {
            TypeReference typeReference = (TypeReference) instruction.getReference();
            RegisterType classType = RegisterType.getRegisterType(this.classPath, typeReference);
            setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterType((byte) 16, classType.type));
        }
    }

    private void analyzeNewArray(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ReferenceInstruction instruction = (ReferenceInstruction) analyzedInstruction.instruction;
        TypeReference type = (TypeReference) instruction.getReference();
        if (type.getType().charAt(0) != '[') {
            throw new AnalysisException("new-array used with non-array type", new Object[0]);
        }
        RegisterType arrayType = RegisterType.getRegisterType(this.classPath, type);
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, arrayType);
    }

    private void analyzeFloatWideCmp(@Nonnull AnalyzedInstruction analyzedInstruction) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.BYTE_TYPE);
    }

    private void analyze32BitPrimitiveAget(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, registerType);
    }

    private void analyzeAgetWide(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ThreeRegisterInstruction instruction = (ThreeRegisterInstruction) analyzedInstruction.instruction;
        RegisterType arrayRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
        if (arrayRegisterType.category != 2) {
            if (arrayRegisterType.category != 18 || !(arrayRegisterType.type instanceof ArrayProto)) {
                throw new AnalysisException("aget-wide used with non-array register: %s", arrayRegisterType.toString());
            }
            ArrayProto arrayProto = (ArrayProto) arrayRegisterType.type;
            if (arrayProto.dimensions != 1) {
                throw new AnalysisException("aget-wide used with multi-dimensional array: %s", arrayRegisterType.toString());
            }
            char arrayBaseType = arrayProto.getElementType().charAt(0);
            if (arrayBaseType == 'J') {
                setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
                return;
            } else {
                if (arrayBaseType == 'D') {
                    setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
                    return;
                }
                throw new AnalysisException("aget-wide used with narrow array: %s", arrayRegisterType);
            }
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
    }

    private void analyzeAgetObject(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ThreeRegisterInstruction instruction = (ThreeRegisterInstruction) analyzedInstruction.instruction;
        RegisterType arrayRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
        if (arrayRegisterType.category != 2) {
            if (arrayRegisterType.category != 18 || !(arrayRegisterType.type instanceof ArrayProto)) {
                throw new AnalysisException("aget-object used with non-array register: %s", arrayRegisterType.toString());
            }
            ArrayProto arrayProto = (ArrayProto) arrayRegisterType.type;
            String elementType = arrayProto.getImmediateElementType();
            setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterType((byte) 18, this.classPath.getClass(elementType)));
            return;
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.NULL_TYPE);
    }

    private void analyze32BitPrimitiveIgetSget(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, registerType);
    }

    private void analyzeIgetSgetWideObject(@Nonnull AnalyzedInstruction analyzedInstruction) {
        ReferenceInstruction referenceInstruction = (ReferenceInstruction) analyzedInstruction.instruction;
        FieldReference fieldReference = (FieldReference) referenceInstruction.getReference();
        RegisterType fieldType = RegisterType.getRegisterType(this.classPath, fieldReference.getType());
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, fieldType);
    }

    private void analyzeInvokeDirect(@Nonnull AnalyzedInstruction analyzedInstruction) {
        FiveRegisterInstruction instruction = (FiveRegisterInstruction) analyzedInstruction.instruction;
        analyzeInvokeDirectCommon(analyzedInstruction, instruction.getRegisterC());
    }

    private void analyzeInvokeDirectRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
        RegisterRangeInstruction instruction = (RegisterRangeInstruction) analyzedInstruction.instruction;
        analyzeInvokeDirectCommon(analyzedInstruction, instruction.getStartRegister());
    }

    private void analyzeInvokeDirectCommon(@Nonnull AnalyzedInstruction analyzedInstruction, int objectRegister) {
        if (analyzedInstruction.isInvokeInit()) {
            RegisterType uninitRef = analyzedInstruction.getPreInstructionRegisterType(objectRegister);
            if (uninitRef.category != 16 && uninitRef.category != 17) {
                if (!analyzedInstruction.getSetRegisters().isEmpty()) {
                    throw new AssertionError();
                }
                return;
            }
            RegisterType initRef = RegisterType.getRegisterType((byte) 18, uninitRef.type);
            Iterator<Integer> it = analyzedInstruction.getSetRegisters().iterator();
            while (it.hasNext()) {
                int register = it.next().intValue();
                RegisterType registerType = analyzedInstruction.getPreInstructionRegisterType(register);
                if (registerType == uninitRef) {
                    setPostRegisterTypeAndPropagateChanges(analyzedInstruction, register, initRef);
                } else {
                    setPostRegisterTypeAndPropagateChanges(analyzedInstruction, register, registerType);
                }
            }
        }
    }

    private void analyzeUnaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType) {
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
    }

    private void analyzeBinaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
        if (checkForBoolean) {
            ThreeRegisterInstruction instruction = (ThreeRegisterInstruction) analyzedInstruction.instruction;
            RegisterType source1RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
            RegisterType source2RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterC());
            BitSet bitSet = BooleanCategories;
            if (bitSet.get(source1RegisterType.category) && bitSet.get(source2RegisterType.category)) {
                destRegisterType = RegisterType.BOOLEAN_TYPE;
            }
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
    }

    private void analyzeBinary2AddrOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
        if (checkForBoolean) {
            TwoRegisterInstruction instruction = (TwoRegisterInstruction) analyzedInstruction.instruction;
            RegisterType source1RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterA());
            RegisterType source2RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
            BitSet bitSet = BooleanCategories;
            if (bitSet.get(source1RegisterType.category) && bitSet.get(source2RegisterType.category)) {
                destRegisterType = RegisterType.BOOLEAN_TYPE;
            }
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
    }

    private void analyzeLiteralBinaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
        int literal;
        if (checkForBoolean) {
            TwoRegisterInstruction instruction = (TwoRegisterInstruction) analyzedInstruction.instruction;
            RegisterType sourceRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
            if (BooleanCategories.get(sourceRegisterType.category) && ((literal = ((NarrowLiteralInstruction) analyzedInstruction.instruction).getNarrowLiteral()) == 0 || literal == 1)) {
                destRegisterType = RegisterType.BOOLEAN_TYPE;
            }
        }
        setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private RegisterType getDestTypeForLiteralShiftRight(@Nonnull AnalyzedInstruction analyzedInstruction, boolean signedShift) {
        RegisterType destRegisterType;
        TwoRegisterInstruction instruction = (TwoRegisterInstruction) analyzedInstruction.instruction;
        RegisterType sourceRegisterType = getAndCheckSourceRegister(analyzedInstruction, instruction.getRegisterB(), Primitive32BitCategories);
        long literalShift = ((NarrowLiteralInstruction) analyzedInstruction.instruction).getNarrowLiteral();
        if (literalShift == 0) {
            return sourceRegisterType;
        }
        if (!signedShift) {
            destRegisterType = RegisterType.INTEGER_TYPE;
        } else {
            destRegisterType = sourceRegisterType;
        }
        long literalShift2 = literalShift & 31;
        switch (sourceRegisterType.category) {
            case 2:
            case 3:
            case 4:
                return RegisterType.NULL_TYPE;
            case 5:
                return destRegisterType;
            case 6:
                return RegisterType.POS_BYTE_TYPE;
            case 7:
                if (signedShift && literalShift2 >= 8) {
                    return RegisterType.BYTE_TYPE;
                }
                return destRegisterType;
            case 8:
                if (literalShift2 >= 8) {
                    return RegisterType.POS_BYTE_TYPE;
                }
                return destRegisterType;
            case 9:
                if (literalShift2 > 8) {
                    return RegisterType.POS_BYTE_TYPE;
                }
                return destRegisterType;
            case 10:
            case 11:
                if (signedShift) {
                    if (literalShift2 >= 24) {
                        return RegisterType.BYTE_TYPE;
                    }
                    if (literalShift2 >= 16) {
                        return RegisterType.SHORT_TYPE;
                    }
                } else {
                    if (literalShift2 > 24) {
                        return RegisterType.POS_BYTE_TYPE;
                    }
                    if (literalShift2 >= 16) {
                        return RegisterType.CHAR_TYPE;
                    }
                }
                return destRegisterType;
            default:
                throw new AssertionError();
        }
    }

    private void analyzeExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
        Opcode deodexedOpcode;
        if (this.inlineResolver == null) {
            throw new AnalysisException("Cannot analyze an odexed instruction unless we are deodexing", new Object[0]);
        }
        Instruction35mi instruction = (Instruction35mi) analyzedInstruction.instruction;
        Method resolvedMethod = this.inlineResolver.resolveExecuteInline(analyzedInstruction);
        int acccessFlags = resolvedMethod.getAccessFlags();
        if (AccessFlags.STATIC.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_STATIC;
        } else if (AccessFlags.PRIVATE.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_DIRECT;
        } else {
            Opcode deodexedOpcode2 = Opcode.INVOKE_VIRTUAL;
            deodexedOpcode = deodexedOpcode2;
        }
        Instruction35c deodexedInstruction = new ImmutableInstruction35c(deodexedOpcode, instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), resolvedMethod);
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        analyzeInstruction(analyzedInstruction);
    }

    private void analyzeExecuteInlineRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
        Opcode deodexedOpcode;
        if (this.inlineResolver == null) {
            throw new AnalysisException("Cannot analyze an odexed instruction unless we are deodexing", new Object[0]);
        }
        Instruction3rmi instruction = (Instruction3rmi) analyzedInstruction.instruction;
        Method resolvedMethod = this.inlineResolver.resolveExecuteInline(analyzedInstruction);
        int acccessFlags = resolvedMethod.getAccessFlags();
        if (AccessFlags.STATIC.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_STATIC_RANGE;
        } else if (AccessFlags.PRIVATE.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_DIRECT_RANGE;
        } else {
            deodexedOpcode = Opcode.INVOKE_VIRTUAL_RANGE;
        }
        Instruction3rc deodexedInstruction = new ImmutableInstruction3rc(deodexedOpcode, instruction.getStartRegister(), instruction.getRegisterCount(), resolvedMethod);
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        analyzeInstruction(analyzedInstruction);
    }

    private void analyzeInvokeDirectEmpty(@Nonnull AnalyzedInstruction analyzedInstruction) {
        analyzeInvokeDirectEmpty(analyzedInstruction, true);
    }

    private void analyzeInvokeDirectEmpty(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
        Instruction35c instruction = (Instruction35c) analyzedInstruction.instruction;
        Instruction35c deodexedInstruction = new ImmutableInstruction35c(Opcode.INVOKE_DIRECT, instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        if (analyzeResult) {
            analyzeInstruction(analyzedInstruction);
        }
    }

    private void analyzeInvokeObjectInitRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
        analyzeInvokeObjectInitRange(analyzedInstruction, true);
    }

    private void analyzeInvokeObjectInitRange(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
        Instruction deodexedInstruction;
        Instruction3rc instruction = (Instruction3rc) analyzedInstruction.instruction;
        int startRegister = instruction.getStartRegister();
        if (startRegister < 16) {
            deodexedInstruction = new ImmutableInstruction35c(Opcode.INVOKE_DIRECT, 1, startRegister, 0, 0, 0, 0, instruction.getReference());
        } else {
            deodexedInstruction = new ImmutableInstruction3rc(Opcode.INVOKE_DIRECT_RANGE, startRegister, 1, instruction.getReference());
        }
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        if (analyzeResult) {
            analyzeInstruction(analyzedInstruction);
        }
    }

    private boolean analyzeIputIgetQuick(@Nonnull AnalyzedInstruction analyzedInstruction) {
        Instruction22cs instruction = (Instruction22cs) analyzedInstruction.instruction;
        int fieldOffset = instruction.getFieldOffset();
        RegisterType objectRegisterType = getAndCheckSourceRegister(analyzedInstruction, instruction.getRegisterB(), ReferenceOrUninitCategories);
        if (objectRegisterType.category == 2) {
            return false;
        }
        TypeProto objectRegisterTypeProto = objectRegisterType.type;
        if (objectRegisterTypeProto == null) {
            throw new AssertionError();
        }
        TypeProto classTypeProto = this.classPath.getClass(objectRegisterTypeProto.getType());
        FieldReference resolvedField = classTypeProto.getFieldByOffset(fieldOffset);
        if (resolvedField == null) {
            throw new AnalysisException("Could not resolve the field in class %s at offset %d", objectRegisterType.type.getType(), Integer.valueOf(fieldOffset));
        }
        ClassDef thisClass = this.classPath.getClassDef(this.method.getDefiningClass());
        if (!TypeUtils.canAccessClass(thisClass.getType(), this.classPath.getClassDef(resolvedField.getDefiningClass()))) {
            ClassDef fieldClass = this.classPath.getClassDef(objectRegisterTypeProto.getType());
            while (!TypeUtils.canAccessClass(thisClass.getType(), fieldClass)) {
                String superclass = fieldClass.getSuperclass();
                if (superclass == null) {
                    throw new ExceptionWithContext("Couldn't find accessible class while resolving field %s", resolvedField);
                }
                fieldClass = this.classPath.getClassDef(superclass);
            }
            FieldReference newResolvedField = this.classPath.getClass(fieldClass.getType()).getFieldByOffset(fieldOffset);
            if (newResolvedField == null) {
                throw new ExceptionWithContext("Couldn't find accessible class while resolving field %s", resolvedField);
            }
            resolvedField = new ImmutableFieldReference(fieldClass.getType(), newResolvedField.getName(), newResolvedField.getType());
        }
        String fieldType = resolvedField.getType();
        Opcode opcode = this.classPath.getFieldInstructionMapper().getAndCheckDeodexedOpcode(fieldType, instruction.getOpcode());
        Instruction22c deodexedInstruction = new ImmutableInstruction22c(opcode, (byte) instruction.getRegisterA(), (byte) instruction.getRegisterB(), resolvedField);
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        analyzeInstruction(analyzedInstruction);
        return true;
    }

    private boolean analyzeInvokeVirtual(@Nonnull AnalyzedInstruction analyzedInstruction, boolean isRange) {
        MethodReference targetMethod;
        Instruction deodexedInstruction;
        if (!this.normalizeVirtualMethods) {
            return true;
        }
        if (isRange) {
            targetMethod = (MethodReference) ((Instruction3rc) analyzedInstruction.instruction).getReference();
        } else {
            targetMethod = (MethodReference) ((Instruction35c) analyzedInstruction.instruction).getReference();
        }
        MethodReference replacementMethod = normalizeMethodReference(targetMethod);
        if (replacementMethod == null || replacementMethod.equals(targetMethod)) {
            return true;
        }
        if (isRange) {
            Instruction3rc instruction = (Instruction3rc) analyzedInstruction.instruction;
            deodexedInstruction = new ImmutableInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), replacementMethod);
        } else {
            Instruction deodexedInstruction2 = analyzedInstruction.instruction;
            Instruction35c instruction2 = (Instruction35c) deodexedInstruction2;
            deodexedInstruction = new ImmutableInstruction35c(instruction2.getOpcode(), instruction2.getRegisterCount(), instruction2.getRegisterC(), instruction2.getRegisterD(), instruction2.getRegisterE(), instruction2.getRegisterF(), instruction2.getRegisterG(), replacementMethod);
        }
        analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
        return true;
    }

    private boolean analyzeInvokeVirtualQuick(@Nonnull AnalyzedInstruction analyzedInstruction, boolean isSuper, boolean isRange) {
        int methodIndex;
        int objectRegister;
        MethodReference resolvedMethod;
        Opcode opcode;
        Instruction deodexedInstruction;
        Opcode opcode2;
        MethodReference replacementMethod;
        TypeProto superType;
        if (isRange) {
            Instruction3rms instruction = (Instruction3rms) analyzedInstruction.instruction;
            methodIndex = instruction.getVtableIndex();
            objectRegister = instruction.getStartRegister();
        } else {
            Instruction35ms instruction2 = (Instruction35ms) analyzedInstruction.instruction;
            methodIndex = instruction2.getVtableIndex();
            objectRegister = instruction2.getRegisterC();
        }
        RegisterType objectRegisterType = getAndCheckSourceRegister(analyzedInstruction, objectRegister, ReferenceOrUninitCategories);
        TypeProto objectRegisterTypeProto = objectRegisterType.type;
        if (objectRegisterType.category == 2) {
            return false;
        }
        if (objectRegisterTypeProto == null) {
            throw new AssertionError();
        }
        if (isSuper) {
            TypeProto typeProto = this.classPath.getClass(this.method.getDefiningClass());
            String superclassType = typeProto.getSuperclass();
            if (superclassType != null) {
                superType = this.classPath.getClass(superclassType);
            } else {
                superType = typeProto;
            }
            resolvedMethod = superType.getMethodByVtableIndex(methodIndex);
        } else {
            resolvedMethod = objectRegisterTypeProto.getMethodByVtableIndex(methodIndex);
        }
        if (resolvedMethod != null) {
            ClassDef thisClass = this.classPath.getClassDef(this.method.getDefiningClass());
            if (this.classPath.getClass(resolvedMethod.getDefiningClass()).isInterface()) {
                resolvedMethod = new ReparentedMethodReference(resolvedMethod, objectRegisterTypeProto.getType());
            } else if (!isSuper && !TypeUtils.canAccessClass(thisClass.getType(), this.classPath.getClassDef(resolvedMethod.getDefiningClass()))) {
                ClassDef methodClass = this.classPath.getClassDef(objectRegisterTypeProto.getType());
                while (!TypeUtils.canAccessClass(thisClass.getType(), methodClass)) {
                    String superclass = methodClass.getSuperclass();
                    if (superclass == null) {
                        throw new ExceptionWithContext("Couldn't find accessible class while resolving method %s", resolvedMethod);
                    }
                    methodClass = this.classPath.getClassDef(superclass);
                }
                MethodReference newResolvedMethod = this.classPath.getClass(methodClass.getType()).getMethodByVtableIndex(methodIndex);
                if (newResolvedMethod == null) {
                    throw new ExceptionWithContext("Couldn't find accessible class while resolving method %s", resolvedMethod);
                }
                resolvedMethod = new ImmutableMethodReference(methodClass.getType(), newResolvedMethod.getName(), newResolvedMethod.getParameterTypes(), newResolvedMethod.getReturnType());
            }
            if (this.normalizeVirtualMethods && (replacementMethod = normalizeMethodReference(resolvedMethod)) != null) {
                resolvedMethod = replacementMethod;
            }
            if (isRange) {
                Instruction3rms instruction3 = (Instruction3rms) analyzedInstruction.instruction;
                if (isSuper) {
                    opcode2 = Opcode.INVOKE_SUPER_RANGE;
                } else {
                    opcode2 = Opcode.INVOKE_VIRTUAL_RANGE;
                }
                deodexedInstruction = new ImmutableInstruction3rc(opcode2, instruction3.getStartRegister(), instruction3.getRegisterCount(), resolvedMethod);
            } else {
                Instruction deodexedInstruction2 = analyzedInstruction.instruction;
                Instruction35ms instruction4 = (Instruction35ms) deodexedInstruction2;
                if (isSuper) {
                    opcode = Opcode.INVOKE_SUPER;
                } else {
                    Opcode opcode3 = Opcode.INVOKE_VIRTUAL;
                    opcode = opcode3;
                }
                deodexedInstruction = new ImmutableInstruction35c(opcode, instruction4.getRegisterCount(), instruction4.getRegisterC(), instruction4.getRegisterD(), instruction4.getRegisterE(), instruction4.getRegisterF(), instruction4.getRegisterG(), resolvedMethod);
            }
            analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
            analyzeInstruction(analyzedInstruction);
            return true;
        }
        throw new AnalysisException("Could not resolve the method in class %s at index %d", objectRegisterType.type.getType(), Integer.valueOf(methodIndex));
    }

    private boolean analyzePutGetVolatile(@Nonnull AnalyzedInstruction analyzedInstruction) {
        return analyzePutGetVolatile(analyzedInstruction, true);
    }

    private boolean analyzePutGetVolatile(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
        OneRegisterInstruction instruction;
        FieldReference field = (FieldReference) ((ReferenceInstruction) analyzedInstruction.instruction).getReference();
        String fieldType = field.getType();
        Opcode originalOpcode = analyzedInstruction.instruction.getOpcode();
        Opcode opcode = this.classPath.getFieldInstructionMapper().getAndCheckDeodexedOpcode(fieldType, originalOpcode);
        if (originalOpcode.isStaticFieldAccessor()) {
            instruction = new ImmutableInstruction21c(opcode, ((OneRegisterInstruction) analyzedInstruction.instruction).getRegisterA(), field);
        } else {
            Instruction deodexedInstruction = analyzedInstruction.instruction;
            TwoRegisterInstruction instruction2 = (TwoRegisterInstruction) deodexedInstruction;
            instruction = new ImmutableInstruction22c(opcode, instruction2.getRegisterA(), instruction2.getRegisterB(), field);
        }
        analyzedInstruction.setDeodexedInstruction(instruction);
        if (analyzeResult) {
            analyzeInstruction(analyzedInstruction);
            return true;
        }
        return true;
    }

    @Nonnull
    private static RegisterType getAndCheckSourceRegister(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, BitSet validCategories) {
        if (registerNumber < 0 || registerNumber >= analyzedInstruction.postRegisterMap.length) {
            throw new AssertionError();
        }
        RegisterType registerType = analyzedInstruction.getPreInstructionRegisterType(registerNumber);
        checkRegister(registerType, registerNumber, validCategories);
        BitSet bitSet = WideLowCategories;
        if (validCategories == bitSet) {
            checkRegister(registerType, registerNumber, bitSet);
            checkWidePair(registerNumber, analyzedInstruction);
            RegisterType secondRegisterType = analyzedInstruction.getPreInstructionRegisterType(registerNumber + 1);
            checkRegister(secondRegisterType, registerNumber + 1, WideHighCategories);
        }
        return registerType;
    }

    private static void checkRegister(RegisterType registerType, int registerNumber, BitSet validCategories) {
        if (!validCategories.get(registerType.category)) {
            throw new AnalysisException(String.format("Invalid register type %s for register v%d.", registerType.toString(), Integer.valueOf(registerNumber)), new Object[0]);
        }
    }

    private static void checkWidePair(int registerNumber, AnalyzedInstruction analyzedInstruction) {
        if (registerNumber + 1 >= analyzedInstruction.postRegisterMap.length) {
            throw new AnalysisException(String.format("v%d cannot be used as the first register in a wide registerpair because it is the last register.", Integer.valueOf(registerNumber)), new Object[0]);
        }
    }

    @Nullable
    private MethodReference normalizeMethodReference(@Nonnull MethodReference methodRef) {
        Method resolvedMethod;
        TypeProto typeProto = this.classPath.getClass(methodRef.getDefiningClass());
        try {
            int methodIndex = typeProto.findMethodIndexInVtable(methodRef);
            if (methodIndex < 0) {
                return null;
            }
            ClassProto thisClass = (ClassProto) this.classPath.getClass(this.method.getDefiningClass());
            Method replacementMethod = typeProto.getMethodByVtableIndex(methodIndex);
            if (replacementMethod == null) {
                throw new AssertionError();
            }
            while (true) {
                String superType = typeProto.getSuperclass();
                if (superType != null && (resolvedMethod = (typeProto = this.classPath.getClass(superType)).getMethodByVtableIndex(methodIndex)) != null) {
                    if (!resolvedMethod.equals(replacementMethod) && AnalyzedMethodUtil.canAccess(thisClass, resolvedMethod, false, false, true)) {
                        replacementMethod = resolvedMethod;
                    }
                }
            }
            return replacementMethod;
        } catch (UnresolvedClassException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ReparentedMethodReference extends BaseMethodReference {
        private final MethodReference baseReference;
        private final String definingClass;

        public ReparentedMethodReference(MethodReference baseReference, String definingClass) {
            this.baseReference = baseReference;
            this.definingClass = definingClass;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.baseReference.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
        @Nonnull
        public List<? extends CharSequence> getParameterTypes() {
            return this.baseReference.getParameterTypes();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
        @Nonnull
        public String getReturnType() {
            return this.baseReference.getReturnType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return this.definingClass;
        }
    }
}
