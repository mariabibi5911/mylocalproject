package com.android.tools.smali.dexlib2.dexbacked.value;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableBooleanEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableByteEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableCharEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableIntEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableLongEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableNullEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableShortEncodedValue;
import com.android.tools.smali.dexlib2.util.Preconditions;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class DexBackedEncodedValue {
    @Nonnull
    public static EncodedValue readFrom(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        int startOffset = reader.getOffset();
        try {
            int b = reader.readUbyte();
            int valueType = b & 31;
            int valueArg = b >>> 5;
            switch (valueType) {
                case 0:
                    Preconditions.checkValueArg(valueArg, 0);
                    return new ImmutableByteEncodedValue((byte) reader.readByte());
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
                    throw new ExceptionWithContext("Invalid encoded_value type: 0x%x", Integer.valueOf(valueType));
                case 2:
                    Preconditions.checkValueArg(valueArg, 1);
                    return new ImmutableShortEncodedValue((short) reader.readSizedInt(valueArg + 1));
                case 3:
                    Preconditions.checkValueArg(valueArg, 1);
                    return new ImmutableCharEncodedValue((char) reader.readSizedSmallUint(valueArg + 1));
                case 4:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new ImmutableIntEncodedValue(reader.readSizedInt(valueArg + 1));
                case 6:
                    Preconditions.checkValueArg(valueArg, 7);
                    return new ImmutableLongEncodedValue(reader.readSizedLong(valueArg + 1));
                case 16:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new ImmutableFloatEncodedValue(Float.intBitsToFloat(reader.readSizedRightExtendedInt(valueArg + 1)));
                case 17:
                    Preconditions.checkValueArg(valueArg, 7);
                    return new ImmutableDoubleEncodedValue(Double.longBitsToDouble(reader.readSizedRightExtendedLong(valueArg + 1)));
                case 21:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedMethodTypeEncodedValue(dexFile, reader, valueArg);
                case 22:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedMethodHandleEncodedValue(dexFile, reader, valueArg);
                case 23:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedStringEncodedValue(dexFile, reader, valueArg);
                case 24:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedTypeEncodedValue(dexFile, reader, valueArg);
                case 25:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedFieldEncodedValue(dexFile, reader, valueArg);
                case 26:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedMethodEncodedValue(dexFile, reader, valueArg);
                case 27:
                    Preconditions.checkValueArg(valueArg, 3);
                    return new DexBackedEnumEncodedValue(dexFile, reader, valueArg);
                case 28:
                    Preconditions.checkValueArg(valueArg, 0);
                    return new DexBackedArrayEncodedValue(dexFile, reader);
                case 29:
                    Preconditions.checkValueArg(valueArg, 0);
                    return new DexBackedAnnotationEncodedValue(dexFile, reader);
                case 30:
                    Preconditions.checkValueArg(valueArg, 0);
                    return ImmutableNullEncodedValue.INSTANCE;
                case 31:
                    Preconditions.checkValueArg(valueArg, 1);
                    return ImmutableBooleanEncodedValue.forBoolean(valueArg == 1);
            }
        } catch (Exception ex) {
            throw ExceptionWithContext.withContext(ex, "Error while reading encoded value at offset 0x%x", Integer.valueOf(startOffset));
        }
    }

    public static void skipFrom(@Nonnull DexReader reader) {
        int startOffset = reader.getOffset();
        try {
            int b = reader.readUbyte();
            int valueType = b & 31;
            switch (valueType) {
                case 0:
                    reader.skipByte();
                    return;
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
                    throw new ExceptionWithContext("Invalid encoded_value type: 0x%x", Integer.valueOf(valueType));
                case 2:
                case 3:
                case 4:
                case 6:
                case 16:
                case 17:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                    int valueArg = b >>> 5;
                    reader.moveRelative(valueArg + 1);
                    return;
                case 28:
                    DexBackedArrayEncodedValue.skipFrom(reader);
                    return;
                case 29:
                    DexBackedAnnotationEncodedValue.skipFrom(reader);
                    return;
                case 30:
                case 31:
                    return;
            }
        } catch (Exception ex) {
            throw ExceptionWithContext.withContext(ex, "Error while skipping encoded value at offset 0x%x", Integer.valueOf(startOffset));
        }
    }
}
