package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseLongEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.LongEncodedValue;

/* loaded from: classes.dex */
public class ImmutableLongEncodedValue extends BaseLongEncodedValue implements ImmutableEncodedValue {
    protected final long value;

    public ImmutableLongEncodedValue(long value) {
        this.value = value;
    }

    public static ImmutableLongEncodedValue of(LongEncodedValue longEncodedValue) {
        if (longEncodedValue instanceof ImmutableLongEncodedValue) {
            return (ImmutableLongEncodedValue) longEncodedValue;
        }
        return new ImmutableLongEncodedValue(longEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.LongEncodedValue
    public long getValue() {
        return this.value;
    }
}
