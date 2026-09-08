package nika.ngipro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import nika.ngipro.LoginActivity;
import nika.ngipro.auth.SessionStore;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class LoginActivity extends AppCompatActivity {
    private FrameLayout cardGithub;
    private LinearLayout loadingContainer;
    private TextView loadingText;
    private LinearLayout loginContent;
    private RelativeLayout mainContainer;
    private WebView webView;
    private FrameLayout webViewContainer;

    /* JADX INFO: Access modifiers changed from: private */
    public native String getConfigValue(String str);

    static {
        System.loadLibrary("NGI");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashGuard.initNative();
        SharedPreferences prefs = getSharedPreferences(getConfigValue("PREFS_NAME"), 0);
        if (prefs.getString(getConfigValue("KEY_TOKEN"), null) != null) {
            startActivity(new Intent(this, (Class<?>) MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.login_activity);
        this.mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        this.loginContent = (LinearLayout) findViewById(R.id.loginContent);
        this.cardGithub = (FrameLayout) findViewById(R.id.cardGithub);
        this.loadingContainer = (LinearLayout) findViewById(R.id.loadingContainer);
        this.loadingText = (TextView) findViewById(R.id.loadingText);
        this.webViewContainer = (FrameLayout) findViewById(R.id.webViewContainer);
        setupWebView();
        this.cardGithub.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LoginActivity.this.m1772lambda$onCreate$0$nikangiproLoginActivity(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1772lambda$onCreate$0$nikangiproLoginActivity(View v) {
        startLoginProcess();
    }

    private void setupWebView() {
        this.webView = new WebView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        this.webView.setLayoutParams(params);
        this.webViewContainer.addView(this.webView);
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(2);
        this.webView.clearCache(true);
        this.webView.clearHistory();
        this.webView.setWebViewClient(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: nika.ngipro.LoginActivity$1, reason: invalid class name */
    /* loaded from: classes4.dex */
    public class AnonymousClass1 extends WebViewClient {
        AnonymousClass1() {
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(LoginActivity.this.getConfigValue("REDIRECT_URI"))) {
                LoginActivity.this.handleGithubCallback(url);
                return true;
            }
            return false;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith(LoginActivity.this.getConfigValue("REDIRECT_URI"))) {
                LoginActivity.this.handleGithubCallback(url);
                return true;
            }
            return false;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.clearCache(true);
            LoginActivity.this.runOnUiThread(new Runnable() { // from class: nika.ngipro.LoginActivity$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.AnonymousClass1.this.m1776lambda$onReceivedError$0$nikangiproLoginActivity$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onReceivedError$0$nika-ngipro-LoginActivity$1, reason: not valid java name */
        public /* synthetic */ void m1776lambda$onReceivedError$0$nikangiproLoginActivity$1() {
            LoginActivity loginActivity = LoginActivity.this;
            Toast.makeText(loginActivity, loginActivity.getString(R.string.connection_failed), 0).show();
            LoginActivity.this.resetToLoginState();
        }
    }

    private void startLoginProcess() {
        this.loginContent.animate().alpha(0.0f).setDuration(400L).withEndAction(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1775lambda$startLoginProcess$3$nikangiproLoginActivity();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$startLoginProcess$3$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1775lambda$startLoginProcess$3$nikangiproLoginActivity() {
        this.loginContent.setVisibility(8);
        showLoading(getString(R.string.loading_connecting));
        this.webView.postDelayed(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1774lambda$startLoginProcess$2$nikangiproLoginActivity();
            }
        }, 600L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$startLoginProcess$2$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1774lambda$startLoginProcess$2$nikangiproLoginActivity() {
        String authUrl = "https://github.com/login/oauth/authorize?client_id=" + getConfigValue("CLIENT_ID") + "&scope=user&redirect_uri=" + getConfigValue("REDIRECT_URI");
        this.webView.loadUrl(authUrl);
        hideLoading(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1773lambda$startLoginProcess$1$nikangiproLoginActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$startLoginProcess$1$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1773lambda$startLoginProcess$1$nikangiproLoginActivity() {
        this.webViewContainer.setVisibility(0);
        this.webViewContainer.setAlpha(0.0f);
        this.webViewContainer.animate().alpha(1.0f).setDuration(400L).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGithubCallback(String url) {
        Uri uri = Uri.parse(url);
        final String code = uri.getQueryParameter("code");
        if (code != null) {
            this.webViewContainer.animate().alpha(0.0f).setDuration(300L).withEndAction(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.m1770lambda$handleGithubCallback$4$nikangiproLoginActivity(code);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$handleGithubCallback$4$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1770lambda$handleGithubCallback$4$nikangiproLoginActivity(String code) {
        this.webViewContainer.setVisibility(8);
        showLoading(getString(R.string.loading_verifying));
        exchangeCodeForToken(code);
    }

    private void exchangeCodeForToken(final String code) {
        new Thread(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1767lambda$exchangeCodeForToken$6$nikangiproLoginActivity(code);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$exchangeCodeForToken$6$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1767lambda$exchangeCodeForToken$6$nikangiproLoginActivity(String code) {
        try {
            URL url = new URL("https://github.com/login/oauth/access_token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
            conn.setDoOutput(true);
            String params = "client_id=" + getConfigValue("CLIENT_ID") + "&client_secret=" + getConfigValue("CLIENT_SECRET") + "&code=" + code;
            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONObject json = new JSONObject(br.readLine());
            String token = json.getString("access_token");
            getSharedPreferences(getConfigValue("PREFS_NAME"), 0).edit().putString(getConfigValue("KEY_TOKEN"), XorUtil.obfuscate(token)).apply();
            fetchUserData(token);
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.m1766lambda$exchangeCodeForToken$5$nikangiproLoginActivity();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$exchangeCodeForToken$5$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1766lambda$exchangeCodeForToken$5$nikangiproLoginActivity() {
        Toast.makeText(this, getString(R.string.login_failed), 0).show();
        resetToLoginState();
    }

    private void fetchUserData(final String token) {
        new Thread(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1769lambda$fetchUserData$8$nikangiproLoginActivity(token);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$fetchUserData$8$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1769lambda$fetchUserData$8$nikangiproLoginActivity(String token) {
        try {
            URL url = new URL("https://api.github.com/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "token " + token);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONObject user = new JSONObject(br.readLine());
            String username = user.getString("login");
            String avatarUrl = user.getString("avatar_url");
            getSharedPreferences(getConfigValue("PREFS_NAME"), 0).edit().putString("github_login_x", XorUtil.obfuscate(username)).putString("github_avatar_x", XorUtil.obfuscate(avatarUrl)).apply();
            // Provider-specific OAuth remains here; the abstraction stores only non-sensitive metadata.
            new SessionStore(LoginActivity.this).recordProviderSession("github", username, avatarUrl);
            runOnUiThread(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.m1768lambda$fetchUserData$7$nikangiproLoginActivity();
                }
            });
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.resetToLoginState();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$fetchUserData$7$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1768lambda$fetchUserData$7$nikangiproLoginActivity() {
        startActivity(new Intent(this, (Class<?>) MainActivity.class));
        finish();
    }

    private void showLoading(String message) {
        this.loadingText.setText(message);
        this.loadingContainer.setVisibility(0);
        this.loadingContainer.setAlpha(0.0f);
        this.loadingContainer.animate().alpha(1.0f).setDuration(350L).start();
    }

    private void hideLoading(final Runnable onEnd) {
        this.loadingContainer.animate().alpha(0.0f).setDuration(300L).withEndAction(new Runnable() { // from class: nika.ngipro.LoginActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.m1771lambda$hideLoading$9$nikangiproLoginActivity(onEnd);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$hideLoading$9$nika-ngipro-LoginActivity, reason: not valid java name */
    public /* synthetic */ void m1771lambda$hideLoading$9$nikangiproLoginActivity(Runnable onEnd) {
        this.loadingContainer.setVisibility(8);
        if (onEnd != null) {
            onEnd.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetToLoginState() {
        this.webViewContainer.setVisibility(8);
        this.loadingContainer.setVisibility(8);
        this.loginContent.setVisibility(0);
        this.loginContent.setAlpha(0.0f);
        this.loginContent.animate().alpha(1.0f).setDuration(350L).start();
    }
}
