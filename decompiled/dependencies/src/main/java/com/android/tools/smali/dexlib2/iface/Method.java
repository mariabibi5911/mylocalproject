package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Method extends MethodReference, Member {
    @Override // com.android.tools.smali.dexlib2.iface.Member
    int getAccessFlags();

    @Override // com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Override // com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    String getDefiningClass();

    @Override // com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    Set<HiddenApiRestriction> getHiddenApiRestrictions();

    @Nullable
    MethodImplementation getImplementation();

    @Override // com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    String getName();

    @Nonnull
    List<? extends MethodParameter> getParameters();

    @Nonnull
    String getReturnType();
}
