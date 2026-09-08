package com.android.tools.smali.dexlib2.immutable.debug;

import com.android.tools.smali.dexlib2.base.reference.BaseStringReference;
import com.android.tools.smali.dexlib2.iface.debug.SetSourceFile;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableSetSourceFile extends ImmutableDebugItem implements SetSourceFile {

    @Nullable
    protected final String sourceFile;

    public ImmutableSetSourceFile(int codeAddress, @Nullable String sourceFile) {
        super(codeAddress);
        this.sourceFile = sourceFile;
    }

    @Nonnull
    public static ImmutableSetSourceFile of(@Nonnull SetSourceFile setSourceFile) {
        if (setSourceFile instanceof ImmutableSetSourceFile) {
            return (ImmutableSetSourceFile) setSourceFile;
        }
        return new ImmutableSetSourceFile(setSourceFile.getCodeAddress(), setSourceFile.getSourceFile());
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public String getSourceFile() {
        return this.sourceFile;
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public StringReference getSourceFileReference() {
        if (this.sourceFile == null) {
            return null;
        }
        return new BaseStringReference() { // from class: com.android.tools.smali.dexlib2.immutable.debug.ImmutableSetSourceFile.1
            @Override // com.android.tools.smali.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableSetSourceFile.this.sourceFile;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 9;
    }
}
