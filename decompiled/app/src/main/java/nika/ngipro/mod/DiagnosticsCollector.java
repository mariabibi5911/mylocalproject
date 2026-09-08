package nika.ngipro.mod;

import android.content.Context;
import android.content.pm.PackageInfo;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class DiagnosticsCollector {
    public static final String FRAMEWORK_VERSION = "1.0.0";
    private final Context context;
    private final ModManager manager;

    public DiagnosticsCollector(Context context, ModManager manager) {
        this.context = context.getApplicationContext();
        this.manager = manager;
    }

    public JSONObject collect() {
        JSONObject report = new JSONObject();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            report.put("applicationVersion", info.versionName == null ? "Unknown" : info.versionName);
        } catch (Exception ignored) { put(report, "applicationVersion", "Unknown"); }
        put(report, "modFrameworkVersion", FRAMEWORK_VERSION);
        JSONArray modules = new JSONArray();
        for (String module : manager.getNativeIntegration().loadedModules()) modules.put(module);
        put(report, "loadedModules", modules);
        JSONArray enabled = new JSONArray();
        for (ModDefinition mod : manager.getMods()) if (mod.isEnabled()) enabled.put(mod.getId());
        put(report, "enabledMods", enabled);
        JSONArray issues = new JSONArray();
        for (String issue : manager.validate()) issues.put(issue);
        put(report, "configurationIssues", issues);
        JSONArray errors = new JSONArray();
        for (String error : manager.getRecentErrors()) errors.put(error);
        put(report, "recentErrors", errors);
        put(report, "activeProfile", manager.getActiveProfile() == null ? "Unknown" : manager.getActiveProfile().getName());
        put(report, "schemaVersion", ModManagerState.CURRENT_SCHEMA);
        return report;
    }

    private static void put(JSONObject object, String key, Object value) { try { object.put(key, value); } catch (JSONException ignored) {} }
}
