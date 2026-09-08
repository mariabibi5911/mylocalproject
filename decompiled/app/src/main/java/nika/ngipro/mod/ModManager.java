package nika.ngipro.mod;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ModManager {
    private final ModStore store;
    private final NativeIntegration nativeIntegration;
    private final List<String> recentErrors = new ArrayList<>();
    private ModManagerState state;

    public ModManager(Context context) { this(context, new NativeIntegration.NoOp()); }
    public ModManager(Context context, NativeIntegration nativeIntegration) {
        store = new ModStore(context.getApplicationContext());
        this.nativeIntegration = nativeIntegration == null ? new NativeIntegration.NoOp() : nativeIntegration;
        state = store.load();
        applyProfile(getActiveProfile());
    }

    public ModManagerState getState() { return state; }
    public NativeIntegration getNativeIntegration() { return nativeIntegration; }
    public List<String> getRecentErrors() { return new ArrayList<>(recentErrors); }
    public void recordError(String message) {
        if (message == null || message.trim().isEmpty()) return;
        recentErrors.add(message);
        while (recentErrors.size() > 20) recentErrors.remove(0);
    }
    public List<ModDefinition> getMods() { return new ArrayList<>(state.mods.values()); }
    public ModDefinition getMod(String id) { return state.mods.get(id); }
    public List<ModProfile> getProfiles() { return new ArrayList<>(state.profiles.values()); }
    public ModProfile getActiveProfile() { return state.profiles.get(state.activeProfileId); }

    public List<ModDefinition> search(String query, String category) {
        String needle = query == null ? "" : query.toLowerCase(Locale.US).trim();
        List<ModDefinition> result = new ArrayList<>();
        for (ModDefinition mod : state.mods.values()) {
            boolean categoryMatches = category == null || category.isEmpty() || category.equals(mod.getCategory());
            boolean textMatches = needle.isEmpty() || mod.getName().toLowerCase(Locale.US).contains(needle) || mod.getId().toLowerCase(Locale.US).contains(needle) || mod.getDescription().toLowerCase(Locale.US).contains(needle);
            if (categoryMatches && textMatches) result.add(mod);
        }
        return result;
    }

    public void setModEnabled(String modId, boolean enabled) throws Exception { ModDefinition mod = requireMod(modId); mod.setEnabled(enabled); getActiveProfile().setModEnabled(modId, enabled); persist(); }
    public void setFeatureEnabled(String modId, String featureId, boolean enabled) throws Exception {
        ModDefinition mod = requireMod(modId);
        for (ModFeature feature : mod.getFeatures()) if (feature.getId().equals(featureId)) { feature.setEnabled(enabled); getActiveProfile().setFeatureEnabled(featureKey(modId, featureId), enabled); persist(); return; }
        throw new IllegalArgumentException("Unknown feature: " + featureId);
    }
    public void setModConfiguration(String modId, String key, String value) throws Exception { requireMod(modId).setConfiguration(key, value); persist(); }
    public void setFeatureConfiguration(String modId, String featureId, String key, String value) throws Exception {
        ModDefinition mod = requireMod(modId);
        for (ModFeature feature : mod.getFeatures()) if (feature.getId().equals(featureId)) { feature.setConfiguration(key, value); persist(); return; }
        throw new IllegalArgumentException("Unknown feature: " + featureId);
    }

    public void createProfile(String name) throws Exception {
        String id = uniqueId(name);
        ModProfile profile = new ModProfile(id, name);
        snapshotCurrentState(profile);
        state.profiles.put(id, profile);
        persist();
    }
    public void renameProfile(String id, String name) throws Exception { requireProfile(id).setName(name); persist(); }
    public void duplicateProfile(String id, String name) throws Exception {
        ModProfile source = requireProfile(id);
        String newId = uniqueId(name);
        state.profiles.put(newId, source.duplicate(newId, name));
        persist();
    }
    public void deleteProfile(String id) throws Exception {
        if ("default".equals(id)) throw new IllegalArgumentException("The default profile cannot be deleted");
        if (state.profiles.remove(id) != null) { if (id.equals(state.activeProfileId)) state.activeProfileId = "default"; persist(); }
    }
    public void activateProfile(String id) throws Exception { state.activeProfileId = requireProfile(id).getId(); applyProfile(getActiveProfile()); persist(); }

    public List<String> validate() {
        List<String> issues = new ArrayList<>();
        for (ModDefinition mod : state.mods.values()) {
            for (String dependency : mod.getDependencies()) if (!state.mods.containsKey(dependency)) issues.add(mod.getName() + " requires missing mod " + dependency);
            for (String conflict : mod.getConflicts()) { ModDefinition other = state.mods.get(conflict); if (other != null && mod.isEnabled() && other.isEnabled()) issues.add(mod.getName() + " conflicts with " + other.getName()); }
            if (mod.getCompatibility() == CompatibilityStatus.ERROR) issues.add(mod.getName() + " has a configuration error");
        }
        return issues;
    }

    public NativeIntegration.ApplyResult apply() throws Exception {
        NativeIntegration.ApplyResult result = nativeIntegration.apply(state.mods.values(), getActiveProfile());
        persist();
        return result;
    }
    public String exportJson() throws Exception { return store.exportJson(state); }
    public void importJson(String json) throws Exception { state = store.importJson(json); persist(); }
    public void backup() throws Exception { persist(); store.backup(); }
    public boolean restoreBackup() throws Exception { boolean restored = store.restoreBackup(); if (restored) state = store.load(); return restored; }
    public void reset() throws Exception { store.reset(); state = store.load(); }
    public void persist() throws Exception { store.save(state); }

    private void snapshotCurrentState(ModProfile profile) {
        for (ModDefinition mod : state.mods.values()) {
            profile.setModEnabled(mod.getId(), mod.isEnabled());
            for (ModFeature feature : mod.getFeatures()) profile.setFeatureEnabled(featureKey(mod.getId(), feature.getId()), feature.isEnabled());
        }
    }

    private void applyProfile(ModProfile profile) {
        if (profile == null) return;
        for (ModDefinition mod : state.mods.values()) {
            Boolean enabled = profile.getModEnabled(mod.getId());
            if (enabled != null) mod.setEnabled(enabled);
            for (ModFeature feature : mod.getFeatures()) {
                Boolean featureEnabled = profile.getFeatureEnabled(featureKey(mod.getId(), feature.getId()));
                if (featureEnabled != null) feature.setEnabled(featureEnabled);
            }
        }
    }

    private String featureKey(String modId, String featureId) { return modId + "::" + featureId; }
    private ModDefinition requireMod(String id) { ModDefinition mod = state.mods.get(id); if (mod == null) throw new IllegalArgumentException("Unknown mod: " + id); return mod; }
    private ModProfile requireProfile(String id) { ModProfile profile = state.profiles.get(id); if (profile == null) throw new IllegalArgumentException("Unknown profile: " + id); return profile; }
    private String uniqueId(String label) { String base = (label == null ? "profile" : label.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "-")); if (base.isEmpty()) base = "profile"; String id = base; int n = 2; while (state.profiles.containsKey(id)) id = base + "-" + n++; return id; }
}
