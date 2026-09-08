package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public final class TypeUtils {
    public static boolean isWideType(@Nonnull String type) {
        char c = type.charAt(0);
        return c == 'J' || c == 'D';
    }

    public static boolean isWideType(@Nonnull TypeReference type) {
        return isWideType(type.getType());
    }

    public static boolean isPrimitiveType(String type) {
        return type.length() == 1;
    }

    @Nonnull
    public static String getPackage(@Nonnull String type) {
        int lastSlash = type.lastIndexOf(47);
        if (lastSlash < 0) {
            return "";
        }
        return type.substring(1, lastSlash);
    }

    public static boolean canAccessClass(@Nonnull String accessorType, @Nonnull ClassDef accesseeClassDef) {
        if (AccessFlags.PUBLIC.isSet(accesseeClassDef.getAccessFlags())) {
            return true;
        }
        return getPackage(accesseeClassDef.getType()).equals(getPackage(accessorType));
    }

    private TypeUtils() {
    }
}
