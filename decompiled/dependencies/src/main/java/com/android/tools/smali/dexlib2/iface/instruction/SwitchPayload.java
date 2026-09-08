package com.android.tools.smali.dexlib2.iface.instruction;

import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface SwitchPayload extends PayloadInstruction {
    @Nonnull
    List<? extends SwitchElement> getSwitchElements();
}
