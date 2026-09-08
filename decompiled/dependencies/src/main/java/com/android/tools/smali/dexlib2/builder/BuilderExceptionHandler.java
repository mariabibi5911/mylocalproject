package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.base.BaseExceptionHandler;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BuilderExceptionHandler extends BaseExceptionHandler {

    @Nonnull
    protected final Label handler;

    private BuilderExceptionHandler(@Nonnull Label handler) {
        this.handler = handler;
    }

    @Nonnull
    public Label getHandler() {
        return this.handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nullable final TypeReference exceptionType, @Nonnull Label handler) {
        if (exceptionType == null) {
            return newExceptionHandler(handler);
        }
        return new BuilderExceptionHandler(handler) { // from class: com.android.tools.smali.dexlib2.builder.BuilderExceptionHandler.1
            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return exceptionType.getType();
            }

            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }

            @Override // com.android.tools.smali.dexlib2.base.BaseExceptionHandler, com.android.tools.smali.dexlib2.iface.ExceptionHandler
            @Nullable
            public TypeReference getExceptionTypeReference() {
                return exceptionType;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nonnull Label handler) {
        return new BuilderExceptionHandler(handler) { // from class: com.android.tools.smali.dexlib2.builder.BuilderExceptionHandler.2
            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return null;
            }

            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BuilderExceptionHandler newExceptionHandler(@Nullable final String exceptionType, @Nonnull Label handler) {
        if (exceptionType == null) {
            return newExceptionHandler(handler);
        }
        return new BuilderExceptionHandler(handler) { // from class: com.android.tools.smali.dexlib2.builder.BuilderExceptionHandler.3
            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            @Nullable
            public String getExceptionType() {
                return exceptionType;
            }

            @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
            public int getHandlerCodeAddress() {
                return this.handler.getCodeAddress();
            }
        };
    }
}
