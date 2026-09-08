package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseIntEncodedValue implements IntEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.IntEncodedValue
    public int hashCode() {
        return getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.IntEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof IntEncodedValue) && getValue() == ((IntEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Ints.compare(getValue(), ((IntEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 4;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
