package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.analysis.PathEntryLoader;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedOdexFile;
import com.android.tools.smali.dexlib2.dexbacked.OatFile;
import com.android.tools.smali.dexlib2.iface.MultiDexContainer;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ClassPathResolver {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Iterable<String> classPathDirs;
    private final PathEntryLoader pathEntryLoader;

    /* JADX WARN: Type inference failed for: r11v0, types: [com.android.tools.smali.dexlib2.iface.DexFile] */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.tools.smali.dexlib2.iface.DexFile] */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.android.tools.smali.dexlib2.iface.DexFile] */
    public ClassPathResolver(@Nonnull List<String> bootClassPathDirs, @Nullable List<String> bootClassPathEntries, @Nonnull List<String> extraClassPathEntries, @Nonnull MultiDexContainer.DexEntry<?> dexEntry) throws IOException {
        ?? dexFile = dexEntry.getDexFile();
        this.classPathDirs = bootClassPathDirs;
        this.pathEntryLoader = new PathEntryLoader(dexEntry.getDexFile().getOpcodes());
        for (String entry : bootClassPathEntries == null ? getDefaultBootClassPath(dexEntry, dexFile.getOpcodes().api) : bootClassPathEntries) {
            try {
                loadLocalOrDeviceBootClassPathEntry(entry);
            } catch (NotFoundException ex) {
                if (entry.endsWith(".odex")) {
                    String jarEntry = entry.substring(0, entry.length() - 5) + ".jar";
                    try {
                        loadLocalOrDeviceBootClassPathEntry(jarEntry);
                    } catch (NotFoundException e) {
                        throw new ResolveException(ex);
                    } catch (PathEntryLoader.NoDexException e2) {
                        throw new ResolveException("Neither %s nor %s contain a dex file", entry, jarEntry);
                    }
                } else {
                    throw new ResolveException(ex);
                }
            } catch (PathEntryLoader.NoDexException ex2) {
                if (entry.endsWith(".jar")) {
                    String odexEntry = entry.substring(0, entry.length() - 4) + ".odex";
                    try {
                        loadLocalOrDeviceBootClassPathEntry(odexEntry);
                    } catch (NotFoundException e3) {
                        throw new ResolveException(ex2);
                    } catch (PathEntryLoader.NoDexException e4) {
                        throw new ResolveException("Neither %s nor %s contain a dex file", entry, odexEntry);
                    }
                } else {
                    throw new ResolveException(ex2);
                }
            }
        }
        for (String entry2 : extraClassPathEntries) {
            try {
                loadLocalClassPathEntry(entry2);
            } catch (PathEntryLoader.NoDexException ex3) {
                throw new ResolveException(ex3);
            }
        }
        MultiDexContainer<? extends Object> container = dexEntry.getContainer();
        for (String entry3 : container.getDexEntryNames()) {
            MultiDexContainer.DexEntry<? extends Object> entry4 = container.getEntry(entry3);
            if (entry4 == null) {
                throw new AssertionError();
            }
            this.pathEntryLoader.getClassProviders().add(new DexClassProvider(entry4.getDexFile()));
        }
    }

    public ClassPathResolver(@Nonnull List<String> bootClassPathDirs, @Nonnull List<String> extraClassPathEntries, @Nonnull MultiDexContainer.DexEntry<?> dexEntry) throws IOException {
        this(bootClassPathDirs, null, extraClassPathEntries, dexEntry);
    }

    @Nonnull
    public List<ClassProvider> getResolvedClassProviders() {
        return this.pathEntryLoader.getResolvedClassProviders();
    }

    private boolean loadLocalClassPathEntry(@Nonnull String entry) throws PathEntryLoader.NoDexException, IOException {
        File entryFile = new File(entry);
        if (!entryFile.exists() || !entryFile.isFile()) {
            return false;
        }
        try {
            this.pathEntryLoader.loadEntry(entryFile, true);
            return true;
        } catch (DexFileFactory.UnsupportedFileTypeException ex) {
            throw new ResolveException(ex, "Couldn't load classpath entry %s", entry);
        }
    }

    private void loadLocalOrDeviceBootClassPathEntry(@Nonnull String entry) throws IOException, PathEntryLoader.NoDexException, NotFoundException {
        if (loadLocalClassPathEntry(entry)) {
            return;
        }
        List<String> pathComponents = splitDevicePath(entry);
        Joiner pathJoiner = Joiner.on(File.separatorChar);
        for (String directory : this.classPathDirs) {
            File directoryFile = new File(directory);
            if (directoryFile.exists()) {
                for (int i = 0; i < pathComponents.size(); i++) {
                    String partialPath = pathJoiner.join(pathComponents.subList(i, pathComponents.size()));
                    File entryFile = new File(directoryFile, partialPath);
                    if (entryFile.exists() && entryFile.isFile()) {
                        this.pathEntryLoader.loadEntry(entryFile, true);
                        return;
                    }
                }
            }
        }
        throw new NotFoundException("Could not find classpath entry %s", entry);
    }

    @Nonnull
    private static List<String> splitDevicePath(@Nonnull String path) {
        return Lists.newArrayList(Splitter.on('/').split(path));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class NotFoundException extends Exception {
        public NotFoundException(String message, Object... formatArgs) {
            super(String.format(message, formatArgs));
        }
    }

    /* loaded from: classes.dex */
    public static class ResolveException extends RuntimeException {
        public ResolveException(String message, Object... formatArgs) {
            super(String.format(message, formatArgs));
        }

        public ResolveException(Throwable cause) {
            super(cause);
        }

        public ResolveException(Throwable cause, String message, Object... formatArgs) {
            super(String.format(message, formatArgs), cause);
        }
    }

    @Nonnull
    private static List<String> getDefaultBootClassPath(@Nonnull MultiDexContainer.DexEntry<?> dexEntry, int apiLevel) {
        MultiDexContainer<? extends Object> container = dexEntry.getContainer();
        if (container instanceof OatFile) {
            return bootClassPathForOat((OatFile) container);
        }
        Object dexFile = dexEntry.getDexFile();
        if (!(dexFile instanceof DexBackedOdexFile)) {
            if (apiLevel > 8) {
                if (apiLevel > 11) {
                    if (apiLevel > 13) {
                        if (apiLevel > 15) {
                            if (apiLevel > 17) {
                                if (apiLevel > 18) {
                                    if (apiLevel > 19) {
                                        if (apiLevel > 22) {
                                            if (apiLevel <= 23) {
                                                return Lists.newArrayList("/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/apache-xml.jar", "/system/framework/org.apache.http.legacy.boot.jar");
                                            }
                                            return Lists.newArrayList("/system/framework/core-oj.jar", "/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/apache-xml.jar", "/system/framework/org.apache.http.legacy.boot.jar");
                                        }
                                        return Lists.newArrayList("/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/apache-xml.jar");
                                    }
                                    return Lists.newArrayList("/system/framework/core.jar", "/system/framework/conscrypt.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/framework2.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar", "/system/framework/webviewchromium.jar");
                                }
                                return Lists.newArrayList("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar");
                            }
                            return Lists.newArrayList("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar");
                        }
                        return Lists.newArrayList("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar", "/system/framework/filterfw.jar");
                    }
                    return Lists.newArrayList("/system/framework/core.jar", "/system/framework/apache-xml.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/core-junit.jar");
                }
                return Lists.newArrayList("/system/framework/core.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/core-junit.jar");
            }
            return Lists.newArrayList("/system/framework/core.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar");
        }
        return ((DexBackedOdexFile) dexFile).getDependencies();
    }

    private static List<String> bootClassPathForOat(@Nonnull OatFile oatFile) {
        List<String> bcp = oatFile.getBootClassPath();
        if (bcp.isEmpty()) {
            return Lists.newArrayList("boot.oat");
        }
        return replaceElementsSuffix(bcp, ".art", ".oat");
    }

    private static List<String> replaceElementsSuffix(List<String> bcp, String originalSuffix, String newSuffix) {
        for (int i = 0; i < bcp.size(); i++) {
            String entry = bcp.get(i);
            if (entry.endsWith(originalSuffix)) {
                bcp.set(i, entry.substring(0, entry.length() - originalSuffix.length()) + newSuffix);
            }
        }
        return bcp;
    }
}
