package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.BaseTryBlock;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class TryBlockRewriter implements Rewriter<TryBlock<? extends ExceptionHandler>> {

    @Nonnull
    protected final Rewriters rewriters;

    public TryBlockRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public TryBlock<? extends ExceptionHandler> rewrite(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
        return new RewrittenTryBlock(tryBlock);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenTryBlock extends BaseTryBlock<ExceptionHandler> {

        @Nonnull
        protected TryBlock<? extends ExceptionHandler> tryBlock;

        public RewrittenTryBlock(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
            this.tryBlock = tryBlock;
        }

        @Override // com.android.tools.smali.dexlib2.iface.TryBlock
        public int getStartCodeAddress() {
            return this.tryBlock.getStartCodeAddress();
        }

        @Override // com.android.tools.smali.dexlib2.iface.TryBlock
        public int getCodeUnitCount() {
            return this.tryBlock.getCodeUnitCount();
        }

        @Override // com.android.tools.smali.dexlib2.iface.TryBlock
        @Nonnull
        public List<? extends ExceptionHandler> getExceptionHandlers() {
            return RewriterUtils.rewriteList(TryBlockRewriter.this.rewriters.getExceptionHandlerRewriter(), this.tryBlock.getExceptionHandlers());
        }
    }
}
