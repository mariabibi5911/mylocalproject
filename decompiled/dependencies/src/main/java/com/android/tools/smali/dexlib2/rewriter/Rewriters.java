package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface Rewriters {
    @Nonnull
    Rewriter<AnnotationElement> getAnnotationElementRewriter();

    @Nonnull
    Rewriter<Annotation> getAnnotationRewriter();

    @Nonnull
    Rewriter<CallSiteReference> getCallSiteReferenceRewriter();

    @Nonnull
    Rewriter<ClassDef> getClassDefRewriter();

    @Nonnull
    Rewriter<DebugItem> getDebugItemRewriter();

    @Nonnull
    Rewriter<DexFile> getDexFileRewriter();

    @Nonnull
    Rewriter<EncodedValue> getEncodedValueRewriter();

    @Nonnull
    Rewriter<ExceptionHandler> getExceptionHandlerRewriter();

    @Nonnull
    Rewriter<FieldReference> getFieldReferenceRewriter();

    @Nonnull
    Rewriter<Field> getFieldRewriter();

    @Nonnull
    Rewriter<Instruction> getInstructionRewriter();

    @Nonnull
    Rewriter<MethodImplementation> getMethodImplementationRewriter();

    @Nonnull
    Rewriter<MethodParameter> getMethodParameterRewriter();

    @Nonnull
    Rewriter<MethodReference> getMethodReferenceRewriter();

    @Nonnull
    Rewriter<Method> getMethodRewriter();

    @Nonnull
    Rewriter<TryBlock<? extends ExceptionHandler>> getTryBlockRewriter();

    @Nonnull
    Rewriter<String> getTypeRewriter();
}
