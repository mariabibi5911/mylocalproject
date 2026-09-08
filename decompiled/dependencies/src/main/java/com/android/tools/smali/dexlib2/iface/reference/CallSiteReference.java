package com.android.tools.smali.dexlib2.iface.reference;

import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface CallSiteReference extends Reference {
    boolean equals(@Nullable Object obj);

    @Nonnull
    List<? extends EncodedValue> getExtraArguments();

    @Nonnull
    MethodHandleReference getMethodHandle();

    @Nonnull
    String getMethodName();

    @Nonnull
    MethodProtoReference getMethodProto();

    @Nonnull
    String getName();

    int hashCode();
}
