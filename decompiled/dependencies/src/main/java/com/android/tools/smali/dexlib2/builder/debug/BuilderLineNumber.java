package com.android.tools.smali.dexlib2.builder.debug;

import com.android.tools.smali.dexlib2.builder.BuilderDebugItem;
import com.android.tools.smali.dexlib2.iface.debug.LineNumber;

/* loaded from: classes.dex */
public class BuilderLineNumber extends BuilderDebugItem implements LineNumber {
    private final int lineNumber;

    public BuilderLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
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
