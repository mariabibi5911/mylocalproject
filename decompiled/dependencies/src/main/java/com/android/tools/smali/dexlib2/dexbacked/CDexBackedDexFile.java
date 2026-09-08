package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.dexbacked.raw.CdexHeaderItem;
import com.android.tools.smali.dexlib2.util.DexUtil;
import java.io.UnsupportedEncodingException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class CDexBackedDexFile extends DexBackedDexFile {
    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset, boolean verifyMagic) {
        super(opcodes, buf, offset, verifyMagic);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull DexBuffer buf) {
        super(opcodes, buf);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset) {
        super(opcodes, buf, offset);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf) {
        super(opcodes, buf);
    }

    public static boolean isCdex(byte[] buf, int offset) {
        if (offset + 4 > buf.length) {
            return false;
        }
        try {
            byte[] cdexMagic = "cdex".getBytes("US-ASCII");
            return buf[offset] == cdexMagic[0] && buf[offset + 1] == cdexMagic[1] && buf[offset + 2] == cdexMagic[2] && buf[offset + 3] == cdexMagic[3];
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
    protected int getVersion(byte[] buf, int offset, boolean verifyMagic) {
        if (verifyMagic) {
            return DexUtil.verifyCdexHeader(buf, offset);
        }
        return CdexHeaderItem.getVersion(buf, offset);
    }

    @Override // com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
    protected Opcodes getDefaultOpcodes(int version) {
        return Opcodes.forApi(28);
    }

    @Override // com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
    public int getBaseDataOffset() {
        return getBuffer().readSmallUint(108);
    }

    public int getDebugInfoOffsetsPos() {
        return getBuffer().readSmallUint(CdexHeaderItem.DEBUG_INFO_OFFSETS_POS_OFFSET);
    }

    public int getDebugInfoOffsetsTableOffset() {
        return getBuffer().readSmallUint(CdexHeaderItem.DEBUG_INFO_OFFSETS_TABLE_OFFSET);
    }

    public int getDebugInfoBase() {
        return getBuffer().readSmallUint(CdexHeaderItem.DEBUG_INFO_BASE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
    public DexBackedMethodImplementation createMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
        return new CDexBackedMethodImplementation(dexFile, method, codeOffset);
    }
}
