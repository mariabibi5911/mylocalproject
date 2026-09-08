package com.android.tools.smali.dexlib2.analysis;

import androidx.exifinterface.media.ExifInterface;
import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.instruction.InlineIndexInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.VariableRegisterInstruction;
import com.android.tools.smali.dexlib2.immutable.ImmutableAnnotation;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodImplementation;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter;
import com.android.tools.smali.dexlib2.immutable.util.ParamUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class InlineMethodResolver {
    public static final int DIRECT = 2;
    public static final int STATIC = 8;
    public static final int VIRTUAL = 1;

    @Nonnull
    public abstract Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction);

    @Nonnull
    public static InlineMethodResolver createInlineMethodResolver(int odexVersion) {
        if (odexVersion == 35) {
            return new InlineMethodResolver_version35();
        }
        if (odexVersion == 36) {
            return new InlineMethodResolver_version36();
        }
        throw new RuntimeException(String.format("odex version %d is not supported yet", Integer.valueOf(odexVersion)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public static Method inlineMethod(int accessFlags, @Nonnull String cls, @Nonnull String name, @Nonnull String params, @Nonnull String returnType) {
        ImmutableList<ImmutableMethodParameter> paramList = ImmutableList.copyOf(ParamUtil.parseParamString(params));
        return new ImmutableMethod(cls, name, (ImmutableList<? extends ImmutableMethodParameter>) paramList, returnType, accessFlags, (ImmutableSet<? extends ImmutableAnnotation>) null, (ImmutableSet<HiddenApiRestriction>) null, (ImmutableMethodImplementation) null);
    }

    /* loaded from: classes.dex */
    private static class InlineMethodResolver_version35 extends InlineMethodResolver {
        private final Method[] inlineMethods = {InlineMethodResolver.inlineMethod(8, "Lorg/apache/harmony/dalvik/NativeTestTarget;", "emptyInlineMethod", "", ExifInterface.GPS_MEASUREMENT_INTERRUPTED), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "charAt", "I", "C"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "compareTo", "Ljava/lang/String;", "I"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "equals", "Ljava/lang/Object;", "Z"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "length", "", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sqrt", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "cos", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sin", "D", "D")};

        @Override // com.android.tools.smali.dexlib2.analysis.InlineMethodResolver
        @Nonnull
        public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
            InlineIndexInstruction instruction = (InlineIndexInstruction) analyzedInstruction.instruction;
            int inlineIndex = instruction.getInlineIndex();
            if (inlineIndex >= 0) {
                Method[] methodArr = this.inlineMethods;
                if (inlineIndex < methodArr.length) {
                    return methodArr[inlineIndex];
                }
            }
            throw new RuntimeException("Invalid inline index: " + inlineIndex);
        }
    }

    /* loaded from: classes.dex */
    private static class InlineMethodResolver_version36 extends InlineMethodResolver {
        private final Method indexOfIMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "indexOf", "I", "I");
        private final Method indexOfIIMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "indexOf", "II", "I");
        private final Method fastIndexOfMethod = InlineMethodResolver.inlineMethod(2, "Ljava/lang/String;", "fastIndexOf", "II", "I");
        private final Method isEmptyMethod = InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "isEmpty", "", "Z");
        private final Method[] inlineMethods = {InlineMethodResolver.inlineMethod(8, "Lorg/apache/harmony/dalvik/NativeTestTarget;", "emptyInlineMethod", "", ExifInterface.GPS_MEASUREMENT_INTERRUPTED), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "charAt", "I", "C"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "compareTo", "Ljava/lang/String;", "I"), InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "equals", "Ljava/lang/Object;", "Z"), null, null, InlineMethodResolver.inlineMethod(1, "Ljava/lang/String;", "length", "", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sqrt", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "cos", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Math;", "sin", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "floatToIntBits", "F", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "floatToRawIntBits", "F", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Float;", "intBitsToFloat", "I", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "doubleToLongBits", "D", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "doubleToRawLongBits", "D", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/Double;", "longBitsToDouble", "J", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "I", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "J", "J"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "F", "F"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "abs", "D", "D"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "min", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "max", "II", "I"), InlineMethodResolver.inlineMethod(8, "Ljava/lang/StrictMath;", "sqrt", "D", "D")};

        @Override // com.android.tools.smali.dexlib2.analysis.InlineMethodResolver
        @Nonnull
        public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
            InlineIndexInstruction instruction = (InlineIndexInstruction) analyzedInstruction.instruction;
            int inlineIndex = instruction.getInlineIndex();
            if (inlineIndex >= 0) {
                Method[] methodArr = this.inlineMethods;
                if (inlineIndex < methodArr.length) {
                    if (inlineIndex == 4) {
                        int parameterCount = ((VariableRegisterInstruction) instruction).getRegisterCount();
                        if (parameterCount == 2) {
                            return this.indexOfIMethod;
                        }
                        if (parameterCount == 3) {
                            return this.fastIndexOfMethod;
                        }
                        throw new RuntimeException("Could not determine the correct inline method to use");
                    }
                    if (inlineIndex == 5) {
                        int parameterCount2 = ((VariableRegisterInstruction) instruction).getRegisterCount();
                        if (parameterCount2 == 3) {
                            return this.indexOfIIMethod;
                        }
                        if (parameterCount2 == 1) {
                            return this.isEmptyMethod;
                        }
                        throw new RuntimeException("Could not determine the correct inline method to use");
                    }
                    return methodArr[inlineIndex];
                }
            }
            throw new RuntimeException("Invalid method index: " + inlineIndex);
        }
    }
}
