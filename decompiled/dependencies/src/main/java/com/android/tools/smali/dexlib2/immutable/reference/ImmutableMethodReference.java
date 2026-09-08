package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.immutable.util.CharSequenceConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMethodReference extends BaseMethodReference implements ImmutableReference {

    @Nonnull
    protected final String definingClass;

    @Nonnull
    protected final String name;

    @Nonnull
    protected final ImmutableList<String> parameters;

    @Nonnull
    protected final String returnType;

    public ImmutableMethodReference(@Nonnull String definingClass, @Nonnull String name, @Nullable Iterable<? extends CharSequence> parameters, @Nonnull String returnType) {
        this.definingClass = definingClass;
        this.name = name;
        this.parameters = CharSequenceConverter.immutableStringList(parameters);
        this.returnType = returnType;
    }

    public ImmutableMethodReference(@Nonnull String definingClass, @Nonnull String name, @Nullable ImmutableList<String> parameters, @Nonnull String returnType) {
        this.definingClass = definingClass;
        this.name = name;
        this.parameters = ImmutableUtils.nullToEmptyList(parameters);
        this.returnType = returnType;
    }

    @Nonnull
    public static ImmutableMethodReference of(@Nonnull MethodReference methodReference) {
        if (methodReference instanceof ImmutableMethodReference) {
            return (ImmutableMethodReference) methodReference;
        }
        return new ImmutableMethodReference(methodReference.getDefiningClass(), methodReference.getName(), methodReference.getParameterTypes(), methodReference.getReturnType());
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
    public ImmutableList<String> getParameterTypes() {
        return this.parameters;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.returnType;
    }
}
