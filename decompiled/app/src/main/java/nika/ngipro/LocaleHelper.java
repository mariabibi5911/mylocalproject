package nika.ngipro;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import java.util.Locale;

/* loaded from: classes4.dex */
public final class LocaleHelper {
    private static final String KEY_LANG = "app_language";
    public static final String LANG_CHINESE = "zh";
    public static final String LANG_ENGLISH = "en";
    private static final String PREFS_NAME = "NGI_PREFS";

    private LocaleHelper() {
    }

    public static Context attach(Context context) {
        String lang = getSavedLanguage(context);
        return setLocale(context, lang);
    }

    public static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_LANG, LANG_ENGLISH);
    }

    public static void setSavedLanguage(Context context, String lang) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(KEY_LANG, lang).apply();
    }

    public static Context setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    public static void applyLanguage(Context context, String lang) {
        setSavedLanguage(context, lang);
    }
}
