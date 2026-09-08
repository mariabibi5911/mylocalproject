package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseNullEncodedValue;

/* loaded from: classes.dex */
public class ImmutableNullEncodedValue extends BaseNullEncodedValue implements ImmutableEncodedValue {
    public static final ImmutableNullEncodedValue INSTANCE = new ImmutableNullEncodedValue();

    private ImmutableNullEncodedValue() {
    }
}
