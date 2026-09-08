package com.android.tools.smali.dexlib2.base.value;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.util.CollectionUtils;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseAnnotationEncodedValue implements AnnotationEncodedValue {
    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue
    public int hashCode() {
        int hashCode = getType().hashCode();
        return (hashCode * 31) + getElements().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof AnnotationEncodedValue)) {
            return false;
        }
        AnnotationEncodedValue other = (AnnotationEncodedValue) o;
        return getType().equals(other.getType()) && getElements().equals(other.getElements());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        if (res != 0) {
            return res;
        }
        AnnotationEncodedValue other = (AnnotationEncodedValue) o;
        int res2 = getType().compareTo(other.getType());
        return res2 != 0 ? res2 : CollectionUtils.compareAsSet(getElements(), other.getElements());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 29;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
