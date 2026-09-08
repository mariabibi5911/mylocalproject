package nika.ngipro;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.jf.dexlib2.dexbacked.raw.CdexHeaderItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class AppExtractorActivity extends AppCompatActivity {
    private List<ApplicationInfo> apps = new ArrayList();
    private PackageManager pm;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pm = getPackageManager();
        loadApps();
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(32, 32, 32, 32);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
        for (final ApplicationInfo app : this.apps) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(0);
            row.setPadding(16, 16, 16, 16);
            ImageView icon = new ImageView(this);
            Drawable appIcon = app.loadIcon(this.pm);
            icon.setImageDrawable(appIcon);
            icon.setLayoutParams(new LinearLayout.LayoutParams(CdexHeaderItem.DEBUG_INFO_OFFSETS_TABLE_OFFSET, CdexHeaderItem.DEBUG_INFO_OFFSETS_TABLE_OFFSET));
            TextView name = new TextView(this);
            name.setText(app.packageName);
            name.setTextSize(16.0f);
            name.setPadding(24, 0, 24, 0);
            Button extractBtn = new Button(this);
            extractBtn.setText("Extract");
            extractBtn.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.AppExtractorActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AppExtractorActivity.this.m1717lambda$onCreate$0$nikangiproAppExtractorActivity(app, view);
                }
            });
            row.addView(icon);
            row.addView(name);
            row.addView(extractBtn);
            linearLayout.addView(row);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-AppExtractorActivity, reason: not valid java name */
    public /* synthetic */ void m1717lambda$onCreate$0$nikangiproAppExtractorActivity(ApplicationInfo app, View v) {
        extractApk(app.packageName);
    }

    private void loadApps() {
        List<ApplicationInfo> installed = this.pm.getInstalledApplications(128);
        for (ApplicationInfo ai : installed) {
            if ((ai.flags & 1) == 0) {
                this.apps.add(ai);
            }
        }
    }

    private void extractApk(String packageName) {
        try {
            String apkPath = Extractor.realApkPath(this, packageName);
            Toast.makeText(this, "APK Path:\n" + apkPath, 1).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), 1).show();
        }
    }
}
