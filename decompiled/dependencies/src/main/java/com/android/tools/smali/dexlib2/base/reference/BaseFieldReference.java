package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseFieldReference extends BaseReference implements FieldReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference
    public int hashCode() {
        int hashCode = getDefiningClass().hashCode();
        return (((hashCode * 31) + getName().hashCode()) * 31) + getType().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.FieldReference
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof FieldReference)) {
            return false;
        }
        FieldReference other = (FieldReference) o;
        return getDefiningClass().equals(other.getDefiningClass()) && getName().equals(other.getName()) && getType().equals(other.getType());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull FieldReference o) {
        int res = getDefiningClass().compareTo(o.getDefiningClass());
        if (res != 0) {
            return res;
        }
        int res2 = getName().compareTo(o.getName());
        return res2 != 0 ? res2 : getType().compareTo(o.getType());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getFieldDescriptor(this);
    }
}
