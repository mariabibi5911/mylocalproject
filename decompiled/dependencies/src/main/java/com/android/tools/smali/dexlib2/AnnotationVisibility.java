package com.android.tools.smali.dexlib2;

import com.android.tools.smali.util.ExceptionWithContext;

/* loaded from: classes.dex */
public final class AnnotationVisibility {
    public static final int BUILD = 0;
    private static String[] NAMES = {"build", "runtime", "system"};
    public static final int RUNTIME = 1;
    public static final int SYSTEM = 2;

    public static String getVisibility(int visibility) {
        if (visibility >= 0) {
            String[] strArr = NAMES;
            if (visibility < strArr.length) {
                return strArr[visibility];
            }
        }
        throw new ExceptionWithContext("Invalid annotation visibility %d", Integer.valueOf(visibility));
    }

    public static int getVisibility(String visibility) {
        String visibility2 = visibility.toLowerCase();
        if (visibility2.equals("build")) {
            return 0;
        }
        if (visibility2.equals("runtime")) {
            return 1;
        }
        if (visibility2.equals("system")) {
            return 2;
        }
        throw new ExceptionWithContext("Invalid annotation visibility: %s", visibility2);
    }

    private AnnotationVisibility() {
    }
}
