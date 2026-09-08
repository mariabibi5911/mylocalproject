package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseCallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableCallSiteReference extends BaseCallSiteReference implements ImmutableReference {

    @Nonnull
    protected final ImmutableList<? extends ImmutableEncodedValue> extraArguments;

    @Nonnull
    protected final ImmutableMethodHandleReference methodHandle;

    @Nonnull
    protected final String methodName;

    @Nonnull
    protected final ImmutableMethodProtoReference methodProto;

    @Nonnull
    protected final String name;

    public ImmutableCallSiteReference(@Nonnull String name, @Nonnull MethodHandleReference methodHandle, @Nonnull String methodName, @Nonnull MethodProtoReference methodProto, @Nonnull Iterable<? extends EncodedValue> extraArguments) {
        this.name = name;
        this.methodHandle = ImmutableMethodHandleReference.of(methodHandle);
        this.methodName = methodName;
        this.methodProto = ImmutableMethodProtoReference.of(methodProto);
        this.extraArguments = ImmutableEncodedValueFactory.immutableListOf(extraArguments);
    }

    public ImmutableCallSiteReference(@Nonnull String name, @Nonnull ImmutableMethodHandleReference methodHandle, @Nonnull String methodName, @Nonnull ImmutableMethodProtoReference methodProto, @Nullable ImmutableList<? extends ImmutableEncodedValue> extraArguments) {
        this.name = name;
        this.methodHandle = methodHandle;
        this.methodName = methodName;
        this.methodProto = methodProto;
        this.extraArguments = ImmutableUtils.nullToEmptyList(extraArguments);
    }

    @Nonnull
    public static ImmutableCallSiteReference of(@Nonnull CallSiteReference callSiteReference) {
        if (callSiteReference instanceof ImmutableCallSiteReference) {
            return (ImmutableCallSiteReference) callSiteReference;
        }
        return new ImmutableCallSiteReference(callSiteReference.getName(), ImmutableMethodHandleReference.of(callSiteReference.getMethodHandle()), callSiteReference.getMethodName(), ImmutableMethodProtoReference.of(callSiteReference.getMethodProto()), (ImmutableList<? extends ImmutableEncodedValue>) ImmutableEncodedValueFactory.immutableListOf(callSiteReference.getExtraArguments()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodHandleReference getMethodHandle() {
        return this.methodHandle;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getMethodName() {
        return this.methodName;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodProtoReference getMethodProto() {
        return this.methodProto;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public List<? extends EncodedValue> getExtraArguments() {
        return this.extraArguments;
    }
}
