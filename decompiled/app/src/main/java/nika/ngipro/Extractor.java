package nika.ngipro;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/* loaded from: classes4.dex */
public final class Extractor {
    public static native String getApkPath(String str);

    public static native String readManifest(String str);

    static {
        System.loadLibrary("extractor");
    }

    public static String realApkPath(Context ctx, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo info = pm.getPackageInfo(packageName, 0);
        if (info.applicationInfo != null && info.applicationInfo.sourceDir != null) {
            return info.applicationInfo.sourceDir;
        }
        return getApkPath(packageName);
    }
}
