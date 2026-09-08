package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.iface.debug.RestartLocal;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableRestartLocal extends ImmutableDebugItem implements RestartLocal {

    @Nullable
    protected final String name;
    protected final int register;

    @Nullable
    protected final String signature;

    @Nullable
    protected final String type;

    public ImmutableRestartLocal(int codeAddress, int register) {
        super(codeAddress);
        this.register = register;
        this.name = null;
        this.type = null;
        this.signature = null;
    }

    public ImmutableRestartLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
        super(codeAddress);
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Nonnull
    public static ImmutableRestartLocal of(@Nonnull RestartLocal restartLocal) {
        if (restartLocal instanceof ImmutableRestartLocal) {
            return (ImmutableRestartLocal) restartLocal;
        }
        return new ImmutableRestartLocal(restartLocal.getCodeAddress(), restartLocal.getRegister(), restartLocal.getType(), restartLocal.getName(), restartLocal.getSignature());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.RestartLocal
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
        return 6;
    }
}
