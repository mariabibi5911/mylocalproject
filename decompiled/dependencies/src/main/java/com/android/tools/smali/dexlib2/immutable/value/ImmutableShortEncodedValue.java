package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseShortEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue;

/* loaded from: classes.dex */
public class ImmutableShortEncodedValue extends BaseShortEncodedValue implements ImmutableEncodedValue {
    protected final short value;

    public ImmutableShortEncodedValue(short value) {
        this.value = value;
    }

    public static ImmutableShortEncodedValue of(ShortEncodedValue shortEncodedValue) {
        if (shortEncodedValue instanceof ImmutableShortEncodedValue) {
            return (ImmutableShortEncodedValue) shortEncodedValue;
        }
        return new ImmutableShortEncodedValue(shortEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue
    public short getValue() {
        return this.value;
    }
}
