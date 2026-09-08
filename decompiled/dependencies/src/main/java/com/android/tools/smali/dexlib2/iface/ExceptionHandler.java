package com.android.tools.smali.dexlib2.iface;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ExceptionHandler extends Comparable<ExceptionHandler> {
    int compareTo(@Nonnull ExceptionHandler exceptionHandler);

    boolean equals(@Nullable Object obj);

    @Nullable
    String getExceptionType();

    @Nullable
    TypeReference getExceptionTypeReference();

    int getHandlerCodeAddress();

    int hashCode();

    @SynthesizedClassV2(kind = 8, versionHash = "b33e07cc0d03f9f0e6c4c883743d0373fd130388f5a551bfa15ea60a927a2ecb")
    /* renamed from: com.android.tools.smali.dexlib2.iface.ExceptionHandler$-CC, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
    }
}
