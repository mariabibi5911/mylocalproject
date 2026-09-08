package nika.ngipro;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/* loaded from: classes4.dex */
public final class ThemeManager {
    private static final String KEY_ACCENT = "accent_color";
    private static final String PREFS_NAME = "NGI_PREFS";
    public static final int DEFAULT_ACCENT = Color.parseColor("#00D4FF");
    public static final int[] PRESET_COLORS = {Color.parseColor("#00D4FF"), Color.parseColor("#7C4DFF"), Color.parseColor("#00E5A0"), Color.parseColor("#FF4560"), Color.parseColor("#FFB300"), Color.parseColor("#FF6EC7")};

    private ThemeManager() {
    }

    public static int getAccent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(KEY_ACCENT, DEFAULT_ACCENT);
    }

    public static void setAccent(Context context, int color) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putInt(KEY_ACCENT, color).apply();
    }
}
