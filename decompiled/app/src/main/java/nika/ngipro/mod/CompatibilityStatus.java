package nika.ngipro.mod;

/** Compatibility is deliberately explicit; unknown data is never presented as compatible. */
public enum CompatibilityStatus {
    UNKNOWN,
    COMPATIBLE,
    UNSUPPORTED,
    EXPERIMENTAL,
    ERROR;

    public String displayLabel() {
        switch (this) {
            case COMPATIBLE:
                return "Compatible";
            case UNSUPPORTED:
                return "Unsupported";
            case EXPERIMENTAL:
                return "Experimental";
            case ERROR:
                return "Configuration error";
            default:
                return "Unknown";
        }
    }

    public static CompatibilityStatus from(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return UNKNOWN;
        }
    }
}
