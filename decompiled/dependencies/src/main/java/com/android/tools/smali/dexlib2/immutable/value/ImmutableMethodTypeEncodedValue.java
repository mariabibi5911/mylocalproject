package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseMethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodProtoReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableMethodTypeEncodedValue extends BaseMethodTypeEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableMethodProtoReference methodProtoReference;

    public ImmutableMethodTypeEncodedValue(@Nonnull ImmutableMethodProtoReference methodProtoReference) {
        this.methodProtoReference = methodProtoReference;
    }

    @Nonnull
    public static ImmutableMethodTypeEncodedValue of(@Nonnull MethodTypeEncodedValue methodTypeEncodedValue) {
        if (methodTypeEncodedValue instanceof ImmutableMethodTypeEncodedValue) {
            return (ImmutableMethodTypeEncodedValue) methodTypeEncodedValue;
        }
        return new ImmutableMethodTypeEncodedValue(ImmutableMethodProtoReference.of(methodTypeEncodedValue.getValue()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
    @Nonnull
    public ImmutableMethodProtoReference getValue() {
        return this.methodProtoReference;
    }
}
