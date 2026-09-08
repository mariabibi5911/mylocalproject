package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.analysis.ClassPathResolver;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.OatFile;
import com.android.tools.smali.dexlib2.iface.MultiDexContainer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class PathEntryLoader {
    Opcodes opcodes;
    final Set<File> loadedFiles = Sets.newHashSet();
    final List<ClassProvider> classProviders = Lists.newArrayList();

    public Opcodes getOpcodes() {
        return this.opcodes;
    }

    public List<ClassProvider> getClassProviders() {
        return this.classProviders;
    }

    public PathEntryLoader(Opcodes opcodes) {
        this.opcodes = opcodes;
    }

    @Nonnull
    public List<ClassProvider> getResolvedClassProviders() {
        return this.classProviders;
    }

    public void loadEntry(@Nonnull File entryFile, boolean loadOatDependencies) throws IOException, NoDexException {
        if (this.loadedFiles.contains(entryFile)) {
            return;
        }
        try {
            MultiDexContainer<? extends DexBackedDexFile> container = DexFileFactory.loadDexContainer(entryFile, this.opcodes);
            List<String> entryNames = container.getDexEntryNames();
            if (entryNames.isEmpty()) {
                throw new NoDexException("%s contains no dex file", entryFile);
            }
            this.loadedFiles.add(entryFile);
            for (String entryName : entryNames) {
                this.classProviders.add(new DexClassProvider(container.getEntry(entryName).getDexFile()));
            }
            if (loadOatDependencies && (container instanceof OatFile)) {
                List<String> oatDependencies = ((OatFile) container).getBootClassPath();
                if (!oatDependencies.isEmpty()) {
                    try {
                        loadOatDependencies(entryFile.getParentFile(), oatDependencies);
                    } catch (ClassPathResolver.NotFoundException ex) {
                        throw new ClassPathResolver.ResolveException(ex, "Error while loading oat file %s", entryFile);
                    } catch (NoDexException ex2) {
                        throw new ClassPathResolver.ResolveException(ex2, "Error while loading dependencies for oat file %s", entryFile);
                    }
                }
            }
        } catch (DexFileFactory.UnsupportedFileTypeException ex3) {
            throw new ClassPathResolver.ResolveException(ex3);
        }
    }

    private void loadOatDependencies(@Nonnull File directory, @Nonnull List<String> oatDependencies) throws IOException, NoDexException, ClassPathResolver.NotFoundException {
        for (String oatDependency : oatDependencies) {
            String oatDependencyName = getFilenameForOatDependency(oatDependency);
            File file = new File(directory, oatDependencyName);
            if (!file.exists()) {
                throw new ClassPathResolver.NotFoundException("Cannot find dependency %s in %s", oatDependencyName, directory);
            }
            loadEntry(file, false);
        }
    }

    @Nonnull
    private String getFilenameForOatDependency(String oatDependency) {
        int index = oatDependency.lastIndexOf(47);
        String dependencyLeaf = oatDependency.substring(index + 1);
        if (dependencyLeaf.endsWith(".art")) {
            return dependencyLeaf.substring(0, dependencyLeaf.length() - 4) + ".oat";
        }
        return dependencyLeaf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class NoDexException extends Exception {
        public NoDexException(String message, Object... formatArgs) {
            super(String.format(message, formatArgs));
        }
    }
}
