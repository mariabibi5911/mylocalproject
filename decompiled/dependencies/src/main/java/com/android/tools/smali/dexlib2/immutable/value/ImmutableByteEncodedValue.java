package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseByteEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue;

/* loaded from: classes.dex */
public class ImmutableByteEncodedValue extends BaseByteEncodedValue implements ImmutableEncodedValue {
    protected final byte value;

    public ImmutableByteEncodedValue(byte value) {
        this.value = value;
    }

    public static ImmutableByteEncodedValue of(ByteEncodedValue byteEncodedValue) {
        if (byteEncodedValue instanceof ImmutableByteEncodedValue) {
            return (ImmutableByteEncodedValue) byteEncodedValue;
        }
        return new ImmutableByteEncodedValue(byteEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue
    public byte getValue() {
        return this.value;
    }
}
