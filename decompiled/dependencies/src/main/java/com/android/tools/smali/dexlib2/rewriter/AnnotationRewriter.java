package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.BaseAnnotation;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class AnnotationRewriter implements Rewriter<Annotation> {

    @Nonnull
    protected final Rewriters rewriters;

    public AnnotationRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public Annotation rewrite(@Nonnull Annotation value) {
        return new RewrittenAnnotation(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenAnnotation extends BaseAnnotation {

        @Nonnull
        protected Annotation annotation;

        public RewrittenAnnotation(@Nonnull Annotation annotation) {
            this.annotation = annotation;
        }

        @Override // com.android.tools.smali.dexlib2.iface.Annotation
        public int getVisibility() {
            return this.annotation.getVisibility();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
        @Nonnull
        public String getType() {
            return AnnotationRewriter.this.rewriters.getTypeRewriter().rewrite(this.annotation.getType());
        }

        @Override // com.android.tools.smali.dexlib2.iface.Annotation, com.android.tools.smali.dexlib2.iface.BasicAnnotation
        @Nonnull
        public Set<? extends AnnotationElement> getElements() {
            return RewriterUtils.rewriteSet(AnnotationRewriter.this.rewriters.getAnnotationElementRewriter(), this.annotation.getElements());
        }
    }
}
