package com.android.tools.smali.dexlib2.builder;

import com.android.tools.smali.dexlib2.iface.debug.DebugItem;

/* loaded from: classes.dex */
public abstract class BuilderDebugItem extends ItemWithLocation implements DebugItem {
    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getCodeAddress() {
        if (this.location == null) {
            throw new IllegalStateException("Cannot get the address of a BuilderDebugItem that isn't associated with a method.");
        }
        return this.location.getCodeAddress();
    }
}
