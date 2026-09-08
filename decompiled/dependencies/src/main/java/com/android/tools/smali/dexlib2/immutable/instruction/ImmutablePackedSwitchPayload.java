package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.util.Preconditions;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutablePackedSwitchPayload extends ImmutableInstruction implements PackedSwitchPayload {
    public static final Opcode OPCODE = Opcode.PACKED_SWITCH_PAYLOAD;

    @Nonnull
    protected final ImmutableList<? extends ImmutableSwitchElement> switchElements;

    public ImmutablePackedSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
        super(OPCODE);
        this.switchElements = (ImmutableList) Preconditions.checkSequentialOrderedKeys(ImmutableSwitchElement.immutableListOf(switchElements));
    }

    public ImmutablePackedSwitchPayload(@Nullable ImmutableList<? extends ImmutableSwitchElement> switchElements) {
        super(OPCODE);
        this.switchElements = (ImmutableList) Preconditions.checkSequentialOrderedKeys(ImmutableUtils.nullToEmptyList(switchElements));
    }

    @Nonnull
    public static ImmutablePackedSwitchPayload of(PackedSwitchPayload instruction) {
        if (instruction instanceof ImmutablePackedSwitchPayload) {
            return (ImmutablePackedSwitchPayload) instruction;
        }
        return new ImmutablePackedSwitchPayload(instruction.getSwitchElements());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload, com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<? extends SwitchElement> getSwitchElements() {
        return this.switchElements;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (this.switchElements.size() * 2) + 4;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
