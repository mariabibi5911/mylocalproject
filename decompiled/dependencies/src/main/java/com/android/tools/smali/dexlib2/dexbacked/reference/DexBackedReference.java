package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class DexBackedReference {
    public static Reference makeReference(@Nonnull DexBackedDexFile dexFile, int referenceType, int referenceIndex) {
        switch (referenceType) {
            case 0:
                return new DexBackedStringReference(dexFile, referenceIndex);
            case 1:
                return new DexBackedTypeReference(dexFile, referenceIndex);
            case 2:
                return new DexBackedFieldReference(dexFile, referenceIndex);
            case 3:
                return new DexBackedMethodReference(dexFile, referenceIndex);
            case 4:
                return new DexBackedMethodProtoReference(dexFile, referenceIndex);
            case 5:
                return new DexBackedCallSiteReference(dexFile, referenceIndex);
            case 6:
                return new DexBackedMethodHandleReference(dexFile, referenceIndex);
            default:
                throw new ExceptionWithContext("Invalid reference type: %d", Integer.valueOf(referenceType));
        }
    }
}
