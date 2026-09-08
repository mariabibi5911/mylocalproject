package nika.ngipro;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import nika.ngipro.UpdateChecker;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public final class UpdateChecker {
    private static final String APKPURE_PACKAGE = "com.apkpure.aegon";
    private static final long CHECK_INTERVAL_MS = 43200000;
    private static final String KEY_LAST_CHECK = "update_last_check_ms";
    private static final String PREFS_NAME = "NGI_PREFS";
    private static final String SEEN_VERSIONS_FILE = "nika.txt";
    private static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/Sanji1-Owner/NGIPRO/main/update.json";

    /* loaded from: classes4.dex */
    public interface Callback {
        void onResult(UpdateInfo updateInfo);
    }

    /* loaded from: classes4.dex */
    public static class UpdateInfo {
        public String changelog;
        public String latestVersion;
        public String libUrl;
    }

    private UpdateChecker() {
    }

    public static void checkSilently(Context context, Callback callback) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        long lastCheck = prefs.getLong(KEY_LAST_CHECK, 0L);
        if (System.currentTimeMillis() - lastCheck < CHECK_INTERVAL_MS) {
            return;
        }
        prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();
        check(context, callback, false);
    }

    public static void checkNow(Context context, Callback callback) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();
        check(context, callback, true);
    }

    private static void check(Context context, final Callback callback, final boolean ignoreSeenList) {
        final String currentVersion = getCurrentVersionName(context);
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        final Context appContext = context.getApplicationContext();
        new Thread(new Runnable() { // from class: nika.ngipro.UpdateChecker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                UpdateChecker.lambda$check$1(currentVersion, ignoreSeenList, appContext, mainHandler, callback);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$check$1(String currentVersion, boolean ignoreSeenList, Context appContext, Handler mainHandler, final Callback callback) {
        UpdateInfo result = null;
        try {
            URL url = new URL(UPDATE_JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty(HttpHeaders.USER_AGENT, "NGI-PRO-App");
            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    } else {
                        sb.append(line);
                    }
                }
                br.close();
                JSONObject json = new JSONObject(sb.toString());
                String version = json.optString("version", "");
                String libUrl = json.optString("lib_url", "");
                String changelog = json.optString("changelog", "");
                if (!version.isEmpty()) {
                    try {
                        if (isNewer(version, currentVersion)) {
                            boolean alreadySeen = !ignoreSeenList && isVersionSeen(appContext, version);
                            if (!alreadySeen) {
                                UpdateInfo info = new UpdateInfo();
                                info.latestVersion = version;
                                info.libUrl = libUrl;
                                info.changelog = changelog;
                                result = info;
                                markVersionSeen(appContext, version);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e2) {
        }
        final UpdateInfo finalResult = result;
        mainHandler.post(new Runnable() { // from class: nika.ngipro.UpdateChecker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UpdateChecker.Callback.this.onResult(finalResult);
            }
        });
    }

    public static void openUpdateLink(Context context, String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.setPackage(APKPURE_PACKAGE);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent fallback = new Intent("android.intent.action.VIEW", Uri.parse(url));
            context.startActivity(fallback);
        }
    }

    private static boolean isVersionSeen(Context context, String version) {
        return readSeenVersions(context).contains(version.trim());
    }

    private static void markVersionSeen(Context context, String version) {
        Set<String> versions = readSeenVersions(context);
        versions.add(version.trim());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(context.getFilesDir(), SEEN_VERSIONS_FILE), false));
            try {
                for (String v : versions) {
                    bw.write(v);
                    bw.newLine();
                }
                bw.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> readSeenVersions(Context context) {
        Set<String> versions = new HashSet<>();
        File f = new File(context.getFilesDir(), SEEN_VERSIONS_FILE);
        if (!f.exists()) {
            return versions;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    String line2 = line.trim();
                    if (!line2.isEmpty()) {
                        versions.add(line2);
                    }
                } finally {
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return versions;
    }

    private static String getCurrentVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "0.0";
        }
    }

    private static boolean isNewer(String latest, String current) {
        try {
            String[] a = latest.split("\\.");
            String[] b = current.split("\\.");
            int len = Math.max(a.length, b.length);
            int i = 0;
            while (i < len) {
                int av = i < a.length ? parseIntSafe(a[i]) : 0;
                int bv = i < b.length ? parseIntSafe(b[i]) : 0;
                if (av != bv) {
                    return av > bv;
                }
                i++;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
