package com.android.tools.smali.dexlib2.immutable.value;

import com.android.tools.smali.dexlib2.base.value.BaseArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableArrayEncodedValue extends BaseArrayEncodedValue implements ImmutableEncodedValue {

    @Nonnull
    protected final ImmutableList<? extends ImmutableEncodedValue> value;

    public ImmutableArrayEncodedValue(@Nonnull Collection<? extends EncodedValue> value) {
        this.value = ImmutableEncodedValueFactory.immutableListOf(value);
    }

    public ImmutableArrayEncodedValue(@Nonnull ImmutableList<ImmutableEncodedValue> value) {
        this.value = value;
    }

    public static ImmutableArrayEncodedValue of(@Nonnull ArrayEncodedValue arrayEncodedValue) {
        if (arrayEncodedValue instanceof ImmutableArrayEncodedValue) {
            return (ImmutableArrayEncodedValue) arrayEncodedValue;
        }
        return new ImmutableArrayEncodedValue(arrayEncodedValue.getValue());
    }

    @Override // com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue
    @Nonnull
    public ImmutableList<? extends ImmutableEncodedValue> getValue() {
        return this.value;
    }
}
