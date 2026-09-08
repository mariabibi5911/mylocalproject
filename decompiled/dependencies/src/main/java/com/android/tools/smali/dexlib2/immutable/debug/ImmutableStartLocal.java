package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.base.reference.BaseStringReference;
import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableStartLocal extends ImmutableDebugItem implements StartLocal {

    @Nullable
    protected final String name;
    protected final int register;

    @Nullable
    protected final String signature;

    @Nullable
    protected final String type;

    public ImmutableStartLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
        super(codeAddress);
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Nonnull
    public static ImmutableStartLocal of(@Nonnull StartLocal startLocal) {
        if (startLocal instanceof ImmutableStartLocal) {
            return (ImmutableStartLocal) startLocal;
        }
        return new ImmutableStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), startLocal.getName(), startLocal.getType(), startLocal.getSignature());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    public int getRegister() {
        return this.register;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getNameReference() {
        if (this.name == null) {
            return null;
        }
        return new BaseStringReference() { // from class: com.android.tools.smali.dexlib2.immutable.debug.ImmutableStartLocal.1
            @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableStartLocal.this.name;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public TypeReference getTypeReference() {
        if (this.type == null) {
            return null;
        }
        return new BaseTypeReference() { // from class: com.android.tools.smali.dexlib2.immutable.debug.ImmutableStartLocal.2
            @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return ImmutableStartLocal.this.type;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getSignatureReference() {
        if (this.signature == null) {
            return null;
        }
        return new BaseStringReference() { // from class: com.android.tools.smali.dexlib2.immutable.debug.ImmutableStartLocal.3
            @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableStartLocal.this.signature;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        return this.signature;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 3;
    }
}
