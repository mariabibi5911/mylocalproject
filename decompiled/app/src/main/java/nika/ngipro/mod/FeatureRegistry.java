package nika.ngipro.mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Central registry for NGI PRO research-backed features. */
public final class FeatureRegistry {
    private final Map<String, ModFeature> features = new LinkedHashMap<>();

    public void register(ModFeature feature) {
        if (feature != null && feature.getId() != null && !feature.getId().isEmpty()) features.put(feature.getId(), feature);
    }

    public ModFeature get(String id) { return features.get(id); }
    public List<ModFeature> all() { return new ArrayList<>(features.values()); }

    public List<ModFeature> search(String query, String category) {
        String needle = query == null ? "" : query.toLowerCase(Locale.US).trim();
        List<ModFeature> result = new ArrayList<>();
        for (ModFeature feature : features.values()) {
            boolean categoryMatches = category == null || category.isEmpty() || category.equals(feature.getCategory());
            boolean textMatches = needle.isEmpty() || feature.getId().toLowerCase(Locale.US).contains(needle) || feature.getDisplayName().toLowerCase(Locale.US).contains(needle) || feature.getDescription().toLowerCase(Locale.US).contains(needle);
            if (categoryMatches && textMatches) result.add(feature);
        }
        return result;
    }

    public static FeatureRegistry defaultRegistry() {
        FeatureRegistry registry = new FeatureRegistry();
        for (ModFeature feature : ngiproResearchMod().getFeatures()) registry.register(feature);
        return registry;
    }

    public static ModDefinition ngiproResearchMod() {
        ModDefinition mod = new ModDefinition("ngi.pro.research", "NGI PRO Research Features", "1.0.0", "Existing NGI PRO feature research records. Native handlers and compatibility remain explicitly unresolved until verified.", "NGI PRO", "Research");
        mod.setCompatibility(CompatibilityStatus.UNKNOWN);
        String[][] records = {
                {"ESP::LINES", "ESP Lines", "Existing research record for line rendering."},
                {"ESP::ENEMY_LINES", "ESP Enemy Lines", "Existing research record for enemy-line rendering."},
                {"ESP::POCKETS", "ESP Pockets", "Existing research record; implementation is unresolved."},
                {"ESP::STATES", "ESP States", "Existing research record; implementation is unresolved."},
                {"AUTO::PLAY", "Auto Play", "Existing research record; authorization and handler are unresolved."},
                {"AUTO::QUEUE", "Auto Queue", "Existing research record; authorization and handler are unresolved."}
        };
        for (String[] record : records) {
            // Preserve the analyst's source identifier verbatim; it is a record key, not a native handler.
            ModFeature feature = new ModFeature(record[0], record[1], record[2], record[0].split("::")[0], "1.0.0");
            feature.setNativeMetadata("sourceRecord", record[0]);
            feature.setCompatibility(CompatibilityStatus.UNKNOWN);
            feature.setEnabled(false);
            mod.addFeature(feature);
        }
        return mod;
    }
}
