package com.android.tools.smali.dexlib2.dexbacked.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class DexBackedInstruction implements Instruction {

    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int instructionStart;

    @Nonnull
    public final Opcode opcode;

    public DexBackedInstruction(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        this.dexFile = dexFile;
        this.opcode = opcode;
        this.instructionStart = instructionStart;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    @Nonnull
    public Opcode getOpcode() {
        return this.opcode;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return this.opcode.format.size / 2;
    }

    @Nonnull
    public static Instruction readFrom(DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        int opcodeValue = reader.peekUbyte();
        if (opcodeValue == 0) {
            opcodeValue = reader.peekUshort();
        }
        Opcode opcode = dexFile.getOpcodes().getOpcodeByValue(opcodeValue);
        Instruction instruction = buildInstruction(dexFile, opcode, ((reader.getOffset() + reader.dexBuf.getBaseOffset()) - dexFile.getBuffer().getBaseOffset()) - dexFile.getBaseDataOffset());
        reader.moveRelative(instruction.getCodeUnits() * 2);
        return instruction;
    }

    private static DexBackedInstruction buildInstruction(@Nonnull DexBackedDexFile dexFile, @Nullable Opcode opcode, int instructionStartOffset) {
        if (opcode == null) {
            return new DexBackedUnknownInstruction(dexFile, instructionStartOffset);
        }
        switch (AnonymousClass1.$SwitchMap$com$android$tools$smali$dexlib2$Format[opcode.format.ordinal()]) {
            case 1:
                return new DexBackedInstruction10t(dexFile, opcode, instructionStartOffset);
            case 2:
                return new DexBackedInstruction10x(dexFile, opcode, instructionStartOffset);
            case 3:
                return new DexBackedInstruction11n(dexFile, opcode, instructionStartOffset);
            case 4:
                return new DexBackedInstruction11x(dexFile, opcode, instructionStartOffset);
            case 5:
                return new DexBackedInstruction12x(dexFile, opcode, instructionStartOffset);
            case 6:
                return new DexBackedInstruction20bc(dexFile, opcode, instructionStartOffset);
            case 7:
                return new DexBackedInstruction20t(dexFile, opcode, instructionStartOffset);
            case 8:
                return new DexBackedInstruction21c(dexFile, opcode, instructionStartOffset);
            case 9:
                return new DexBackedInstruction21ih(dexFile, opcode, instructionStartOffset);
            case 10:
                return new DexBackedInstruction21lh(dexFile, opcode, instructionStartOffset);
            case 11:
                return new DexBackedInstruction21s(dexFile, opcode, instructionStartOffset);
            case 12:
                return new DexBackedInstruction21t(dexFile, opcode, instructionStartOffset);
            case 13:
                return new DexBackedInstruction22b(dexFile, opcode, instructionStartOffset);
            case 14:
                return new DexBackedInstruction22c(dexFile, opcode, instructionStartOffset);
            case 15:
                return new DexBackedInstruction22cs(dexFile, opcode, instructionStartOffset);
            case 16:
                return new DexBackedInstruction22s(dexFile, opcode, instructionStartOffset);
            case 17:
                return new DexBackedInstruction22t(dexFile, opcode, instructionStartOffset);
            case 18:
                return new DexBackedInstruction22x(dexFile, opcode, instructionStartOffset);
            case 19:
                return new DexBackedInstruction23x(dexFile, opcode, instructionStartOffset);
            case 20:
                return new DexBackedInstruction30t(dexFile, opcode, instructionStartOffset);
            case 21:
                return new DexBackedInstruction31c(dexFile, opcode, instructionStartOffset);
            case 22:
                return new DexBackedInstruction31i(dexFile, opcode, instructionStartOffset);
            case 23:
                return new DexBackedInstruction31t(dexFile, opcode, instructionStartOffset);
            case 24:
                return new DexBackedInstruction32x(dexFile, opcode, instructionStartOffset);
            case 25:
                return new DexBackedInstruction35c(dexFile, opcode, instructionStartOffset);
            case 26:
                return new DexBackedInstruction35ms(dexFile, opcode, instructionStartOffset);
            case 27:
                return new DexBackedInstruction35mi(dexFile, opcode, instructionStartOffset);
            case 28:
                return new DexBackedInstruction3rc(dexFile, opcode, instructionStartOffset);
            case 29:
                return new DexBackedInstruction3rmi(dexFile, opcode, instructionStartOffset);
            case 30:
                return new DexBackedInstruction3rms(dexFile, opcode, instructionStartOffset);
            case 31:
                return new DexBackedInstruction45cc(dexFile, opcode, instructionStartOffset);
            case 32:
                return new DexBackedInstruction4rcc(dexFile, opcode, instructionStartOffset);
            case 33:
                return new DexBackedInstruction51l(dexFile, opcode, instructionStartOffset);
            case 34:
                return new DexBackedPackedSwitchPayload(dexFile, instructionStartOffset);
            case 35:
                return new DexBackedSparseSwitchPayload(dexFile, instructionStartOffset);
            case 36:
                return new DexBackedArrayPayload(dexFile, instructionStartOffset);
            default:
                throw new ExceptionWithContext("Unexpected opcode format: %s", opcode.format.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;

        static {
            int[] iArr = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr;
            try {
                iArr[Format.Format10t.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format10x.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11n.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format11x.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format12x.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20bc.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format20t.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21c.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21ih.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21lh.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21s.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21t.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22b.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22c.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22cs.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22s.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22t.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22x.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format23x.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format30t.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31c.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31i.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31t.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format32x.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35ms.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35mi.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rmi.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rms.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format45cc.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format4rcc.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format51l.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.PackedSwitchPayload.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.SparseSwitchPayload.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.ArrayPayload.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
        }
    }
}
