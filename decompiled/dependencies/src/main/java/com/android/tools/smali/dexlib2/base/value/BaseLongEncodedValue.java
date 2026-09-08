package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.LongEncodedValue;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseLongEncodedValue implements LongEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.LongEncodedValue
    public int hashCode() {
        long value = getValue();
        int hashCode = (int) value;
        return (hashCode * 31) + ((int) (value >>> 32));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.LongEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof LongEncodedValue) && getValue() == ((LongEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Longs.compare(getValue(), ((LongEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 6;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
