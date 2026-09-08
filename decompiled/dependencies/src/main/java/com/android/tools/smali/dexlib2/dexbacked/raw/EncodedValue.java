package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.ValueType;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.value.DexBackedEncodedValue;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class EncodedValue {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static void annotateEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
        int valueArgType = reader.readUbyte();
        int valueArg = valueArgType >>> 5;
        int valueType = valueArgType & 31;
        switch (valueType) {
            case 0:
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
                out.annotate(1, "valueArg = %d, valueType = 0x%x: %s", Integer.valueOf(valueArg), Integer.valueOf(valueType), ValueType.getValueTypeName(valueType));
                reader.setOffset(reader.getOffset() - 1);
                out.annotate(valueArg + 1, "value = %s", asString(dexFile, reader));
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
                throw new IllegalArgumentException(String.format("Invalid encoded value type 0x%x at offset 0x%x", Integer.valueOf(valueType), Integer.valueOf(reader.getOffset())));
            case 28:
                out.annotate(1, "valueArg = %d, valueType = 0x%x: array", Integer.valueOf(valueArg), Integer.valueOf(valueType));
                annotateEncodedArray(dexFile, out, reader);
                return;
            case 29:
                out.annotate(1, "valueArg = %d, valueType = 0x%x: annotation", Integer.valueOf(valueArg), Integer.valueOf(valueType));
                annotateEncodedAnnotation(dexFile, out, reader);
                return;
            case 30:
                out.annotate(1, "valueArg = %d, valueType = 0x%x: null", Integer.valueOf(valueArg), Integer.valueOf(valueType));
                return;
            case 31:
                Object[] objArr = new Object[3];
                objArr[0] = Integer.valueOf(valueArg);
                objArr[1] = Integer.valueOf(valueType);
                objArr[2] = Boolean.valueOf(valueArg == 1);
                out.annotate(1, "valueArg = %d, valueType = 0x%x: boolean, value=%s", objArr);
                return;
        }
    }

    public static void annotateEncodedAnnotation(@Nonnull DexBackedDexFile dexFile, @Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
        if (out.getCursor() != reader.getOffset()) {
            throw new AssertionError();
        }
        int typeIndex = reader.readSmallUleb128();
        out.annotateTo(reader.getOffset(), TypeIdItem.getReferenceAnnotation(dexFile, typeIndex), new Object[0]);
        int size = reader.readSmallUleb128();
        out.annotateTo(reader.getOffset(), "size: %d", Integer.valueOf(size));
        for (int i = 0; i < size; i++) {
            out.annotate(0, "element[%d]", Integer.valueOf(i));
            out.indent();
            int nameIndex = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "name = %s", StringIdItem.getReferenceAnnotation(dexFile, nameIndex));
            annotateEncodedValue(dexFile, out, reader);
            out.deindent();
        }
    }

    public static void annotateEncodedArray(@Nonnull DexBackedDexFile dexFile, @Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
        if (out.getCursor() != reader.getOffset()) {
            throw new AssertionError();
        }
        int size = reader.readSmallUleb128();
        out.annotateTo(reader.getOffset(), "size: %d", Integer.valueOf(size));
        for (int i = 0; i < size; i++) {
            out.annotate(0, "element[%d]", Integer.valueOf(i));
            out.indent();
            annotateEncodedValue(dexFile, out, reader);
            out.deindent();
        }
    }

    public static String asString(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        int valueArgType = reader.readUbyte();
        int valueArg = valueArgType >>> 5;
        int valueType = valueArgType & 31;
        switch (valueType) {
            case 0:
                int intValue = reader.readByte();
                return String.format("0x%x", Integer.valueOf(intValue));
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
                throw new IllegalArgumentException(String.format("Invalid encoded value type 0x%x at offset 0x%x", Integer.valueOf(valueType), Integer.valueOf(reader.getOffset())));
            case 2:
                int intValue2 = valueArg + 1;
                return String.format("0x%x", Integer.valueOf(reader.readSizedInt(intValue2)));
            case 3:
                int intValue3 = valueArg + 1;
                return String.format("0x%x", Integer.valueOf(reader.readSizedSmallUint(intValue3)));
            case 4:
                int intValue4 = reader.readSizedInt(valueArg + 1);
                return String.format("0x%x", Integer.valueOf(intValue4));
            case 6:
                long longValue = reader.readSizedLong(valueArg + 1);
                return String.format("0x%x", Long.valueOf(longValue));
            case 16:
                float floatValue = Float.intBitsToFloat(reader.readSizedRightExtendedInt(valueArg + 1));
                return String.format("%f", Float.valueOf(floatValue));
            case 17:
                double doubleValue = Double.longBitsToDouble(reader.readSizedRightExtendedLong(valueArg + 1));
                return String.format("%f", Double.valueOf(doubleValue));
            case 21:
                int protoIndex = reader.readSizedSmallUint(valueArg + 1);
                return ProtoIdItem.getReferenceAnnotation(dexFile, protoIndex);
            case 22:
            case 28:
            case 29:
                int stringIndex = reader.getOffset();
                reader.setOffset(stringIndex - 1);
                return DexBackedEncodedValue.readFrom(dexFile, reader).toString();
            case 23:
                int typeIndex = valueArg + 1;
                int stringIndex2 = reader.readSizedSmallUint(typeIndex);
                return StringIdItem.getReferenceAnnotation(dexFile, stringIndex2, true);
            case 24:
                int fieldIndex = valueArg + 1;
                int typeIndex2 = reader.readSizedSmallUint(fieldIndex);
                return TypeIdItem.getReferenceAnnotation(dexFile, typeIndex2);
            case 25:
                int methodIndex = valueArg + 1;
                int fieldIndex2 = reader.readSizedSmallUint(methodIndex);
                return FieldIdItem.getReferenceAnnotation(dexFile, fieldIndex2);
            case 26:
                int fieldIndex3 = valueArg + 1;
                int methodIndex2 = reader.readSizedSmallUint(fieldIndex3);
                return MethodIdItem.getReferenceAnnotation(dexFile, methodIndex2);
            case 27:
                int fieldIndex4 = reader.readSizedSmallUint(valueArg + 1);
                return FieldIdItem.getReferenceAnnotation(dexFile, fieldIndex4);
            case 30:
                return "null";
            case 31:
                return Boolean.toString(valueArg == 1);
        }
    }
}
