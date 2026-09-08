package com.android.tools.smali.dexlib2.writer.builder;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.base.value.BaseAnnotationEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseArrayEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseBooleanEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseEnumEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseFieldEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseNullEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseStringEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseTypeEncodedValue;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableByteEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableCharEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableIntEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableLongEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableShortEncodedValue;
import com.android.tools.smali.util.ExceptionWithContext;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BuilderEncodedValues {

    /* loaded from: classes.dex */
    public interface BuilderEncodedValue extends EncodedValue {
    }

    /* loaded from: classes.dex */
    public static class BuilderAnnotationEncodedValue extends BaseAnnotationEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final Set<? extends BuilderAnnotationElement> elements;

        @Nonnull
        final BuilderTypeReference typeReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderAnnotationEncodedValue(@Nonnull BuilderTypeReference typeReference, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
            this.typeReference = typeReference;
            this.elements = elements;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
        @Nonnull
        public String getType() {
            return this.typeReference.getType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue, com.android.tools.smali.dexlib2.iface.BasicAnnotation
        @Nonnull
        public Set<? extends BuilderAnnotationElement> getElements() {
            return this.elements;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderArrayEncodedValue extends BaseArrayEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final List<? extends BuilderEncodedValue> elements;
        int offset = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderArrayEncodedValue(@Nonnull List<? extends BuilderEncodedValue> elements) {
            this.elements = elements;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue
        @Nonnull
        public List<? extends EncodedValue> getValue() {
            return this.elements;
        }
    }

    @Nonnull
    public static BuilderEncodedValue defaultValueForType(String type) {
        switch (type.charAt(0)) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                return new BuilderByteEncodedValue((byte) 0);
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                return new BuilderCharEncodedValue((char) 0);
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                return new BuilderDoubleEncodedValue(0.0d);
            case 'F':
                return new BuilderFloatEncodedValue(0.0f);
            case 'I':
                return new BuilderIntEncodedValue(0);
            case 'J':
                return new BuilderLongEncodedValue(0L);
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return BuilderNullEncodedValue.INSTANCE;
            case 'S':
                return new BuilderShortEncodedValue((short) 0);
            case 'Z':
                return BuilderBooleanEncodedValue.FALSE_VALUE;
            default:
                throw new ExceptionWithContext("Unrecognized type: %s", type);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderBooleanEncodedValue extends BaseBooleanEncodedValue implements BuilderEncodedValue {
        private final boolean value;
        public static final BuilderBooleanEncodedValue TRUE_VALUE = new BuilderBooleanEncodedValue(true);
        public static final BuilderBooleanEncodedValue FALSE_VALUE = new BuilderBooleanEncodedValue(false);

        private BuilderBooleanEncodedValue(boolean value) {
            this.value = value;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue
        public boolean getValue() {
            return this.value;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderByteEncodedValue extends ImmutableByteEncodedValue implements BuilderEncodedValue {
        public BuilderByteEncodedValue(byte value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderCharEncodedValue extends ImmutableCharEncodedValue implements BuilderEncodedValue {
        public BuilderCharEncodedValue(char value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderDoubleEncodedValue extends ImmutableDoubleEncodedValue implements BuilderEncodedValue {
        public BuilderDoubleEncodedValue(double value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderEnumEncodedValue extends BaseEnumEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderFieldReference enumReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderEnumEncodedValue(@Nonnull BuilderFieldReference enumReference) {
            this.enumReference = enumReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue
        @Nonnull
        public BuilderFieldReference getValue() {
            return this.enumReference;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderFieldEncodedValue extends BaseFieldEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderFieldReference fieldReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderFieldEncodedValue(@Nonnull BuilderFieldReference fieldReference) {
            this.fieldReference = fieldReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue
        @Nonnull
        public BuilderFieldReference getValue() {
            return this.fieldReference;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderFloatEncodedValue extends ImmutableFloatEncodedValue implements BuilderEncodedValue {
        public BuilderFloatEncodedValue(float value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderIntEncodedValue extends ImmutableIntEncodedValue implements BuilderEncodedValue {
        public BuilderIntEncodedValue(int value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderLongEncodedValue extends ImmutableLongEncodedValue implements BuilderEncodedValue {
        public BuilderLongEncodedValue(long value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderMethodEncodedValue extends BaseMethodEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderMethodReference methodReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderMethodEncodedValue(@Nonnull BuilderMethodReference methodReference) {
            this.methodReference = methodReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue
        public BuilderMethodReference getValue() {
            return this.methodReference;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderNullEncodedValue extends BaseNullEncodedValue implements BuilderEncodedValue {
        public static final BuilderNullEncodedValue INSTANCE = new BuilderNullEncodedValue();

        private BuilderNullEncodedValue() {
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderShortEncodedValue extends ImmutableShortEncodedValue implements BuilderEncodedValue {
        public BuilderShortEncodedValue(short value) {
            super(value);
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderStringEncodedValue extends BaseStringEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderStringReference stringReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderStringEncodedValue(@Nonnull BuilderStringReference stringReference) {
            this.stringReference = stringReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.StringEncodedValue
        @Nonnull
        public String getValue() {
            return this.stringReference.getString();
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderTypeEncodedValue extends BaseTypeEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderTypeReference typeReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderTypeEncodedValue(@Nonnull BuilderTypeReference typeReference) {
            this.typeReference = typeReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue
        @Nonnull
        public String getValue() {
            return this.typeReference.getType();
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderMethodTypeEncodedValue extends BaseMethodTypeEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderMethodProtoReference methodProtoReference;

        public BuilderMethodTypeEncodedValue(@Nonnull BuilderMethodProtoReference methodProtoReference) {
            this.methodProtoReference = methodProtoReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
        @Nonnull
        public BuilderMethodProtoReference getValue() {
            return this.methodProtoReference;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderMethodHandleEncodedValue extends BaseMethodHandleEncodedValue implements BuilderEncodedValue {

        @Nonnull
        final BuilderMethodHandleReference methodHandleReference;

        public BuilderMethodHandleEncodedValue(@Nonnull BuilderMethodHandleReference methodHandleReference) {
            this.methodHandleReference = methodHandleReference;
        }

        @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
        @Nonnull
        public BuilderMethodHandleReference getValue() {
            return this.methodHandleReference;
        }
    }
}
