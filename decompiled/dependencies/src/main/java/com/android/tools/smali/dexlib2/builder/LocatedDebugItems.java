package com.android.tools.smali.dexlib2.builder;

/* loaded from: classes.dex */
public class LocatedDebugItems extends LocatedItems<BuilderDebugItem> {
    @Override // com.android.tools.smali.dexlib2.builder.LocatedItems
    protected String getAddLocatedItemError() {
        return "Cannot add a debug item that has already been added to a method.You must remove it from its current location first.";
    }
}
