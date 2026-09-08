package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseMethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.immutable.util.CharSequenceConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMethodProtoReference extends BaseMethodProtoReference implements ImmutableReference {

    @Nonnull
    protected final ImmutableList<String> parameters;

    @Nonnull
    protected final String returnType;

    public ImmutableMethodProtoReference(@Nullable ImmutableList<String> parameters, @Nonnull String returnType) {
        this.parameters = ImmutableUtils.nullToEmptyList(parameters);
        this.returnType = returnType;
    }

    public ImmutableMethodProtoReference(@Nullable Iterable<? extends CharSequence> parameters, @Nonnull String returnType) {
        this.parameters = CharSequenceConverter.immutableStringList(parameters);
        this.returnType = returnType;
    }

    @Nonnull
    public static ImmutableMethodProtoReference of(@Nonnull MethodProtoReference methodProtoReference) {
        if (methodProtoReference instanceof ImmutableMethodProtoReference) {
            return (ImmutableMethodProtoReference) methodProtoReference;
        }
        return new ImmutableMethodProtoReference(methodProtoReference.getParameterTypes(), methodProtoReference.getReturnType());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public List<? extends CharSequence> getParameterTypes() {
        return this.parameters;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public String getReturnType() {
        return this.returnType;
    }
}
