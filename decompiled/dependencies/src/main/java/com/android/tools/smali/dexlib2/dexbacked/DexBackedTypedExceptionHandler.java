package com.android.tools.smali.dexlib2.dexbacked;

import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedTypedExceptionHandler extends DexBackedExceptionHandler {

    @Nonnull
    private final DexBackedDexFile dexFile;
    private final int handlerCodeAddress;
    private final int typeId;

    public DexBackedTypedExceptionHandler(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.typeId = reader.readSmallUleb128();
        this.handlerCodeAddress = reader.readSmallUleb128();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    @Nonnull
    public String getExceptionType() {
        return (String) this.dexFile.getTypeSection().get(this.typeId);
    }

    @Override // com.android.tools.smali.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
