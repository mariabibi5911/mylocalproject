package nika.ngipro.mod;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/** Compact, programmatic manager UI so it can be added without changing native analysis screens. */
public final class ModManagerActivity extends AppCompatActivity {
    private ModManager manager;
    private LinearLayout list;
    private EditText search;
    private Spinner profileSpinner;
    private Spinner categorySpinner;
    private String query = "";
    private String category = "";
    private int primaryColor;
    private int secondaryColor;
    private int accentColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        primaryColor = themeColor(android.R.attr.textColorPrimary, Color.WHITE);
        secondaryColor = themeColor(android.R.attr.textColorSecondary, Color.LTGRAY);
        accentColor = themeColor(android.R.attr.colorAccent, Color.CYAN);
        manager = new ModManager(this);
        buildUi();
        render();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = column();
        root.setPadding(dp(16), dp(18), dp(16), dp(24));
        root.setBackgroundColor(themeColor(android.R.attr.colorBackground, Color.BLACK));
        TextView title = text("NGI PRO Mod Manager", 24, primaryColor, true);
        root.addView(title);
        root.addView(text("Profiles, features, and authorized configuration", 12, secondaryColor, false));

        LinearLayout profileRow = row();
        profileSpinner = new Spinner(this);
        profileRow.addView(profileSpinner, new LinearLayout.LayoutParams(0, dp(48), 1));
        Button add = button("New profile");
        add.setOnClickListener(v -> promptProfile(false));
        profileRow.addView(add);
        Button duplicate = button("Duplicate");
        duplicate.setOnClickListener(v -> promptProfile(true));
        profileRow.addView(duplicate);
        root.addView(profileRow);
        Button manageProfiles = button("Manage profiles");
        manageProfiles.setOnClickListener(v -> manageProfiles());
        root.addView(manageProfiles);

        search = new EditText(this);
        search.setSingleLine(true);
        search.setHint("Search mods");
        search.setTextColor(primaryColor);
        search.setHintTextColor(Color.GRAY);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { query = s.toString(); renderMods(); }
            public void afterTextChanged(Editable s) {}
        });
        root.addView(search);
        categorySpinner = new Spinner(this);
        List<String> categories = new ArrayList<>();
        categories.add("All categories");
        for (ModDefinition mod : manager.getMods()) if (!categories.contains(mod.getCategory())) categories.add(mod.getCategory());
        categorySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories));
        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { category = position == 0 ? "" : categories.get(position); renderMods(); }
        });
        root.addView(categorySpinner);

        LinearLayout actions = row();
        addAction(actions, "Export", v -> showExport());
        addAction(actions, "Import", v -> showImport());
        addAction(actions, "Backup", v -> runAction("Backup saved", () -> manager.backup()));
        addAction(actions, "Restore", v -> runAction("Backup restored", () -> manager.restoreBackup()));
        addAction(actions, "Reset", v -> confirmReset());
        root.addView(actions);

        Button diagnostics = button("Open diagnostics");
        diagnostics.setOnClickListener(v -> startActivity(new Intent(this, DiagnosticsActivity.class)));
        root.addView(diagnostics);
        list = column();
        root.addView(list);
        scroll.addView(root);
        setContentView(scroll);
    }

    private void render() {
        List<String> names = new ArrayList<>();
        for (ModProfile profile : manager.getProfiles()) names.add(profile.getName());
        profileSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names));
        profileSpinner.setSelection(profileIndex());
        profileSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                List<ModProfile> profiles = manager.getProfiles();
                if (position < profiles.size()) try { manager.activateProfile(profiles.get(position).getId()); renderMods(); } catch (Exception e) { toast(e.getMessage()); }
            }
        });
        renderMods();
    }

    private void renderMods() {
        if (list == null) return;
        list.removeAllViews();
        List<ModDefinition> mods = manager.search(query, category);
        if (mods.isEmpty()) { list.addView(text("No mods match this search.", 14, secondaryColor, false)); return; }
        for (ModDefinition mod : mods) addModCard(mod);
    }

    private void addModCard(ModDefinition mod) {
        LinearLayout card = column();
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setBackground(round(Color.rgb(37, 37, 50), 16));
        LinearLayout header = row();
        LinearLayout names = column();
        names.addView(text(mod.getName(), 17, primaryColor, true));
        names.addView(text("v" + mod.getVersion() + " · " + mod.getCategory(), 12, secondaryColor, false));
        header.addView(names, new LinearLayout.LayoutParams(0, -2, 1));
        Switch toggle = new Switch(this);
        toggle.setChecked(mod.isEnabled());
        toggle.setText(mod.getStatus().displayLabel());
        toggle.setTextColor(secondaryColor);
        toggle.setOnClickListener(v -> runAction("Mod state saved", () -> manager.setModEnabled(mod.getId(), toggle.isChecked())));
        header.addView(toggle);
        card.addView(header);
        card.addView(text(mod.getDescription(), 13, secondaryColor, false));
        card.addView(text("Compatibility: " + mod.getCompatibility().displayLabel(), 12, accentColor, false));
        if (!mod.getDependencies().isEmpty()) card.addView(text("Requires: " + mod.getDependencies(), 12, Color.YELLOW, false));
        if (!mod.getConflicts().isEmpty()) card.addView(text("Conflicts: " + mod.getConflicts(), 12, Color.YELLOW, false));
        Button details = button("Open feature details");
        details.setOnClickListener(v -> { Intent intent = new Intent(this, ModDetailActivity.class); intent.putExtra(ModDetailActivity.EXTRA_MOD_ID, mod.getId()); startActivity(intent); });
        card.addView(details);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, dp(10), 0, 0);
        list.addView(card, params);
    }

    private void manageProfiles() {
        List<ModProfile> profiles = manager.getProfiles();
        String[] names = new String[profiles.size()];
        for (int i = 0; i < profiles.size(); i++) names[i] = profiles.get(i).getName();
        new AlertDialog.Builder(this).setTitle("Manage profiles").setItems(names, (dialog, which) -> {
            ModProfile selected = profiles.get(which);
            String[] actions = selected.getId().equals("default") ? new String[]{"Rename"} : new String[]{"Rename", "Delete"};
            new AlertDialog.Builder(this).setTitle(selected.getName()).setItems(actions, (actionDialog, action) -> {
                if (action == 0) {
                    EditText input = new EditText(this); input.setText(selected.getName());
                    new AlertDialog.Builder(this).setTitle("Rename profile").setView(input).setPositiveButton("Save", (d, w) -> runAction("Profile renamed", () -> manager.renameProfile(selected.getId(), input.getText().toString()))).setNegativeButton("Cancel", null).show();
                } else {
                    new AlertDialog.Builder(this).setTitle("Delete profile?").setMessage("This cannot be undone.").setPositiveButton("Delete", (d, w) -> runAction("Profile deleted", () -> manager.deleteProfile(selected.getId()))).setNegativeButton("Cancel", null).show();
                }
            }).setNegativeButton("Close", null).show();
        }).setNegativeButton("Close", null).show();
    }

    private void promptProfile(boolean duplicate) {
        EditText input = new EditText(this); input.setHint("Profile name");
        new AlertDialog.Builder(this).setTitle(duplicate ? "Duplicate profile" : "New profile").setView(input).setPositiveButton("Save", (d, w) -> runAction("Profile saved", () -> { if (duplicate) manager.duplicateProfile(manager.getActiveProfile().getId(), input.getText().toString()); else manager.createProfile(input.getText().toString()); render(); })).setNegativeButton("Cancel", null).show();
    }

    private void showExport() { try { String json = manager.exportJson(); new AlertDialog.Builder(this).setTitle("Configuration export").setMessage(json).setPositiveButton("Share", (d, w) -> shareText(json)).setNegativeButton("Close", null).show(); } catch (Exception e) { manager.recordError(e.getMessage()); toast(e.getMessage()); } }
    private void shareText(String text) { Intent intent = new Intent(Intent.ACTION_SEND); intent.setType("application/json"); intent.putExtra(Intent.EXTRA_SUBJECT, "NGI PRO mod configuration"); intent.putExtra(Intent.EXTRA_TEXT, text); startActivity(Intent.createChooser(intent, "Export configuration")); }
    private void showImport() { EditText input = new EditText(this); input.setMinLines(8); input.setGravity(Gravity.TOP); new AlertDialog.Builder(this).setTitle("Paste configuration JSON").setView(input).setPositiveButton("Import", (d, w) -> runAction("Configuration imported", () -> manager.importJson(input.getText().toString()))).setNegativeButton("Cancel", null).show(); }
    private void confirmReset() { new AlertDialog.Builder(this).setTitle("Reset configuration?").setMessage("This removes local mod profiles and settings.").setPositiveButton("Reset", (d, w) -> runAction("Configuration reset", () -> manager.reset())).setNegativeButton("Cancel", null).show(); }
    private void runAction(String message, Action action) { try { action.run(); toast(message); render(); } catch (Exception e) { manager.recordError(e.getMessage()); toast("Error: " + e.getMessage()); } }
    private interface Action { void run() throws Exception; }

    private int profileIndex() { int i = 0; String active = manager.getState().activeProfileId; for (ModProfile profile : manager.getProfiles()) { if (profile.getId().equals(active)) return i; i++; } return 0; }
    private void addAction(LinearLayout parent, String label, View.OnClickListener listener) { Button b = button(label); b.setOnClickListener(listener); parent.addView(b, new LinearLayout.LayoutParams(0, -2, 1)); }
    private LinearLayout row() { LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL); l.setGravity(Gravity.CENTER_VERTICAL); return l; }
    private LinearLayout column() { LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l; }
    private TextView text(String value, int size, int color, boolean bold) { TextView v = new TextView(this); v.setText(value == null ? "" : value); v.setTextSize(size); v.setTextColor(color); v.setPadding(0, dp(4), 0, dp(4)); if (bold) v.setTypeface(null, 1); return v; }
    private Button button(String label) { Button b = new Button(this); b.setText(label); b.setAllCaps(false); return b; }
    private GradientDrawable round(int color, int radius) { GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d; }
    private int themeColor(int attribute, int fallback) { TypedValue value = new TypedValue(); if (!getTheme().resolveAttribute(attribute, value, true)) return fallback; return value.resourceId == 0 ? value.data : getResources().getColorStateList(value.resourceId).getDefaultColor(); }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
    private void toast(String message) { Toast.makeText(this, message == null ? "Unknown error" : message, Toast.LENGTH_SHORT).show(); }
}
