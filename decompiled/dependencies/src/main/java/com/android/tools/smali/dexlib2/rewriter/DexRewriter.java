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
public class DexRewriter implements Rewriters {
    private final Rewriter<AnnotationElement> annotationElementRewriter;
    private final Rewriter<Annotation> annotationRewriter;
    private final Rewriter<CallSiteReference> callSiteReferenceRewriter;
    private final Rewriter<ClassDef> classDefRewriter;
    private final Rewriter<DebugItem> debugItemRewriter;
    private final Rewriter<DexFile> dexFileRewriter;
    private final Rewriter<EncodedValue> encodedValueRewriter;
    private final Rewriter<ExceptionHandler> exceptionHandlerRewriter;
    private final Rewriter<FieldReference> fieldReferenceRewriter;
    private final Rewriter<Field> fieldRewriter;
    private final Rewriter<Instruction> instructionRewriter;
    private final Rewriter<MethodImplementation> methodImplementationRewriter;
    private final Rewriter<MethodParameter> methodParameterRewriter;
    private final Rewriter<MethodReference> methodReferenceRewriter;
    private final Rewriter<Method> methodRewriter;
    private final Rewriter<TryBlock<? extends ExceptionHandler>> tryBlockRewriter;
    private final Rewriter<String> typeRewriter;

    public DexRewriter(RewriterModule module) {
        this.dexFileRewriter = module.getDexFileRewriter(this);
        this.classDefRewriter = module.getClassDefRewriter(this);
        this.fieldRewriter = module.getFieldRewriter(this);
        this.methodRewriter = module.getMethodRewriter(this);
        this.methodParameterRewriter = module.getMethodParameterRewriter(this);
        this.methodImplementationRewriter = module.getMethodImplementationRewriter(this);
        this.instructionRewriter = module.getInstructionRewriter(this);
        this.tryBlockRewriter = module.getTryBlockRewriter(this);
        this.exceptionHandlerRewriter = module.getExceptionHandlerRewriter(this);
        this.debugItemRewriter = module.getDebugItemRewriter(this);
        this.typeRewriter = module.getTypeRewriter(this);
        this.fieldReferenceRewriter = module.getFieldReferenceRewriter(this);
        this.methodReferenceRewriter = module.getMethodReferenceRewriter(this);
        this.callSiteReferenceRewriter = module.getCallSiteReferenceRewriter(this);
        this.annotationRewriter = module.getAnnotationRewriter(this);
        this.annotationElementRewriter = module.getAnnotationElementRewriter(this);
        this.encodedValueRewriter = module.getEncodedValueRewriter(this);
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<DexFile> getDexFileRewriter() {
        return this.dexFileRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<ClassDef> getClassDefRewriter() {
        return this.classDefRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Field> getFieldRewriter() {
        return this.fieldRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Method> getMethodRewriter() {
        return this.methodRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodParameter> getMethodParameterRewriter() {
        return this.methodParameterRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodImplementation> getMethodImplementationRewriter() {
        return this.methodImplementationRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Instruction> getInstructionRewriter() {
        return this.instructionRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<TryBlock<? extends ExceptionHandler>> getTryBlockRewriter() {
        return this.tryBlockRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<ExceptionHandler> getExceptionHandlerRewriter() {
        return this.exceptionHandlerRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<DebugItem> getDebugItemRewriter() {
        return this.debugItemRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<String> getTypeRewriter() {
        return this.typeRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<FieldReference> getFieldReferenceRewriter() {
        return this.fieldReferenceRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<MethodReference> getMethodReferenceRewriter() {
        return this.methodReferenceRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<CallSiteReference> getCallSiteReferenceRewriter() {
        return this.callSiteReferenceRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<Annotation> getAnnotationRewriter() {
        return this.annotationRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<AnnotationElement> getAnnotationElementRewriter() {
        return this.annotationElementRewriter;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriters
    @Nonnull
    public Rewriter<EncodedValue> getEncodedValueRewriter() {
        return this.encodedValueRewriter;
    }
}
