package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.util.CharSequenceUtils;
import com.android.tools.smali.util.CollectionUtils;
import com.google.common.collect.Ordering;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseMethodProtoReference extends BaseReference implements MethodProtoReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public int hashCode() {
        int hashCode = getReturnType().hashCode();
        return (hashCode * 31) + getParameterTypes().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof MethodProtoReference)) {
            return false;
        }
        MethodProtoReference other = (MethodProtoReference) o;
        return getReturnType().equals(other.getReturnType()) && CharSequenceUtils.listEquals(getParameterTypes(), other.getParameterTypes());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull MethodProtoReference o) {
        int res = getReturnType().compareTo(o.getReturnType());
        return res != 0 ? res : CollectionUtils.compareAsIterable(Ordering.usingToString(), getParameterTypes(), o.getParameterTypes());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getMethodProtoDescriptor(this);
    }
}
