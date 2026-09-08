package nika.ngipro;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import nika.ngipro.UpdateChecker;

/* loaded from: classes4.dex */
public class SettingsActivity extends AppCompatActivity {
    private LinearLayout colorRow;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.m1814lambda$onCreate$0$nikangiproSettingsActivity(view);
            }
        });
        this.colorRow = (LinearLayout) findViewById(R.id.colorSwatchRow);
        buildColorSwatches();
        TextView currentLang = (TextView) findViewById(R.id.currentLanguageLabel);
        currentLang.setText(getString(LocaleHelper.getSavedLanguage(this).equals(LocaleHelper.LANG_CHINESE) ? R.string.lang_chinese : R.string.lang_english));
        findViewById(R.id.rowLanguage).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.m1815lambda$onCreate$1$nikangiproSettingsActivity(view);
            }
        });
        findViewById(R.id.rowCheckUpdate).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.m1818lambda$onCreate$4$nikangiproSettingsActivity(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1814lambda$onCreate$0$nikangiproSettingsActivity(View v) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1815lambda$onCreate$1$nikangiproSettingsActivity(View v) {
        showLanguageDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$4$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1818lambda$onCreate$4$nikangiproSettingsActivity(View v) {
        final TextView status = (TextView) findViewById(R.id.updateCheckStatus);
        status.setText("Checking…");
        UpdateChecker.checkNow(this, new UpdateChecker.Callback() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda1
            @Override // nika.ngipro.UpdateChecker.Callback
            public final void onResult(UpdateChecker.UpdateInfo updateInfo) {
                SettingsActivity.this.m1817lambda$onCreate$3$nikangiproSettingsActivity(status, updateInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1817lambda$onCreate$3$nikangiproSettingsActivity(TextView status, final UpdateChecker.UpdateInfo info) {
        if (info != null) {
            status.setText("v" + info.latestVersion + " available");
            new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert).setTitle("⚡ Update available").setMessage("Version " + info.latestVersion + " is available.\n\n" + ((info.changelog == null || info.changelog.isEmpty()) ? "See the update page for details." : info.changelog)).setPositiveButton("Update Now", new DialogInterface.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SettingsActivity.this.m1816lambda$onCreate$2$nikangiproSettingsActivity(info, dialogInterface, i);
                }
            }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
        } else {
            status.setText("Up to date");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1816lambda$onCreate$2$nikangiproSettingsActivity(UpdateChecker.UpdateInfo info, DialogInterface d, int w) {
        UpdateChecker.openUpdateLink(this, info.libUrl);
    }

    private void buildColorSwatches() {
        this.colorRow.removeAllViews();
        int selected = ThemeManager.getAccent(this);
        for (final int color : ThemeManager.PRESET_COLORS) {
            View swatch = new View(this);
            int size = (int) (getResources().getDisplayMetrics().density * 44.0f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(10, 10, 10, 10);
            swatch.setLayoutParams(params);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(1);
            shape.setColor(color);
            if (color == selected) {
                shape.setStroke(6, -1);
            }
            swatch.setBackground(shape);
            swatch.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SettingsActivity.this.m1813lambda$buildColorSwatches$5$nikangiproSettingsActivity(color, view);
                }
            });
            this.colorRow.addView(swatch);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$buildColorSwatches$5$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1813lambda$buildColorSwatches$5$nikangiproSettingsActivity(int color, View v) {
        ThemeManager.setAccent(this, color);
        buildColorSwatches();
    }

    private void showLanguageDialog() {
        String[] labels = {getString(R.string.lang_english), getString(R.string.lang_chinese)};
        final String[] codes = {LocaleHelper.LANG_ENGLISH, LocaleHelper.LANG_CHINESE};
        new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert).setTitle(getString(R.string.dlg_language_title)).setItems(labels, new DialogInterface.OnClickListener() { // from class: nika.ngipro.SettingsActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingsActivity.this.m1819lambda$showLanguageDialog$6$nikangiproSettingsActivity(codes, dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showLanguageDialog$6$nika-ngipro-SettingsActivity, reason: not valid java name */
    public /* synthetic */ void m1819lambda$showLanguageDialog$6$nikangiproSettingsActivity(String[] codes, DialogInterface dialog, int which) {
        LocaleHelper.applyLanguage(this, codes[which]);
        recreate();
    }
}
