package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseMethodHandleEncodedValue implements MethodHandleEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
    public boolean equals(Object o) {
        if (o instanceof MethodHandleEncodedValue) {
            return getValue().equals(((MethodHandleEncodedValue) o).getValue());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : getValue().compareTo(((MethodHandleEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 22;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
