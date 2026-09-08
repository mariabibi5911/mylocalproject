package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class MethodImplementationRewriter implements Rewriter<MethodImplementation> {

    @Nonnull
    protected final Rewriters rewriters;

    public MethodImplementationRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodImplementation rewrite(@Nonnull MethodImplementation methodImplementation) {
        return new RewrittenMethodImplementation(methodImplementation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenMethodImplementation implements MethodImplementation {

        @Nonnull
        protected MethodImplementation methodImplementation;

        public RewrittenMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
            this.methodImplementation = methodImplementation;
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
        public int getRegisterCount() {
            return this.methodImplementation.getRegisterCount();
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
        @Nonnull
        public Iterable<? extends Instruction> getInstructions() {
            return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getInstructionRewriter(), this.methodImplementation.getInstructions());
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
        @Nonnull
        public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks() {
            return RewriterUtils.rewriteList(MethodImplementationRewriter.this.rewriters.getTryBlockRewriter(), this.methodImplementation.getTryBlocks());
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodImplementation
        @Nonnull
        public Iterable<? extends DebugItem> getDebugItems() {
            return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getDebugItemRewriter(), this.methodImplementation.getDebugItems());
        }
    }
}
