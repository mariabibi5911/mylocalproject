package com.android.tools.smali.dexlib2.builder.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.builder.BuilderSwitchPayload;
import com.android.tools.smali.dexlib2.builder.SwitchLabelElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderSparseSwitchPayload extends BuilderSwitchPayload implements SparseSwitchPayload {
    public static final Opcode OPCODE = Opcode.SPARSE_SWITCH_PAYLOAD;

    @Nonnull
    protected final List<BuilderSwitchElement> switchElements;

    public BuilderSparseSwitchPayload(@Nullable List<? extends SwitchLabelElement> switchElements) {
        super(OPCODE);
        if (switchElements == null) {
            this.switchElements = ImmutableList.of();
        } else {
            this.switchElements = Lists.transform(switchElements, new Function<SwitchLabelElement, BuilderSwitchElement>() { // from class: com.android.tools.smali.dexlib2.builder.instruction.BuilderSparseSwitchPayload.1
                static final /* synthetic */ boolean $assertionsDisabled = false;

                @Override // com.google.common.base.Function
                @Nullable
                public BuilderSwitchElement apply(@Nullable SwitchLabelElement element) {
                    if (element == null) {
                        throw new AssertionError();
                    }
                    return new BuilderSwitchElement(BuilderSparseSwitchPayload.this, element.key, element.target);
                }
            });
        }
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderSwitchPayload, com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<BuilderSwitchElement> getSwitchElements() {
        return this.switchElements;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction, com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return (this.switchElements.size() * 4) + 2;
    }

    @Override // com.android.tools.smali.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
