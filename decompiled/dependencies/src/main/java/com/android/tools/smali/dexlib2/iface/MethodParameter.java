package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.iface.debug.LocalInfo;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface MethodParameter extends TypeReference, LocalInfo {
    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    String getName();

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    String getSignature();

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    String getType();
}
