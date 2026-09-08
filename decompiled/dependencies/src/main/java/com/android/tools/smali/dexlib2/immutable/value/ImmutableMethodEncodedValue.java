package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseMethodEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableMethodEncodedValue extends BaseMethodEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableMethodReference value;

    public ImmutableMethodEncodedValue(@Nonnull ImmutableMethodReference value) {
        this.value = value;
    }

    public static ImmutableMethodEncodedValue of(@Nonnull MethodEncodedValue methodEncodedValue) {
        if (methodEncodedValue instanceof ImmutableMethodEncodedValue) {
            return (ImmutableMethodEncodedValue) methodEncodedValue;
        }
        return new ImmutableMethodEncodedValue(ImmutableMethodReference.of(methodEncodedValue.getValue()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue
    @Nonnull
    public ImmutableMethodReference getValue() {
        return this.value;
    }
}
