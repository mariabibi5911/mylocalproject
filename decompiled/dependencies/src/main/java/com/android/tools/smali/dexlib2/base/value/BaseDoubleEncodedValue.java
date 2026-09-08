package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseDoubleEncodedValue implements DoubleEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue
    public int hashCode() {
        long v = Double.doubleToRawLongBits(getValue());
        return (int) ((v >>> 32) ^ v);
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof DoubleEncodedValue) && Double.doubleToRawLongBits(getValue()) == Double.doubleToRawLongBits(((DoubleEncodedValue) o).getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Double.compare(getValue(), ((DoubleEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 17;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
