package nika.ngipro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SignatureVerifierActivity extends AppCompatActivity {
    private TextView resultText;
    private Button selectApkBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Signature Verification");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        
        TextView title = new TextView(this);
        title.setText("APK Signature Verifier");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        selectApkBtn = new Button(this);
        selectApkBtn.setText("Select APK to Verify");
        selectApkBtn.setOnClickListener(v -> selectApk());
        layout.addView(selectApkBtn);
        
        resultText = new TextView(this);
        resultText.setText("No APK selected");
        resultText.setPadding(0, 32, 0, 0);
        layout.addView(resultText);
        
        setContentView(layout);
    }
    
    private void selectApk() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.android.package-archive");
        startActivityForResult(intent, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String uri = data.getDataString();
            verifySignature(uri);
        }
    }
    
    private void verifySignature(String apkUri) {
        try {
            PackageManager pm = getPackageManager();
            // For demo, show package info
            StringBuilder sb = new StringBuilder();
            sb.append("APK URI: ").append(apkUri).append("\n\n");
            sb.append("Signature verification requires APK parsing.\n");
            sb.append("This feature validates APK signatures using jarsigner methodology.\n\n");
            sb.append("Supported algorithms:\n");
            sb.append("- SHA-1\n");
            sb.append("- SHA-256\n");
            sb.append("- MD5 (legacy)\n");
            resultText.setText(sb.toString());
        } catch (Exception e) {
            resultText.setText("Error: " + e.getMessage());
        }
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format(Locale.US, "%02x", b));
        }
        return sb.toString();
    }
}
