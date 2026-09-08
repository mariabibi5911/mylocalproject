package com.android.tools.smali.dexlib2;

import java.util.HashMap;

/* loaded from: classes.dex */
public enum AccessFlags {
    PUBLIC(1, "public", true, true, true),
    PRIVATE(2, "private", true, true, true),
    PROTECTED(4, "protected", true, true, true),
    STATIC(8, "static", true, true, true),
    FINAL(16, "final", true, true, true),
    SYNCHRONIZED(32, "synchronized", false, true, false),
    VOLATILE(64, "volatile", false, false, true),
    BRIDGE(64, "bridge", false, true, false),
    TRANSIENT(128, "transient", false, false, true),
    VARARGS(128, "varargs", false, true, false),
    NATIVE(256, "native", false, true, false),
    INTERFACE(512, "interface", true, false, false),
    ABSTRACT(1024, "abstract", true, true, false),
    STRICTFP(2048, "strictfp", false, true, false),
    SYNTHETIC(4096, "synthetic", true, true, true),
    ANNOTATION(8192, "annotation", true, false, false),
    ENUM(16384, "enum", true, false, true),
    CONSTRUCTOR(65536, "constructor", false, true, false),
    DECLARED_SYNCHRONIZED(131072, "declared-synchronized", false, true, false);

    private static HashMap<String, AccessFlags> accessFlagsByName;
    private static final AccessFlags[] allFlags;
    private String accessFlagName;
    private boolean validForClass;
    private boolean validForField;
    private boolean validForMethod;
    private int value;

    static {
        AccessFlags[] values = values();
        allFlags = values;
        accessFlagsByName = new HashMap<>();
        for (AccessFlags accessFlag : values) {
            accessFlagsByName.put(accessFlag.accessFlagName, accessFlag);
        }
    }

    AccessFlags(int value, String accessFlagName, boolean validForClass, boolean validForMethod, boolean validForField) {
        this.value = value;
        this.accessFlagName = accessFlagName;
        this.validForClass = validForClass;
        this.validForMethod = validForMethod;
        this.validForField = validForField;
    }

    public boolean isSet(int accessFlags) {
        return (this.value & accessFlags) != 0;
    }

    public static AccessFlags[] getAccessFlagsForClass(int accessFlagValue) {
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForClass && (accessFlag.value & accessFlagValue) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForClass && (accessFlag2.value & accessFlagValue) != 0) {
                accessFlags[accessFlagsPosition] = accessFlag2;
                accessFlagsPosition++;
            }
        }
        return accessFlags;
    }

    private static String formatAccessFlags(AccessFlags[] accessFlags) {
        int size = 0;
        for (AccessFlags accessFlag : accessFlags) {
            size += accessFlag.toString().length() + 1;
        }
        StringBuilder sb = new StringBuilder(size);
        for (AccessFlags accessFlag2 : accessFlags) {
            sb.append(accessFlag2.toString());
            sb.append(" ");
        }
        if (accessFlags.length > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public static String formatAccessFlagsForClass(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForClass(accessFlagValue));
    }

    public static AccessFlags[] getAccessFlagsForMethod(int accessFlagValue) {
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForMethod && (accessFlag.value & accessFlagValue) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForMethod && (accessFlag2.value & accessFlagValue) != 0) {
                accessFlags[accessFlagsPosition] = accessFlag2;
                accessFlagsPosition++;
            }
        }
        return accessFlags;
    }

    public static String formatAccessFlagsForMethod(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForMethod(accessFlagValue));
    }

    public static AccessFlags[] getAccessFlagsForField(int accessFlagValue) {
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForField && (accessFlag.value & accessFlagValue) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForField && (accessFlag2.value & accessFlagValue) != 0) {
                accessFlags[accessFlagsPosition] = accessFlag2;
                accessFlagsPosition++;
            }
        }
        return accessFlags;
    }

    public static String formatAccessFlagsForField(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForField(accessFlagValue));
    }

    public static AccessFlags getAccessFlag(String accessFlag) {
        return accessFlagsByName.get(accessFlag);
    }

    public int getValue() {
        return this.value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.accessFlagName;
    }
}
