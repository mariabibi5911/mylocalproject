package com.android.tools.smali.dexlib2.iface.debug;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface LocalInfo {
    @Nullable
    String getName();

    @Nullable
    String getSignature();

    @Nullable
    String getType();
}
