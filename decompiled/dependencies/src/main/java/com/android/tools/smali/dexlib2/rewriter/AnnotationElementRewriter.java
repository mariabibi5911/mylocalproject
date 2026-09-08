package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.BaseAnnotationElement;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class AnnotationElementRewriter implements Rewriter<AnnotationElement> {

    @Nonnull
    protected final Rewriters rewriters;

    public AnnotationElementRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public AnnotationElement rewrite(@Nonnull AnnotationElement annotationElement) {
        return new RewrittenAnnotationElement(annotationElement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenAnnotationElement extends BaseAnnotationElement {

        @Nonnull
        protected AnnotationElement annotationElement;

        public RewrittenAnnotationElement(@Nonnull AnnotationElement annotationElement) {
            this.annotationElement = annotationElement;
        }

        @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
        @Nonnull
        public String getName() {
            return this.annotationElement.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
        @Nonnull
        public EncodedValue getValue() {
            return AnnotationElementRewriter.this.rewriters.getEncodedValueRewriter().rewrite(this.annotationElement.getValue());
        }
    }
}
