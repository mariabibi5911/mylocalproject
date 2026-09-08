package com.android.tools.smali.dexlib2;

import com.google.android.material.internal.ViewUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.RangeMap;
import java.util.EnumMap;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class Opcodes {
    public final int api;
    public final int artVersion;

    @Nonnull
    private final EnumMap<Opcode, Short> opcodeValues;

    @Nonnull
    private final HashMap<String, Opcode> opcodesByName;

    @Nonnull
    private final Opcode[] opcodesByValue = new Opcode[256];

    @Nonnull
    public static Opcodes forApi(int api) {
        return new Opcodes(api, -1);
    }

    @Nonnull
    public static Opcodes forArtVersion(int artVersion) {
        return new Opcodes(-1, artVersion);
    }

    @Nonnull
    public static Opcodes forDexVersion(int dexVersion) {
        int api = VersionMap.mapDexVersionToApi(dexVersion);
        if (api == -1) {
            throw new RuntimeException("Unsupported dex version " + dexVersion);
        }
        return new Opcodes(api, -1);
    }

    @Nonnull
    public static Opcodes getDefault() {
        return forApi(20);
    }

    private Opcodes(int api, int artVersion) {
        int version;
        RangeMap<Integer, Short> versionToValueMap;
        if (api >= 21) {
            this.api = api;
            this.artVersion = VersionMap.mapApiToArtVersion(api);
        } else if (artVersion >= 0 && artVersion < 39) {
            this.api = VersionMap.mapArtVersionToApi(artVersion);
            this.artVersion = artVersion;
        } else {
            this.api = api;
            this.artVersion = artVersion;
        }
        this.opcodeValues = new EnumMap<>(Opcode.class);
        this.opcodesByName = Maps.newHashMap();
        if (isArt()) {
            version = this.artVersion;
        } else {
            version = this.api;
        }
        for (Opcode opcode : Opcode.values()) {
            if (isArt()) {
                versionToValueMap = opcode.artVersionToValueMap;
            } else {
                versionToValueMap = opcode.apiToValueMap;
            }
            Short opcodeValue = versionToValueMap.get(Integer.valueOf(version));
            if (opcodeValue != null) {
                if (!opcode.format.isPayloadFormat) {
                    this.opcodesByValue[opcodeValue.shortValue()] = opcode;
                }
                this.opcodeValues.put((EnumMap<Opcode, Short>) opcode, (Opcode) opcodeValue);
                this.opcodesByName.put(opcode.name.toLowerCase(), opcode);
            }
        }
    }

    @Nullable
    public Opcode getOpcodeByName(@Nonnull String opcodeName) {
        return this.opcodesByName.get(opcodeName.toLowerCase());
    }

    @Nullable
    public Opcode getOpcodeByValue(int opcodeValue) {
        switch (opcodeValue) {
            case 256:
                return Opcode.PACKED_SWITCH_PAYLOAD;
            case 512:
                return Opcode.SPARSE_SWITCH_PAYLOAD;
            case ViewUtils.EDGE_TO_EDGE_FLAGS /* 768 */:
                return Opcode.ARRAY_PAYLOAD;
            default:
                if (opcodeValue < 0) {
                    return null;
                }
                Opcode[] opcodeArr = this.opcodesByValue;
                if (opcodeValue < opcodeArr.length) {
                    return opcodeArr[opcodeValue];
                }
                return null;
        }
    }

    @Nullable
    public Short getOpcodeValue(@Nonnull Opcode opcode) {
        return this.opcodeValues.get(opcode);
    }

    public boolean isArt() {
        return this.artVersion != -1;
    }
}
