package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class MethodReferenceRewriter implements Rewriter<MethodReference> {

    @Nonnull
    protected final Rewriters rewriters;

    public MethodReferenceRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodReference rewrite(@Nonnull MethodReference methodReference) {
        return new RewrittenMethodReference(methodReference);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenMethodReference extends BaseMethodReference {

        @Nonnull
        protected MethodReference methodReference;

        public RewrittenMethodReference(@Nonnull MethodReference methodReference) {
            this.methodReference = methodReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getDefiningClass());
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.methodReference.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
        @Nonnull
        public List<? extends CharSequence> getParameterTypes() {
            return RewriterUtils.rewriteList(MethodReferenceRewriter.this.rewriters.getTypeRewriter(), Lists.transform(this.methodReference.getParameterTypes(), new Function<CharSequence, String>() { // from class: com.android.tools.smali.dexlib2.rewriter.MethodReferenceRewriter.RewrittenMethodReference.1
                @Override // com.google.common.base.Function
                @Nonnull
                public String apply(CharSequence input) {
                    return input.toString();
                }
            }));
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
        @Nonnull
        public String getReturnType() {
            return MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getReturnType());
        }
    }
}
