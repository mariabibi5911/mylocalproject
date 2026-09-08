package com.android.tools.smali.dexlib2.iface.instruction.formats;

import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface PackedSwitchPayload extends SwitchPayload {
    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    List<? extends SwitchElement> getSwitchElements();
}
