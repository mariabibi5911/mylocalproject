package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction;
import com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeListIterator;
import com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.util.AlignmentUtils;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBackedMethodImplementation implements MethodImplementation {
    protected final int codeOffset;

    @Nonnull
    public final DexBackedDexFile dexFile;

    @Nonnull
    public final DexBackedMethod method;

    /* JADX INFO: Access modifiers changed from: protected */
    public DexBackedMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
        this.dexFile = dexFile;
        this.method = method;
        this.codeOffset = codeOffset;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    public int getRegisterCount() {
        return this.dexFile.getDataBuffer().readUshort(this.codeOffset);
    }

    public int getInstructionsSize() {
        return this.dexFile.getDataBuffer().readSmallUint(this.codeOffset + 12);
    }

    protected int getInstructionsStartOffset() {
        return this.codeOffset + 16;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public Iterable<? extends Instruction> getInstructions() {
        int instructionsSize = getInstructionsSize();
        final int instructionsStartOffset = getInstructionsStartOffset();
        final int endOffset = (instructionsSize * 2) + instructionsStartOffset;
        return new Iterable<Instruction>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedMethodImplementation.1
            @Override // java.lang.Iterable
            public Iterator<Instruction> iterator() {
                return new VariableSizeLookaheadIterator<Instruction>(DexBackedMethodImplementation.this.dexFile.getDataBuffer(), instructionsStartOffset) { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedMethodImplementation.1.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                    public Instruction readNextItem(@Nonnull DexReader reader) {
                        if (reader.getOffset() >= endOffset) {
                            return endOfData();
                        }
                        Instruction instruction = DexBackedInstruction.readFrom(DexBackedMethodImplementation.this.dexFile, reader);
                        int offset = reader.getOffset();
                        if (offset > endOffset || offset < 0) {
                            throw new ExceptionWithContext("The last instruction in method %s is truncated", DexBackedMethodImplementation.this.method);
                        }
                        return instruction;
                    }
                };
            }
        };
    }

    protected int getTriesSize() {
        return this.dexFile.getDataBuffer().readUshort(this.codeOffset + 6);
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public List<? extends DexBackedTryBlock> getTryBlocks() {
        final int triesSize = getTriesSize();
        if (triesSize > 0) {
            int instructionsSize = getInstructionsSize();
            final int triesStartOffset = AlignmentUtils.alignOffset(getInstructionsStartOffset() + (instructionsSize * 2), 4);
            final int handlersStartOffset = (triesSize * 8) + triesStartOffset;
            return new FixedSizeList<DexBackedTryBlock>() { // from class: com.android.tools.smali.dexlib2.dexbacked.DexBackedMethodImplementation.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public DexBackedTryBlock readItem(int index) {
                    return new DexBackedTryBlock(DexBackedMethodImplementation.this.dexFile, triesStartOffset + (index * 8), handlersStartOffset);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return triesSize;
                }
            };
        }
        return ImmutableList.of();
    }

    protected int getDebugOffset() {
        return this.dexFile.getDataBuffer().readInt(this.codeOffset + 8);
    }

    @Nonnull
    private DebugInfo getDebugInfo() {
        int debugOffset = getDebugOffset();
        if (debugOffset == -1 || debugOffset == 0) {
            return DebugInfo.newOrEmpty(this.dexFile, 0, this);
        }
        if (debugOffset < 0) {
            System.err.println(String.format("%s: Invalid debug offset", this.method));
            return DebugInfo.newOrEmpty(this.dexFile, 0, this);
        }
        if (this.dexFile.getBaseDataOffset() + debugOffset >= this.dexFile.getBuffer().buf.length) {
            System.err.println(String.format("%s: Invalid debug offset", this.method));
            return DebugInfo.newOrEmpty(this.dexFile, 0, this);
        }
        return DebugInfo.newOrEmpty(this.dexFile, debugOffset, this);
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public Iterable<? extends DebugItem> getDebugItems() {
        return getDebugInfo();
    }

    @Nonnull
    public Iterator<String> getParameterNames(@Nullable DexReader dexReader) {
        return getDebugInfo().getParameterNames(dexReader);
    }

    public int getSize() {
        int lastOffset = getInstructionsStartOffset();
        int lastOffset2 = lastOffset + (getInstructionsSize() * 2);
        for (DexBackedTryBlock tryBlock : getTryBlocks()) {
            Iterator<? extends DexBackedExceptionHandler> tryHandlerIter = tryBlock.getExceptionHandlers().iterator();
            while (tryHandlerIter.hasNext()) {
                tryHandlerIter.next();
            }
            lastOffset2 = ((VariableSizeListIterator) tryHandlerIter).getReaderOffset();
        }
        return lastOffset2 - this.codeOffset;
    }
}
