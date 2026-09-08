package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.util.Preconditions;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableArrayPayload extends ImmutableInstruction implements ArrayPayload {
    public static final Opcode OPCODE = Opcode.ARRAY_PAYLOAD;

    @Nonnull
    protected final ImmutableList<Number> arrayElements;
    protected final int elementWidth;

    public ImmutableArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
        super(OPCODE);
        this.elementWidth = Preconditions.checkArrayPayloadElementWidth(elementWidth);
        this.arrayElements = (ImmutableList) Preconditions.checkArrayPayloadElements(elementWidth, arrayElements == null ? ImmutableList.of() : ImmutableList.copyOf((Collection) arrayElements));
    }

    public ImmutableArrayPayload(int elementWidth, @Nullable ImmutableList<Number> arrayElements) {
        super(OPCODE);
        this.elementWidth = Preconditions.checkArrayPayloadElementWidth(elementWidth);
        this.arrayElements = (ImmutableList) Preconditions.checkArrayPayloadElements(elementWidth, ImmutableUtils.nullToEmptyList(arrayElements));
    }

    @Nonnull
    public static ImmutableArrayPayload of(ArrayPayload instruction) {
        if (instruction instanceof ImmutableArrayPayload) {
            return (ImmutableArrayPayload) instruction;
        }
        return new ImmutableArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
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

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (((this.elementWidth * this.arrayElements.size()) + 1) / 2) + 4;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
