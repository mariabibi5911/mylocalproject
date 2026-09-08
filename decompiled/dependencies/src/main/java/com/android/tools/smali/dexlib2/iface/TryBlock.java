package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface TryBlock<EH extends ExceptionHandler> {
    boolean equals(@Nullable Object obj);

    int getCodeUnitCount();

    @Nonnull
    List<? extends EH> getExceptionHandlers();

    int getStartCodeAddress();
}
