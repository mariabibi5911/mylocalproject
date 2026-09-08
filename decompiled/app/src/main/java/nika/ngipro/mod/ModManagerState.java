package nika.ngipro.mod;

import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ModManagerState {
    public static final int CURRENT_SCHEMA = 2;
    public int schemaVersion = CURRENT_SCHEMA;
    public String activeProfileId = "default";
    public final Map<String, ModDefinition> mods = new LinkedHashMap<>();
    public final Map<String, ModProfile> profiles = new LinkedHashMap<>();
    public final Map<String, String> uiPreferences = new LinkedHashMap<>();

    public static ModManagerState defaults() {
        ModManagerState state = new ModManagerState();
        state.mods.put("ngi.pro.research", FeatureRegistry.ngiproResearchMod());
        state.profiles.put("default", new ModProfile("default", "Default"));
        return state;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("schemaVersion", CURRENT_SCHEMA);
        object.put("activeProfileId", activeProfileId);
        JSONArray modArray = new JSONArray();
        for (ModDefinition mod : mods.values()) modArray.put(mod.toJson());
        object.put("mods", modArray);
        JSONArray profileArray = new JSONArray();
        for (ModProfile profile : profiles.values()) profileArray.put(profile.toJson());
        object.put("profiles", profileArray);
        object.put("uiPreferences", new JSONObject(uiPreferences));
        return object;
    }

    public static ModManagerState fromJson(JSONObject object) throws JSONException {
        ModManagerState state = new ModManagerState();
        state.schemaVersion = object.optInt("schemaVersion", 1);
        state.activeProfileId = object.optString("activeProfileId", "default");
        JSONArray mods = object.optJSONArray("mods");
        if (mods != null) for (int i = 0; i < mods.length(); i++) {
            ModDefinition mod = ModDefinition.fromJson(mods.getJSONObject(i));
            if (mod.getId() != null && !mod.getId().isEmpty()) state.mods.put(mod.getId(), mod);
        }
        JSONArray profiles = object.optJSONArray("profiles");
        if (profiles != null) for (int i = 0; i < profiles.length(); i++) {
            ModProfile profile = ModProfile.fromJson(profiles.getJSONObject(i));
            if (profile.getId() != null && !profile.getId().isEmpty()) state.profiles.put(profile.getId(), profile);
        }
        JSONObject prefs = object.optJSONObject("uiPreferences");
        if (prefs != null) {
            String[] names = JSONObject.getNames(prefs);
            if (names != null) for (String key : names) state.uiPreferences.put(key, prefs.optString(key));
        }
        migrate(state);
        return state;
    }

    private static void migrate(ModManagerState state) {
        // Add new built-in research records without replacing user-owned mods or settings.
        if (!state.mods.containsKey("ngi.pro.research")) state.mods.put("ngi.pro.research", FeatureRegistry.ngiproResearchMod());
        if (!state.profiles.containsKey("default")) state.profiles.put("default", new ModProfile("default", "Default"));
        if (!state.profiles.containsKey(state.activeProfileId)) state.activeProfileId = "default";
        state.schemaVersion = CURRENT_SCHEMA;
    }
}
