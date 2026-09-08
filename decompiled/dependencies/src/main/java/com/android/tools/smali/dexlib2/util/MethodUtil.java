package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.util.CharSequenceUtils;
import com.google.common.base.Predicate;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class MethodUtil {
    private static int directMask = (AccessFlags.STATIC.getValue() | AccessFlags.PRIVATE.getValue()) | AccessFlags.CONSTRUCTOR.getValue();
    public static Predicate<Method> METHOD_IS_DIRECT = new Predicate<Method>() { // from class: com.android.tools.smali.dexlib2.util.MethodUtil.1
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Method input) {
            return input != null && MethodUtil.isDirect(input);
        }
    };
    public static Predicate<Method> METHOD_IS_VIRTUAL = new Predicate<Method>() { // from class: com.android.tools.smali.dexlib2.util.MethodUtil.2
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Method input) {
            return (input == null || MethodUtil.isDirect(input)) ? false : true;
        }
    };

    public static boolean isDirect(@Nonnull Method method) {
        return (method.getAccessFlags() & directMask) != 0;
    }

    public static boolean isStatic(@Nonnull Method method) {
        return AccessFlags.STATIC.isSet(method.getAccessFlags());
    }

    public static boolean isConstructor(@Nonnull MethodReference methodReference) {
        return methodReference.getName().equals("<init>");
    }

    public static boolean isPackagePrivate(@Nonnull Method method) {
        return (method.getAccessFlags() & ((AccessFlags.PRIVATE.getValue() | AccessFlags.PROTECTED.getValue()) | AccessFlags.PUBLIC.getValue())) == 0;
    }

    public static int getParameterRegisterCount(@Nonnull Method method) {
        return getParameterRegisterCount(method, isStatic(method));
    }

    public static int getParameterRegisterCount(@Nonnull MethodReference methodRef, boolean isStatic) {
        return getParameterRegisterCount(methodRef.getParameterTypes(), isStatic);
    }

    public static int getParameterRegisterCount(@Nonnull Collection<? extends CharSequence> parameterTypes, boolean isStatic) {
        int regCount = 0;
        for (CharSequence paramType : parameterTypes) {
            int firstChar = paramType.charAt(0);
            if (firstChar == 74 || firstChar == 68) {
                regCount += 2;
            } else {
                regCount++;
            }
        }
        if (!isStatic) {
            return regCount + 1;
        }
        return regCount;
    }

    private static char getShortyType(CharSequence type) {
        if (type.length() > 1) {
            return 'L';
        }
        return type.charAt(0);
    }

    public static String getShorty(Collection<? extends CharSequence> params, String returnType) {
        StringBuilder sb = new StringBuilder(params.size() + 1);
        sb.append(getShortyType(returnType));
        for (CharSequence typeRef : params) {
            sb.append(getShortyType(typeRef));
        }
        return sb.toString();
    }

    public static boolean methodSignaturesMatch(@Nonnull MethodReference a, @Nonnull MethodReference b) {
        return a.getName().equals(b.getName()) && a.getReturnType().equals(b.getReturnType()) && CharSequenceUtils.listEquals(a.getParameterTypes(), b.getParameterTypes());
    }

    private MethodUtil() {
    }
}
