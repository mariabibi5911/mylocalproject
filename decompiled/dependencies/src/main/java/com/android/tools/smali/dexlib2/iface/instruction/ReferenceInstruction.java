package com.android.tools.smali.dexlib2.iface.instruction;

import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface ReferenceInstruction extends Instruction {
    @Nonnull
    Reference getReference();

    int getReferenceType();
}
