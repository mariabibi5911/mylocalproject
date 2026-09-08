package com.android.tools.smali.dexlib2.immutable.value;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.CharEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.LongEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue;
import com.android.tools.smali.util.ExceptionWithContext;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableEncodedValueFactory {
    private static final ImmutableConverter<ImmutableEncodedValue, EncodedValue> CONVERTER = new ImmutableConverter<ImmutableEncodedValue, EncodedValue>() { // from class: com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValueFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull EncodedValue item) {
            return item instanceof ImmutableEncodedValue;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableEncodedValue makeImmutable(@Nonnull EncodedValue item) {
            return ImmutableEncodedValueFactory.of(item);
        }
    };

    @Nonnull
    public static ImmutableEncodedValue of(@Nonnull EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 0:
                return ImmutableByteEncodedValue.of((ByteEncodedValue) encodedValue);
            case 1:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            case 20:
            default:
                Preconditions.checkArgument(false);
                return null;
            case 2:
                return ImmutableShortEncodedValue.of((ShortEncodedValue) encodedValue);
            case 3:
                return ImmutableCharEncodedValue.of((CharEncodedValue) encodedValue);
            case 4:
                return ImmutableIntEncodedValue.of((IntEncodedValue) encodedValue);
            case 6:
                return ImmutableLongEncodedValue.of((LongEncodedValue) encodedValue);
            case 16:
                return ImmutableFloatEncodedValue.of((FloatEncodedValue) encodedValue);
            case 17:
                return ImmutableDoubleEncodedValue.of((DoubleEncodedValue) encodedValue);
            case 21:
                return ImmutableMethodTypeEncodedValue.of((MethodTypeEncodedValue) encodedValue);
            case 22:
                return ImmutableMethodHandleEncodedValue.of((MethodHandleEncodedValue) encodedValue);
            case 23:
                return ImmutableStringEncodedValue.of((StringEncodedValue) encodedValue);
            case 24:
                return ImmutableTypeEncodedValue.of((TypeEncodedValue) encodedValue);
            case 25:
                return ImmutableFieldEncodedValue.of((FieldEncodedValue) encodedValue);
            case 26:
                return ImmutableMethodEncodedValue.of((MethodEncodedValue) encodedValue);
            case 27:
                return ImmutableEnumEncodedValue.of((EnumEncodedValue) encodedValue);
            case 28:
                return ImmutableArrayEncodedValue.of((ArrayEncodedValue) encodedValue);
            case 29:
                return ImmutableAnnotationEncodedValue.of((AnnotationEncodedValue) encodedValue);
            case 30:
                return ImmutableNullEncodedValue.INSTANCE;
            case 31:
                return ImmutableBooleanEncodedValue.of((BooleanEncodedValue) encodedValue);
        }
    }

    @Nonnull
    public static EncodedValue defaultValueForType(String type) {
        switch (type.charAt(0)) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                return new ImmutableByteEncodedValue((byte) 0);
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                return new ImmutableCharEncodedValue((char) 0);
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                return new ImmutableDoubleEncodedValue(0.0d);
            case 'F':
                return new ImmutableFloatEncodedValue(0.0f);
            case 'I':
                return new ImmutableIntEncodedValue(0);
            case 'J':
                return new ImmutableLongEncodedValue(0L);
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return ImmutableNullEncodedValue.INSTANCE;
            case 'S':
                return new ImmutableShortEncodedValue((short) 0);
            case 'Z':
                return ImmutableBooleanEncodedValue.FALSE_VALUE;
            default:
                throw new ExceptionWithContext("Unrecognized type: %s", type);
        }
    }

    @Nullable
    public static ImmutableEncodedValue ofNullable(@Nullable EncodedValue encodedValue) {
        if (encodedValue == null) {
            return null;
        }
        return of(encodedValue);
    }

    @Nonnull
    public static ImmutableList<ImmutableEncodedValue> immutableListOf(@Nullable Iterable<? extends EncodedValue> list) {
        return CONVERTER.toList(list);
    }
}
