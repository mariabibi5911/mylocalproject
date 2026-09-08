package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.iface.debug.EpilogueBegin;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableEpilogueBegin extends ImmutableDebugItem implements EpilogueBegin {
    public ImmutableEpilogueBegin(int codeAddress) {
        super(codeAddress);
    }

    @Nonnull
    public static ImmutableEpilogueBegin of(@Nonnull EpilogueBegin epilogueBegin) {
        if (epilogueBegin instanceof ImmutableEpilogueBegin) {
            return (ImmutableEpilogueBegin) epilogueBegin;
        }
        return new ImmutableEpilogueBegin(epilogueBegin.getCodeAddress());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 8;
    }
}
