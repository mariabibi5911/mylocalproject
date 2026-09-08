package nika.ngipro.auth;

/**
 * UI-facing authentication boundary. Passwords and tokens never belong in
 * this interface's persistent model; the existing OAuth flow remains the
 * provider-specific implementation.
 */
public interface AuthenticationService {
    AuthSession currentSession();
    void recordProviderSession(String provider, String username, String avatarUrl);
    void logout();
}
