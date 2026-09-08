package com.android.tools.smali.dexlib2.iface.instruction;

import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface DualReferenceInstruction extends ReferenceInstruction {
    @Nonnull
    Reference getReference2();

    int getReferenceType2();
}
