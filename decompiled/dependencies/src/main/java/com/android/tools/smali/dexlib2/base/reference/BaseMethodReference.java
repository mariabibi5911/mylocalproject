package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.util.CharSequenceUtils;
import com.android.tools.smali.util.CollectionUtils;
import com.google.common.collect.Ordering;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseMethodReference extends BaseReference implements MethodReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    public int hashCode() {
        int hashCode = getDefiningClass().hashCode();
        return (((((hashCode * 31) + getName().hashCode()) * 31) + getReturnType().hashCode()) * 31) + getParameterTypes().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
    public boolean equals(@Nullable Object o) {
        if (o == null || !(o instanceof MethodReference)) {
            return false;
        }
        MethodReference other = (MethodReference) o;
        return getDefiningClass().equals(other.getDefiningClass()) && getName().equals(other.getName()) && getReturnType().equals(other.getReturnType()) && CharSequenceUtils.listEquals(getParameterTypes(), other.getParameterTypes());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull MethodReference o) {
        int res = getDefiningClass().compareTo(o.getDefiningClass());
        if (res != 0) {
            return res;
        }
        int res2 = getName().compareTo(o.getName());
        if (res2 != 0) {
            return res2;
        }
        int res3 = getReturnType().compareTo(o.getReturnType());
        return res3 != 0 ? res3 : CollectionUtils.compareAsIterable(Ordering.usingToString(), getParameterTypes(), o.getParameterTypes());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getMethodDescriptor(this);
    }
}
