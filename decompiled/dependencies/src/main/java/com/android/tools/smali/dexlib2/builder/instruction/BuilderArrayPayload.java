package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderArrayPayload extends BuilderInstruction implements ArrayPayload {
    public static final Opcode OPCODE = Opcode.ARRAY_PAYLOAD;

    @Nonnull
    protected final List<Number> arrayElements;
    protected final int elementWidth;

    public BuilderArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
        super(OPCODE);
        this.elementWidth = elementWidth;
        this.arrayElements = arrayElements == null ? ImmutableList.of() : arrayElements;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload
    public int getElementWidth() {
        return this.elementWidth;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload
    @Nonnull
    public List<Number> getArrayElements() {
        return this.arrayElements;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (((this.elementWidth * this.arrayElements.size()) + 1) / 2) + 4;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
