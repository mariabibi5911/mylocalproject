package nika.ngipro.mod;

public enum FeatureStatus {
    ENABLED,
    DISABLED,
    UNSUPPORTED,
    EXPERIMENTAL,
    CONFIGURATION_ERROR;

    public String displayLabel() {
        switch (this) {
            case ENABLED:
                return "Enabled";
            case UNSUPPORTED:
                return "Unsupported";
            case EXPERIMENTAL:
                return "Experimental";
            case CONFIGURATION_ERROR:
                return "Configuration error";
            default:
                return "Disabled";
        }
    }

    public static FeatureStatus from(String value) {
        if (value == null) {
            return DISABLED;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return DISABLED;
        }
    }
}
