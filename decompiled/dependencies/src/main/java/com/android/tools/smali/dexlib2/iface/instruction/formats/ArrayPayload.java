package com.android.tools.smali.dexlib2.iface.instruction.formats;

import com.android.tools.smali.dexlib2.iface.instruction.PayloadInstruction;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface ArrayPayload extends PayloadInstruction {
    @Nonnull
    List<Number> getArrayElements();

    int getElementWidth();
}
