package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class FieldReferenceRewriter implements Rewriter<FieldReference> {

    @Nonnull
    protected final Rewriters rewriters;

    public FieldReferenceRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public FieldReference rewrite(@Nonnull FieldReference fieldReference) {
        return new RewrittenFieldReference(fieldReference);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenFieldReference extends BaseFieldReference {

        @Nonnull
        protected FieldReference fieldReference;

        public RewrittenFieldReference(@Nonnull FieldReference fieldReference) {
            this.fieldReference = fieldReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getDefiningClass());
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.fieldReference.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
        @Nonnull
        public String getType() {
            return FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getType());
        }
    }
}
