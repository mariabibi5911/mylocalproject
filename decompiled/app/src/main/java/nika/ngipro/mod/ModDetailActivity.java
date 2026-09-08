package nika.ngipro.mod;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public final class ModDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOD_ID = "mod_id";
    private ModManager manager;
    private ModDefinition mod;
    private LinearLayout root;
    private int primaryColor;
    private int secondaryColor;
    private int accentColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        primaryColor = themeColor(android.R.attr.textColorPrimary, Color.WHITE);
        secondaryColor = themeColor(android.R.attr.textColorSecondary, Color.LTGRAY);
        accentColor = themeColor(android.R.attr.colorAccent, Color.CYAN);
        manager = new ModManager(this);
        mod = manager.getMod(getIntent().getStringExtra(EXTRA_MOD_ID));
        if (mod == null) { finish(); return; }
        render();
    }

    private void render() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(18), dp(18), dp(18), dp(24)); root.setBackgroundColor(themeColor(android.R.attr.colorBackground, Color.BLACK));
        root.addView(text(mod.getName(), 25, primaryColor, true));
        root.addView(text("Version " + mod.getVersion() + " · " + mod.getAuthor(), 13, secondaryColor, false));
        root.addView(text(mod.getDescription(), 15, secondaryColor, false));
        root.addView(text("Status: " + mod.getStatus().displayLabel(), 14, accentColor, true));
        root.addView(text("Compatibility: " + mod.getCompatibility().displayLabel(), 14, accentColor, false));
        if (!mod.getDependencies().isEmpty()) root.addView(text("Dependencies: " + mod.getDependencies(), 13, Color.YELLOW, false));
        if (!mod.getConflicts().isEmpty()) root.addView(text("Conflicts: " + mod.getConflicts(), 13, Color.YELLOW, false));
        Switch enabled = new Switch(this); enabled.setText("Enable mod"); enabled.setTextColor(primaryColor); enabled.setChecked(mod.isEnabled()); enabled.setOnClickListener(v -> save(() -> manager.setModEnabled(mod.getId(), enabled.isChecked()))); root.addView(enabled);
        root.addView(text("Features", 19, primaryColor, true));
        for (ModFeature feature : mod.getFeatures()) addFeature(feature);
        Button apply = new Button(this); apply.setText("Save and apply"); apply.setOnClickListener(v -> applyConfiguration()); root.addView(apply);
        scroll.addView(root); setContentView(scroll);
    }

    private void addFeature(ModFeature feature) {
        LinearLayout card = new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(12), dp(10), dp(12), dp(10));
        TextView title = text(feature.getDisplayName() + " · " + feature.getStatus().displayLabel(), 16, primaryColor, true); card.addView(title); card.addView(text(feature.getDescription(), 13, secondaryColor, false)); card.addView(text("Compatibility: " + feature.getCompatibility().displayLabel(), 12, accentColor, false));
        Switch toggle = new Switch(this); toggle.setText("Enabled"); toggle.setTextColor(primaryColor); toggle.setChecked(feature.isEnabled()); toggle.setOnClickListener(v -> save(() -> manager.setFeatureEnabled(mod.getId(), feature.getId(), toggle.isChecked()))); card.addView(toggle);
        card.addView(text("Native resolution: manual review; unresolved fields remain unknown", 12, Color.YELLOW, false));
        if (!feature.getOptionalDependencies().isEmpty()) card.addView(text("Optional dependencies: " + feature.getOptionalDependencies(), 12, secondaryColor, false));
        for (java.util.Map.Entry<String, String> entry : feature.getConfiguration().entrySet()) {
            EditText value = new EditText(this); value.setHint(entry.getKey()); value.setText(entry.getValue()); value.setTextColor(primaryColor); value.setHintTextColor(Color.GRAY); value.setOnFocusChangeListener((v, hasFocus) -> { if (!hasFocus) save(() -> manager.setFeatureConfiguration(mod.getId(), feature.getId(), entry.getKey(), value.getText().toString())); }); card.addView(value);
        }
        root.addView(card);
    }

    private void applyConfiguration() { try { NativeIntegration.ApplyResult result = manager.apply(); Toast.makeText(this, result.message, Toast.LENGTH_LONG).show(); } catch (Exception e) { manager.recordError(e.getMessage()); Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show(); } }
    private void save(Action action) { try { action.run(); Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); } catch (Exception e) { manager.recordError(e.getMessage()); Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show(); } }
    private interface Action { void run() throws Exception; }
    private int themeColor(int attribute, int fallback) { TypedValue value = new TypedValue(); if (!getTheme().resolveAttribute(attribute, value, true)) return fallback; return value.resourceId == 0 ? value.data : getResources().getColorStateList(value.resourceId).getDefaultColor(); }
    private TextView text(String value, int size, int color, boolean bold) { TextView v = new TextView(this); v.setText(value); v.setTextSize(size); v.setTextColor(color); v.setPadding(0, dp(5), 0, dp(5)); if (bold) v.setTypeface(null, 1); return v; }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
}
