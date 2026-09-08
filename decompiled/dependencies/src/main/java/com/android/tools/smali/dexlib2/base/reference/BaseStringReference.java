package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class BaseStringReference extends BaseReference implements StringReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
    public int hashCode() {
        return getString().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
    public boolean equals(@Nullable Object o) {
        if (o != null && (o instanceof StringReference)) {
            return getString().equals(((StringReference) o).getString());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(@Nonnull CharSequence o) {
        return getString().compareTo(o.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getString().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getString().subSequence(start, end);
    }

    @Override // java.lang.CharSequence
    @Nonnull
    public String toString() {
        return getString();
    }
}
