package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseEnumEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableEnumEncodedValue extends BaseEnumEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableFieldReference value;

    public ImmutableEnumEncodedValue(@Nonnull ImmutableFieldReference value) {
        this.value = value;
    }

    public static ImmutableEnumEncodedValue of(EnumEncodedValue enumEncodedValue) {
        if (enumEncodedValue instanceof ImmutableEnumEncodedValue) {
            return (ImmutableEnumEncodedValue) enumEncodedValue;
        }
        return new ImmutableEnumEncodedValue(ImmutableFieldReference.of(enumEncodedValue.getValue()));
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue
    @Nonnull
    public ImmutableFieldReference getValue() {
        return this.value;
    }
}
