package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface Member extends Annotatable {
    int getAccessFlags();

    @Nonnull
    String getDefiningClass();

    @Nonnull
    Set<HiddenApiRestriction> getHiddenApiRestrictions();

    @Nonnull
    String getName();
}
