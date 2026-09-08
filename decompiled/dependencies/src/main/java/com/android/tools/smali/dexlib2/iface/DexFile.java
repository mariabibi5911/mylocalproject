package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.Opcodes;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface DexFile {
    @Nonnull
    Set<? extends ClassDef> getClasses();

    @Nonnull
    Opcodes getOpcodes();
}
