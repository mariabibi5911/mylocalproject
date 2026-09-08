package com.android.tools.smali.dexlib2.analysis.reflection.util;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.exifinterface.media.ExifInterface;
import com.google.common.collect.ImmutableBiMap;

/* loaded from: classes.dex */
public class ReflectionUtils {
    private static ImmutableBiMap<String, String> primitiveMap = ImmutableBiMap.builder().put((ImmutableBiMap.Builder) TypedValues.Custom.S_BOOLEAN, "Z").put((ImmutableBiMap.Builder) "int", "I").put((ImmutableBiMap.Builder) "long", "J").put((ImmutableBiMap.Builder) "double", "D").put((ImmutableBiMap.Builder) "void", ExifInterface.GPS_MEASUREMENT_INTERRUPTED).put((ImmutableBiMap.Builder) TypedValues.Custom.S_FLOAT, "F").put((ImmutableBiMap.Builder) "char", "C").put((ImmutableBiMap.Builder) "short", ExifInterface.LATITUDE_SOUTH).put((ImmutableBiMap.Builder) "byte", "B").build();

    public static String javaToDexName(String javaName) {
        if (javaName.charAt(0) == '[') {
            return javaName.replace('.', '/');
        }
        if (primitiveMap.containsKey(javaName)) {
            return primitiveMap.get(javaName);
        }
        return 'L' + javaName.replace('.', '/') + ';';
    }

    public static String dexToJavaName(String dexName) {
        if (dexName.charAt(0) == '[') {
            return dexName.replace('/', '.');
        }
        if (primitiveMap.inverse().containsKey(dexName)) {
            return primitiveMap.inverse().get(dexName);
        }
        return dexName.replace('/', '.').substring(1, dexName.length() - 1);
    }
}
