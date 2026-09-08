package com.android.tools.smali.dexlib2.base.reference;

import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;

/* loaded from: classes.dex */
public abstract class BaseCallSiteReference extends BaseReference implements CallSiteReference {
    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    public int hashCode() {
        int hashCode = getName().hashCode();
        return (((((((hashCode * 31) + getMethodHandle().hashCode()) * 31) + getMethodName().hashCode()) * 31) + getMethodProto().hashCode()) * 31) + getExtraArguments().hashCode();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    public boolean equals(Object o) {
        if (o == null || !(o instanceof CallSiteReference)) {
            return false;
        }
        CallSiteReference other = (CallSiteReference) o;
        return getMethodHandle().equals(other.getMethodHandle()) && getMethodName().equals(other.getMethodName()) && getMethodProto().equals(other.getMethodProto()) && getExtraArguments().equals(other.getExtraArguments());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getCallSite(this);
    }
}
