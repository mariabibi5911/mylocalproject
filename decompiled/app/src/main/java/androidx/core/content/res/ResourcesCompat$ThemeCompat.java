package androidx.core.content.res;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * APK-only compatibility implementation for the recovered AndroidX call site.
 *
 * The dollar sign is intentional: this top-level class has the same binary
 * name as the missing nested ResourcesCompat.ThemeCompat class.
 */
public final class ResourcesCompat$ThemeCompat {
    private static final String TAG = "ResourcesCompat";

    private ResourcesCompat$ThemeCompat() {
    }

    public static void rebase(Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 29) {
            Api29Impl.rebase(theme);
        } else if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.rebase(theme);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void rebase(Resources.Theme theme) {
            theme.rebase();
        }
    }

    static class Api23Impl {
        private static Method sRebaseMethod;
        private static boolean sRebaseMethodFetched;
        private static final Object sRebaseMethodLock = new Object();

        private Api23Impl() {
        }

        static void rebase(Resources.Theme theme) {
            synchronized (sRebaseMethodLock) {
                if (!sRebaseMethodFetched) {
                    try {
                        Method declaredMethod = Resources.Theme.class.getDeclaredMethod(
                                "rebase",
                                new Class[0]
                        );
                        sRebaseMethod = declaredMethod;
                        declaredMethod.setAccessible(true);
                    } catch (NoSuchMethodException exception) {
                        Log.i(
                                TAG,
                                "Failed to retrieve rebase() method",
                                exception
                        );
                    }
                    sRebaseMethodFetched = true;
                }

                Method method = sRebaseMethod;
                if (method != null) {
                    try {
                        method.invoke(theme, new Object[0]);
                    } catch (
                            IllegalAccessException |
                            InvocationTargetException exception
                    ) {
                        Log.i(
                                TAG,
                                "Failed to invoke rebase() method via reflection",
                                exception
                        );
                        sRebaseMethod = null;
                    }
                }
            }
        }
    }
}
