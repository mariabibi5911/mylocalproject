package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseByteEncodedValue implements ByteEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue
    public int hashCode() {
        return getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof ByteEncodedValue) && getValue() == ((ByteEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Ints.compare(getValue(), ((ByteEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 0;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
