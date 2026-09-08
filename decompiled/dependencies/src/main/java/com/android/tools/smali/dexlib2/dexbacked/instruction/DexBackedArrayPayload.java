package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedArrayPayload extends DexBackedInstruction implements ArrayPayload {
    private static final int ELEMENTS_OFFSET = 8;
    private static final int ELEMENT_COUNT_OFFSET = 4;
    private static final int ELEMENT_WIDTH_OFFSET = 2;
    public static final Opcode OPCODE = Opcode.ARRAY_PAYLOAD;
    public final int elementCount;
    public final int elementWidth;

    public DexBackedArrayPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, OPCODE, instructionStart);
        int localElementWidth = dexFile.getDataBuffer().readUshort(instructionStart + 2);
        if (localElementWidth == 0) {
            this.elementWidth = 1;
            this.elementCount = 0;
            return;
        }
        this.elementWidth = localElementWidth;
        int readSmallUint = dexFile.getDataBuffer().readSmallUint(instructionStart + 4);
        this.elementCount = readSmallUint;
        if (localElementWidth * readSmallUint > 2147483647L) {
            throw new ExceptionWithContext("Invalid array-payload instruction: element width*count overflows", new Object[0]);
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload
    public int getElementWidth() {
        return this.elementWidth;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload
    @Nonnull
    public List<Number> getArrayElements() {
        final int elementsStart = this.instructionStart + 8;
        if (this.elementCount == 0) {
            return ImmutableList.of();
        }
        switch (this.elementWidth) {
            case 1:
                return new C1ReturnedList() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedArrayPayload.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readByte(elementsStart + index));
                    }
                };
            case 2:
                return new C1ReturnedList() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedArrayPayload.2
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readShort(elementsStart + (index * 2)));
                    }
                };
            case 4:
                return new C1ReturnedList() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedArrayPayload.3
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readInt(elementsStart + (index * 4)));
                    }
                };
            case 8:
                return new C1ReturnedList() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedArrayPayload.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Long.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readLong(elementsStart + (index * 8)));
                    }
                };
            default:
                throw new ExceptionWithContext("Invalid element width: %d", Integer.valueOf(this.elementWidth));
        }
    }

    /* renamed from: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedArrayPayload$1ReturnedList, reason: invalid class name */
    /* loaded from: classes.dex */
    abstract class C1ReturnedList extends FixedSizeList<Number> {
        C1ReturnedList() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return DexBackedArrayPayload.this.elementCount;
        }
    }

    @Override // com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (((this.elementWidth * this.elementCount) + 1) / 2) + 4;
    }
}
