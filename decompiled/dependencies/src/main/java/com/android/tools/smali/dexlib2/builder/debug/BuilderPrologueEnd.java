package com.android.tools.smali.dexlib2.builder.debug;

import com.android.tools.smali.dexlib2.builder.BuilderDebugItem;
import com.android.tools.smali.dexlib2.iface.debug.PrologueEnd;

/* loaded from: classes.dex */
public class BuilderPrologueEnd extends BuilderDebugItem implements PrologueEnd {
    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 7;
    }
}
