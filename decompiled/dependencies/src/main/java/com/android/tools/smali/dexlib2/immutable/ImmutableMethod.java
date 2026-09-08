package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMethod extends BaseMethodReference implements Method {
    private static final ImmutableConverter<ImmutableMethod, Method> CONVERTER = new ImmutableConverter<ImmutableMethod, Method>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableMethod.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull Method item) {
            return item instanceof ImmutableMethod;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableMethod makeImmutable(@Nonnull Method item) {
            return ImmutableMethod.of(item);
        }
    };
    protected final int accessFlags;

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotation> annotations;

    @Nonnull
    protected final String definingClass;

    @Nonnull
    protected final ImmutableSet<HiddenApiRestriction> hiddenApiRestrictions;

    @Nullable
    protected final ImmutableMethodImplementation methodImplementation;

    @Nonnull
    protected final String name;

    @Nonnull
    protected final ImmutableList<? extends ImmutableMethodParameter> parameters;

    @Nonnull
    protected final String returnType;

    public ImmutableMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable Iterable<? extends MethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nullable Set<? extends Annotation> annotations, @Nullable Set<HiddenApiRestriction> hiddenApiRestrictions, @Nullable MethodImplementation methodImplementation) {
        this.definingClass = definingClass;
        this.name = name;
        this.parameters = ImmutableMethodParameter.immutableListOf(parameters);
        this.returnType = returnType;
        this.accessFlags = accessFlags;
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.hiddenApiRestrictions = hiddenApiRestrictions == null ? ImmutableSet.of() : ImmutableSet.copyOf((Collection) hiddenApiRestrictions);
        this.methodImplementation = ImmutableMethodImplementation.of(methodImplementation);
    }

    public ImmutableMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable ImmutableList<? extends ImmutableMethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable ImmutableSet<HiddenApiRestriction> hiddenApiRestrictions, @Nullable ImmutableMethodImplementation methodImplementation) {
        this.definingClass = definingClass;
        this.name = name;
        this.parameters = ImmutableUtils.nullToEmptyList(parameters);
        this.returnType = returnType;
        this.accessFlags = accessFlags;
        this.annotations = ImmutableUtils.nullToEmptySet(annotations);
        this.hiddenApiRestrictions = ImmutableUtils.nullToEmptySet(hiddenApiRestrictions);
        this.methodImplementation = methodImplementation;
    }

    public static ImmutableMethod of(Method method) {
        if (method instanceof ImmutableMethod) {
            return (ImmutableMethod) method;
        }
        return new ImmutableMethod(method.getDefiningClass(), method.getName(), method.getParameters(), method.getReturnType(), method.getAccessFlags(), method.getAnnotations(), method.getHiddenApiRestrictions(), method.getImplementation());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    @Nonnull
    public ImmutableList<? extends CharSequence> getParameterTypes() {
        return this.parameters;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public ImmutableList<? extends ImmutableMethodParameter> getParameters() {
        return this.parameters;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.returnType;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.hiddenApiRestrictions;
    }

    @Override // com.android.tools.smali.dexlib2.iface.Method
    @Nullable
    public ImmutableMethodImplementation getImplementation() {
        return this.methodImplementation;
    }

    @Nonnull
    public static ImmutableSortedSet<ImmutableMethod> immutableSetOf(@Nullable Iterable<? extends Method> list) {
        return CONVERTER.toSortedSet(Ordering.natural(), list);
    }
}
