package com.android.tools.smali.dexlib2.dexbacked;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.iface.MultiDexContainer;
import com.android.tools.smali.dexlib2.util.DexUtil;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ZipDexContainer implements MultiDexContainer<DexBackedDexFile> {

    @Nullable
    private final Opcodes opcodes;
    private final File zipFilePath;

    /* loaded from: classes.dex */
    public static class NotAZipFileException extends RuntimeException {
    }

    public ZipDexContainer(@Nonnull File zipFilePath, @Nullable Opcodes opcodes) {
        this.zipFilePath = zipFilePath;
        this.opcodes = opcodes;
    }

    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
    @Nonnull
    public List<String> getDexEntryNames() throws IOException {
        List<String> entryNames = Lists.newArrayList();
        ZipFile zipFile = getZipFile();
        try {
            Enumeration<? extends ZipEntry> entriesEnumeration = zipFile.entries();
            while (entriesEnumeration.hasMoreElements()) {
                ZipEntry entry = entriesEnumeration.nextElement();
                if (isDex(zipFile, entry)) {
                    entryNames.add(entry.getName());
                }
            }
            return entryNames;
        } finally {
            zipFile.close();
        }
    }

    @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer
    @Nullable
    public MultiDexContainer.DexEntry<DexBackedDexFile> getEntry(@Nonnull String entryName) throws IOException {
        ZipFile zipFile = getZipFile();
        try {
            ZipEntry entry = zipFile.getEntry(entryName);
            if (entry != null) {
                return loadEntry(zipFile, entry);
            }
            zipFile.close();
            return null;
        } finally {
            zipFile.close();
        }
    }

    public boolean isZipFile() {
        ZipFile zipFile = null;
        try {
            ZipFile zipFile2 = getZipFile();
            if (zipFile2 == null) {
                return true;
            }
            try {
                zipFile2.close();
                return true;
            } catch (IOException e) {
                return true;
            }
        } catch (NotAZipFileException e2) {
            if (0 != 0) {
                try {
                    zipFile.close();
                } catch (IOException e3) {
                }
            }
            return false;
        } catch (IOException e4) {
            if (0 != 0) {
                try {
                    zipFile.close();
                } catch (IOException e5) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    zipFile.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    protected boolean isDex(@Nonnull ZipFile zipFile, @Nonnull ZipEntry zipEntry) throws IOException {
        InputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
        try {
            DexUtil.verifyDexHeader(inputStream);
            inputStream.close();
            return true;
        } catch (DexBackedDexFile.NotADexFile e) {
            inputStream.close();
            return false;
        } catch (DexUtil.InvalidFile e2) {
            inputStream.close();
            return false;
        } catch (DexUtil.UnsupportedFile e3) {
            inputStream.close();
            return false;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    protected ZipFile getZipFile() throws IOException {
        try {
            return new ZipFile(this.zipFilePath);
        } catch (IOException e) {
            throw new NotAZipFileException();
        }
    }

    @Nonnull
    protected MultiDexContainer.DexEntry loadEntry(@Nonnull ZipFile zipFile, @Nonnull final ZipEntry zipEntry) throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        try {
            final byte[] buf = ByteStreams.toByteArray(inputStream);
            return new MultiDexContainer.DexEntry() { // from class: com.android.tools.smali.dexlib2.dexbacked.ZipDexContainer.1
                @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public String getEntryName() {
                    return zipEntry.getName();
                }

                @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public DexFile getDexFile() {
                    return new DexBackedDexFile(ZipDexContainer.this.opcodes, buf);
                }

                @Override // com.android.tools.smali.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public MultiDexContainer getContainer() {
                    return ZipDexContainer.this;
                }
            };
        } finally {
            inputStream.close();
        }
    }
}
