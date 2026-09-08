package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.google.common.base.Function;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PoolMethod extends BaseMethodReference implements Method {
    public static final Function<Method, PoolMethod> TRANSFORM = new Function<Method, PoolMethod>() { // from class: com.android.tools.smali.dexlib2.writer.pool.PoolMethod.1
        @Override // com.google.common.base.Function
        public PoolMethod apply(Method method) {
            return new PoolMethod(method);
        }
    };
    protected int annotationSetRefListOffset = 0;
    protected int codeItemOffset = 0;

    @Nonnull
    private final Method method;

    PoolMethod(@Nonnull Method method) {
        this.method = method;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.method.getDefiningClass();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.method.getName();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public List<? extends CharSequence> getParameterTypes() {
        return this.method.getParameterTypes();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public List<? extends MethodParameter> getParameters() {
        return this.method.getParameters();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.method.getReturnType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.method.getAccessFlags();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return this.method.getAnnotations();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.method.getHiddenApiRestrictions();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nullable
    public MethodImplementation getImplementation() {
        return this.method.getImplementation();
    }
}
