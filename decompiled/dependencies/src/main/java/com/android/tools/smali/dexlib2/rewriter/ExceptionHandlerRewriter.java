package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.BaseExceptionHandler;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ExceptionHandlerRewriter implements Rewriter<ExceptionHandler> {

    @Nonnull
    protected final Rewriters rewriters;

    public ExceptionHandlerRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public ExceptionHandler rewrite(@Nonnull ExceptionHandler value) {
        return new RewrittenExceptionHandler(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenExceptionHandler extends BaseExceptionHandler {

        @Nonnull
        protected ExceptionHandler exceptionHandler;

        public RewrittenExceptionHandler(@Nonnull ExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
        }

        @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
        @Nullable
        public String getExceptionType() {
            return (String) RewriterUtils.rewriteNullable(ExceptionHandlerRewriter.this.rewriters.getTypeRewriter(), this.exceptionHandler.getExceptionType());
        }

        @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
        public int getHandlerCodeAddress() {
            return this.exceptionHandler.getHandlerCodeAddress();
        }
    }
}
