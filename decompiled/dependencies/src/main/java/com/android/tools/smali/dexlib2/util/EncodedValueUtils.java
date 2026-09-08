package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.iface.AnnotationElement;
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
import com.android.tools.smali.util.StringUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/* loaded from: classes.dex */
public final class EncodedValueUtils {
    public static boolean isDefaultValue(EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 0:
                return ((ByteEncodedValue) encodedValue).getValue() == 0;
            case 2:
                return ((ShortEncodedValue) encodedValue).getValue() == 0;
            case 3:
                return ((CharEncodedValue) encodedValue).getValue() == 0;
            case 4:
                return ((IntEncodedValue) encodedValue).getValue() == 0;
            case 6:
                return ((LongEncodedValue) encodedValue).getValue() == 0;
            case 16:
                return ((FloatEncodedValue) encodedValue).getValue() == 0.0f;
            case 17:
                return ((DoubleEncodedValue) encodedValue).getValue() == 0.0d;
            case 30:
                return true;
            case 31:
                return !((BooleanEncodedValue) encodedValue).getValue();
            default:
                return false;
        }
    }

    @Deprecated
    public static void writeEncodedValue(Writer writer, EncodedValue encodedValue) throws IOException {
        switch (encodedValue.getValueType()) {
            case 0:
                writer.write(Byte.toString(((ByteEncodedValue) encodedValue).getValue()));
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
                throw new IllegalArgumentException("Unknown encoded value type");
            case 2:
                writer.write(Short.toString(((ShortEncodedValue) encodedValue).getValue()));
                return;
            case 3:
                writer.write(Integer.toString(((CharEncodedValue) encodedValue).getValue()));
                return;
            case 4:
                writer.write(Integer.toString(((IntEncodedValue) encodedValue).getValue()));
                return;
            case 6:
                writer.write(Long.toString(((LongEncodedValue) encodedValue).getValue()));
                return;
            case 16:
                writer.write(Float.toString(((FloatEncodedValue) encodedValue).getValue()));
                return;
            case 17:
                writer.write(Double.toString(((DoubleEncodedValue) encodedValue).getValue()));
                return;
            case 21:
                ReferenceUtil.writeMethodProtoDescriptor(writer, ((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                ReferenceUtil.writeMethodHandle(writer, ((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                writer.write(34);
                StringUtils.writeEscapedString(writer, ((StringEncodedValue) encodedValue).getValue());
                writer.write(34);
                return;
            case 24:
                writer.write(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                ReferenceUtil.writeFieldDescriptor(writer, ((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                ReferenceUtil.writeMethodDescriptor(writer, ((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                ReferenceUtil.writeFieldDescriptor(writer, ((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                writeArray(writer, (ArrayEncodedValue) encodedValue);
                return;
            case 29:
                writeAnnotation(writer, (AnnotationEncodedValue) encodedValue);
                return;
            case 30:
                writer.write("null");
                return;
            case 31:
                writer.write(Boolean.toString(((BooleanEncodedValue) encodedValue).getValue()));
                return;
        }
    }

    private static void writeAnnotation(Writer writer, AnnotationEncodedValue annotation) throws IOException {
        writer.write("Annotation[");
        writer.write(annotation.getType());
        Set<? extends AnnotationElement> elements = annotation.getElements();
        for (AnnotationElement element : elements) {
            writer.write(", ");
            writer.write(element.getName());
            writer.write(61);
            writeEncodedValue(writer, element.getValue());
        }
        writer.write(93);
    }

    private static void writeArray(Writer writer, ArrayEncodedValue array) throws IOException {
        writer.write("Array[");
        boolean first = true;
        for (EncodedValue element : array.getValue()) {
            if (first) {
                first = false;
            } else {
                writer.write(", ");
            }
            writeEncodedValue(writer, element);
        }
        writer.write(93);
    }

    private EncodedValueUtils() {
    }
}
