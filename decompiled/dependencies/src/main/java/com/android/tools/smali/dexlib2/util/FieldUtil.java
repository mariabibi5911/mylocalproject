package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.iface.Field;
import com.google.common.base.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class FieldUtil {
    public static Predicate<Field> FIELD_IS_STATIC = new Predicate<Field>() { // from class: com.android.tools.smali.dexlib2.util.FieldUtil.1
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Field input) {
            return input != null && FieldUtil.isStatic(input);
        }
    };
    public static Predicate<Field> FIELD_IS_INSTANCE = new Predicate<Field>() { // from class: com.android.tools.smali.dexlib2.util.FieldUtil.2
        @Override // com.google.common.base.Predicate
        public boolean apply(@Nullable Field input) {
            return (input == null || FieldUtil.isStatic(input)) ? false : true;
        }
    };

    public static boolean isStatic(@Nonnull Field field) {
        return AccessFlags.STATIC.isSet(field.getAccessFlags());
    }

    private FieldUtil() {
    }
}
