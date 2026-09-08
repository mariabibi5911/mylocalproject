package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction4rcc;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class InstructionRewriter implements Rewriter<Instruction> {

    @Nonnull
    protected final Rewriters rewriters;

    public InstructionRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.rewriter.InstructionRewriter$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;

        static {
            int[] iArr = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr;
            try {
                iArr[Format.Format20bc.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format21c.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format22c.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format31c.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format45cc.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format4rcc.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public Instruction rewrite(@Nonnull Instruction instruction) {
        if (instruction instanceof ReferenceInstruction) {
            switch (AnonymousClass1.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction.getOpcode().format.ordinal()]) {
                case 1:
                    return new RewrittenInstruction20bc((Instruction20bc) instruction);
                case 2:
                    return new RewrittenInstruction21c((Instruction21c) instruction);
                case 3:
                    return new RewrittenInstruction22c((Instruction22c) instruction);
                case 4:
                    return new RewrittenInstruction31c((Instruction31c) instruction);
                case 5:
                    return new RewrittenInstruction35c((Instruction35c) instruction);
                case 6:
                    return new RewrittenInstruction3rc((Instruction3rc) instruction);
                case 7:
                    return new RewrittenInstruction45cc((Instruction45cc) instruction);
                case 8:
                    return new RewrittenInstruction4rcc((Instruction4rcc) instruction);
                default:
                    throw new IllegalArgumentException();
            }
        }
        return instruction;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public Reference rewriteReference(int type, @Nonnull Reference reference) {
        switch (type) {
            case 0:
                return reference;
            case 1:
                return RewriterUtils.rewriteTypeReference(this.rewriters.getTypeRewriter(), (TypeReference) reference);
            case 2:
                return this.rewriters.getFieldReferenceRewriter().rewrite((FieldReference) reference);
            case 3:
                return this.rewriters.getMethodReferenceRewriter().rewrite((MethodReference) reference);
            case 4:
                return RewriterUtils.rewriteMethodProtoReference(this.rewriters.getTypeRewriter(), (MethodProtoReference) reference);
            case 5:
                return this.rewriters.getCallSiteReferenceRewriter().rewrite((CallSiteReference) reference);
            case 6:
                return RewriterUtils.rewriteMethodHandleReference(this.rewriters, (MethodHandleReference) reference);
            default:
                throw new ExceptionWithContext("Invalid reference type: %d", Integer.valueOf(type));
        }
    }

    /* loaded from: classes.dex */
    protected class BaseRewrittenReferenceInstruction<T extends ReferenceInstruction> implements ReferenceInstruction {

        @Nonnull
        protected T instruction;

        protected BaseRewrittenReferenceInstruction(@Nonnull T instruction) {
            this.instruction = instruction;
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
        @Nonnull
        public Reference getReference() {
            return InstructionRewriter.this.rewriteReference(this.instruction.getReferenceType(), this.instruction.getReference());
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
        public int getReferenceType() {
            return this.instruction.getReferenceType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
        public Opcode getOpcode() {
            return this.instruction.getOpcode();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.Instruction
        public int getCodeUnits() {
            return this.instruction.getCodeUnits();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction20bc extends BaseRewrittenReferenceInstruction<Instruction20bc> implements Instruction20bc {
        public RewrittenInstruction20bc(@Nonnull Instruction20bc instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.VerificationErrorInstruction
        public int getVerificationError() {
            return ((Instruction20bc) this.instruction).getVerificationError();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction21c extends BaseRewrittenReferenceInstruction<Instruction21c> implements Instruction21c {
        public RewrittenInstruction21c(@Nonnull Instruction21c instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction21c) this.instruction).getRegisterA();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction22c extends BaseRewrittenReferenceInstruction<Instruction22c> implements Instruction22c {
        public RewrittenInstruction22c(@Nonnull Instruction22c instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction22c) this.instruction).getRegisterA();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
        public int getRegisterB() {
            return ((Instruction22c) this.instruction).getRegisterB();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction31c extends BaseRewrittenReferenceInstruction<Instruction31c> implements Instruction31c {
        public RewrittenInstruction31c(@Nonnull Instruction31c instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction31c) this.instruction).getRegisterA();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction35c extends BaseRewrittenReferenceInstruction<Instruction35c> implements Instruction35c {
        public RewrittenInstruction35c(@Nonnull Instruction35c instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterC() {
            return ((Instruction35c) this.instruction).getRegisterC();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterE() {
            return ((Instruction35c) this.instruction).getRegisterE();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterG() {
            return ((Instruction35c) this.instruction).getRegisterG();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction35c) this.instruction).getRegisterCount();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterD() {
            return ((Instruction35c) this.instruction).getRegisterD();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterF() {
            return ((Instruction35c) this.instruction).getRegisterF();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction3rc extends BaseRewrittenReferenceInstruction<Instruction3rc> implements Instruction3rc {
        public RewrittenInstruction3rc(@Nonnull Instruction3rc instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
        public int getStartRegister() {
            return ((Instruction3rc) this.instruction).getStartRegister();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction3rc) this.instruction).getRegisterCount();
        }
    }

    /* loaded from: classes.dex */
    protected class BaseRewrittenDualReferenceInstruction<T extends DualReferenceInstruction> extends BaseRewrittenReferenceInstruction<T> implements DualReferenceInstruction {
        public BaseRewrittenDualReferenceInstruction(@Nonnull T instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
        @Nonnull
        public Reference getReference2() {
            return InstructionRewriter.this.rewriteReference(((DualReferenceInstruction) this.instruction).getReferenceType2(), ((DualReferenceInstruction) this.instruction).getReference2());
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction
        public int getReferenceType2() {
            return ((DualReferenceInstruction) this.instruction).getReferenceType2();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction45cc extends BaseRewrittenDualReferenceInstruction<Instruction45cc> implements Instruction45cc {
        public RewrittenInstruction45cc(@Nonnull Instruction45cc instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterC() {
            return ((Instruction45cc) this.instruction).getRegisterC();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterE() {
            return ((Instruction45cc) this.instruction).getRegisterE();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterG() {
            return ((Instruction45cc) this.instruction).getRegisterG();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction45cc) this.instruction).getRegisterCount();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterD() {
            return ((Instruction45cc) this.instruction).getRegisterD();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterF() {
            return ((Instruction45cc) this.instruction).getRegisterF();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenInstruction4rcc extends BaseRewrittenDualReferenceInstruction<Instruction4rcc> implements Instruction4rcc {
        public RewrittenInstruction4rcc(@Nonnull Instruction4rcc instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
        public int getStartRegister() {
            return ((Instruction4rcc) this.instruction).getStartRegister();
        }

        @Override // com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction4rcc) this.instruction).getRegisterCount();
        }
    }
}
