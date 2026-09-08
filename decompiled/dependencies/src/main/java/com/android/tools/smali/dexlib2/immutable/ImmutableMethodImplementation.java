package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableDebugItem;
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMethodImplementation implements MethodImplementation {

    @Nonnull
    protected final ImmutableList<? extends ImmutableDebugItem> debugItems;

    @Nonnull
    protected final ImmutableList<? extends ImmutableInstruction> instructions;
    protected final int registerCount;

    @Nonnull
    protected final ImmutableList<? extends ImmutableTryBlock> tryBlocks;

    public ImmutableMethodImplementation(int registerCount, @Nullable Iterable<? extends Instruction> instructions, @Nullable List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks, @Nullable Iterable<? extends DebugItem> debugItems) {
        this.registerCount = registerCount;
        this.instructions = ImmutableInstruction.immutableListOf(instructions);
        this.tryBlocks = ImmutableTryBlock.immutableListOf(tryBlocks);
        this.debugItems = ImmutableDebugItem.immutableListOf(debugItems);
    }

    public ImmutableMethodImplementation(int registerCount, @Nullable ImmutableList<? extends ImmutableInstruction> instructions, @Nullable ImmutableList<? extends ImmutableTryBlock> tryBlocks, @Nullable ImmutableList<? extends ImmutableDebugItem> debugItems) {
        this.registerCount = registerCount;
        this.instructions = ImmutableUtils.nullToEmptyList(instructions);
        this.tryBlocks = ImmutableUtils.nullToEmptyList(tryBlocks);
        this.debugItems = ImmutableUtils.nullToEmptyList(debugItems);
    }

    @Nullable
    public static ImmutableMethodImplementation of(@Nullable MethodImplementation methodImplementation) {
        if (methodImplementation == null) {
            return null;
        }
        if (methodImplementation instanceof ImmutableMethodImplementation) {
            return (ImmutableMethodImplementation) methodImplementation;
        }
        return new ImmutableMethodImplementation(methodImplementation.getRegisterCount(), methodImplementation.getInstructions(), methodImplementation.getTryBlocks(), methodImplementation.getDebugItems());
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public ImmutableList<? extends ImmutableInstruction> getInstructions() {
        return this.instructions;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public ImmutableList<? extends ImmutableTryBlock> getTryBlocks() {
        return this.tryBlocks;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
    @Nonnull
    public ImmutableList<? extends ImmutableDebugItem> getDebugItems() {
        return this.debugItems;
    }
}
