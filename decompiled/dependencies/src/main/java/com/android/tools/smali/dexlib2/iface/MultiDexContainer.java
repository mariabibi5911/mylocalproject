package com.android.tools.smali.dexlib2.iface;

import com.android.tools.smali.dexlib2.iface.DexFile;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface MultiDexContainer<T extends DexFile> {

    /* loaded from: classes.dex */
    public interface DexEntry<T extends DexFile> {
        @Nonnull
        MultiDexContainer<? extends T> getContainer();

        @Nonnull
        T getDexFile();

        @Nonnull
        String getEntryName();
    }

    @Nonnull
    List<String> getDexEntryNames() throws IOException;

    @Nullable
    DexEntry<T> getEntry(@Nonnull String str) throws IOException;
}
