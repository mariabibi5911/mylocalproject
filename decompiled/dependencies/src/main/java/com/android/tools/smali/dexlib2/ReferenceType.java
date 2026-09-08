package com.android.tools.smali.dexlib2;

import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ExceptionWithContext;

/* loaded from: classes.dex */
public final class ReferenceType {
    public static final int CALL_SITE = 5;
    public static final int FIELD = 2;
    public static final int METHOD = 3;
    public static final int METHOD_HANDLE = 6;
    public static final int METHOD_PROTO = 4;
    public static final int NONE = 7;
    public static final int STRING = 0;
    public static final int TYPE = 1;

    public static int getReferenceType(Reference reference) {
        if (reference instanceof StringReference) {
            return 0;
        }
        if (reference instanceof TypeReference) {
            return 1;
        }
        if (reference instanceof FieldReference) {
            return 2;
        }
        if (reference instanceof MethodReference) {
            return 3;
        }
        if (reference instanceof MethodProtoReference) {
            return 4;
        }
        if (reference instanceof CallSiteReference) {
            return 5;
        }
        if (reference instanceof MethodHandleReference) {
            return 6;
        }
        throw new IllegalStateException("Invalid reference");
    }

    public static void validateReferenceType(int referenceType) {
        if (referenceType < 0 || referenceType > 4) {
            throw new InvalidReferenceTypeException(referenceType);
        }
    }

    /* loaded from: classes.dex */
    public static class InvalidReferenceTypeException extends ExceptionWithContext {
        private final int referenceType;

        public InvalidReferenceTypeException(int referenceType) {
            super("Invalid reference type: %d", Integer.valueOf(referenceType));
            this.referenceType = referenceType;
        }

        public InvalidReferenceTypeException(int referenceType, String message, Object... formatArgs) {
            super(message, formatArgs);
            this.referenceType = referenceType;
        }

        public int getReferenceType() {
            return this.referenceType;
        }
    }

    private ReferenceType() {
    }
}
