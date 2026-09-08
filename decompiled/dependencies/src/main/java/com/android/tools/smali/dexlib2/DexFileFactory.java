package com.android.tools.smali.dexlib2;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedOdexFile;
import com.android.tools.smali.dexlib2.dexbacked.OatFile;
import com.android.tools.smali.dexlib2.dexbacked.ZipDexContainer;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.iface.MultiDexContainer;
import com.android.tools.smali.dexlib2.writer.pool.DexPool;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class DexFileFactory {
    @Nonnull
    public static DexBackedDexFile loadDexFile(@Nonnull String path, @Nullable Opcodes opcodes) throws IOException {
        return loadDexFile(new File(path), opcodes);
    }

    @Nonnull
    public static DexBackedDexFile loadDexFile(@Nonnull File file, @Nullable Opcodes opcodes) throws IOException {
        if (!file.exists()) {
            throw new DexFileNotFoundException("%s does not exist", file.getName());
        }
        try {
            ZipDexContainer container = new ZipDexContainer(file, opcodes);
            return new DexEntryFinder(file.getPath(), container).findEntry("classes.dex", true).getDexFile();
        } catch (ZipDexContainer.NotAZipFileException e) {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                return DexBackedDexFile.fromInputStream(opcodes, inputStream);
            } catch (DexBackedDexFile.NotADexFile e2) {
                return DexBackedOdexFile.fromInputStream(opcodes, inputStream);
            } catch (DexBackedOdexFile.NotAnOdexFile e3) {
                OatFile oatFile = null;
                try {
                    oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
                } catch (OatFile.NotAnOatFileException e4) {
                }
                if (oatFile == null) {
                    inputStream.close();
                    throw new UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", file.getPath());
                }
                if (oatFile.isSupportedVersion() == 0) {
                    throw new UnsupportedOatVersionException(oatFile);
                }
                List<DexBackedDexFile> oatDexFiles = oatFile.getDexFiles();
                if (oatDexFiles.size() != 0) {
                    return oatDexFiles.get(0);
                }
                throw new DexFileNotFoundException("Oat file %s contains no dex files", file.getName());
            } finally {
                inputStream.close();
            }
        }
    }

    public static MultiDexContainer.DexEntry<? extends DexBackedDexFile> loadDexEntry(@Nonnull File file, @Nonnull String dexEntry, boolean exactMatch, @Nullable Opcodes opcodes) throws IOException {
        if (!file.exists()) {
            throw new DexFileNotFoundException("Container file %s does not exist", file.getName());
        }
        try {
            ZipDexContainer container = new ZipDexContainer(file, opcodes);
            return new DexEntryFinder(file.getPath(), container).findEntry(dexEntry, exactMatch);
        } catch (ZipDexContainer.NotAZipFileException e) {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            OatFile oatFile = null;
            try {
                try {
                    oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
                } finally {
                    inputStream.close();
                }
            } catch (OatFile.NotAnOatFileException e2) {
            }
            if (oatFile == null) {
                inputStream.close();
                throw new UnsupportedFileTypeException("%s is not an apk or oat file.", file.getPath());
            }
            if (oatFile.isSupportedVersion() == 0) {
                throw new UnsupportedOatVersionException(oatFile);
            }
            List<? extends DexFile> oatDexFiles = oatFile.getDexFiles();
            if (oatDexFiles.size() != 0) {
                return new DexEntryFinder(file.getPath(), oatFile).findEntry(dexEntry, exactMatch);
            }
            throw new DexFileNotFoundException("Oat file %s contains no dex files", file.getName());
        }
    }

    public static MultiDexContainer<? extends DexBackedDexFile> loadDexContainer(@Nonnull File file, @Nullable Opcodes opcodes) throws IOException {
        if (!file.exists()) {
            throw new DexFileNotFoundException("%s does not exist", file.getName());
        }
        ZipDexContainer zipDexContainer = new ZipDexContainer(file, opcodes);
        if (zipDexContainer.isZipFile()) {
            return zipDexContainer;
        }
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(opcodes, inputStream);
            return new SingletonMultiDexContainer(file.getPath(), dexFile);
        } catch (DexBackedOdexFile.NotAnOdexFile e) {
            OatFile oatFile = null;
            try {
                oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
            } catch (OatFile.NotAnOatFileException e2) {
            }
            if (oatFile == null) {
                inputStream.close();
                throw new UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", file.getPath());
            }
            if (oatFile.isSupportedVersion() != 0) {
                return oatFile;
            }
            throw new UnsupportedOatVersionException(oatFile);
        } catch (DexBackedDexFile.NotADexFile e3) {
            DexBackedOdexFile odexFile = DexBackedOdexFile.fromInputStream(opcodes, inputStream);
            return new SingletonMultiDexContainer(file.getPath(), odexFile);
        } finally {
            inputStream.close();
        }
    }

    public static void writeDexFile(@Nonnull String path, @Nonnull DexFile dexFile) throws IOException {
        DexPool.writeTo(path, dexFile);
    }

    private DexFileFactory() {
    }

    /* loaded from: classes.dex */
    public static class DexFileNotFoundException extends ExceptionWithContext {
        public DexFileNotFoundException(@Nullable String message, Object... formatArgs) {
            super(message, formatArgs);
        }

        public DexFileNotFoundException(Throwable cause, @Nullable String message, Object... formatArgs) {
            super(cause, message, formatArgs);
        }
    }

    /* loaded from: classes.dex */
    public static class UnsupportedOatVersionException extends ExceptionWithContext {

        @Nonnull
        public final OatFile oatFile;

        public UnsupportedOatVersionException(@Nonnull OatFile oatFile) {
            super("Unsupported oat version: %d", Integer.valueOf(oatFile.getOatVersion()));
            this.oatFile = oatFile;
        }
    }

    /* loaded from: classes.dex */
    public static class MultipleMatchingDexEntriesException extends ExceptionWithContext {
        public MultipleMatchingDexEntriesException(@Nonnull String message, Object... formatArgs) {
            super(String.format(message, formatArgs), new Object[0]);
        }
    }

    /* loaded from: classes.dex */
    public static class UnsupportedFileTypeException extends ExceptionWithContext {
        public UnsupportedFileTypeException(@Nonnull String message, Object... formatArgs) {
            super(String.format(message, formatArgs), new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean fullEntryMatch(@Nonnull String entry, @Nonnull String targetEntry) {
        if (entry.equals(targetEntry)) {
            return true;
        }
        if (entry.charAt(0) == '/') {
            entry = entry.substring(1);
        }
        if (targetEntry.charAt(0) == '/') {
            targetEntry = targetEntry.substring(1);
        }
        return entry.equals(targetEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean partialEntryMatch(String entry, String targetEntry) {
        if (entry.equals(targetEntry)) {
            return true;
        }
        if (!entry.endsWith(targetEntry)) {
            return false;
        }
        char precedingChar = entry.charAt((entry.length() - targetEntry.length()) - 1);
        char firstTargetChar = targetEntry.charAt(0);
        return firstTargetChar == ':' || firstTargetChar == '/' || firstTargetChar == '!' || precedingChar == ':' || precedingChar == '/' || precedingChar == '!';
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class DexEntryFinder {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final MultiDexContainer<? extends DexBackedDexFile> dexContainer;
        private final String filename;

        public DexEntryFinder(@Nonnull String filename, @Nonnull MultiDexContainer<? extends DexBackedDexFile> dexContainer) {
            this.filename = filename;
            this.dexContainer = dexContainer;
        }

        @Nonnull
        public MultiDexContainer.DexEntry<? extends DexBackedDexFile> findEntry(@Nonnull String targetEntry, boolean exactMatch) throws IOException {
            if (exactMatch) {
                try {
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> entry = this.dexContainer.getEntry(targetEntry);
                    if (entry == null) {
                        throw new DexFileNotFoundException("Could not find entry %s in %s.", targetEntry, this.filename);
                    }
                    return entry;
                } catch (DexBackedDexFile.NotADexFile e) {
                    throw new UnsupportedFileTypeException("Entry %s in %s is not a dex file", targetEntry, this.filename);
                }
            }
            List<String> fullMatches = Lists.newArrayList();
            List<MultiDexContainer.DexEntry<? extends DexBackedDexFile>> fullEntries = Lists.newArrayList();
            List<String> partialMatches = Lists.newArrayList();
            List<MultiDexContainer.DexEntry<? extends DexBackedDexFile>> partialEntries = Lists.newArrayList();
            for (String entry2 : this.dexContainer.getDexEntryNames()) {
                if (!DexFileFactory.fullEntryMatch(entry2, targetEntry)) {
                    if (DexFileFactory.partialEntryMatch(entry2, targetEntry)) {
                        partialMatches.add(entry2);
                        MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry = this.dexContainer.getEntry(entry2);
                        if (dexEntry == null) {
                            throw new AssertionError();
                        }
                        partialEntries.add(dexEntry);
                    } else {
                        continue;
                    }
                } else {
                    fullMatches.add(entry2);
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry2 = this.dexContainer.getEntry(entry2);
                    if (dexEntry2 == null) {
                        throw new AssertionError();
                    }
                    fullEntries.add(dexEntry2);
                }
            }
            if (fullEntries.size() == 1) {
                try {
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry3 = fullEntries.get(0);
                    if (dexEntry3 == null) {
                        throw new AssertionError();
                    }
                    return dexEntry3;
                } catch (DexBackedDexFile.NotADexFile e2) {
                    throw new UnsupportedFileTypeException("Entry %s in %s is not a dex file", fullMatches.get(0), this.filename);
                }
            }
            if (fullEntries.size() > 1) {
                throw new MultipleMatchingDexEntriesException(String.format("Multiple entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join(fullMatches)), new Object[0]);
            }
            if (partialEntries.size() == 0) {
                throw new DexFileNotFoundException("Could not find a dex entry in %s matching %s", this.filename, targetEntry);
            }
            if (partialEntries.size() > 1) {
                throw new MultipleMatchingDexEntriesException(String.format("Multiple dex entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join(partialMatches)), new Object[0]);
            }
            return partialEntries.get(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonMultiDexContainer implements MultiDexContainer<DexBackedDexFile> {
        private final DexBackedDexFile dexFile;
        private final String entryName;

        public SingletonMultiDexContainer(@Nonnull String entryName, @Nonnull DexBackedDexFile dexFile) {
            this.entryName = entryName;
            this.dexFile = dexFile;
        }

        @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
        @Nonnull
        public List<String> getDexEntryNames() {
            return ImmutableList.of(this.entryName);
        }

        @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
        @Nullable
        public MultiDexContainer.DexEntry<DexBackedDexFile> getEntry(@Nonnull final String entryName) {
            if (entryName.equals(this.entryName)) {
                return new MultiDexContainer.DexEntry<DexBackedDexFile>() { // from class: com.android.tools.smali.dexlib2.DexFileFactory.SingletonMultiDexContainer.1
                    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public String getEntryName() {
                        return entryName;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public DexBackedDexFile getDexFile() {
                        return SingletonMultiDexContainer.this.dexFile;
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public MultiDexContainer<? extends DexBackedDexFile> getContainer() {
                        return SingletonMultiDexContainer.this;
                    }
                };
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class FilenameVdexProvider implements OatFile.VdexProvider {

        @Nullable
        private byte[] buf = null;
        private boolean loadedVdex = false;
        private final File vdexFile;

        public FilenameVdexProvider(File oatFile) {
            File oatParent = oatFile.getAbsoluteFile().getParentFile();
            String baseName = Files.getNameWithoutExtension(oatFile.getAbsolutePath());
            this.vdexFile = new File(oatParent, baseName + ".vdex");
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.OatFile.VdexProvider
        @Nullable
        public byte[] getVdex() {
            File parentDirectory;
            if (!this.loadedVdex) {
                File candidateFile = this.vdexFile;
                if (!candidateFile.exists() && (parentDirectory = candidateFile.getParentFile().getParentFile()) != null) {
                    candidateFile = new File(parentDirectory, this.vdexFile.getName());
                }
                if (candidateFile.exists()) {
                    try {
                        this.buf = ByteStreams.toByteArray(new FileInputStream(candidateFile));
                    } catch (FileNotFoundException e) {
                        this.buf = null;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                this.loadedVdex = true;
            }
            return this.buf;
        }
    }
}
