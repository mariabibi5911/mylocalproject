package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.BaseTryBlock;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableTryBlock extends BaseTryBlock<ImmutableExceptionHandler> {
    private static final ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>> CONVERTER = new ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableTryBlock.1
        @Override // com.android.tools.smali.util.ImmutableConverter
        protected /* bridge */ /* synthetic */ boolean isImmutable(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
            return isImmutable2((TryBlock) tryBlock);
        }

        /* renamed from: isImmutable, reason: avoid collision after fix types in other method */
        protected boolean isImmutable2(@Nonnull TryBlock item) {
            return item instanceof ImmutableTryBlock;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableTryBlock makeImmutable(@Nonnull TryBlock<? extends ExceptionHandler> item) {
            return ImmutableTryBlock.of(item);
        }
    };
    protected final int codeUnitCount;

    @Nonnull
    protected final ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers;
    protected final int startCodeAddress;

    public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable List<? extends ExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = ImmutableExceptionHandler.immutableListOf(exceptionHandlers);
    }

    public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = ImmutableUtils.nullToEmptyList(exceptionHandlers);
    }

    public static ImmutableTryBlock of(TryBlock<? extends ExceptionHandler> tryBlock) {
        if (tryBlock instanceof ImmutableTryBlock) {
            return (ImmutableTryBlock) tryBlock;
        }
        return new ImmutableTryBlock(tryBlock.getStartCodeAddress(), tryBlock.getCodeUnitCount(), (List<? extends ExceptionHandler>) tryBlock.getExceptionHandlers());
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
    public ImmutableList<? extends ImmutableExceptionHandler> getExceptionHandlers() {
        return this.exceptionHandlers;
    }

    @Nonnull
    public static ImmutableList<ImmutableTryBlock> immutableListOf(@Nullable List<? extends TryBlock<? extends ExceptionHandler>> list) {
        return CONVERTER.toList(list);
    }
}
