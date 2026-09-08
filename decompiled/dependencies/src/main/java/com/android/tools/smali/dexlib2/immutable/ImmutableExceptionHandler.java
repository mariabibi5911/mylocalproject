package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.BaseExceptionHandler;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableExceptionHandler extends BaseExceptionHandler implements ExceptionHandler {
    private static final ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler> CONVERTER = new ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableExceptionHandler.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull ExceptionHandler item) {
            return item instanceof ImmutableExceptionHandler;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableExceptionHandler makeImmutable(@Nonnull ExceptionHandler item) {
            return ImmutableExceptionHandler.of(item);
        }
    };

    @Nullable
    protected final String exceptionType;
    protected final int handlerCodeAddress;

    public ImmutableExceptionHandler(@Nullable String exceptionType, int handlerCodeAddress) {
        this.exceptionType = exceptionType;
        this.handlerCodeAddress = handlerCodeAddress;
    }

    public static ImmutableExceptionHandler of(ExceptionHandler exceptionHandler) {
        if (exceptionHandler instanceof ImmutableExceptionHandler) {
            return (ImmutableExceptionHandler) exceptionHandler;
        }
        return new ImmutableExceptionHandler(exceptionHandler.getExceptionType(), exceptionHandler.getHandlerCodeAddress());
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        return this.exceptionType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }

    @Nonnull
    public static ImmutableList<ImmutableExceptionHandler> immutableListOf(@Nullable Iterable<? extends ExceptionHandler> list) {
        return CONVERTER.toList(list);
    }
}
