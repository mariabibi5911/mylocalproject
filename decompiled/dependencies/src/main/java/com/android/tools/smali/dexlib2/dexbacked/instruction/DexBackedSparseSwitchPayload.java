package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedSparseSwitchPayload extends DexBackedInstruction implements SparseSwitchPayload {
    private static final int ELEMENT_COUNT_OFFSET = 2;
    private static final int KEYS_OFFSET = 4;
    public final int elementCount;

    public DexBackedSparseSwitchPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, Opcode.SPARSE_SWITCH_PAYLOAD, instructionStart);
        this.elementCount = dexFile.getDataBuffer().readUshort(instructionStart + 2);
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<? extends SwitchElement> getSwitchElements() {
        return new FixedSizeList<SwitchElement>() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedSparseSwitchPayload.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.tools.smali.dexlib2.dexbacked.util.FixedSizeList
            @Nonnull
            public SwitchElement readItem(final int index) {
                return new SwitchElement() { // from class: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedSparseSwitchPayload.1.1
                    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchElement
                    public int getKey() {
                        return DexBackedSparseSwitchPayload.this.dexFile.getDataBuffer().readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + (index * 4));
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchElement
                    public int getOffset() {
                        return DexBackedSparseSwitchPayload.this.dexFile.getDataBuffer().readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + (DexBackedSparseSwitchPayload.this.elementCount * 4) + (index * 4));
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedSparseSwitchPayload.this.elementCount;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (this.elementCount * 4) + 2;
    }
}
