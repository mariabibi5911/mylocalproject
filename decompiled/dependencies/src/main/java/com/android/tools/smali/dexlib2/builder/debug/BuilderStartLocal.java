package com.android.tools.smali.dexlib2.builder.debug;

import com.android.tools.smali.dexlib2.builder.BuilderDebugItem;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderStartLocal extends BuilderDebugItem implements StartLocal {

    @Nullable
    private final StringReference name;
    private final int register;

    @Nullable
    private final StringReference signature;

    @Nullable
    private final TypeReference type;

    public BuilderStartLocal(int register, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
        this.register = register;
        this.name = name;
        this.type = type;
        this.signature = signature;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    public int getRegister() {
        return this.register;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getNameReference() {
        return this.name;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public TypeReference getTypeReference() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
    @Nullable
    public StringReference getSignatureReference() {
        return this.signature;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        StringReference stringReference = this.name;
        if (stringReference == null) {
            return null;
        }
        return stringReference.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getType() {
        TypeReference typeReference = this.type;
        if (typeReference == null) {
            return null;
        }
        return typeReference.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        StringReference stringReference = this.signature;
        if (stringReference == null) {
            return null;
        }
        return stringReference.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 3;
    }
}
