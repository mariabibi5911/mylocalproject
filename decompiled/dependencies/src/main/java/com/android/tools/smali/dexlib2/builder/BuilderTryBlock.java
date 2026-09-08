package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.base.BaseTryBlock;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderTryBlock extends BaseTryBlock<BuilderExceptionHandler> {

    @Nonnull
    public final Label end;

    @Nonnull
    public final BuilderExceptionHandler exceptionHandler;

    @Nonnull
    public final Label start;

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nullable String exceptionType, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(exceptionType, handler);
    }

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nullable TypeReference exceptionType, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(exceptionType, handler);
    }

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(handler);
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getStartCodeAddress() {
        return this.start.getCodeAddress();
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public int getCodeUnitCount() {
        return this.end.getCodeAddress() - this.start.getCodeAddress();
    }

    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    @Nonnull
    public List<? extends BuilderExceptionHandler> getExceptionHandlers() {
        return ImmutableList.of(this.exceptionHandler);
    }
}
