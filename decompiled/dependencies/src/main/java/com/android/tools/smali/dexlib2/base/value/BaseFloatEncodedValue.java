package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseFloatEncodedValue implements FloatEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue
    public int hashCode() {
        return Float.floatToRawIntBits(getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue
    public boolean equals(@Nullable Object o) {
        return o != null && (o instanceof FloatEncodedValue) && Float.floatToRawIntBits(getValue()) == Float.floatToRawIntBits(((FloatEncodedValue) o).getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Float.compare(getValue(), ((FloatEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 16;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
