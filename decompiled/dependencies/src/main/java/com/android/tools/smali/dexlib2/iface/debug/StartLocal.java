package com.android.tools.smali.dexlib2.iface.debug;

import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface StartLocal extends DebugItem, LocalInfo {
    @Nullable
    StringReference getNameReference();

    int getRegister();

    @Nullable
    StringReference getSignatureReference();

    @Nullable
    TypeReference getTypeReference();
}
