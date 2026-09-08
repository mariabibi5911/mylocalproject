package com.android.tools.smali.dexlib2.base;

import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.util.CollectionUtils;
import com.google.common.primitives.Ints;
import java.util.Comparator;

/* loaded from: classes.dex */
public abstract class BaseAnnotation implements Annotation {
    public static final Comparator<? super Annotation> BY_TYPE = new Comparator<Annotation>() { // from class: com.android.tools.smali.dexlib2.base.BaseAnnotation.1
        @Override // java.util.Comparator
        public int compare(Annotation annotation1, Annotation annotation2) {
            return annotation1.getType().compareTo(annotation2.getType());
        }
    };

    @Override // com.android.tools.smali.dexlib2.iface.Annotation
    public int hashCode() {
        int hashCode = getVisibility();
        return (((hashCode * 31) + getType().hashCode()) * 31) + getElements().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.Annotation
    public boolean equals(Object o) {
        if (!(o instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) o;
        return getVisibility() == other.getVisibility() && getType().equals(other.getType()) && getElements().equals(other.getElements());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(Annotation o) {
        int res = Ints.compare(getVisibility(), o.getVisibility());
        if (res != 0) {
            return res;
        }
        int res2 = getType().compareTo(o.getType());
        return res2 != 0 ? res2 : CollectionUtils.compareAsSet(getElements(), o.getElements());
    }
}
