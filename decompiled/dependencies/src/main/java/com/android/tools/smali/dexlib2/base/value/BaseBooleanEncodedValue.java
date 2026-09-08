package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseBooleanEncodedValue implements BooleanEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue
    public int hashCode() {
        return getValue() ? 1 : 0;
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof BooleanEncodedValue) && getValue() == ((BooleanEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Booleans.compare(getValue(), ((BooleanEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 31;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
