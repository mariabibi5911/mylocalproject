package nika.ngipro.mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Boundary between the configuration UI and authorized NGI PRO integrations.
 * No unresolved research address is invoked by this interface.
 */
public interface NativeIntegration {
    ApplyResult apply(Collection<ModDefinition> mods, ModProfile profile);

    List<String> loadedModules();

    final class ApplyResult {
        public final boolean success;
        public final String message;

        public ApplyResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    /** Safe default until a verified NGI PRO native adapter is registered. */
    final class NoOp implements NativeIntegration {
        @Override
        public ApplyResult apply(Collection<ModDefinition> mods, ModProfile profile) {
            return new ApplyResult(true, "Configuration saved locally; no native adapter registered.");
        }

        @Override
        public List<String> loadedModules() {
            return Collections.emptyList();
        }
    }
}
