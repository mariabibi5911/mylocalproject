package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class FieldRewriter implements Rewriter<Field> {

    @Nonnull
    protected final Rewriters rewriters;

    public FieldRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public Field rewrite(@Nonnull Field field) {
        return new RewrittenField(field);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenField extends BaseFieldReference implements Field {

        @Nonnull
        protected Field field;

        public RewrittenField(@Nonnull Field field) {
            this.field = field;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field).getDefiningClass();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field).getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
        @Nonnull
        public String getType() {
            return FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field).getType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        public int getAccessFlags() {
            return this.field.getAccessFlags();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Field
        @Nullable
        public EncodedValue getInitialValue() {
            return (EncodedValue) RewriterUtils.rewriteNullable(FieldRewriter.this.rewriters.getEncodedValueRewriter(), this.field.getInitialValue());
        }

        @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Annotatable
        @Nonnull
        public Set<? extends Annotation> getAnnotations() {
            return RewriterUtils.rewriteSet(FieldRewriter.this.rewriters.getAnnotationRewriter(), this.field.getAnnotations());
        }

        @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
            return this.field.getHiddenApiRestrictions();
        }
    }
}
