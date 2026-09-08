package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableSparseSwitchPayload extends ImmutableInstruction implements SparseSwitchPayload {
    public static final Opcode OPCODE = Opcode.SPARSE_SWITCH_PAYLOAD;

    @Nonnull
    protected final ImmutableList<? extends ImmutableSwitchElement> switchElements;

    public ImmutableSparseSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
        super(OPCODE);
        this.switchElements = ImmutableSwitchElement.immutableListOf(switchElements);
    }

    public ImmutableSparseSwitchPayload(@Nullable ImmutableList<? extends ImmutableSwitchElement> switchElements) {
        super(OPCODE);
        this.switchElements = ImmutableUtils.nullToEmptyList(switchElements);
    }

    @Nonnull
    public static ImmutableSparseSwitchPayload of(SparseSwitchPayload instruction) {
        if (instruction instanceof ImmutableSparseSwitchPayload) {
            return (ImmutableSparseSwitchPayload) instruction;
        }
        return new ImmutableSparseSwitchPayload(instruction.getSwitchElements());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<? extends SwitchElement> getSwitchElements() {
        return this.switchElements;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (this.switchElements.size() * 4) + 2;
    }

    @Override // com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
