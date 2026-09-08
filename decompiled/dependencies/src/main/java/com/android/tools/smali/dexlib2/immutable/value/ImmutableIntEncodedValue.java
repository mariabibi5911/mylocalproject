package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseIntEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue;

/* loaded from: classes.dex */
public class ImmutableIntEncodedValue extends BaseIntEncodedValue implements ImmutableEncodedValue {
    protected final int value;

    public ImmutableIntEncodedValue(int value) {
        this.value = value;
    }

    public static ImmutableIntEncodedValue of(IntEncodedValue intEncodedValue) {
        if (intEncodedValue instanceof ImmutableIntEncodedValue) {
            return (ImmutableIntEncodedValue) intEncodedValue;
        }
        return new ImmutableIntEncodedValue(intEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.IntEncodedValue
    public int getValue() {
        return this.value;
    }
}
