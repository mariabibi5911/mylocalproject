package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseFieldReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableField extends BaseFieldReference implements Field {
    private static final ImmutableConverter<ImmutableField, Field> CONVERTER = new ImmutableConverter<ImmutableField, Field>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableField.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull Field item) {
            return item instanceof ImmutableField;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableField makeImmutable(@Nonnull Field item) {
            return ImmutableField.of(item);
        }
    };
    protected final int accessFlags;

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotation> annotations;

    @Nonnull
    protected final String definingClass;

    @Nonnull
    protected final ImmutableSet<HiddenApiRestriction> hiddenApiRestrictions;

    @Nullable
    protected final ImmutableEncodedValue initialValue;

    @Nonnull
    protected final String name;

    @Nonnull
    protected final String type;

    public ImmutableField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable EncodedValue initialValue, @Nullable Collection<? extends Annotation> annotations, @Nullable Set<HiddenApiRestriction> hiddenApiRestrictions) {
        this.definingClass = definingClass;
        this.name = name;
        this.type = type;
        this.accessFlags = accessFlags;
        this.initialValue = ImmutableEncodedValueFactory.ofNullable(initialValue);
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.hiddenApiRestrictions = hiddenApiRestrictions == null ? ImmutableSet.of() : ImmutableSet.copyOf((Collection) hiddenApiRestrictions);
    }

    public ImmutableField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable ImmutableEncodedValue initialValue, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable ImmutableSet<HiddenApiRestriction> hiddenApiRestrictions) {
        this.definingClass = definingClass;
        this.name = name;
        this.type = type;
        this.accessFlags = accessFlags;
        this.initialValue = initialValue;
        this.annotations = ImmutableUtils.nullToEmptySet(annotations);
        this.hiddenApiRestrictions = ImmutableUtils.nullToEmptySet(hiddenApiRestrictions);
    }

    public static ImmutableField of(Field field) {
        if (field instanceof ImmutableField) {
            return (ImmutableField) field;
        }
        return new ImmutableField(field.getDefiningClass(), field.getName(), field.getType(), field.getAccessFlags(), field.getInitialValue(), field.getAnnotations(), field.getHiddenApiRestrictions());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference, com.android.tools.smali.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field
    public EncodedValue getInitialValue() {
        return this.initialValue;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Field, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.hiddenApiRestrictions;
    }

    @Nonnull
    public static ImmutableSortedSet<ImmutableField> immutableSetOf(@Nullable Iterable<? extends Field> list) {
        return CONVERTER.toSortedSet(Ordering.natural(), list);
    }
}
