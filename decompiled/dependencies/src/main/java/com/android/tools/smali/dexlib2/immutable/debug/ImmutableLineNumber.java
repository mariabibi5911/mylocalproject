package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.iface.debug.LineNumber;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableLineNumber extends ImmutableDebugItem implements LineNumber {
    protected final int lineNumber;

    public ImmutableLineNumber(int codeAddress, int lineNumber) {
        super(codeAddress);
        this.lineNumber = lineNumber;
    }

    @Nonnull
    public static ImmutableLineNumber of(@Nonnull LineNumber lineNumber) {
        if (lineNumber instanceof ImmutableLineNumber) {
            return (ImmutableLineNumber) lineNumber;
        }
        return new ImmutableLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LineNumber
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 10;
    }
}
