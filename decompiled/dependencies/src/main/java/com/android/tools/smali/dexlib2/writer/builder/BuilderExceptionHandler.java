package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.BaseExceptionHandler;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderExceptionHandler extends BaseExceptionHandler {

    @Nullable
    final BuilderTypeReference exceptionType;
    final int handlerCodeAddress;

    BuilderExceptionHandler(@Nullable BuilderTypeReference exceptionType, int handlerCodeAddress) {
        this.exceptionType = exceptionType;
        this.handlerCodeAddress = handlerCodeAddress;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        BuilderTypeReference builderTypeReference = this.exceptionType;
        if (builderTypeReference == null) {
            return null;
        }
        return builderTypeReference.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
