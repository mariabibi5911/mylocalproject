package nika.ngipro.auth;

public final class AuthSession {
    public final String provider;
    public final String username;
    public final String avatarUrl;
    public final long savedAt;

    public AuthSession(String provider, String username, String avatarUrl, long savedAt) {
        this.provider = provider == null ? "unknown" : provider;
        this.username = username == null ? "" : username;
        this.avatarUrl = avatarUrl == null ? "" : avatarUrl;
        this.savedAt = savedAt;
    }
}
