package com.android.tools.smali.dexlib2.iface.value;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.BasicAnnotation;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface AnnotationEncodedValue extends EncodedValue, BasicAnnotation {
    int compareTo(@Nonnull EncodedValue encodedValue);

    boolean equals(@Nullable Object obj);

    @Nonnull
    Set<? extends AnnotationElement> getElements();

    @Nonnull
    String getType();

    int hashCode();

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
    }
}
