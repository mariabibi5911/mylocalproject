package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderField extends BaseFieldReference implements Field {
    final int accessFlags;

    @Nonnull
    final BuilderAnnotationSet annotations;

    @Nonnull
    final BuilderFieldReference fieldReference;

    @Nonnull
    Set<HiddenApiRestriction> hiddenApiRestrictions;

    @Nullable
    final BuilderEncodedValues.BuilderEncodedValue initialValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderField(@Nonnull BuilderFieldReference fieldReference, int accessFlags, @Nullable BuilderEncodedValues.BuilderEncodedValue initialValue, @Nonnull BuilderAnnotationSet annotations, @Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions) {
        this.fieldReference = fieldReference;
        this.accessFlags = accessFlags;
        this.initialValue = initialValue;
        this.annotations = annotations;
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field
    @Nullable
    public BuilderEncodedValues.BuilderEncodedValue getInitialValue() {
        return this.initialValue;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.fieldReference.definingClass.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.fieldReference.name.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.fieldReference.fieldType.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.hiddenApiRestrictions;
    }
}
