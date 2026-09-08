package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderSwitchPayload;
import com.android.tools.smali.dexlib2.builder.Label;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderPackedSwitchPayload extends BuilderSwitchPayload implements PackedSwitchPayload {
    public static final Opcode OPCODE = Opcode.PACKED_SWITCH_PAYLOAD;

    @Nonnull
    protected final List<BuilderSwitchElement> switchElements;

    public BuilderPackedSwitchPayload(int startKey, @Nullable List<? extends Label> switchElements) {
        super(OPCODE);
        if (switchElements == null) {
            this.switchElements = ImmutableList.of();
            return;
        }
        this.switchElements = Lists.newArrayList();
        int key = startKey;
        for (Label target : switchElements) {
            this.switchElements.add(new BuilderSwitchElement(this, key, target));
            key++;
        }
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderSwitchPayload, com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<BuilderSwitchElement> getSwitchElements() {
        return this.switchElements;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (this.switchElements.size() * 2) + 4;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
