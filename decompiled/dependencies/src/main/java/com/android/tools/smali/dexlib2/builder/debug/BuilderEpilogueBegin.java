package com.android.tools.smali.dexlib2.builder.debug;

import com.android.tools.smali.dexlib2.builder.BuilderDebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EpilogueBegin;

/* loaded from: classes.dex */
public class BuilderEpilogueBegin extends BuilderDebugItem implements EpilogueBegin {
    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 8;
    }
}
