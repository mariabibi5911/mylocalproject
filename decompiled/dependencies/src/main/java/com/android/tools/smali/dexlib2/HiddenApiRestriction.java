package com.android.tools.smali.dexlib2;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/* loaded from: classes.dex */
public enum HiddenApiRestriction {
    WHITELIST(0, "whitelist", false),
    GREYLIST(1, "greylist", false),
    BLACKLIST(2, "blacklist", false),
    GREYLIST_MAX_O(3, "greylist-max-o", false),
    GREYLIST_MAX_P(4, "greylist-max-p", false),
    GREYLIST_MAX_Q(5, "greylist-max-q", false),
    GREYLIST_MAX_R(6, "greylist-max-r", false),
    CORE_PLATFORM_API(8, "core-platform-api", true),
    TEST_API(16, "test-api", true);

    private static final int HIDDENAPI_FLAG_MASK = 7;
    private static final HiddenApiRestriction[] domainSpecificApiFlags;
    private static final HiddenApiRestriction[] hiddenApiFlags;
    private static final Map<String, HiddenApiRestriction> hiddenApiRestrictionsByName;
    private final boolean isDomainSpecificApiFlag;
    private final String name;
    private final int value;

    static {
        HiddenApiRestriction hiddenApiRestriction = WHITELIST;
        HiddenApiRestriction hiddenApiRestriction2 = GREYLIST;
        HiddenApiRestriction hiddenApiRestriction3 = BLACKLIST;
        HiddenApiRestriction hiddenApiRestriction4 = GREYLIST_MAX_O;
        HiddenApiRestriction hiddenApiRestriction5 = GREYLIST_MAX_P;
        HiddenApiRestriction hiddenApiRestriction6 = GREYLIST_MAX_Q;
        HiddenApiRestriction hiddenApiRestriction7 = GREYLIST_MAX_R;
        HiddenApiRestriction hiddenApiRestriction8 = CORE_PLATFORM_API;
        HiddenApiRestriction hiddenApiRestriction9 = TEST_API;
        hiddenApiFlags = new HiddenApiRestriction[]{hiddenApiRestriction, hiddenApiRestriction2, hiddenApiRestriction3, hiddenApiRestriction4, hiddenApiRestriction5, hiddenApiRestriction6, hiddenApiRestriction7};
        domainSpecificApiFlags = new HiddenApiRestriction[]{hiddenApiRestriction8, hiddenApiRestriction9};
        hiddenApiRestrictionsByName = new HashMap();
        for (HiddenApiRestriction hiddenApiRestriction10 : values()) {
            hiddenApiRestrictionsByName.put(hiddenApiRestriction10.toString(), hiddenApiRestriction10);
        }
    }

    HiddenApiRestriction(int value, String name, boolean isDomainSpecificApiFlag) {
        this.value = value;
        this.name = name;
        this.isDomainSpecificApiFlag = isDomainSpecificApiFlag;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isSet(int value) {
        return this.isDomainSpecificApiFlag ? (this.value & value) != 0 : (value & 7) == this.value;
    }

    public boolean isDomainSpecificApiFlag() {
        return this.isDomainSpecificApiFlag;
    }

    public static Set<HiddenApiRestriction> getAllFlags(int value) {
        HiddenApiRestriction normalRestriction = hiddenApiFlags[value & 7];
        int domainSpecificPart = value & (-8);
        if (domainSpecificPart == 0) {
            return ImmutableSet.of(normalRestriction);
        }
        ImmutableSet.Builder<HiddenApiRestriction> builder = ImmutableSet.builder();
        builder.add((ImmutableSet.Builder<HiddenApiRestriction>) normalRestriction);
        for (HiddenApiRestriction domainSpecificApiFlag : domainSpecificApiFlags) {
            if (domainSpecificApiFlag.isSet(value)) {
                builder.add((ImmutableSet.Builder<HiddenApiRestriction>) domainSpecificApiFlag);
            }
        }
        return builder.build();
    }

    public static String formatHiddenRestrictions(int value) {
        StringJoiner joiner = new StringJoiner("|");
        for (HiddenApiRestriction hiddenApiRestriction : getAllFlags(value)) {
            joiner.add(hiddenApiRestriction.toString());
        }
        return joiner.toString();
    }

    public static int combineFlags(Iterable<HiddenApiRestriction> flags) {
        boolean gotHiddenApiFlag = false;
        int value = 0;
        for (HiddenApiRestriction flag : flags) {
            if (flag.isDomainSpecificApiFlag) {
                value += flag.value;
            } else {
                if (gotHiddenApiFlag) {
                    throw new IllegalArgumentException("Cannot combine multiple flags for hidden api restrictions");
                }
                gotHiddenApiFlag = true;
                value += flag.value;
            }
        }
        return value;
    }

    public static HiddenApiRestriction forName(String name) {
        return hiddenApiRestrictionsByName.get(name);
    }
}
