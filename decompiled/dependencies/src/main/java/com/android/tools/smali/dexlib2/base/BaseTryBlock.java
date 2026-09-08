package com.android.tools.smali.dexlib2.base;

import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.TryBlock;

/* loaded from: classes.dex */
public abstract class BaseTryBlock<EH extends ExceptionHandler> implements TryBlock<EH> {
    @Override // com.android.tools.smali.dexlib2.iface.TryBlock
    public boolean equals(Object o) {
        if (!(o instanceof TryBlock)) {
            return false;
        }
        TryBlock other = (TryBlock) o;
        return getStartCodeAddress() == other.getStartCodeAddress() && getCodeUnitCount() == other.getCodeUnitCount() && getExceptionHandlers().equals(other.getExceptionHandlers());
    }
}
