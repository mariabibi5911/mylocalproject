package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseCharEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.CharEncodedValue;

/* loaded from: classes.dex */
public class ImmutableCharEncodedValue extends BaseCharEncodedValue implements ImmutableEncodedValue {
    protected final char value;

    public ImmutableCharEncodedValue(char value) {
        this.value = value;
    }

    public static ImmutableCharEncodedValue of(CharEncodedValue charEncodedValue) {
        if (charEncodedValue instanceof ImmutableCharEncodedValue) {
            return (ImmutableCharEncodedValue) charEncodedValue;
        }
        return new ImmutableCharEncodedValue(charEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.CharEncodedValue
    public char getValue() {
        return this.value;
    }
}
