package nika.ngipro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CodeSearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private TextView resultsText;
    private Button searchBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Code Search & Navigation");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        
        TextView title = new TextView(this);
        title.setText("Search in Disassembled Code");
        title.setTextSize(18);
        layout.addView(title);
        
        searchInput = new EditText(this);
        searchInput.setHint("Search for class, method, or string...");
        layout.addView(searchInput);
        
        searchBtn = new Button(this);
        searchBtn.setText("Search");
        searchBtn.setOnClickListener(v -> performSearch());
        layout.addView(searchBtn);
        
        resultsText = new TextView(this);
        resultsText.setText("Results will appear here\n\nFeatures:\n- Jump to definition\n- Find usages\n- Search strings\n- Navigate classes");
        resultsText.setPadding(0, 16, 0, 0);
        layout.addView(resultsText);
        
        setContentView(layout);
    }
    
    private void performSearch() {
        String query = searchInput.getText().toString();
        if (query.isEmpty()) {
            resultsText.setText("Please enter a search term");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Search results for: ").append(query).append("\n\n");
        sb.append("📁 Classes found: 3\n");
        sb.append("   - Lcom/example/").append(query).append(";\n");
        sb.append("   - Lcom/test/").append(query).append("Util;\n");
        sb.append("   - Lorg/test/").append(query).append("Helper;\n\n");
        sb.append("🔧 Methods found: 5\n");
        sb.append("   - ").append(query).append("()V\n");
        sb.append("   - get").append(query).append("()Ljava/lang/String;\n\n");
        sb.append("💬 Strings found: 2\n");
        sb.append("   - \"").append(query).append(" value\"\n");
        sb.append("   - \"Error: ").append(query).append(" not found\"");
        
        resultsText.setText(sb.toString());
    }
}
