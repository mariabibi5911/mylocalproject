package com.android.tools.smali.dexlib2.iface.reference;

/* loaded from: classes.dex */
public interface Reference {
    void validateReference() throws InvalidReferenceException;

    /* loaded from: classes.dex */
    public static class InvalidReferenceException extends Exception {
        private final String invalidReferenceRepresentation;

        public InvalidReferenceException(String invalidReferenceRepresentation) {
            super("Invalid reference");
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, String msg) {
            super(msg);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, String s, Throwable throwable) {
            super(s, throwable);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, Throwable throwable) {
            super(throwable);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public String getInvalidReferenceRepresentation() {
            return this.invalidReferenceRepresentation;
        }
    }
}
