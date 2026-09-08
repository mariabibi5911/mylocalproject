package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11n;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction12x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21ih;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22b;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22cs;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction23x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction30t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31i;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35mi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35ms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction4rcc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction51l;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.UnknownInstruction;
import com.android.tools.smali.dexlib2.util.Preconditions;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class ImmutableInstruction implements Instruction {
    private static final ImmutableConverter<ImmutableInstruction, Instruction> CONVERTER = new ImmutableConverter<ImmutableInstruction, Instruction>() { // from class: com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull Instruction item) {
            return item instanceof ImmutableInstruction;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableInstruction makeImmutable(@Nonnull Instruction item) {
            return ImmutableInstruction.of(item);
        }
    };

    @Nonnull
    protected final Opcode opcode;

    public abstract Format getFormat();

    /* JADX INFO: Access modifiers changed from: protected */
    public ImmutableInstruction(@Nonnull Opcode opcode) {
        Preconditions.checkFormat(opcode, getFormat());
        this.opcode = opcode;
    }

    @Nonnull
    public static ImmutableInstruction of(Instruction instruction) {
        if (instruction instanceof ImmutableInstruction) {
            return (ImmutableInstruction) instruction;
        }
        switch (AnonymousClass2.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction.getOpcode().format.ordinal()]) {
            case 1:
                return ImmutableInstruction10t.of((Instruction10t) instruction);
            case 2:
                if (instruction instanceof UnknownInstruction) {
                    return ImmutableUnknownInstruction.of((UnknownInstruction) instruction);
                }
                return ImmutableInstruction10x.of((Instruction10x) instruction);
            case 3:
                return ImmutableInstruction11n.of((Instruction11n) instruction);
            case 4:
                return ImmutableInstruction11x.of((Instruction11x) instruction);
            case 5:
                return ImmutableInstruction12x.of((Instruction12x) instruction);
            case 6:
                return ImmutableInstruction20bc.of((Instruction20bc) instruction);
            case 7:
                return ImmutableInstruction20t.of((Instruction20t) instruction);
            case 8:
                return ImmutableInstruction21c.of((Instruction21c) instruction);
            case 9:
                return ImmutableInstruction21ih.of((Instruction21ih) instruction);
            case 10:
                return ImmutableInstruction21lh.of((Instruction21lh) instruction);
            case 11:
                return ImmutableInstruction21s.of((Instruction21s) instruction);
            case 12:
                return ImmutableInstruction21t.of((Instruction21t) instruction);
            case 13:
                return ImmutableInstruction22b.of((Instruction22b) instruction);
            case 14:
                return ImmutableInstruction22c.of((Instruction22c) instruction);
            case 15:
                return ImmutableInstruction22cs.of((Instruction22cs) instruction);
            case 16:
                return ImmutableInstruction22s.of((Instruction22s) instruction);
            case 17:
                return ImmutableInstruction22t.of((Instruction22t) instruction);
            case 18:
                return ImmutableInstruction22x.of((Instruction22x) instruction);
            case 19:
                return ImmutableInstruction23x.of((Instruction23x) instruction);
            case 20:
                return ImmutableInstruction30t.of((Instruction30t) instruction);
            case 21:
                return ImmutableInstruction31c.of((Instruction31c) instruction);
            case 22:
                return ImmutableInstruction31i.of((Instruction31i) instruction);
            case 23:
                return ImmutableInstruction31t.of((Instruction31t) instruction);
            case 24:
                return ImmutableInstruction32x.of((Instruction32x) instruction);
            case 25:
                return ImmutableInstruction35c.of((Instruction35c) instruction);
            case 26:
                return ImmutableInstruction35mi.of((Instruction35mi) instruction);
            case 27:
                return ImmutableInstruction35ms.of((Instruction35ms) instruction);
            case 28:
                return ImmutableInstruction3rc.of((Instruction3rc) instruction);
            case 29:
                return ImmutableInstruction3rmi.of((Instruction3rmi) instruction);
            case 30:
                return ImmutableInstruction3rms.of((Instruction3rms) instruction);
            case 31:
                return ImmutableInstruction45cc.of((Instruction45cc) instruction);
            case 32:
                return ImmutableInstruction4rcc.of((Instruction4rcc) instruction);
            case 33:
                return ImmutableInstruction51l.of((Instruction51l) instruction);
            case 34:
                return ImmutablePackedSwitchPayload.of((PackedSwitchPayload) instruction);
            case 35:
                return ImmutableSparseSwitchPayload.of((SparseSwitchPayload) instruction);
            case 36:
                return ImmutableArrayPayload.of((ArrayPayload) instruction);
            default:
                throw new RuntimeException("Unexpected instruction type");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
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
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35mi.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35ms.ordinal()] = 27;
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

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    @Nonnull
    public Opcode getOpcode() {
        return this.opcode;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return getFormat().size / 2;
    }

    @Nonnull
    public static ImmutableList<ImmutableInstruction> immutableListOf(Iterable<? extends Instruction> list) {
        return CONVERTER.toList(list);
    }
}
