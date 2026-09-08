package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseMethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodHandleReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableMethodHandleEncodedValue extends BaseMethodHandleEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableMethodHandleReference methodHandleReference;

    public ImmutableMethodHandleEncodedValue(@Nonnull ImmutableMethodHandleReference methodHandleReference) {
        this.methodHandleReference = methodHandleReference;
    }

    @Nonnull
    public static ImmutableMethodHandleEncodedValue of(@Nonnull MethodHandleEncodedValue methodHandleEncodedValue) {
        if (methodHandleEncodedValue instanceof ImmutableMethodHandleEncodedValue) {
            return (ImmutableMethodHandleEncodedValue) methodHandleEncodedValue;
        }
        return new ImmutableMethodHandleEncodedValue(ImmutableMethodHandleReference.of(methodHandleEncodedValue.getValue()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
    @Nonnull
    public ImmutableMethodHandleReference getValue() {
        return this.methodHandleReference;
    }
}
