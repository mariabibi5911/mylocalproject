package com.android.tools.smali.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface Annotatable {
    @Nonnull
    Set<? extends Annotation> getAnnotations();
}
