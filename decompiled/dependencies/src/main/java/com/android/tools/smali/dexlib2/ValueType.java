package com.android.tools.smali.dexlib2;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes.dex */
public final class ValueType {
    public static final int ANNOTATION = 29;
    public static final int ARRAY = 28;
    public static final int BOOLEAN = 31;
    public static final int BYTE = 0;
    public static final int CHAR = 3;
    public static final int DOUBLE = 17;
    public static final int ENUM = 27;
    public static final int FIELD = 25;
    public static final int FLOAT = 16;
    public static final int INT = 4;
    public static final int LONG = 6;
    public static final int METHOD = 26;
    public static final int METHOD_HANDLE = 22;
    public static final int METHOD_TYPE = 21;
    public static final int NULL = 30;
    public static final int SHORT = 2;
    public static final int STRING = 23;
    public static final int TYPE = 24;

    private ValueType() {
    }

    public static String getValueTypeName(int valueType) {
        switch (valueType) {
            case 0:
                return "byte";
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
                throw new IllegalArgumentException("Unknown encoded value type: " + valueType);
            case 2:
                return "short";
            case 3:
                return "char";
            case 4:
                return "int";
            case 6:
                return "long";
            case 16:
                return TypedValues.Custom.S_FLOAT;
            case 17:
                return "double";
            case 21:
                return "method_type";
            case 22:
                return "method_handle";
            case 23:
                return TypedValues.Custom.S_STRING;
            case 24:
                return "type";
            case 25:
                return "field";
            case 26:
                return "method";
            case 27:
                return "enum";
            case 28:
                return "array";
            case 29:
                return "annotation";
            case 30:
                return "null";
            case 31:
                return TypedValues.Custom.S_BOOLEAN;
        }
    }
}
