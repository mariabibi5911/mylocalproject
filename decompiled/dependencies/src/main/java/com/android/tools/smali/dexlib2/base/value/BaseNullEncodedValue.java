package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.NullEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseNullEncodedValue implements NullEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.NullEncodedValue
    public int hashCode() {
        return 0;
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.NullEncodedValue
    public boolean equals(@Nullable Object o) {
        return o instanceof NullEncodedValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        return Ints.compare(getValueType(), o.getValueType());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 30;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
