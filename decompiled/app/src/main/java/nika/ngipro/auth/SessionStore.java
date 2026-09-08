package nika.ngipro.auth;

import android.content.Context;
import android.content.SharedPreferences;

/** Stores only non-sensitive account metadata; never stores a password. */
public final class SessionStore implements AuthenticationService {
    private static final String PREFS = "NGI_ACCOUNT_SESSION";
    private static final String PROVIDER = "provider";
    private static final String USERNAME = "username";
    private static final String AVATAR = "avatar";
    private static final String SAVED_AT = "saved_at";
    private final SharedPreferences preferences;

    public SessionStore(Context context) { preferences = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE); }

    @Override public AuthSession currentSession() {
        String username = preferences.getString(USERNAME, "");
        if (username == null || username.isEmpty()) return null;
        return new AuthSession(preferences.getString(PROVIDER, "oauth"), username, preferences.getString(AVATAR, ""), preferences.getLong(SAVED_AT, 0L));
    }

    @Override public void recordProviderSession(String provider, String username, String avatarUrl) {
        preferences.edit().putString(PROVIDER, provider).putString(USERNAME, username).putString(AVATAR, avatarUrl).putLong(SAVED_AT, System.currentTimeMillis()).apply();
    }

    @Override public void logout() { preferences.edit().clear().apply(); }
}
