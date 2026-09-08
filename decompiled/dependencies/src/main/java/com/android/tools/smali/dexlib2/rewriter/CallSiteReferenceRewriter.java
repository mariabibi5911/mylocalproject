package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.reference.BaseCallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CallSiteReferenceRewriter implements Rewriter<CallSiteReference> {

    @Nonnull
    protected final Rewriters rewriters;

    public CallSiteReferenceRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public CallSiteReference rewrite(@Nonnull CallSiteReference callSiteReference) {
        return new RewrittenCallSiteReference(callSiteReference);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenCallSiteReference extends BaseCallSiteReference {

        @Nonnull
        protected CallSiteReference callSiteReference;

        public RewrittenCallSiteReference(@Nonnull CallSiteReference callSiteReference) {
            this.callSiteReference = callSiteReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
        @Nonnull
        public String getName() {
            return this.callSiteReference.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
        @Nonnull
        public MethodHandleReference getMethodHandle() {
            return RewriterUtils.rewriteMethodHandleReference(CallSiteReferenceRewriter.this.rewriters, this.callSiteReference.getMethodHandle());
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
        @Nonnull
        public String getMethodName() {
            return this.callSiteReference.getMethodName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
        @Nonnull
        public MethodProtoReference getMethodProto() {
            return RewriterUtils.rewriteMethodProtoReference(CallSiteReferenceRewriter.this.rewriters.getTypeRewriter(), this.callSiteReference.getMethodProto());
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
        @Nonnull
        public List<? extends EncodedValue> getExtraArguments() {
            return Lists.transform(this.callSiteReference.getExtraArguments(), new Function<EncodedValue, EncodedValue>() { // from class: com.android.tools.smali.dexlib2.rewriter.CallSiteReferenceRewriter.RewrittenCallSiteReference.1
                @Override // com.google.common.base.Function
                @Nonnull
                public EncodedValue apply(EncodedValue encodedValue) {
                    return RewriterUtils.rewriteValue(CallSiteReferenceRewriter.this.rewriters, encodedValue);
                }
            });
        }
    }
}
