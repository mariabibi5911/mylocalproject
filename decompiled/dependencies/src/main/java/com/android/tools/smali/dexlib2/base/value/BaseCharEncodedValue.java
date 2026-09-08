package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.CharEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseCharEncodedValue implements CharEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.CharEncodedValue
    public int hashCode() {
        return getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.CharEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof CharEncodedValue) && getValue() == ((CharEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Chars.compare(getValue(), ((CharEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 3;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
