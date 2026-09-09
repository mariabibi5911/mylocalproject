package nika.ngipro.mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ModDefinition {
    private final String id;
    private String name;
    private String version;
    private String description;
    private String author;
    private String category;
    private boolean enabled;
    private CompatibilityStatus compatibility = CompatibilityStatus.UNKNOWN;
    private final List<String> dependencies = new ArrayList<>();
    private final List<String> conflicts = new ArrayList<>();
    private final List<ModFeature> features = new ArrayList<>();
    private final Map<String, String> configuration = new LinkedHashMap<>();

    public ModDefinition(String id, String name, String version, String description, String author, String category) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public CompatibilityStatus getCompatibility() { return compatibility; }
    public List<String> getDependencies() { return new ArrayList<>(dependencies); }
    public List<String> getConflicts() { return new ArrayList<>(conflicts); }
    public List<ModFeature> getFeatures() { return new ArrayList<>(features); }
    public Map<String, String> getConfiguration() { return new LinkedHashMap<>(configuration); }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setCompatibility(CompatibilityStatus value) { compatibility = value == null ? CompatibilityStatus.UNKNOWN : value; }
    public void setConfiguration(String key, String value) { if (key != null && !isSensitiveKey(key)) configuration.put(key, value == null ? "" : value); }
    private boolean isSensitiveKey(String key) { String normalized = key.toLowerCase(java.util.Locale.US); return normalized.contains("password") || normalized.contains("token") || normalized.contains("secret"); }
    public void addDependency(String id) { if (id != null && !dependencies.contains(id)) dependencies.add(id); }
    public void addConflict(String id) { if (id != null && !conflicts.contains(id)) conflicts.add(id); }
    public void addFeature(ModFeature feature) { if (feature != null) features.add(feature); }

    public ModStatus getStatus() {
        if (compatibility == CompatibilityStatus.UNSUPPORTED) return ModStatus.UNSUPPORTED;
        if (compatibility == CompatibilityStatus.ERROR) return ModStatus.CONFIGURATION_ERROR;
        if (compatibility == CompatibilityStatus.EXPERIMENTAL) return ModStatus.EXPERIMENTAL;
        return enabled ? ModStatus.ENABLED : ModStatus.DISABLED;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("version", version);
        object.put("description", description);
        object.put("author", author);
        object.put("category", category);
        object.put("enabled", enabled);
        object.put("compatibility", compatibility.name());
        object.put("dependencies", new JSONArray(dependencies));
        object.put("conflicts", new JSONArray(conflicts));
        object.put("configuration", new JSONObject(configuration));
        JSONArray featureArray = new JSONArray();
        for (ModFeature feature : features) featureArray.put(feature.toJson());
        object.put("features", featureArray);
        return object;
    }

    public static ModDefinition fromJson(JSONObject object) throws JSONException {
        ModDefinition mod = new ModDefinition(object.optString("id"), object.optString("name"), object.optString("version", "1.0.0"), object.optString("description"), object.optString("author", "Unknown"), object.optString("category", "Other"));
        mod.enabled = object.optBoolean("enabled", false);
        mod.compatibility = CompatibilityStatus.from(object.optString("compatibility"));
        JSONArray deps = object.optJSONArray("dependencies");
        if (deps != null) for (int i = 0; i < deps.length(); i++) mod.addDependency(deps.optString(i));
        JSONArray conflicts = object.optJSONArray("conflicts");
        if (conflicts != null) for (int i = 0; i < conflicts.length(); i++) mod.addConflict(conflicts.optString(i));
        JSONObject config = object.optJSONObject("configuration");
        if (config != null) {
            String[] names = ModJson.names(config);
            if (names != null) for (String key : names) mod.setConfiguration(key, config.optString(key));
        }
        JSONArray featureArray = object.optJSONArray("features");
        if (featureArray != null) for (int i = 0; i < featureArray.length(); i++) mod.addFeature(ModFeature.fromJson(featureArray.getJSONObject(i)));
        return mod;
    }
}
