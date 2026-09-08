package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface MethodImplementation {
    @Nonnull
    Iterable<? extends DebugItem> getDebugItems();

    @Nonnull
    Iterable<? extends Instruction> getInstructions();

    int getRegisterCount();

    @Nonnull
    List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks();
}
