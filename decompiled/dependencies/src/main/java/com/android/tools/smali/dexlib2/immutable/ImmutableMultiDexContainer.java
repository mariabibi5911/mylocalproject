package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.iface.MultiDexContainer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableMultiDexContainer implements MultiDexContainer<ImmutableDexFile> {
    private final ImmutableMap<String, ImmutableDexEntry> entries;

    public ImmutableMultiDexContainer(Map<String, ImmutableDexFile> entries) {
        ImmutableMap.Builder<String, ImmutableDexEntry> builder = ImmutableMap.builder();
        for (Map.Entry<String, ImmutableDexFile> entry : entries.entrySet()) {
            ImmutableDexEntry dexEntry = new ImmutableDexEntry(entry.getKey(), entry.getValue());
            builder.put(dexEntry.getEntryName(), dexEntry);
        }
        this.entries = builder.build();
    }

    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
    @Nonnull
    public List<String> getDexEntryNames() {
        return ImmutableList.copyOf((Collection) this.entries.keySet());
    }

    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
    @Nullable
    public MultiDexContainer.DexEntry<ImmutableDexFile> getEntry(@Nonnull String entryName) {
        return this.entries.get(entryName);
    }

    /* loaded from: classes.dex */
    public class ImmutableDexEntry implements MultiDexContainer.DexEntry<ImmutableDexFile> {
        private final ImmutableDexFile dexFile;
        private final String entryName;

        protected ImmutableDexEntry(String entryName, ImmutableDexFile dexFile) {
            this.entryName = entryName;
            this.dexFile = dexFile;
        }

        @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public String getEntryName() {
            return this.entryName;
        }

        @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public ImmutableDexFile getDexFile() {
            return this.dexFile;
        }

        @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public MultiDexContainer<? extends ImmutableDexFile> getContainer() {
            return ImmutableMultiDexContainer.this;
        }
    }
}
