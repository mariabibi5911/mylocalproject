package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseMethodHandleReference extends BaseReference implements MethodHandleReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
    public int hashCode() {
        int hashCode = getMethodHandleType();
        return (hashCode * 31) + getMemberReference().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference
    public boolean equals(Object o) {
        if (o == null || !(o instanceof MethodHandleReference)) {
            return false;
        }
        MethodHandleReference other = (MethodHandleReference) o;
        return getMethodHandleType() == other.getMethodHandleType() && getMemberReference().equals(other.getMemberReference());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull MethodHandleReference o) {
        int res = Ints.compare(getMethodHandleType(), o.getMethodHandleType());
        if (res != 0) {
            return res;
        }
        Reference reference = getMemberReference();
        if (reference instanceof FieldReference) {
            if (!(o.getMemberReference() instanceof FieldReference)) {
                return -1;
            }
            return ((FieldReference) reference).compareTo((FieldReference) o.getMemberReference());
        }
        if (!(o.getMemberReference() instanceof MethodReference)) {
            return 1;
        }
        return ((MethodReference) reference).compareTo((MethodReference) o.getMemberReference());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getMethodHandle(this);
    }
}
