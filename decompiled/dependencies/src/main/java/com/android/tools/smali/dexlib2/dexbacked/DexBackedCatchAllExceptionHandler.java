package com.android.tools.smali.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexBackedCatchAllExceptionHandler extends DexBackedExceptionHandler {
    private final int handlerCodeAddress;

    public DexBackedCatchAllExceptionHandler(@Nonnull DexReader reader) {
        this.handlerCodeAddress = reader.readSmallUleb128();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
