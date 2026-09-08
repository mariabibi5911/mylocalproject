package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableDexFile implements DexFile {

    @Nonnull
    protected final ImmutableSet<? extends ImmutableClassDef> classes;

    @Nonnull
    private final Opcodes opcodes;

    public ImmutableDexFile(@Nonnull Opcodes opcodes, @Nullable Collection<? extends ClassDef> classes) {
        this.classes = ImmutableClassDef.immutableSetOf(classes);
        this.opcodes = opcodes;
    }

    public ImmutableDexFile(@Nonnull Opcodes opcodes, @Nullable ImmutableSet<? extends ImmutableClassDef> classes) {
        this.classes = ImmutableUtils.nullToEmptySet(classes);
        this.opcodes = opcodes;
    }

    public static ImmutableDexFile of(DexFile dexFile) {
        if (dexFile instanceof ImmutableDexFile) {
            return (ImmutableDexFile) dexFile;
        }
        return new ImmutableDexFile(dexFile.getOpcodes(), dexFile.getClasses());
    }

    @Override // com.android.tools.smali.dexlib2.iface.DexFile
    @Nonnull
    public ImmutableSet<? extends ImmutableClassDef> getClasses() {
        return this.classes;
    }

    @Override // com.android.tools.smali.dexlib2.iface.DexFile
    @Nonnull
    public Opcodes getOpcodes() {
        return this.opcodes;
    }
}
