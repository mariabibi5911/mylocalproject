package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.debug.BuilderEndLocal;
import com.android.tools.smali.dexlib2.builder.debug.BuilderEpilogueBegin;
import com.android.tools.smali.dexlib2.builder.debug.BuilderLineNumber;
import com.android.tools.smali.dexlib2.builder.debug.BuilderPrologueEnd;
import com.android.tools.smali.dexlib2.builder.debug.BuilderRestartLocal;
import com.android.tools.smali.dexlib2.builder.debug.BuilderSetSourceFile;
import com.android.tools.smali.dexlib2.builder.debug.BuilderStartLocal;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderArrayPayload;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction12x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction20bc;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction20t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21ih;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21lh;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21s;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22b;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22cs;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22s;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction23x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction30t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31i;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction32x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35mi;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35ms;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rc;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rmi;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rms;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction45cc;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction51l;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderPackedSwitchPayload;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderSparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import com.android.tools.smali.dexlib2.iface.debug.LineNumber;
import com.android.tools.smali.dexlib2.iface.debug.RestartLocal;
import com.android.tools.smali.dexlib2.iface.debug.SetSourceFile;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11n;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction12x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21ih;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22b;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22cs;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction23x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction30t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31i;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35mi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35ms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction51l;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MutableMethodImplementation implements MethodImplementation {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean fixInstructions;
    final ArrayList<MethodLocation> instructionList;
    private final int registerCount;
    private final ArrayList<BuilderTryBlock> tryBlocks;

    /* loaded from: classes.dex */
    private interface Task {
        void perform();
    }

    public MutableMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
        this.instructionList = Lists.newArrayList(new MethodLocation(null, 0, 0));
        this.tryBlocks = Lists.newArrayList();
        this.fixInstructions = true;
        this.registerCount = methodImplementation.getRegisterCount();
        int codeAddress = 0;
        int index = 0;
        Iterator<? extends Instruction> it = methodImplementation.getInstructions().iterator();
        while (it.hasNext()) {
            codeAddress += it.next().getCodeUnits();
            index++;
            this.instructionList.add(new MethodLocation(null, codeAddress, index));
        }
        final int[] codeAddressToIndex = new int[codeAddress + 1];
        Arrays.fill(codeAddressToIndex, -1);
        for (int i = 0; i < this.instructionList.size(); i++) {
            codeAddressToIndex[this.instructionList.get(i).codeAddress] = i;
        }
        List<Task> switchPayloadTasks = Lists.newArrayList();
        int index2 = 0;
        for (final Instruction instruction : methodImplementation.getInstructions()) {
            final MethodLocation location = this.instructionList.get(index2);
            Opcode opcode = instruction.getOpcode();
            if (opcode == Opcode.PACKED_SWITCH_PAYLOAD || opcode == Opcode.SPARSE_SWITCH_PAYLOAD) {
                switchPayloadTasks.add(new Task() { // from class: com.android.tools.smali.dexlib2.builder.MutableMethodImplementation.1
                    @Override // com.android.tools.smali.dexlib2.builder.MutableMethodImplementation.Task
                    public void perform() {
                        MutableMethodImplementation.this.convertAndSetInstruction(location, codeAddressToIndex, instruction);
                    }
                });
            } else {
                convertAndSetInstruction(location, codeAddressToIndex, instruction);
            }
            index2++;
        }
        for (Task switchPayloadTask : switchPayloadTasks) {
            switchPayloadTask.perform();
        }
        for (DebugItem debugItem : methodImplementation.getDebugItems()) {
            int debugCodeAddress = debugItem.getCodeAddress();
            int locationIndex = mapCodeAddressToIndex(codeAddressToIndex, debugCodeAddress);
            MethodLocation debugLocation = this.instructionList.get(locationIndex);
            BuilderDebugItem builderDebugItem = convertDebugItem(debugItem);
            debugLocation.getDebugItems().add(builderDebugItem);
            builderDebugItem.location = debugLocation;
        }
        for (TryBlock<? extends ExceptionHandler> tryBlock : methodImplementation.getTryBlocks()) {
            Label startLabel = newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress());
            Label endLabel = newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount());
            Iterator<? extends Object> it2 = tryBlock.getExceptionHandlers().iterator();
            while (it2.hasNext()) {
                ExceptionHandler exceptionHandler = (ExceptionHandler) it2.next();
                this.tryBlocks.add(new BuilderTryBlock(startLabel, endLabel, exceptionHandler.getExceptionTypeReference(), newLabel(codeAddressToIndex, exceptionHandler.getHandlerCodeAddress())));
            }
        }
    }

    public MutableMethodImplementation(int registerCount) {
        this.instructionList = Lists.newArrayList(new MethodLocation(null, 0, 0));
        this.tryBlocks = Lists.newArrayList();
        this.fixInstructions = true;
        this.registerCount = registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public List<BuilderInstruction> getInstructions() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return new AbstractList<BuilderInstruction>() { // from class: com.android.tools.smali.dexlib2.builder.MutableMethodImplementation.2
            @Override // java.util.AbstractList, java.util.List
            public BuilderInstruction get(int i) {
                if (i < size()) {
                    if (MutableMethodImplementation.this.fixInstructions) {
                        MutableMethodImplementation.this.fixInstructions();
                    }
                    return MutableMethodImplementation.this.instructionList.get(i).instruction;
                }
                throw new IndexOutOfBoundsException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                if (MutableMethodImplementation.this.fixInstructions) {
                    MutableMethodImplementation.this.fixInstructions();
                }
                return MutableMethodImplementation.this.instructionList.size() - 1;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public List<BuilderTryBlock> getTryBlocks() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return Collections.unmodifiableList(this.tryBlocks);
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public Iterable<? extends DebugItem> getDebugItems() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return Iterables.concat(Iterables.transform(this.instructionList, new Function<MethodLocation, Iterable<? extends DebugItem>>() { // from class: com.android.tools.smali.dexlib2.builder.MutableMethodImplementation.3
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // com.google.common.base.Function
            @Nullable
            public Iterable<? extends DebugItem> apply(@Nullable MethodLocation input) {
                if (input != null) {
                    if (MutableMethodImplementation.this.fixInstructions) {
                        throw new IllegalStateException("This iterator was invalidated by a change to this MutableMethodImplementation.");
                    }
                    return input.getDebugItems();
                }
                throw new AssertionError();
            }
        }));
    }

    public void addCatch(@Nullable TypeReference type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
    }

    public void addCatch(@Nullable String type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
    }

    public void addCatch(@Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, handler));
    }

    public void addInstruction(int index, BuilderInstruction instruction) {
        if (index >= this.instructionList.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.instructionList.size() - 1) {
            addInstruction(instruction);
            return;
        }
        int codeAddress = this.instructionList.get(index).getCodeAddress();
        MethodLocation newLoc = new MethodLocation(instruction, codeAddress, index);
        this.instructionList.add(index, newLoc);
        instruction.location = newLoc;
        int codeAddress2 = codeAddress + instruction.getCodeUnits();
        for (int i = index + 1; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.index++;
            location.codeAddress = codeAddress2;
            if (location.instruction != null) {
                codeAddress2 += location.instruction.getCodeUnits();
            } else if (i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void addInstruction(@Nonnull BuilderInstruction instruction) {
        ArrayList<MethodLocation> arrayList = this.instructionList;
        MethodLocation last = arrayList.get(arrayList.size() - 1);
        last.instruction = instruction;
        instruction.location = last;
        int nextCodeAddress = last.codeAddress + instruction.getCodeUnits();
        ArrayList<MethodLocation> arrayList2 = this.instructionList;
        arrayList2.add(new MethodLocation(null, nextCodeAddress, arrayList2.size()));
        this.fixInstructions = true;
    }

    public void replaceInstruction(int index, @Nonnull BuilderInstruction replacementInstruction) {
        if (index >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation replaceLocation = this.instructionList.get(index);
        replacementInstruction.location = replaceLocation;
        BuilderInstruction old = replaceLocation.instruction;
        if (old == null) {
            throw new AssertionError();
        }
        old.location = null;
        replaceLocation.instruction = replacementInstruction;
        int codeAddress = replaceLocation.codeAddress + replaceLocation.instruction.getCodeUnits();
        for (int i = index + 1; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
                codeAddress += instruction.getCodeUnits();
            } else if (i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void removeInstruction(int index) {
        if (index >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation toRemove = this.instructionList.get(index);
        toRemove.instruction = null;
        MethodLocation next = this.instructionList.get(index + 1);
        toRemove.mergeInto(next);
        this.instructionList.remove(index);
        int codeAddress = toRemove.codeAddress;
        for (int i = index; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.index = i;
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
                codeAddress += instruction.getCodeUnits();
            } else if (i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void swapInstructions(int index1, int index2) {
        if (index1 >= this.instructionList.size() - 1 || index2 >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation first = this.instructionList.get(index1);
        MethodLocation second = this.instructionList.get(index2);
        if (first.instruction == null) {
            throw new AssertionError();
        }
        if (second.instruction == null) {
            throw new AssertionError();
        }
        first.instruction.location = second;
        second.instruction.location = first;
        BuilderInstruction tmp = second.instruction;
        second.instruction = first.instruction;
        first.instruction = tmp;
        if (index2 < index1) {
            index2 = index1;
            index1 = index2;
        }
        int tmp2 = first.codeAddress;
        int codeAddress = tmp2 + first.instruction.getCodeUnits();
        for (int i = index1 + 1; i <= index2; i++) {
            MethodLocation location = this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.instruction;
            if (instruction == null) {
                throw new AssertionError();
            }
            codeAddress += location.instruction.getCodeUnits();
        }
        this.fixInstructions = true;
    }

    @Nullable
    private BuilderInstruction getFirstNonNop(int startIndex) {
        for (int i = startIndex; i < this.instructionList.size() - 1; i++) {
            BuilderInstruction instruction = this.instructionList.get(i).instruction;
            if (instruction == null) {
                throw new AssertionError();
            }
            if (instruction.getOpcode() != Opcode.NOP) {
                return instruction;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x000a, code lost:

        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01a9, code lost:

        continue;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:55:0x0105. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fixInstructions() {
        boolean madeChanges;
        BuilderOffsetInstruction replacement;
        HashSet<MethodLocation> payloadLocations = Sets.newHashSet();
        Iterator<MethodLocation> it = this.instructionList.iterator();
        while (it.hasNext()) {
            MethodLocation location = it.next();
            BuilderInstruction instruction = location.instruction;
            if (instruction != null) {
                switch (AnonymousClass4.$SwitchMap$com$android$tools$smali$dexlib2$Opcode[instruction.getOpcode().ordinal()]) {
                    case 1:
                    case 2:
                        MethodLocation targetLocation = ((BuilderOffsetInstruction) instruction).getTarget().getLocation();
                        BuilderInstruction targetInstruction = targetLocation.instruction;
                        if (targetInstruction == null) {
                            throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d points to the end of the method.", Integer.valueOf(location.codeAddress), Integer.valueOf(location.index)));
                        }
                        if (targetInstruction.getOpcode() == Opcode.NOP) {
                            targetInstruction = getFirstNonNop(targetLocation.index + 1);
                        }
                        if (targetInstruction == null || !(targetInstruction instanceof BuilderSwitchPayload)) {
                            throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d does not refer to a payload instruction.", Integer.valueOf(location.codeAddress), Integer.valueOf(location.index)));
                        }
                        if ((instruction.opcode == Opcode.PACKED_SWITCH && targetInstruction.getOpcode() != Opcode.PACKED_SWITCH_PAYLOAD) || (instruction.opcode == Opcode.SPARSE_SWITCH && targetInstruction.getOpcode() != Opcode.SPARSE_SWITCH_PAYLOAD)) {
                            throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d refers to the wrong type of payload instruction.", Integer.valueOf(location.codeAddress), Integer.valueOf(location.index)));
                        }
                        if (!payloadLocations.add(targetLocation)) {
                            throw new IllegalStateException("Multiple switch instructions refer to the same payload. This is not currently supported. Please file a bug :)");
                        }
                        ((BuilderSwitchPayload) targetInstruction).referrer = location;
                        break;
                }
            }
        }
        do {
            madeChanges = false;
            int index = 0;
            while (index < this.instructionList.size()) {
                MethodLocation location2 = this.instructionList.get(index);
                BuilderInstruction instruction2 = location2.instruction;
                if (instruction2 != null) {
                    switch (instruction2.getOpcode()) {
                        case GOTO:
                            int offset = ((BuilderOffsetInstruction) instruction2).internalGetCodeOffset();
                            if (offset >= -128 && offset <= 127) {
                                break;
                            } else {
                                if (offset < -32768 || offset > 32767) {
                                    replacement = new BuilderInstruction30t(Opcode.GOTO_32, ((BuilderOffsetInstruction) instruction2).getTarget());
                                } else {
                                    replacement = new BuilderInstruction20t(Opcode.GOTO_16, ((BuilderOffsetInstruction) instruction2).getTarget());
                                }
                                replaceInstruction(location2.index, replacement);
                                madeChanges = true;
                                break;
                            }
                        case GOTO_16:
                            int offset2 = ((BuilderOffsetInstruction) instruction2).internalGetCodeOffset();
                            if (offset2 >= -32768 && offset2 <= 32767) {
                                break;
                            } else {
                                BuilderOffsetInstruction replacement2 = new BuilderInstruction30t(Opcode.GOTO_32, ((BuilderOffsetInstruction) instruction2).getTarget());
                                replaceInstruction(location2.index, replacement2);
                                madeChanges = true;
                                break;
                            }
                        case SPARSE_SWITCH_PAYLOAD:
                        case PACKED_SWITCH_PAYLOAD:
                            if (((BuilderSwitchPayload) instruction2).referrer == null) {
                                removeInstruction(index);
                                index--;
                                madeChanges = true;
                                break;
                            }
                        case ARRAY_PAYLOAD:
                            if ((location2.codeAddress & 1) != 0) {
                                int previousIndex = location2.index - 1;
                                MethodLocation previousLocation = this.instructionList.get(previousIndex);
                                Instruction previousInstruction = previousLocation.instruction;
                                if (previousInstruction == null) {
                                    throw new AssertionError();
                                }
                                if (previousInstruction.getOpcode() == Opcode.NOP) {
                                    removeInstruction(previousIndex);
                                    index--;
                                } else {
                                    addInstruction(location2.index, new BuilderInstruction10x(Opcode.NOP));
                                    index++;
                                }
                                madeChanges = true;
                                break;
                            } else {
                                continue;
                            }
                    }
                }
                index++;
            }
        } while (madeChanges);
        this.fixInstructions = false;
    }

    private int mapCodeAddressToIndex(@Nonnull int[] codeAddressToIndex, int codeAddress) {
        while (true) {
            if (codeAddress >= codeAddressToIndex.length) {
                codeAddress = codeAddressToIndex.length - 1;
            }
            int index = codeAddressToIndex[codeAddress];
            if (index < 0) {
                codeAddress--;
            } else {
                return index;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0027, code lost:

        r1 = r1 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:

        if (r4.instructionList.get(r1).codeAddress > r5) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0035, code lost:

        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:

        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003e, code lost:

        if (r1 >= r4.instructionList.size()) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:

        if (r4.instructionList.get(r1).codeAddress <= r5) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004e, code lost:

        return r1 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0025, code lost:

        if (r2.codeAddress > r5) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int mapCodeAddressToIndex(int codeAddress) {
        int index = (int) (codeAddress / 1.9f);
        if (index >= this.instructionList.size()) {
            index = this.instructionList.size() - 1;
        }
        MethodLocation guessedLocation = this.instructionList.get(index);
        if (guessedLocation.codeAddress == codeAddress) {
            return index;
        }
    }

    @Nonnull
    public Label newLabelForAddress(int codeAddress) {
        if (codeAddress >= 0) {
            ArrayList<MethodLocation> arrayList = this.instructionList;
            if (codeAddress <= arrayList.get(arrayList.size() - 1).codeAddress) {
                MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddress));
                return referent.addNewLabel();
            }
        }
        throw new IndexOutOfBoundsException(String.format("codeAddress %d out of bounds", Integer.valueOf(codeAddress)));
    }

    @Nonnull
    public Label newLabelForIndex(int instructionIndex) {
        if (instructionIndex < 0 || instructionIndex >= this.instructionList.size()) {
            throw new IndexOutOfBoundsException(String.format("instruction index %d out of bounds", Integer.valueOf(instructionIndex)));
        }
        MethodLocation referent = this.instructionList.get(instructionIndex);
        return referent.addNewLabel();
    }

    @Nonnull
    private Label newLabel(@Nonnull int[] codeAddressToIndex, int codeAddress) {
        MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
        return referent.addNewLabel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SwitchPayloadReferenceLabel extends Label {

        @Nonnull
        public MethodLocation switchLocation;

        private SwitchPayloadReferenceLabel() {
        }
    }

    @Nonnull
    public Label newSwitchPayloadReferenceLabel(@Nonnull MethodLocation switchLocation, @Nonnull int[] codeAddressToIndex, int codeAddress) {
        MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
        SwitchPayloadReferenceLabel label = new SwitchPayloadReferenceLabel();
        label.switchLocation = switchLocation;
        referent.getLabels().add(label);
        return label;
    }

    private void setInstruction(@Nonnull MethodLocation location, @Nonnull BuilderInstruction instruction) {
        location.instruction = instruction;
        instruction.location = location;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.builder.MutableMethodImplementation$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;

        static {
            int[] iArr = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr;
            try {
                iArr[Format.Format10t.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format10x.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11n.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11x.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format12x.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20bc.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20t.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21c.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21ih.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21lh.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21s.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21t.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22b.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22c.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22cs.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22s.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22t.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22x.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format23x.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format30t.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31c.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31i.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31t.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format32x.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35mi.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35ms.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rmi.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rms.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format45cc.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format51l.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.PackedSwitchPayload.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.SparseSwitchPayload.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.ArrayPayload.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            int[] iArr2 = new int[Opcode.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Opcode = iArr2;
            try {
                iArr2[Opcode.SPARSE_SWITCH.ordinal()] = 1;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.PACKED_SWITCH.ordinal()] = 2;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.GOTO.ordinal()] = 3;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.GOTO_16.ordinal()] = 4;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.SPARSE_SWITCH_PAYLOAD.ordinal()] = 5;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.PACKED_SWITCH_PAYLOAD.ordinal()] = 6;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Opcode[Opcode.ARRAY_PAYLOAD.ordinal()] = 7;
            } catch (NoSuchFieldError e42) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void convertAndSetInstruction(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction instruction) {
        switch (AnonymousClass4.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction.getOpcode().format.ordinal()]) {
            case 1:
                setInstruction(location, newBuilderInstruction10t(location.codeAddress, codeAddressToIndex, (Instruction10t) instruction));
                return;
            case 2:
                setInstruction(location, newBuilderInstruction10x((Instruction10x) instruction));
                return;
            case 3:
                setInstruction(location, newBuilderInstruction11n((Instruction11n) instruction));
                return;
            case 4:
                setInstruction(location, newBuilderInstruction11x((Instruction11x) instruction));
                return;
            case 5:
                setInstruction(location, newBuilderInstruction12x((Instruction12x) instruction));
                return;
            case 6:
                setInstruction(location, newBuilderInstruction20bc((Instruction20bc) instruction));
                return;
            case 7:
                setInstruction(location, newBuilderInstruction20t(location.codeAddress, codeAddressToIndex, (Instruction20t) instruction));
                return;
            case 8:
                setInstruction(location, newBuilderInstruction21c((Instruction21c) instruction));
                return;
            case 9:
                setInstruction(location, newBuilderInstruction21ih((Instruction21ih) instruction));
                return;
            case 10:
                setInstruction(location, newBuilderInstruction21lh((Instruction21lh) instruction));
                return;
            case 11:
                setInstruction(location, newBuilderInstruction21s((Instruction21s) instruction));
                return;
            case 12:
                setInstruction(location, newBuilderInstruction21t(location.codeAddress, codeAddressToIndex, (Instruction21t) instruction));
                return;
            case 13:
                setInstruction(location, newBuilderInstruction22b((Instruction22b) instruction));
                return;
            case 14:
                setInstruction(location, newBuilderInstruction22c((Instruction22c) instruction));
                return;
            case 15:
                setInstruction(location, newBuilderInstruction22cs((Instruction22cs) instruction));
                return;
            case 16:
                setInstruction(location, newBuilderInstruction22s((Instruction22s) instruction));
                return;
            case 17:
                setInstruction(location, newBuilderInstruction22t(location.codeAddress, codeAddressToIndex, (Instruction22t) instruction));
                return;
            case 18:
                setInstruction(location, newBuilderInstruction22x((Instruction22x) instruction));
                return;
            case 19:
                setInstruction(location, newBuilderInstruction23x((Instruction23x) instruction));
                return;
            case 20:
                setInstruction(location, newBuilderInstruction30t(location.codeAddress, codeAddressToIndex, (Instruction30t) instruction));
                return;
            case 21:
                setInstruction(location, newBuilderInstruction31c((Instruction31c) instruction));
                return;
            case 22:
                setInstruction(location, newBuilderInstruction31i((Instruction31i) instruction));
                return;
            case 23:
                setInstruction(location, newBuilderInstruction31t(location, codeAddressToIndex, (Instruction31t) instruction));
                return;
            case 24:
                setInstruction(location, newBuilderInstruction32x((Instruction32x) instruction));
                return;
            case 25:
                setInstruction(location, newBuilderInstruction35c((Instruction35c) instruction));
                return;
            case 26:
                setInstruction(location, newBuilderInstruction35mi((Instruction35mi) instruction));
                return;
            case 27:
                setInstruction(location, newBuilderInstruction35ms((Instruction35ms) instruction));
                return;
            case 28:
                setInstruction(location, newBuilderInstruction3rc((Instruction3rc) instruction));
                return;
            case 29:
                setInstruction(location, newBuilderInstruction3rmi((Instruction3rmi) instruction));
                return;
            case 30:
                setInstruction(location, newBuilderInstruction3rms((Instruction3rms) instruction));
                return;
            case 31:
                setInstruction(location, newBuilderInstruction45cc((Instruction45cc) instruction));
                return;
            case 32:
                setInstruction(location, newBuilderInstruction51l((Instruction51l) instruction));
                return;
            case 33:
                setInstruction(location, newBuilderPackedSwitchPayload(location, codeAddressToIndex, (PackedSwitchPayload) instruction));
                return;
            case 34:
                setInstruction(location, newBuilderSparseSwitchPayload(location, codeAddressToIndex, (SparseSwitchPayload) instruction));
                return;
            case 35:
                setInstruction(location, newBuilderArrayPayload((ArrayPayload) instruction));
                return;
            default:
                throw new ExceptionWithContext("Instruction format %s not supported", instruction.getOpcode().format);
        }
    }

    @Nonnull
    private BuilderInstruction10t newBuilderInstruction10t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction10t instruction) {
        return new BuilderInstruction10t(instruction.getOpcode(), newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress));
    }

    @Nonnull
    private BuilderInstruction10x newBuilderInstruction10x(@Nonnull Instruction10x instruction) {
        return new BuilderInstruction10x(instruction.getOpcode());
    }

    @Nonnull
    private BuilderInstruction11n newBuilderInstruction11n(@Nonnull Instruction11n instruction) {
        return new BuilderInstruction11n(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction11x newBuilderInstruction11x(@Nonnull Instruction11x instruction) {
        return new BuilderInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
    }

    @Nonnull
    private BuilderInstruction12x newBuilderInstruction12x(@Nonnull Instruction12x instruction) {
        return new BuilderInstruction12x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction20bc newBuilderInstruction20bc(@Nonnull Instruction20bc instruction) {
        return new BuilderInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction20t newBuilderInstruction20t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction20t instruction) {
        return new BuilderInstruction20t(instruction.getOpcode(), newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress));
    }

    @Nonnull
    private BuilderInstruction21c newBuilderInstruction21c(@Nonnull Instruction21c instruction) {
        return new BuilderInstruction21c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction21ih newBuilderInstruction21ih(@Nonnull Instruction21ih instruction) {
        return new BuilderInstruction21ih(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction21lh newBuilderInstruction21lh(@Nonnull Instruction21lh instruction) {
        return new BuilderInstruction21lh(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Nonnull
    private BuilderInstruction21s newBuilderInstruction21s(@Nonnull Instruction21s instruction) {
        return new BuilderInstruction21s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction21t newBuilderInstruction21t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction21t instruction) {
        return new BuilderInstruction21t(instruction.getOpcode(), instruction.getRegisterA(), newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress));
    }

    @Nonnull
    private BuilderInstruction22b newBuilderInstruction22b(@Nonnull Instruction22b instruction) {
        return new BuilderInstruction22b(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction22c newBuilderInstruction22c(@Nonnull Instruction22c instruction) {
        return new BuilderInstruction22c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction22cs newBuilderInstruction22cs(@Nonnull Instruction22cs instruction) {
        return new BuilderInstruction22cs(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getFieldOffset());
    }

    @Nonnull
    private BuilderInstruction22s newBuilderInstruction22s(@Nonnull Instruction22s instruction) {
        return new BuilderInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction22t newBuilderInstruction22t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction22t instruction) {
        return new BuilderInstruction22t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress));
    }

    @Nonnull
    private BuilderInstruction22x newBuilderInstruction22x(@Nonnull Instruction22x instruction) {
        return new BuilderInstruction22x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction23x newBuilderInstruction23x(@Nonnull Instruction23x instruction) {
        return new BuilderInstruction23x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getRegisterC());
    }

    @Nonnull
    private BuilderInstruction30t newBuilderInstruction30t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction30t instruction) {
        return new BuilderInstruction30t(instruction.getOpcode(), newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress));
    }

    @Nonnull
    private BuilderInstruction31c newBuilderInstruction31c(@Nonnull Instruction31c instruction) {
        return new BuilderInstruction31c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction31i newBuilderInstruction31i(@Nonnull Instruction31i instruction) {
        return new BuilderInstruction31i(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction31t newBuilderInstruction31t(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction31t instruction) {
        Label newLabel;
        int codeAddress = location.getCodeAddress();
        if (instruction.getOpcode() != Opcode.FILL_ARRAY_DATA) {
            newLabel = newSwitchPayloadReferenceLabel(location, codeAddressToIndex, instruction.getCodeOffset() + codeAddress);
        } else {
            newLabel = newLabel(codeAddressToIndex, instruction.getCodeOffset() + codeAddress);
        }
        return new BuilderInstruction31t(instruction.getOpcode(), instruction.getRegisterA(), newLabel);
    }

    @Nonnull
    private BuilderInstruction32x newBuilderInstruction32x(@Nonnull Instruction32x instruction) {
        return new BuilderInstruction32x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction35c newBuilderInstruction35c(@Nonnull Instruction35c instruction) {
        return new BuilderInstruction35c(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction35mi newBuilderInstruction35mi(@Nonnull Instruction35mi instruction) {
        return new BuilderInstruction35mi(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getInlineIndex());
    }

    @Nonnull
    private BuilderInstruction35ms newBuilderInstruction35ms(@Nonnull Instruction35ms instruction) {
        return new BuilderInstruction35ms(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getVtableIndex());
    }

    @Nonnull
    private BuilderInstruction3rc newBuilderInstruction3rc(@Nonnull Instruction3rc instruction) {
        return new BuilderInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction3rmi newBuilderInstruction3rmi(@Nonnull Instruction3rmi instruction) {
        return new BuilderInstruction3rmi(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getInlineIndex());
    }

    @Nonnull
    private BuilderInstruction3rms newBuilderInstruction3rms(@Nonnull Instruction3rms instruction) {
        return new BuilderInstruction3rms(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getVtableIndex());
    }

    @Nonnull
    private BuilderInstruction45cc newBuilderInstruction45cc(@Nonnull Instruction45cc instruction) {
        return new BuilderInstruction45cc(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference(), instruction.getReference2());
    }

    @Nonnull
    private BuilderInstruction51l newBuilderInstruction51l(@Nonnull Instruction51l instruction) {
        return new BuilderInstruction51l(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Nullable
    private MethodLocation findSwitchForPayload(@Nonnull MethodLocation payloadLocation) {
        MethodLocation location = payloadLocation;
        MethodLocation switchLocation = null;
        do {
            for (Label label : location.getLabels()) {
                if (label instanceof SwitchPayloadReferenceLabel) {
                    if (switchLocation != null) {
                        throw new IllegalStateException("Multiple switch instructions refer to the same payload. This is not currently supported. Please file a bug :)");
                    }
                    switchLocation = ((SwitchPayloadReferenceLabel) label).switchLocation;
                }
            }
            if (location.index == 0) {
                return switchLocation;
            }
            location = this.instructionList.get(location.index - 1);
            if (location.instruction == null) {
                break;
            }
        } while (location.instruction.getOpcode() == Opcode.NOP);
        return switchLocation;
    }

    @Nonnull
    private BuilderPackedSwitchPayload newBuilderPackedSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull PackedSwitchPayload instruction) {
        int baseAddress;
        List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
        if (switchElements.size() == 0) {
            return new BuilderPackedSwitchPayload(0, null);
        }
        MethodLocation switchLocation = findSwitchForPayload(location);
        if (switchLocation == null) {
            baseAddress = 0;
        } else {
            baseAddress = switchLocation.codeAddress;
        }
        List<Label> labels = Lists.newArrayList();
        for (SwitchElement element : switchElements) {
            labels.add(newLabel(codeAddressToIndex, element.getOffset() + baseAddress));
        }
        return new BuilderPackedSwitchPayload(switchElements.get(0).getKey(), labels);
    }

    @Nonnull
    private BuilderSparseSwitchPayload newBuilderSparseSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull SparseSwitchPayload instruction) {
        int baseAddress;
        List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
        if (switchElements.size() == 0) {
            return new BuilderSparseSwitchPayload(null);
        }
        MethodLocation switchLocation = findSwitchForPayload(location);
        if (switchLocation == null) {
            baseAddress = 0;
        } else {
            baseAddress = switchLocation.codeAddress;
        }
        List<SwitchLabelElement> labelElements = Lists.newArrayList();
        for (SwitchElement element : switchElements) {
            labelElements.add(new SwitchLabelElement(element.getKey(), newLabel(codeAddressToIndex, element.getOffset() + baseAddress)));
        }
        return new BuilderSparseSwitchPayload(labelElements);
    }

    @Nonnull
    private BuilderArrayPayload newBuilderArrayPayload(@Nonnull ArrayPayload instruction) {
        return new BuilderArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
    }

    @Nonnull
    private BuilderDebugItem convertDebugItem(@Nonnull DebugItem debugItem) {
        switch (debugItem.getDebugItemType()) {
            case 3:
                StartLocal startLocal = (StartLocal) debugItem;
                return new BuilderStartLocal(startLocal.getRegister(), startLocal.getNameReference(), startLocal.getTypeReference(), startLocal.getSignatureReference());
            case 4:
            default:
                throw new ExceptionWithContext("Invalid debug item type: " + debugItem.getDebugItemType(), new Object[0]);
            case 5:
                EndLocal endLocal = (EndLocal) debugItem;
                return new BuilderEndLocal(endLocal.getRegister());
            case 6:
                RestartLocal restartLocal = (RestartLocal) debugItem;
                return new BuilderRestartLocal(restartLocal.getRegister());
            case 7:
                return new BuilderPrologueEnd();
            case 8:
                return new BuilderEpilogueBegin();
            case 9:
                SetSourceFile setSourceFile = (SetSourceFile) debugItem;
                return new BuilderSetSourceFile(setSourceFile.getSourceFileReference());
            case 10:
                LineNumber lineNumber = (LineNumber) debugItem;
                return new BuilderLineNumber(lineNumber.getLineNumber());
        }
    }
}
