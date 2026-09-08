package com.android.tools.smali.dexlib2.builder;

import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class Label extends ItemWithLocation {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Label() {
    }

    Label(MethodLocation location) {
        this.location = location;
    }

    public int getCodeAddress() {
        return getLocation().getCodeAddress();
    }

    @Nonnull
    public MethodLocation getLocation() {
        if (this.location == null) {
            throw new IllegalStateException("Cannot get the location of a label that hasn't been placed yet.");
        }
        return this.location;
    }
}
