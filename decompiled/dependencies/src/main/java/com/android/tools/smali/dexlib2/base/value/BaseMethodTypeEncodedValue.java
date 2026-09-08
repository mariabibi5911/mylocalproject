package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseMethodTypeEncodedValue implements MethodTypeEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
    public boolean equals(Object o) {
        if (o instanceof MethodTypeEncodedValue) {
            return getValue().equals(((MethodTypeEncodedValue) o).getValue());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : getValue().compareTo(((MethodTypeEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 21;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
