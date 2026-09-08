package com.android.tools.smali.dexlib2.rewriter;

import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface Rewriter<T> {
    @Nonnull
    T rewrite(@Nonnull T t);
}
