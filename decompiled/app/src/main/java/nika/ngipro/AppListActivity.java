package nika.ngipro;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class AppListActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private List<ApplicationInfo> apps = new ArrayList();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(24, 24, 24, 24);
        TextView title = new TextView(this);
        title.setText("Installed Apps (Tap to extract APK)");
        title.setTextSize(18.0f);
        ListView list = new ListView(this);
        linearLayout.addView(title, new LinearLayout.LayoutParams(-1, -2));
        linearLayout.addView(list, new LinearLayout.LayoutParams(-1, -1));
        setContentView(linearLayout);
        loadApps();
        List<String> names = new ArrayList<>();
        for (ApplicationInfo ai : this.apps) {
            names.add(ai.packageName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        this.adapter = arrayAdapter;
        list.setAdapter((ListAdapter) arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: nika.ngipro.AppListActivity$$ExternalSyntheticLambda0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                AppListActivity.this.m1718lambda$onCreate$0$nikangiproAppListActivity(adapterView, view, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-AppListActivity, reason: not valid java name */
    public /* synthetic */ void m1718lambda$onCreate$0$nikangiproAppListActivity(AdapterView parent, View view, int position, long id) {
        ApplicationInfo ai = this.apps.get(position);
        extractApk(ai.packageName);
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> installed = pm.getInstalledApplications(128);
        this.apps.clear();
        for (ApplicationInfo ai : installed) {
            this.apps.add(ai);
        }
    }

    private void extractApk(String packageName) {
        try {
            String srcPath = Extractor.realApkPath(this, packageName);
            File src = new File(srcPath);
            File outDir = new File(getExternalFilesDir(null), "extracted");
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, packageName + ".apk");
            copyFile(src, outFile);
            Toast.makeText(this, "Extracted: " + outFile.getAbsolutePath(), 1).show();
            String manifestPreview = Extractor.readManifest(srcPath);
            Intent intent = new Intent(this, (Class<?>) ManifestPreviewActivity.class);
            intent.putExtra("apkPath", srcPath);
            intent.putExtra("preview", manifestPreview);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), 1).show();
        }
    }

    private void copyFile(File src, File dst) throws Exception {
        FileInputStream in = new FileInputStream(src);
        try {
            FileOutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[65536];
                while (true) {
                    int len = in.read(buf);
                    if (len > 0) {
                        out.write(buf, 0, len);
                    } else {
                        out.close();
                        in.close();
                        return;
                    }
                }
            } finally {
            }
        } catch (Throwable th) {
            try {
                in.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
