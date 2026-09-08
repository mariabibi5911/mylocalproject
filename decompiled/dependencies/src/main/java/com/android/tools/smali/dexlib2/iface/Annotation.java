package com.android.tools.smali.dexlib2.iface;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Annotation extends BasicAnnotation, Comparable<Annotation> {
    int compareTo(Annotation annotation);

    boolean equals(@Nullable Object obj);

    @Override // com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    Set<? extends AnnotationElement> getElements();

    @Override // com.android.tools.smali.dexlib2.iface.BasicAnnotation
    @Nonnull
    String getType();

    int getVisibility();

    int hashCode();

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.android.tools.smali.dexlib2.iface.Annotation$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
    }
}
