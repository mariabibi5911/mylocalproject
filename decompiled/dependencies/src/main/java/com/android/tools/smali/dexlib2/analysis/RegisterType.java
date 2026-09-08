package com.android.tools.smali.dexlib2.analysis;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.util.ExceptionWithContext;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class RegisterType {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final byte BOOLEAN = 4;
    public static final byte BYTE = 5;
    public static final byte CHAR = 9;
    public static final byte CONFLICTED = 19;
    public static final byte DOUBLE_HI = 15;
    public static final byte DOUBLE_LO = 14;
    public static final byte FLOAT = 11;
    public static final byte INTEGER = 10;
    public static final byte LONG_HI = 13;
    public static final byte LONG_LO = 12;
    public static final byte NULL = 2;
    public static final byte ONE = 3;
    public static final byte POS_BYTE = 6;
    public static final byte POS_SHORT = 8;
    public static final byte REFERENCE = 18;
    public static final byte SHORT = 7;
    public static final byte UNINIT = 1;
    public static final byte UNINIT_REF = 16;
    public static final byte UNINIT_THIS = 17;
    public static final byte UNKNOWN = 0;
    public final byte category;

    @Nullable
    public final TypeProto type;
    public static final String[] CATEGORY_NAMES = {"Unknown", "Uninit", "Null", "One", "Boolean", "Byte", "PosByte", "Short", "PosShort", "Char", "Integer", "Float", "LongLo", "LongHi", "DoubleLo", "DoubleHi", "UninitRef", "UninitThis", "Reference", "Conflicted"};
    protected static byte[][] mergeTable = {new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, new byte[]{1, 1, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{2, 19, 2, 4, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 18, 19}, new byte[]{3, 19, 4, 3, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{4, 19, 4, 4, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{5, 19, 5, 5, 5, 5, 5, 7, 7, 10, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{6, 19, 6, 6, 6, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{7, 19, 7, 7, 7, 7, 7, 7, 7, 10, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{8, 19, 8, 8, 8, 7, 8, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{9, 19, 9, 9, 9, 10, 9, 10, 9, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{10, 19, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{11, 19, 11, 11, 11, 11, 11, 11, 11, 11, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{12, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 12, 19, 12, 19, 19, 19, 19, 19}, new byte[]{13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 13, 19, 13, 19, 19, 19, 19}, new byte[]{14, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 12, 19, 14, 19, 19, 19, 19, 19}, new byte[]{15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 13, 19, 15, 19, 19, 19, 19}, new byte[]{16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{17, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 17, 19, 19}, new byte[]{18, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19}, new byte[]{19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}};
    public static final RegisterType UNKNOWN_TYPE = new RegisterType((byte) 0, null);
    public static final RegisterType UNINIT_TYPE = new RegisterType((byte) 1, null);
    public static final RegisterType NULL_TYPE = new RegisterType((byte) 2, null);
    public static final RegisterType ONE_TYPE = new RegisterType((byte) 3, null);
    public static final RegisterType BOOLEAN_TYPE = new RegisterType((byte) 4, null);
    public static final RegisterType BYTE_TYPE = new RegisterType((byte) 5, null);
    public static final RegisterType POS_BYTE_TYPE = new RegisterType((byte) 6, null);
    public static final RegisterType SHORT_TYPE = new RegisterType((byte) 7, null);
    public static final RegisterType POS_SHORT_TYPE = new RegisterType((byte) 8, null);
    public static final RegisterType CHAR_TYPE = new RegisterType((byte) 9, null);
    public static final RegisterType INTEGER_TYPE = new RegisterType((byte) 10, null);
    public static final RegisterType FLOAT_TYPE = new RegisterType((byte) 11, null);
    public static final RegisterType LONG_LO_TYPE = new RegisterType((byte) 12, null);
    public static final RegisterType LONG_HI_TYPE = new RegisterType((byte) 13, null);
    public static final RegisterType DOUBLE_LO_TYPE = new RegisterType((byte) 14, null);
    public static final RegisterType DOUBLE_HI_TYPE = new RegisterType((byte) 15, null);
    public static final RegisterType CONFLICTED_TYPE = new RegisterType((byte) 19, null);

    private RegisterType(byte category, @Nullable TypeProto type) {
        if (((category != 18 && category != 16 && category != 17) || type == null) && (category == 18 || category == 16 || category == 17 || type != null)) {
            throw new AssertionError();
        }
        this.category = category;
        this.type = type;
    }

    public String toString() {
        return "(" + CATEGORY_NAMES[this.category] + (this.type == null ? "" : "," + this.type) + ")";
    }

    public void writeTo(Writer writer) throws IOException {
        writer.write(40);
        writer.write(CATEGORY_NAMES[this.category]);
        if (this.type != null) {
            writer.write(44);
            writer.write(this.type.getType());
        }
        writer.write(41);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterType that = (RegisterType) o;
        byte b = this.category;
        if (b != that.category || b == 16 || b == 17) {
            return false;
        }
        TypeProto typeProto = this.type;
        return typeProto != null ? typeProto.equals(that.type) : that.type == null;
    }

    public int hashCode() {
        int result = this.category;
        int i = result * 31;
        TypeProto typeProto = this.type;
        int result2 = i + (typeProto != null ? typeProto.hashCode() : 0);
        return result2;
    }

    @Nonnull
    public static RegisterType getWideRegisterType(@Nonnull CharSequence type, boolean firstRegister) {
        switch (type.charAt(0)) {
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                if (firstRegister) {
                    return getRegisterType((byte) 14, (TypeProto) null);
                }
                return getRegisterType((byte) 15, (TypeProto) null);
            case 'J':
                if (firstRegister) {
                    return getRegisterType((byte) 12, (TypeProto) null);
                }
                return getRegisterType((byte) 13, (TypeProto) null);
            default:
                throw new ExceptionWithContext("Cannot use this method for narrow register type: %s", type);
        }
    }

    @Nonnull
    public static RegisterType getRegisterType(@Nonnull ClassPath classPath, @Nonnull CharSequence type) {
        switch (type.charAt(0)) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                return BYTE_TYPE;
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                return CHAR_TYPE;
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                return DOUBLE_LO_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INTEGER_TYPE;
            case 'J':
                return LONG_LO_TYPE;
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return getRegisterType((byte) 18, classPath.getClass(type));
            case 'S':
                return SHORT_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            default:
                throw new AnalysisException("Invalid type: " + ((Object) type), new Object[0]);
        }
    }

    @Nonnull
    public static RegisterType getRegisterTypeForLiteral(int literalValue) {
        if (literalValue < -32768) {
            return INTEGER_TYPE;
        }
        if (literalValue < -128) {
            return SHORT_TYPE;
        }
        if (literalValue < 0) {
            return BYTE_TYPE;
        }
        if (literalValue == 0) {
            return NULL_TYPE;
        }
        if (literalValue == 1) {
            return ONE_TYPE;
        }
        if (literalValue < 128) {
            return POS_BYTE_TYPE;
        }
        if (literalValue < 32768) {
            return POS_SHORT_TYPE;
        }
        if (literalValue < 65536) {
            return CHAR_TYPE;
        }
        return INTEGER_TYPE;
    }

    @Nonnull
    public RegisterType merge(@Nonnull RegisterType other) {
        if (other.equals(this)) {
            return this;
        }
        byte[][] bArr = mergeTable;
        byte b = this.category;
        byte[] bArr2 = bArr[b];
        byte b2 = other.category;
        byte mergedCategory = bArr2[b2];
        TypeProto mergedType = null;
        if (mergedCategory == 18) {
            TypeProto type = this.type;
            if (type != null) {
                TypeProto typeProto = other.type;
                if (typeProto != null) {
                    mergedType = type.getCommonSuperclass(typeProto);
                } else {
                    mergedType = type;
                }
            } else {
                TypeProto mergedType2 = other.type;
                mergedType = mergedType2;
            }
        } else if (mergedCategory == 16 || mergedCategory == 17) {
            if (b == 0) {
                return other;
            }
            if (b2 != 0) {
                throw new AssertionError();
            }
            return this;
        }
        if (mergedType != null) {
            if (mergedType.equals(this.type)) {
                return this;
            }
            if (mergedType.equals(other.type)) {
                return other;
            }
        }
        return getRegisterType(mergedCategory, mergedType);
    }

    @Nonnull
    public static RegisterType getRegisterType(byte category, @Nullable TypeProto typeProto) {
        switch (category) {
            case 0:
                return UNKNOWN_TYPE;
            case 1:
                return UNINIT_TYPE;
            case 2:
                return NULL_TYPE;
            case 3:
                return ONE_TYPE;
            case 4:
                return BOOLEAN_TYPE;
            case 5:
                return BYTE_TYPE;
            case 6:
                return POS_BYTE_TYPE;
            case 7:
                return SHORT_TYPE;
            case 8:
                return POS_SHORT_TYPE;
            case 9:
                return CHAR_TYPE;
            case 10:
                return INTEGER_TYPE;
            case 11:
                return FLOAT_TYPE;
            case 12:
                return LONG_LO_TYPE;
            case 13:
                return LONG_HI_TYPE;
            case 14:
                return DOUBLE_LO_TYPE;
            case 15:
                return DOUBLE_HI_TYPE;
            case 16:
            case 17:
            case 18:
            default:
                return new RegisterType(category, typeProto);
            case 19:
                return CONFLICTED_TYPE;
        }
    }
}
