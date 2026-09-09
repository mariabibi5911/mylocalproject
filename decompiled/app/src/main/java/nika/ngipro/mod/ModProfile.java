package nika.ngipro.mod;

import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public final class ModProfile {
    private final String id;
    private String name;
    private final Map<String, Boolean> modEnabled = new LinkedHashMap<>();
    private final Map<String, Boolean> featureEnabled = new LinkedHashMap<>();
    private final Map<String, Map<String, String>> modConfiguration = new LinkedHashMap<>();

    public ModProfile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? "Profile" : name; }
    public Map<String, Boolean> getModEnabled() { return new LinkedHashMap<>(modEnabled); }
    public Map<String, Boolean> getFeatureEnabled() { return new LinkedHashMap<>(featureEnabled); }
    public Map<String, Map<String, String>> getModConfiguration() { return copyConfiguration(); }

    public void setModEnabled(String modId, boolean enabled) { modEnabled.put(modId, enabled); }
    public Boolean getModEnabled(String modId) { return modEnabled.get(modId); }
    public void setFeatureEnabled(String key, boolean enabled) { featureEnabled.put(key, enabled); }
    public Boolean getFeatureEnabled(String key) { return featureEnabled.get(key); }
    public void setModConfiguration(String modId, Map<String, String> config) {
        Map<String, String> safe = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : config.entrySet()) if (!isSensitiveKey(entry.getKey())) safe.put(entry.getKey(), entry.getValue());
        modConfiguration.put(modId, safe);
    }

    private boolean isSensitiveKey(String key) { String normalized = key == null ? "" : key.toLowerCase(java.util.Locale.US); return normalized.contains("password") || normalized.contains("token") || normalized.contains("secret"); }
    public Map<String, String> getConfiguration(String modId) {
        Map<String, String> config = modConfiguration.get(modId);
        return config == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<>(config);
    }

    public ModProfile duplicate(String newId, String newName) {
        ModProfile copy = new ModProfile(newId, newName);
        copy.modEnabled.putAll(modEnabled);
        copy.featureEnabled.putAll(featureEnabled);
        copy.modConfiguration.putAll(copyConfiguration());
        return copy;
    }

    private Map<String, Map<String, String>> copyConfiguration() {
        Map<String, Map<String, String>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : modConfiguration.entrySet()) {
            copy.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
        }
        return copy;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        JSONObject enabled = new JSONObject();
        for (Map.Entry<String, Boolean> entry : modEnabled.entrySet()) {
            enabled.put(entry.getKey(), entry.getValue());
        }
        object.put("modEnabled", enabled);
        JSONObject features = new JSONObject();
        for (Map.Entry<String, Boolean> entry : featureEnabled.entrySet()) features.put(entry.getKey(), entry.getValue());
        object.put("featureEnabled", features);
        JSONObject configs = new JSONObject();
        for (Map.Entry<String, Map<String, String>> entry : modConfiguration.entrySet()) {
            configs.put(entry.getKey(), new JSONObject(entry.getValue()));
        }
        object.put("modConfiguration", configs);
        return object;
    }

    public static ModProfile fromJson(JSONObject object) throws JSONException {
        ModProfile profile = new ModProfile(object.optString("id"), object.optString("name", "Profile"));
        JSONObject enabled = object.optJSONObject("modEnabled");
        if (enabled != null) {
            for (String key : ModJson.names(enabled) == null ? new String[0] : ModJson.names(enabled)) {
                profile.setModEnabled(key, enabled.optBoolean(key));
            }
        }
        JSONObject features = object.optJSONObject("featureEnabled");
        if (features != null) {
            String[] names = ModJson.names(features);
            if (names != null) for (String key : names) profile.setFeatureEnabled(key, features.optBoolean(key));
        }
        JSONObject configs = object.optJSONObject("modConfiguration");
        if (configs != null) {
            String[] names = ModJson.names(configs);
            if (names != null) {
                for (String modId : names) {
                    JSONObject config = configs.optJSONObject(modId);
                    Map<String, String> values = new LinkedHashMap<>();
                    if (config != null) {
                        String[] keys = ModJson.names(config);
                        if (keys != null) {
                            for (String key : keys) values.put(key, config.optString(key));
                        }
                    }
                    profile.setModConfiguration(modId, values);
                }
            }
        }
        return profile;
    }
}
