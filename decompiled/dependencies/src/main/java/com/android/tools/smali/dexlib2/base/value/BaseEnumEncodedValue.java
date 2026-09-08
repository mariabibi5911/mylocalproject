package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseEnumEncodedValue implements EnumEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue
    public boolean equals(@Nullable Object o) {
        if (o instanceof EnumEncodedValue) {
            return getValue().equals(((EnumEncodedValue) o).getValue());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : getValue().compareTo(((EnumEncodedValue) o).getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 27;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
