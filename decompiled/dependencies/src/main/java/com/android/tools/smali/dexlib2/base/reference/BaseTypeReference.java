package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseTypeReference extends BaseReference implements TypeReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference
    public int hashCode() {
        return getType().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof TypeReference) {
                return getType().equals(((TypeReference) o).getType());
            }
            if (o instanceof CharSequence) {
                return getType().equals(o.toString());
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull CharSequence o) {
        return getType().compareTo(o.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getType().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getType().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getType().subSequence(start, end);
    }

    @Override // java.lang.CharSequence
    @Nonnull
    public String toString() {
        return DexFormatter.INSTANCE.getType(this);
    }
}
