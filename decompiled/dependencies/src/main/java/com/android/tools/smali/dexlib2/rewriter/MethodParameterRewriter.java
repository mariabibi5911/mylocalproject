package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.BaseMethodParameter;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MethodParameterRewriter implements Rewriter<MethodParameter> {

    @Nonnull
    protected final Rewriters rewriters;

    public MethodParameterRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodParameter rewrite(@Nonnull MethodParameter methodParameter) {
        return new RewrittenMethodParameter(methodParameter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenMethodParameter extends BaseMethodParameter {

        @Nonnull
        protected MethodParameter methodParameter;

        public RewrittenMethodParameter(@Nonnull MethodParameter methodParameter) {
            this.methodParameter = methodParameter;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
        @Nonnull
        public String getType() {
            return MethodParameterRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodParameter.getType());
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodParameter
        @Nonnull
        public Set<? extends Annotation> getAnnotations() {
            return RewriterUtils.rewriteSet(MethodParameterRewriter.this.rewriters.getAnnotationRewriter(), this.methodParameter.getAnnotations());
        }

        @Override // com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getName() {
            return this.methodParameter.getName();
        }

        @Override // com.android.tools.smali.dexlib2.base.BaseMethodParameter, com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getSignature() {
            return this.methodParameter.getSignature();
        }
    }
}
