package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.iface.debug.PrologueEnd;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutablePrologueEnd extends ImmutableDebugItem implements PrologueEnd {
    public ImmutablePrologueEnd(int codeAddress) {
        super(codeAddress);
    }

    @Nonnull
    public static ImmutablePrologueEnd of(@Nonnull PrologueEnd prologueEnd) {
        if (prologueEnd instanceof ImmutablePrologueEnd) {
            return (ImmutablePrologueEnd) prologueEnd;
        }
        return new ImmutablePrologueEnd(prologueEnd.getCodeAddress());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 7;
    }
}
