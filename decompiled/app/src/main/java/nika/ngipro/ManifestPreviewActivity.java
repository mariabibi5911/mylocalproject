package nika.ngipro;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes4.dex */
public class ManifestPreviewActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView root = new ScrollView(this);
        TextView tv = new TextView(this);
        tv.setPadding(32, 32, 32, 32);
        tv.setTextSize(15.0f);
        root.addView(tv);
        setContentView(root);
        String apkPath = getIntent().getStringExtra("apkPath");
        String preview = getIntent().getStringExtra("preview");
        String text = "APK: " + apkPath + "\n\n" + (preview != null ? preview : "No preview.");
        tv.setText(text);
    }
}
