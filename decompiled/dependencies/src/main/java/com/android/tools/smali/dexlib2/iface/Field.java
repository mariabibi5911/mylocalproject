package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Field extends FieldReference, Member {
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
    EncodedValue getInitialValue();

    @Override // com.android.tools.smali.dexlib2.iface.Member
    @Nonnull
    String getName();

    @Nonnull
    String getType();
}
