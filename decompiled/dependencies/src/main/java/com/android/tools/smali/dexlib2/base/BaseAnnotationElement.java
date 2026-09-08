package com.android.tools.smali.dexlib2.base;

import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import java.util.Comparator;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BaseAnnotationElement implements AnnotationElement {
    public static final Comparator<AnnotationElement> BY_NAME = new Comparator<AnnotationElement>() { // from class: com.android.tools.smali.dexlib2.base.BaseAnnotationElement.1
        @Override // java.util.Comparator
        public int compare(@Nonnull AnnotationElement element1, @Nonnull AnnotationElement element2) {
            return element1.getName().compareTo(element2.getName());
        }
    };

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    public int hashCode() {
        int hashCode = getName().hashCode();
        return (hashCode * 31) + getValue().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.AnnotationElement
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AnnotationElement)) {
            return false;
        }
        AnnotationElement other = (AnnotationElement) o;
        return getName().equals(other.getName()) && getValue().equals(other.getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(AnnotationElement o) {
        int res = getName().compareTo(o.getName());
        return res != 0 ? res : getValue().compareTo(o.getValue());
    }
}
