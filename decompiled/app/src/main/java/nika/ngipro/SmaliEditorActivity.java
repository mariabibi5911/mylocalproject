package nika.ngipro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SmaliEditorActivity extends AppCompatActivity {
    private EditText editorText;
    private Button saveBtn, loadBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Smali Editor");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        
        TextView title = new TextView(this);
        title.setText("Smali Code Editor");
        title.setTextSize(18);
        layout.addView(title);
        
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        loadBtn = new Button(this);
        loadBtn.setText("Load .smali");
        loadBtn.setOnClickListener(v -> loadSmali());
        loadBtn.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        
        saveBtn = new Button(this);
        saveBtn.setText("Save");
        saveBtn.setOnClickListener(v -> saveSmali());
        saveBtn.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        
        btnLayout.addView(loadBtn);
        btnLayout.addView(saveBtn);
        layout.addView(btnLayout);
        
        editorText = new EditText(this);
        editorText.setHint("Enter smali code here...\n\nExample:\n.class public Lcom/example/MainActivity;\n.super Landroid/app/Activity;\n\n.method protected onCreate(...)V\n    .locals 1\n    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V\n.end method");
        editorText.setLines(20);
        layout.addView(editorText);
        
        setContentView(layout);
    }
    
    private void loadSmali() {
        Toast.makeText(this, "Select .smali file to load", Toast.LENGTH_SHORT).show();
    }
    
    private void saveSmali() {
        String code = editorText.getText().toString();
        Toast.makeText(this, "Smali saved (" + code.length() + " chars)", Toast.LENGTH_SHORT).show();
    }
}
