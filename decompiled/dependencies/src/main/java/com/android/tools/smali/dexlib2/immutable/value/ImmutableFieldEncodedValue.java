package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseFieldEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableFieldEncodedValue extends BaseFieldEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableFieldReference value;

    public ImmutableFieldEncodedValue(@Nonnull ImmutableFieldReference value) {
        this.value = value;
    }

    public static ImmutableFieldEncodedValue of(@Nonnull FieldEncodedValue fieldEncodedValue) {
        if (fieldEncodedValue instanceof ImmutableFieldEncodedValue) {
            return (ImmutableFieldEncodedValue) fieldEncodedValue;
        }
        return new ImmutableFieldEncodedValue(ImmutableFieldReference.of(fieldEncodedValue.getValue()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue
    @Nonnull
    public ImmutableFieldReference getValue() {
        return this.value;
    }
}
