package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseStringEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableStringEncodedValue extends BaseStringEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final String value;

    public ImmutableStringEncodedValue(@Nonnull String value) {
        this.value = value;
    }

    public static ImmutableStringEncodedValue of(@Nonnull StringEncodedValue stringEncodedValue) {
        if (stringEncodedValue instanceof ImmutableStringEncodedValue) {
            return (ImmutableStringEncodedValue) stringEncodedValue;
        }
        return new ImmutableStringEncodedValue(stringEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.StringEncodedValue
    @Nonnull
    public String getValue() {
        return this.value;
    }
}
