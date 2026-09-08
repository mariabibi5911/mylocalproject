package com.android.tools.smali.dexlib2.analysis.reflection;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils;
import com.android.tools.smali.dexlib2.base.BaseMethodParameter;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.google.common.collect.ImmutableSet;
import java.util.AbstractList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ReflectionMethod extends BaseMethodReference implements Method {
    private final java.lang.reflect.Method method;

    public ReflectionMethod(java.lang.reflect.Method method) {
        this.method = method;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public List<? extends MethodParameter> getParameters() {
        java.lang.reflect.Method method = this.method;
        return new AbstractList<MethodParameter>(method) { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionMethod.1
            private final Class[] parameters;
            final /* synthetic */ java.lang.reflect.Method val$method;

            {
                this.val$method = method;
                this.parameters = method.getParameterTypes();
            }

            @Override // java.util.AbstractList, java.util.List
            public MethodParameter get(final int index) {
                return new BaseMethodParameter() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionMethod.1.1
                    @Override // com.android.tools.smali.dexlib2.iface.MethodParameter
                    @Nonnull
                    public Set<? extends Annotation> getAnnotations() {
                        return ImmutableSet.of();
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.MethodParameter, com.android.tools.smali.dexlib2.iface.debug.LocalInfo
                    @Nullable
                    public String getName() {
                        return null;
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
                    @Nonnull
                    public String getType() {
                        return ReflectionUtils.javaToDexName(AnonymousClass1.this.parameters[index].getName());
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.parameters.length;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.method.getModifiers();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return ImmutableSet.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nullable
    public MethodImplementation getImplementation() {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return ReflectionUtils.javaToDexName(this.method.getDeclaringClass().getName());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.method.getName();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public List<String> getParameterTypes() {
        return new AbstractList<String>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionMethod.2
            private final List<? extends MethodParameter> parameters;

            {
                this.parameters = ReflectionMethod.this.getParameters();
            }

            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                return this.parameters.get(index).getType();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.parameters.size();
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return ReflectionUtils.javaToDexName(this.method.getReturnType().getName());
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return ImmutableSet.of();
    }
}
