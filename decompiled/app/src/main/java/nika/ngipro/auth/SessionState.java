package nika.ngipro.auth;

public final class SessionState {
    private final AuthSession session;
    public SessionState(AuthSession session) { this.session = session; }
    public boolean isSignedIn() { return session != null; }
    public AuthSession getSession() { return session; }
}
