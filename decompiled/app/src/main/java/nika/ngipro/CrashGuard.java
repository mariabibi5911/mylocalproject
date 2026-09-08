package nika.ngipro;

/* loaded from: classes4.dex */
public final class CrashGuard {

    /* loaded from: classes4.dex */
    public interface OnCrashListener {
        void onCrashOccurred();
    }

    private static native boolean executeProtected(Runnable runnable);

    public static native void initNative();

    static {
        System.loadLibrary("crash");
    }

    public static void run(Runnable action, OnCrashListener listener) {
        if (executeProtected(action) || listener == null) {
            return;
        }
        listener.onCrashOccurred();
    }
}
