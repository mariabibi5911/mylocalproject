package com.android.tools.smali.dexlib2.analysis;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class OdexedFieldInstructionMapper {
    private static final int GET = 0;
    private static final int INSTANCE = 0;
    private static final int PRIMITIVE = 0;
    private static final int PUT = 1;
    private static final int REFERENCE = 2;
    private static final int STATIC = 1;
    private static final int WIDE = 1;
    private final FieldOpcode[][][] opcodeMap = (FieldOpcode[][][]) Array.newInstance((Class<?>) FieldOpcode.class, 2, 2, 10);
    private final Map<Opcode, Integer> opcodeValueTypeMap = new HashMap(30);
    private static final FieldOpcode[] dalvikFieldOpcodes = {new FieldOpcode('Z', Opcode.IGET_BOOLEAN, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('B', Opcode.IGET_BYTE, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('S', Opcode.IGET_SHORT, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('C', Opcode.IGET_CHAR, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('I', Opcode.IGET, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('F', Opcode.IGET, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new FieldOpcode('J', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK, Opcode.IGET_WIDE_VOLATILE), new FieldOpcode('D', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK, Opcode.IGET_WIDE_VOLATILE), new FieldOpcode('L', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK, Opcode.IGET_OBJECT_VOLATILE), new FieldOpcode('[', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK, Opcode.IGET_OBJECT_VOLATILE), new FieldOpcode('Z', Opcode.IPUT_BOOLEAN, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('B', Opcode.IPUT_BYTE, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('S', Opcode.IPUT_SHORT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('C', Opcode.IPUT_CHAR, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('I', Opcode.IPUT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('F', Opcode.IPUT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new FieldOpcode('J', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK, Opcode.IPUT_WIDE_VOLATILE), new FieldOpcode('D', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK, Opcode.IPUT_WIDE_VOLATILE), new FieldOpcode('L', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK, Opcode.IPUT_OBJECT_VOLATILE), new FieldOpcode('[', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK, Opcode.IPUT_OBJECT_VOLATILE), new FieldOpcode('Z', true, Opcode.SPUT_BOOLEAN, Opcode.SPUT_VOLATILE), new FieldOpcode('B', true, Opcode.SPUT_BYTE, Opcode.SPUT_VOLATILE), new FieldOpcode('S', true, Opcode.SPUT_SHORT, Opcode.SPUT_VOLATILE), new FieldOpcode('C', true, Opcode.SPUT_CHAR, Opcode.SPUT_VOLATILE), new FieldOpcode('I', true, Opcode.SPUT, Opcode.SPUT_VOLATILE), new FieldOpcode('F', true, Opcode.SPUT, Opcode.SPUT_VOLATILE), new FieldOpcode('J', true, Opcode.SPUT_WIDE, Opcode.SPUT_WIDE_VOLATILE), new FieldOpcode('D', true, Opcode.SPUT_WIDE, Opcode.SPUT_WIDE_VOLATILE), new FieldOpcode('L', true, Opcode.SPUT_OBJECT, Opcode.SPUT_OBJECT_VOLATILE), new FieldOpcode('[', true, Opcode.SPUT_OBJECT, Opcode.SPUT_OBJECT_VOLATILE), new FieldOpcode('Z', true, Opcode.SGET_BOOLEAN, Opcode.SGET_VOLATILE), new FieldOpcode('B', true, Opcode.SGET_BYTE, Opcode.SGET_VOLATILE), new FieldOpcode('S', true, Opcode.SGET_SHORT, Opcode.SGET_VOLATILE), new FieldOpcode('C', true, Opcode.SGET_CHAR, Opcode.SGET_VOLATILE), new FieldOpcode('I', true, Opcode.SGET, Opcode.SGET_VOLATILE), new FieldOpcode('F', true, Opcode.SGET, Opcode.SGET_VOLATILE), new FieldOpcode('J', true, Opcode.SGET_WIDE, Opcode.SGET_WIDE_VOLATILE), new FieldOpcode('D', true, Opcode.SGET_WIDE, Opcode.SGET_WIDE_VOLATILE), new FieldOpcode('L', true, Opcode.SGET_OBJECT, Opcode.SGET_OBJECT_VOLATILE), new FieldOpcode('[', true, Opcode.SGET_OBJECT, Opcode.SGET_OBJECT_VOLATILE)};
    private static final FieldOpcode[] artFieldOpcodes = {new FieldOpcode('Z', Opcode.IGET_BOOLEAN, Opcode.IGET_BOOLEAN_QUICK), new FieldOpcode('B', Opcode.IGET_BYTE, Opcode.IGET_BYTE_QUICK), new FieldOpcode('S', Opcode.IGET_SHORT, Opcode.IGET_SHORT_QUICK), new FieldOpcode('C', Opcode.IGET_CHAR, Opcode.IGET_CHAR_QUICK), new FieldOpcode('I', Opcode.IGET, Opcode.IGET_QUICK), new FieldOpcode('F', Opcode.IGET, Opcode.IGET_QUICK), new FieldOpcode('J', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK), new FieldOpcode('D', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK), new FieldOpcode('L', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK), new FieldOpcode('[', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK), new FieldOpcode('Z', Opcode.IPUT_BOOLEAN, Opcode.IPUT_BOOLEAN_QUICK), new FieldOpcode('B', Opcode.IPUT_BYTE, Opcode.IPUT_BYTE_QUICK), new FieldOpcode('S', Opcode.IPUT_SHORT, Opcode.IPUT_SHORT_QUICK), new FieldOpcode('C', Opcode.IPUT_CHAR, Opcode.IPUT_CHAR_QUICK), new FieldOpcode('I', Opcode.IPUT, Opcode.IPUT_QUICK), new FieldOpcode('F', Opcode.IPUT, Opcode.IPUT_QUICK), new FieldOpcode('J', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK), new FieldOpcode('D', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK), new FieldOpcode('L', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK), new FieldOpcode('[', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK)};

    /* loaded from: classes.dex */
    private static class FieldOpcode {
        public final boolean isStatic;

        @Nonnull
        public final Opcode normalOpcode;

        @Nullable
        public final Opcode quickOpcode;
        public final char type;

        @Nullable
        public final Opcode volatileOpcode;

        public FieldOpcode(char type, @Nonnull Opcode normalOpcode, @Nullable Opcode quickOpcode, @Nullable Opcode volatileOpcode) {
            this.type = type;
            this.isStatic = false;
            this.normalOpcode = normalOpcode;
            this.quickOpcode = quickOpcode;
            this.volatileOpcode = volatileOpcode;
        }

        public FieldOpcode(char type, boolean isStatic, @Nonnull Opcode normalOpcode, @Nullable Opcode volatileOpcode) {
            this.type = type;
            this.isStatic = isStatic;
            this.normalOpcode = normalOpcode;
            this.quickOpcode = null;
            this.volatileOpcode = volatileOpcode;
        }

        public FieldOpcode(char type, @Nonnull Opcode normalOpcode, @Nullable Opcode quickOpcode) {
            this.type = type;
            this.isStatic = false;
            this.normalOpcode = normalOpcode;
            this.quickOpcode = quickOpcode;
            this.volatileOpcode = null;
        }
    }

    private static int getValueType(char type) {
        switch (type) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
            case 'F':
            case 'I':
            case 'S':
            case 'Z':
                return 0;
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
            case 'J':
                return 1;
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return 2;
            default:
                throw new RuntimeException(String.format("Unknown type %s: ", Character.valueOf(type)));
        }
    }

    private static int getTypeIndex(char type) {
        switch (type) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                return 1;
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                return 3;
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
                return 7;
            case 'F':
                return 5;
            case 'I':
                return 4;
            case 'J':
                return 6;
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
                return 8;
            case 'S':
                return 2;
            case 'Z':
                return 0;
            case '[':
                return 9;
            default:
                throw new RuntimeException(String.format("Unknown type %s: ", Character.valueOf(type)));
        }
    }

    private static boolean isGet(@Nonnull Opcode opcode) {
        return (opcode.flags & 16) != 0;
    }

    private static boolean isStatic(@Nonnull Opcode opcode) {
        return (opcode.flags & 256) != 0;
    }

    public OdexedFieldInstructionMapper(boolean z) {
        FieldOpcode[] fieldOpcodeArr;
        if (z) {
            fieldOpcodeArr = artFieldOpcodes;
        } else {
            fieldOpcodeArr = dalvikFieldOpcodes;
        }
        for (FieldOpcode fieldOpcode : fieldOpcodeArr) {
            this.opcodeMap[!isGet(fieldOpcode.normalOpcode) ? 1 : 0][isStatic(fieldOpcode.normalOpcode) ? 1 : 0][getTypeIndex(fieldOpcode.type)] = fieldOpcode;
            if (fieldOpcode.quickOpcode != null) {
                this.opcodeValueTypeMap.put(fieldOpcode.quickOpcode, Integer.valueOf(getValueType(fieldOpcode.type)));
            }
            if (fieldOpcode.volatileOpcode != null) {
                this.opcodeValueTypeMap.put(fieldOpcode.volatileOpcode, Integer.valueOf(getValueType(fieldOpcode.type)));
            }
        }
    }

    @Nonnull
    public Opcode getAndCheckDeodexedOpcode(@Nonnull String str, @Nonnull Opcode opcode) {
        FieldOpcode fieldOpcode = this.opcodeMap[!isGet(opcode) ? 1 : 0][isStatic(opcode) ? 1 : 0][getTypeIndex(str.charAt(0))];
        if (!isCompatible(opcode, fieldOpcode.type)) {
            throw new AnalysisException(String.format("Incorrect field type \"%s\" for %s", str, opcode.name), new Object[0]);
        }
        return fieldOpcode.normalOpcode;
    }

    private boolean isCompatible(Opcode opcode, char type) {
        Integer valueType = this.opcodeValueTypeMap.get(opcode);
        if (valueType != null) {
            return valueType.intValue() == getValueType(type);
        }
        throw new RuntimeException("Unexpected opcode: " + opcode.name);
    }
}
