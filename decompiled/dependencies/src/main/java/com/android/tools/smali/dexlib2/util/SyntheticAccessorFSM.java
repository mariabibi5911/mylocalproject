package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction;
import com.google.common.base.Ascii;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class SyntheticAccessorFSM {
    public static final int ADD = 7;
    public static final int AND = 12;
    public static final int DIV = 10;
    public static final int DOUBLE = 3;
    public static final int FLOAT = 2;
    public static final int INT = 0;
    public static final int LONG = 1;
    public static final int MUL = 9;
    public static final int NEGATIVE_ONE = -1;
    public static final int OR = 13;
    public static final int OTHER = 0;
    public static final int POSITIVE_ONE = 1;
    public static final int REM = 11;
    public static final int SHL = 15;
    public static final int SHR = 16;
    public static final int SUB = 8;
    static final int SyntheticAccessorFSM_en_main = 1;
    static final int SyntheticAccessorFSM_error = 0;
    static final int SyntheticAccessorFSM_first_final = 17;
    static final int SyntheticAccessorFSM_start = 1;
    public static final int USHR = 17;
    public static final int XOR = 14;

    @Nonnull
    private final Opcodes opcodes;
    private static final byte[] _SyntheticAccessorFSM_actions = init__SyntheticAccessorFSM_actions_0();
    private static final short[] _SyntheticAccessorFSM_key_offsets = init__SyntheticAccessorFSM_key_offsets_0();
    private static final short[] _SyntheticAccessorFSM_trans_keys = init__SyntheticAccessorFSM_trans_keys_0();
    private static final byte[] _SyntheticAccessorFSM_single_lengths = init__SyntheticAccessorFSM_single_lengths_0();
    private static final byte[] _SyntheticAccessorFSM_range_lengths = init__SyntheticAccessorFSM_range_lengths_0();
    private static final short[] _SyntheticAccessorFSM_index_offsets = init__SyntheticAccessorFSM_index_offsets_0();
    private static final byte[] _SyntheticAccessorFSM_indicies = init__SyntheticAccessorFSM_indicies_0();
    private static final byte[] _SyntheticAccessorFSM_trans_targs = init__SyntheticAccessorFSM_trans_targs_0();
    private static final byte[] _SyntheticAccessorFSM_trans_actions = init__SyntheticAccessorFSM_trans_actions_0();

    private static byte[] init__SyntheticAccessorFSM_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 13, 1, 14, 1, 15, 1, 16, 1, 17, 1, 18, 1, 19, 1, Ascii.DC4, 1, Ascii.NAK, 1, Ascii.EM, 2, 3, 7, 2, 4, 7, 2, 5, 7, 2, 6, 7, 2, 8, 12, 2, 9, 12, 2, 10, 12, 2, 11, 12, 2, Ascii.SYN, Ascii.ETB, 2, Ascii.SYN, Ascii.CAN, 2, Ascii.SYN, Ascii.EM, 2, Ascii.SYN, Ascii.SUB, 2, Ascii.SYN, Ascii.ESC, 2, Ascii.SYN, Ascii.FS};
    }

    private static short[] init__SyntheticAccessorFSM_key_offsets_0() {
        return new short[]{0, 0, 12, 82, 98, 102, 104, 166, 172, 174, 180, 184, 190, 192, 196, 198, 201, 203};
    }

    private static short[] init__SyntheticAccessorFSM_trans_keys_0() {
        return new short[]{82, 88, 89, 95, 96, 102, 103, 109, 110, 114, 116, 120, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 177, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 206, 207, 208, 216, 15, 17, 18, 25, 129, 143, 144, 176, 178, 205, 144, 145, 155, 156, 166, 167, 171, 172, 176, 177, 187, 188, 198, 199, 203, 204, 89, 95, 103, 109, 15, 17, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 177, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 206, 207, 144, 176, 178, 205, 89, 95, 103, 109, 129, 143, 15, 17, 89, 95, 103, 109, 129, 143, 89, 95, 103, 109, 89, 95, 103, 109, 129, 143, 15, 17, 89, 95, 103, 109, 15, 17, 14, 10, 12, 15, 17, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_single_lengths_0() {
        return new byte[]{0, 0, 60, 16, 0, 0, 58, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_range_lengths_0() {
        return new byte[]{0, 6, 5, 0, 2, 1, 2, 3, 1, 3, 2, 3, 1, 2, 1, 1, 1, 0};
    }

    private static short[] init__SyntheticAccessorFSM_index_offsets_0() {
        return new short[]{0, 0, 7, 73, 90, 93, 95, 156, 160, 162, 166, 169, 173, 175, 178, 180, 183, 185};
    }

    private static byte[] init__SyntheticAccessorFSM_indicies_0() {
        return new byte[]{0, 2, 0, 2, 3, 3, 1, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 9, 10, 11, 12, 13, 14, 15, 16, 17, Ascii.DC4, Ascii.NAK, 9, 10, 11, Ascii.SYN, Ascii.ETB, 9, 10, 11, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 11, 12, 13, 14, 15, 16, 17, Ascii.DC4, Ascii.NAK, 10, 11, Ascii.SYN, Ascii.ETB, 10, 11, Ascii.CAN, Ascii.CAN, 4, 5, 6, 7, 9, 1, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, 32, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, 32, 1, 33, 33, 1, 34, 1, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 9, 10, 11, 12, 13, 14, 15, 16, 17, Ascii.DC4, Ascii.NAK, 9, 10, 11, Ascii.SYN, Ascii.ETB, 9, 10, 11, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 11, 12, 13, 14, 15, 16, 17, Ascii.DC4, Ascii.NAK, 10, 11, Ascii.SYN, Ascii.ETB, 10, 11, 7, 9, 1, 35, 35, 36, 1, 37, 1, 35, 35, 38, 1, 35, 35, 1, 39, 39, 40, 1, 41, 1, 39, 39, 1, 42, 1, 44, 43, 1, 45, 1, 1, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_trans_targs_0() {
        return new byte[]{2, 0, 14, 15, 17, 3, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 11, 4, 4, 4, 4, 4, 4, 4, 4, 5, 17, 8, 9, 17, 10, 12, 13, 17, 17, 16, 17, 17};
    }

    private static byte[] init__SyntheticAccessorFSM_trans_actions_0() {
        return new byte[]{0, 0, 1, 0, 51, 3, 0, Ascii.ESC, 39, 7, 9, 11, 13, 15, 17, 19, Ascii.NAK, Ascii.ETB, Ascii.RS, 42, 33, 45, 36, 48, 5, Ascii.ESC, 39, Ascii.RS, 42, 33, 45, 36, 48, 1, 63, 1, 0, 66, 0, 1, 0, 60, 54, 0, Ascii.EM, 57};
    }

    public SyntheticAccessorFSM(@Nonnull Opcodes opcodes) {
        this.opcodes = opcodes;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:27:0x0133. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0013. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0226 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x021c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0229 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0036  */
    /* JADX WARN: Type inference failed for: r12v3, types: [int] */
    /* JADX WARN: Type inference failed for: r12v4, types: [int] */
    /* JADX WARN: Type inference failed for: r12v7, types: [int] */
    /* JADX WARN: Type inference failed for: r12v8, types: [int] */
    /* JADX WARN: Type inference failed for: r14v3, types: [int] */
    /* JADX WARN: Type inference failed for: r21v1, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int test(List<? extends Instruction> list) {
        byte b;
        int i;
        int i2;
        int i3;
        short s;
        short s2;
        byte b2;
        long j;
        short s3;
        byte b3;
        byte[] bArr;
        int i4 = -1;
        int i5 = 0;
        int size = list.size();
        int i6 = -1;
        int i7 = -1;
        long j2 = 0;
        int i8 = -1;
        int i9 = -1;
        byte b4 = 1;
        int i10 = 0;
        while (true) {
            switch (i10) {
                case 0:
                    if (i5 == size) {
                        i10 = 4;
                    } else if (b4 == 0) {
                        i10 = 5;
                    } else {
                        short s4 = _SyntheticAccessorFSM_key_offsets[b4];
                        short s5 = _SyntheticAccessorFSM_index_offsets[b4];
                        b = _SyntheticAccessorFSM_single_lengths[b4];
                        if (b > 0) {
                            i = i4;
                            i2 = i6;
                            i3 = i7;
                            s2 = s5;
                            s = s4;
                        } else {
                            i = i4;
                            int i11 = s4;
                            i2 = i6;
                            int i12 = (s4 + b) - 1;
                            while (i12 >= i11) {
                                int i13 = i11 + ((i12 - i11) >> 1);
                                int i14 = i11;
                                int i15 = i12;
                                short shortValue = this.opcodes.getOpcodeValue(list.get(i5).getOpcode()).shortValue();
                                short[] sArr = _SyntheticAccessorFSM_trans_keys;
                                i3 = i7;
                                if (shortValue < sArr[i13]) {
                                    i12 = i13 - 1;
                                    i11 = i14;
                                    i7 = i3;
                                } else if (this.opcodes.getOpcodeValue(list.get(i5).getOpcode()).shortValue() > sArr[i13]) {
                                    i11 = i13 + 1;
                                    i7 = i3;
                                    i12 = i15;
                                } else {
                                    j = j2;
                                    s3 = s5 + (i13 - s4);
                                    b3 = _SyntheticAccessorFSM_indicies[s3];
                                    b4 = _SyntheticAccessorFSM_trans_targs[b3];
                                    bArr = _SyntheticAccessorFSM_trans_actions;
                                    if (bArr[b3] == 0) {
                                        i6 = i2;
                                        i4 = i;
                                        i7 = i3;
                                        j2 = j;
                                    } else {
                                        byte b5 = bArr[b3];
                                        int i16 = b5 + 1;
                                        byte b6 = _SyntheticAccessorFSM_actions[b5];
                                        int i17 = i8;
                                        int i18 = i9;
                                        while (true) {
                                            ?? r21 = b6 - 1;
                                            if (b6 <= 0) {
                                                i6 = i2;
                                                i8 = i17;
                                                i4 = i;
                                                i9 = i18;
                                                i7 = i3;
                                                j2 = j;
                                            } else {
                                                int i19 = i16 + 1;
                                                switch (_SyntheticAccessorFSM_actions[i16]) {
                                                    case 0:
                                                        i17 = ((OneRegisterInstruction) list.get(i5)).getRegisterA();
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 1:
                                                        j = ((WideLiteralInstruction) list.get(i5)).getWideLiteral();
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 2:
                                                        i3 = 0;
                                                        i2 = 7;
                                                        j = ((WideLiteralInstruction) list.get(i5)).getWideLiteral();
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 3:
                                                        i3 = 0;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 4:
                                                        i3 = 1;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 5:
                                                        i3 = 2;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 6:
                                                        i3 = 3;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 7:
                                                        i2 = 7;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 8:
                                                        i3 = 0;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 9:
                                                        i3 = 1;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 10:
                                                        i3 = 2;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 11:
                                                        i3 = 3;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 12:
                                                        i2 = 8;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 13:
                                                        i2 = 9;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 14:
                                                        i2 = 10;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 15:
                                                        i2 = 11;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 16:
                                                        i2 = 12;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 17:
                                                        i2 = 13;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 18:
                                                        i2 = 14;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 19:
                                                        i2 = 15;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 20:
                                                        i2 = 16;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 21:
                                                        i2 = 17;
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 22:
                                                        i18 = ((OneRegisterInstruction) list.get(i5)).getRegisterA();
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 23:
                                                        i4 = 1;
                                                        i5++;
                                                        i10 = 5;
                                                        i6 = i2;
                                                        i8 = i17;
                                                        i9 = i18;
                                                        i7 = i3;
                                                        j2 = j;
                                                        break;
                                                    case 24:
                                                        i4 = 2;
                                                        i5++;
                                                        i10 = 5;
                                                        i6 = i2;
                                                        i8 = i17;
                                                        i9 = i18;
                                                        i7 = i3;
                                                        j2 = j;
                                                        break;
                                                    case 25:
                                                        i4 = 0;
                                                        i5++;
                                                        i10 = 5;
                                                        i6 = i2;
                                                        i8 = i17;
                                                        i9 = i18;
                                                        i7 = i3;
                                                        j2 = j;
                                                        break;
                                                    case 26:
                                                        i = getIncrementType(i2, i3, j, i17, i18);
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 27:
                                                        i = getIncrementType(i2, i3, j, i17, i18);
                                                        b6 = r21;
                                                        i16 = i19;
                                                    case 28:
                                                        i4 = i2;
                                                        i5++;
                                                        i10 = 5;
                                                        i6 = i2;
                                                        i8 = i17;
                                                        i9 = i18;
                                                        i7 = i3;
                                                        j2 = j;
                                                        break;
                                                    default:
                                                        b6 = r21;
                                                        i16 = i19;
                                                }
                                            }
                                        }
                                    }
                                    if (b4 != 0) {
                                        i10 = 5;
                                    } else {
                                        i5++;
                                        if (i5 == size) {
                                            break;
                                        } else {
                                            i10 = 1;
                                        }
                                    }
                                }
                            }
                            i3 = i7;
                            s2 = s5 + b;
                            s = s4 + b;
                        }
                        b2 = _SyntheticAccessorFSM_range_lengths[b4];
                        if (b2 > 0) {
                            j = j2;
                            s3 = s2;
                        } else {
                            int i20 = s;
                            int i21 = ((b2 << 1) + s) - 2;
                            while (true) {
                                if (i21 >= i20) {
                                    int i22 = (((i21 - i20) >> 1) & (-2)) + i20;
                                    int i23 = i20;
                                    int i24 = i21;
                                    short shortValue2 = this.opcodes.getOpcodeValue(list.get(i5).getOpcode()).shortValue();
                                    short[] sArr2 = _SyntheticAccessorFSM_trans_keys;
                                    j = j2;
                                    if (shortValue2 < sArr2[i22]) {
                                        i21 = i22 - 2;
                                        i20 = i23;
                                        j2 = j;
                                    } else if (this.opcodes.getOpcodeValue(list.get(i5).getOpcode()).shortValue() > sArr2[i22 + 1]) {
                                        i20 = i22 + 2;
                                        i21 = i24;
                                        j2 = j;
                                    } else {
                                        s3 = s2 + ((i22 - s) >> 1);
                                    }
                                } else {
                                    j = j2;
                                    s3 = s2 + b2;
                                }
                            }
                        }
                        b3 = _SyntheticAccessorFSM_indicies[s3];
                        b4 = _SyntheticAccessorFSM_trans_targs[b3];
                        bArr = _SyntheticAccessorFSM_trans_actions;
                        if (bArr[b3] == 0) {
                        }
                        if (b4 != 0) {
                        }
                    }
                case 1:
                    short s42 = _SyntheticAccessorFSM_key_offsets[b4];
                    short s52 = _SyntheticAccessorFSM_index_offsets[b4];
                    b = _SyntheticAccessorFSM_single_lengths[b4];
                    if (b > 0) {
                    }
                    b2 = _SyntheticAccessorFSM_range_lengths[b4];
                    if (b2 > 0) {
                    }
                    b3 = _SyntheticAccessorFSM_indicies[s3];
                    b4 = _SyntheticAccessorFSM_trans_targs[b3];
                    bArr = _SyntheticAccessorFSM_trans_actions;
                    if (bArr[b3] == 0) {
                    }
                    if (b4 != 0) {
                    }
                    break;
                case 2:
                    if (b4 != 0) {
                    }
                    break;
            }
        }
        return i4;
    }

    private static int getIncrementType(int mathOp, int mathType, long constantValue, int putRegister, int returnRegister) {
        boolean isAdd = true;
        boolean isPrefix = putRegister == returnRegister;
        boolean negativeConstant = false;
        switch (mathType) {
            case 0:
            case 1:
                if (constantValue == 1) {
                    negativeConstant = false;
                    break;
                } else {
                    if (constantValue != -1) {
                        return -1;
                    }
                    negativeConstant = true;
                    break;
                }
            case 2:
                float val = Float.intBitsToFloat((int) constantValue);
                if (val == 1.0f) {
                    negativeConstant = false;
                    break;
                } else {
                    if (val != -1.0f) {
                        return -1;
                    }
                    negativeConstant = true;
                    break;
                }
            case 3:
                double val2 = Double.longBitsToDouble(constantValue);
                if (val2 == 1.0d) {
                    negativeConstant = false;
                    break;
                } else {
                    if (val2 != -1.0d) {
                        return -1;
                    }
                    negativeConstant = true;
                    break;
                }
        }
        if ((mathOp != 7 || negativeConstant) && (mathOp != 8 || !negativeConstant)) {
            isAdd = false;
        }
        if (isPrefix) {
            if (isAdd) {
                return 4;
            }
            return 6;
        }
        if (isAdd) {
            return 3;
        }
        return 5;
    }
}
