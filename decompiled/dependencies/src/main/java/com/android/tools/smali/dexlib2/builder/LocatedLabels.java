package com.android.tools.smali.dexlib2.builder;

/* loaded from: classes.dex */
public class LocatedLabels extends LocatedItems<Label> {
    @Override // com.android.tools.smali.dexlib2.builder.LocatedItems
    protected String getAddLocatedItemError() {
        return "Cannot add a label that is already placed.You must remove it from its current location first.";
    }
}
