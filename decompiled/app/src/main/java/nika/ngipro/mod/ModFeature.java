package nika.ngipro.mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ModFeature {
    private final String id;
    private String displayName;
    private String description;
    private String category;
    private boolean enabled;
    private String version;
    private CompatibilityStatus compatibility;
    private FeatureStatus status;
    private final List<String> dependencies = new ArrayList<>();
    private final List<String> optionalDependencies = new ArrayList<>();
    private final Map<String, String> configuration = new LinkedHashMap<>();
    /** Analyst-owned native fields remain unresolved until verified; they are not invoked by the UI. */
    private final Map<String, String> nativeMetadata = new LinkedHashMap<>();

    public ModFeature(String id, String displayName, String description, String category, String version) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.version = version;
        this.compatibility = CompatibilityStatus.UNKNOWN;
        this.status = FeatureStatus.DISABLED;
        nativeMetadata.put("nativeHandler", "unknown");
        nativeMetadata.put("functionPointer", "unknown");
        nativeMetadata.put("nativeGlobal", "unknown");
        nativeMetadata.put("nativeVtable", "unknown");
        nativeMetadata.put("stateInteraction", "unknown");
        nativeMetadata.put("reviewStatus", "manual-review");
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public String getVersion() { return version; }
    public CompatibilityStatus getCompatibility() { return compatibility; }
    public FeatureStatus getStatus() { return status; }
    public List<String> getDependencies() { return new ArrayList<>(dependencies); }
    public List<String> getOptionalDependencies() { return new ArrayList<>(optionalDependencies); }
    public Map<String, String> getConfiguration() { return new LinkedHashMap<>(configuration); }
    public Map<String, String> getNativeMetadata() { return new LinkedHashMap<>(nativeMetadata); }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (compatibility == CompatibilityStatus.UNSUPPORTED) {
            status = FeatureStatus.UNSUPPORTED;
        } else if (compatibility == CompatibilityStatus.EXPERIMENTAL) {
            status = FeatureStatus.EXPERIMENTAL;
        } else if (compatibility == CompatibilityStatus.ERROR) {
            status = FeatureStatus.CONFIGURATION_ERROR;
        } else {
            status = enabled ? FeatureStatus.ENABLED : FeatureStatus.DISABLED;
        }
    }

    public void setCompatibility(CompatibilityStatus compatibility) {
        this.compatibility = compatibility == null ? CompatibilityStatus.UNKNOWN : compatibility;
        setEnabled(enabled);
    }

    public void setStatus(FeatureStatus status) {
        this.status = status == null ? FeatureStatus.DISABLED : status;
    }

    public void setConfiguration(String key, String value) {
        if (key != null && !key.trim().isEmpty() && !isSensitiveKey(key)) {
            configuration.put(key, value == null ? "" : value);
        }
    }

    private boolean isSensitiveKey(String key) {
        String normalized = key.toLowerCase(java.util.Locale.US);
        return normalized.contains("password") || normalized.contains("token") || normalized.contains("secret");
    }

    public void setNativeMetadata(String key, String value) {
        if (key != null && !key.trim().isEmpty()) nativeMetadata.put(key, value == null ? "unknown" : value);
    }

    public void addDependency(String featureId) {
        if (featureId != null && !featureId.trim().isEmpty() && !dependencies.contains(featureId)) {
            dependencies.add(featureId);
        }
    }

    public void addOptionalDependency(String featureId) {
        if (featureId != null && !featureId.trim().isEmpty() && !optionalDependencies.contains(featureId)) optionalDependencies.add(featureId);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("displayName", displayName);
        object.put("description", description);
        object.put("category", category);
        object.put("enabled", enabled);
        object.put("version", version);
        object.put("compatibility", compatibility.name());
        object.put("status", status.name());
        object.put("dependencies", new JSONArray(dependencies));
        object.put("optionalDependencies", new JSONArray(optionalDependencies));
        object.put("configuration", new JSONObject(configuration));
        object.put("nativeMetadata", new JSONObject(nativeMetadata));
        return object;
    }

    public static ModFeature fromJson(JSONObject object) throws JSONException {
        ModFeature feature = new ModFeature(
                object.optString("id"),
                object.optString("displayName"),
                object.optString("description"),
                object.optString("category"),
                object.optString("version", "1.0.0"));
        feature.compatibility = CompatibilityStatus.from(object.optString("compatibility"));
        JSONArray dependencies = object.optJSONArray("dependencies");
        if (dependencies != null) {
            for (int i = 0; i < dependencies.length(); i++) {
                feature.addDependency(dependencies.optString(i));
            }
        }
        JSONArray optionalDependencies = object.optJSONArray("optionalDependencies");
        if (optionalDependencies != null) for (int i = 0; i < optionalDependencies.length(); i++) feature.addOptionalDependency(optionalDependencies.optString(i));
        JSONObject config = object.optJSONObject("configuration");
        if (config != null) {
            JSONArray names = config.names();
            if (names != null) {
                for (int i = 0; i < names.length(); i++) {
                    String key = names.optString(i);
                    feature.setConfiguration(key, config.optString(key));
                }
            }
        }
        JSONObject nativeMetadata = object.optJSONObject("nativeMetadata");
        if (nativeMetadata != null) {
            String[] names = JSONObject.getNames(nativeMetadata);
            if (names != null) for (String key : names) feature.setNativeMetadata(key, nativeMetadata.optString(key));
        }
        feature.setEnabled(object.optBoolean("enabled", false));
        feature.status = FeatureStatus.from(object.optString("status"));
        return feature;
    }
}
