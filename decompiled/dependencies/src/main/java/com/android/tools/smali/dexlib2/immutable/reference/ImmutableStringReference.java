package com.android.tools.smali.dexlib2.immutable.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseStringReference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ImmutableStringReference extends BaseStringReference implements ImmutableReference {

    @Nonnull
    protected final String str;

    public ImmutableStringReference(String str) {
        this.str = str;
    }

    @Nonnull
    public static ImmutableStringReference of(@Nonnull StringReference stringReference) {
        if (stringReference instanceof ImmutableStringReference) {
            return (ImmutableStringReference) stringReference;
        }
        return new ImmutableStringReference(stringReference.getString());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return this.str;
    }
}
