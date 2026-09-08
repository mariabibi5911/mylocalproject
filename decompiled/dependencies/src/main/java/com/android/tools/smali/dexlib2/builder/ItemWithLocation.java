package com.android.tools.smali.dexlib2.builder;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class ItemWithLocation {

    @Nullable
    MethodLocation location;

    public boolean isPlaced() {
        return this.location != null;
    }

    public void setLocation(MethodLocation methodLocation) {
        this.location = methodLocation;
    }
}
