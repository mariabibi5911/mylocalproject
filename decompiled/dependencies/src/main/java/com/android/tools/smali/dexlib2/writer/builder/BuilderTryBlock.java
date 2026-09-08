package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.BaseTryBlock;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderTryBlock extends BaseTryBlock<BuilderExceptionHandler> {
    private final int codeUnitCount;

    @Nonnull
    private final List<? extends BuilderExceptionHandler> exceptionHandlers;
    private final int startCodeAddress;

    public BuilderTryBlock(int startCodeAddress, int codeUnitCount, @Nonnull List<? extends BuilderExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = exceptionHandlers;
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getStartCodeAddress() {
        return this.startCodeAddress;
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getCodeUnitCount() {
        return this.codeUnitCount;
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    @Nonnull
    public List<? extends BuilderExceptionHandler> getExceptionHandlers() {
        return this.exceptionHandlers;
    }
}
