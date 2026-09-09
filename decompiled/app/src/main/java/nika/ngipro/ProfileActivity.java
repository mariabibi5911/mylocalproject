package nika.ngipro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import nika.ngipro.auth.SessionStore;

/* loaded from: classes4.dex */
public class ProfileActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.ProfileActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ProfileActivity.this.m1811lambda$onCreate$0$nikangiproProfileActivity(view);
            }
        });
        ImageView avatar = (ImageView) findViewById(R.id.profileAvatar);
        TextView userName = (TextView) findViewById(R.id.profileUserName);
        TextView filesCount = (TextView) findViewById(R.id.profileFilesCount);
        CardView btnLogout = (CardView) findViewById(R.id.btnProfileLogout);
        final SharedPreferences prefs = getSharedPreferences("NGI_PREFS", 0);
        String login = XorUtil.deobfuscate(prefs.getString("github_login_x", null));
        String avatarUrl = XorUtil.deobfuscate(prefs.getString("github_avatar_x", null));
        nika.ngipro.auth.AuthSession session = new SessionStore(this).currentSession();
        if ((login == null || login.isEmpty()) && session != null) login = session.username;
        if ((avatarUrl == null || avatarUrl.isEmpty()) && session != null) avatarUrl = session.avatarUrl;
        int filesAnalyzed = prefs.getInt("files_analyzed_count", 0);
        userName.setText(login != null ? login : "NGI User");
        filesCount.setText(String.valueOf(filesAnalyzed));
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with((FragmentActivity) this).load(avatarUrl).circleCrop().into(avatar);
        }
        btnLogout.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.ProfileActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ProfileActivity.this.m1812lambda$onCreate$1$nikangiproProfileActivity(prefs, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-ProfileActivity, reason: not valid java name */
    public /* synthetic */ void m1811lambda$onCreate$0$nikangiproProfileActivity(View v) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$nika-ngipro-ProfileActivity, reason: not valid java name */
    public /* synthetic */ void m1812lambda$onCreate$1$nikangiproProfileActivity(SharedPreferences prefs, View v) {
        prefs.edit().clear().apply();
        new SessionStore(this).logout();
        Intent intent = new Intent(this, (Class<?>) LoginActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);
        finish();
    }
}
