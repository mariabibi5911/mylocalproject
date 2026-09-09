package nika.ngipro.mod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public final class DiagnosticsActivity extends AppCompatActivity {
    private int primaryColor;
    private int secondaryColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        primaryColor = themeColor(android.R.attr.textColorPrimary, Color.WHITE);
        secondaryColor = themeColor(android.R.attr.textColorSecondary, Color.LTGRAY);
        ModManager manager = new ModManager(this);
        JSONObject report = new DiagnosticsCollector(this, manager).collect();
        final String reportText = formatReport(report);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(18), dp(18), dp(18), dp(24)); root.setBackgroundColor(themeColor(android.R.attr.colorBackground, Color.BLACK));
        TextView title = new TextView(this); title.setText("NGI PRO Diagnostics"); title.setTextSize(24); title.setTextColor(primaryColor); title.setTypeface(null, 1); root.addView(title);
        TextView content = new TextView(this); content.setTextColor(secondaryColor); content.setTextSize(13); content.setText(reportText); root.addView(content);
        Button export = new Button(this); export.setText("Export diagnostics"); export.setOnClickListener(v -> share(reportText)); root.addView(export);
        ScrollView scroll = new ScrollView(this); scroll.addView(root); setContentView(scroll);
    }

    private int themeColor(int attribute, int fallback) { TypedValue value = new TypedValue(); if (!getTheme().resolveAttribute(attribute, value, true)) return fallback; return value.resourceId == 0 ? value.data : getResources().getColorStateList(value.resourceId).getDefaultColor(); }
    private static String formatReport(JSONObject report) { try { return report.toString(2); } catch (JSONException e) { return report.toString(); } }
    private void share(String text) { Intent intent = new Intent(Intent.ACTION_SEND); intent.setType("text/plain"); intent.putExtra(Intent.EXTRA_SUBJECT, "NGI PRO diagnostics"); intent.putExtra(Intent.EXTRA_TEXT, text); startActivity(Intent.createChooser(intent, "Export diagnostics")); }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
}
