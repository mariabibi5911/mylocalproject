package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableEndLocal extends ImmutableDebugItem implements EndLocal {

    @Nullable
    protected final String name;
    protected final int register;

    @Nullable
    protected final String signature;

    @Nullable
    protected final String type;

    public ImmutableEndLocal(int codeAddress, int register) {
        super(codeAddress);
        this.register = register;
        this.name = null;
        this.type = null;
        this.signature = null;
    }

    public ImmutableEndLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
        super(codeAddress);
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Nonnull
    public static ImmutableEndLocal of(@Nonnull EndLocal endLocal) {
        if (endLocal instanceof ImmutableEndLocal) {
            return (ImmutableEndLocal) endLocal;
        }
        return new ImmutableEndLocal(endLocal.getCodeAddress(), endLocal.getRegister(), endLocal.getType(), endLocal.getName(), endLocal.getSignature());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.EndLocal
    public int getRegister() {
        return this.register;
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
        return 5;
    }
}
