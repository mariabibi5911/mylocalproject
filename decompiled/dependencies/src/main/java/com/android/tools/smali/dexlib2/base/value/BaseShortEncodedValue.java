package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseShortEncodedValue implements ShortEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue
    public int hashCode() {
        return getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof ShortEncodedValue) && getValue() == ((ShortEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Shorts.compare(getValue(), ((ShortEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 2;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
