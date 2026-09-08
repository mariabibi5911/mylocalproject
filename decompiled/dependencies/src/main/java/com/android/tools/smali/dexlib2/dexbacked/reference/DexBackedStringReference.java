package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseStringReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedStringReference extends BaseStringReference {

    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int stringIndex;

    public DexBackedStringReference(@Nonnull DexBackedDexFile dexBuf, int stringIndex) {
        this.dexFile = dexBuf;
        this.stringIndex = stringIndex;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return (String) this.dexFile.getStringSection().get(this.stringIndex);
    }

    public int getSize() {
        int stringOffset = this.dexFile.getStringSection().getOffset(this.stringIndex);
        int stringDataOffset = this.dexFile.getBuffer().readSmallUint(stringOffset);
        DexReader reader = this.dexFile.getDataBuffer().readerAt(stringDataOffset);
        int size = 4 + reader.peekSmallUleb128Size();
        int utf16Length = reader.readSmallUleb128();
        return size + reader.peekStringLength(utf16Length);
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.stringIndex;
        if (i < 0 || i >= this.dexFile.getStringSection().size()) {
            throw new Reference.InvalidReferenceException("string@" + this.stringIndex);
        }
    }
}
