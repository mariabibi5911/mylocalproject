package com.google.common.reflect;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.common.reflect.ClassPath;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        ImmutableSet<LocationInfo> locations = locationsFrom(classloader);
        Set<File> scanned = new HashSet<>();
        UnmodifiableIterator<LocationInfo> it = locations.iterator();
        while (it.hasNext()) {
            LocationInfo location = it.next();
            scanned.add(location.file());
        }
        ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<LocationInfo> it2 = locations.iterator();
        while (it2.hasNext()) {
            LocationInfo location2 = it2.next();
            builder.addAll(location2.scanResources(scanned));
        }
        return new ClassPath(builder.build());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(new Predicate() { // from class: com.google.common.reflect.ClassPath$$ExternalSyntheticLambda0
            @Override // com.google.common.base.Predicate
            public final boolean apply(Object obj) {
                return ((ClassPath.ClassInfo) obj).isTopLevel();
            }
        }).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
        Preconditions.checkNotNull(packageName);
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = it.next();
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
        Preconditions.checkNotNull(packageName);
        String packagePrefix = new StringBuilder(String.valueOf(packageName).length() + 1).append(packageName).append('.').toString();
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = it.next();
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    /* loaded from: classes7.dex */
    public static class ResourceInfo {
        private final File file;
        final ClassLoader loader;
        private final String resourceName;

        static ResourceInfo of(File file, String resourceName, ClassLoader loader) {
            if (resourceName.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(file, resourceName, loader);
            }
            return new ResourceInfo(file, resourceName, loader);
        }

        ResourceInfo(File file, String resourceName, ClassLoader loader) {
            this.file = (File) Preconditions.checkNotNull(file);
            this.resourceName = (String) Preconditions.checkNotNull(resourceName);
            this.loader = (ClassLoader) Preconditions.checkNotNull(loader);
        }

        public final URL url() {
            URL url = this.loader.getResource(this.resourceName);
            if (url == null) {
                throw new NoSuchElementException(this.resourceName);
            }
            return url;
        }

        public final ByteSource asByteSource() {
            return Resources.asByteSource(url());
        }

        public final CharSource asCharSource(Charset charset) {
            return Resources.asCharSource(url(), charset);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        final File getFile() {
            return this.file;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo that = (ResourceInfo) obj;
            return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    /* loaded from: classes7.dex */
    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(File file, String resourceName, ClassLoader loader) {
            super(file, resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.inRange('0', '9').trimLeadingFrom(innerClassName);
            }
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public boolean isTopLevel() {
            return this.className.indexOf(36) == -1;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.reflect.ClassPath.ResourceInfo
        public String toString() {
            return this.className;
        }
    }

    static ImmutableSet<LocationInfo> locationsFrom(ClassLoader classloader) {
        ImmutableSet.Builder<LocationInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<Map.Entry<File, ClassLoader>> it = getClassPathEntries(classloader).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<File, ClassLoader> entry = it.next();
            builder.add((ImmutableSet.Builder<LocationInfo>) new LocationInfo(entry.getKey(), entry.getValue()));
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public static final class LocationInfo {
        private final ClassLoader classloader;
        final File home;

        LocationInfo(File home, ClassLoader classloader) {
            this.home = (File) Preconditions.checkNotNull(home);
            this.classloader = (ClassLoader) Preconditions.checkNotNull(classloader);
        }

        public final File file() {
            return this.home;
        }

        public ImmutableSet<ResourceInfo> scanResources() throws IOException {
            return scanResources(new HashSet());
        }

        public ImmutableSet<ResourceInfo> scanResources(Set<File> scannedFiles) throws IOException {
            ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
            scannedFiles.add(this.home);
            scan(this.home, scannedFiles, builder);
            return builder.build();
        }

        private void scan(File file, Set<File> scannedUris, ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            try {
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {
                    scanDirectory(file, builder);
                } else {
                    scanJar(file, scannedUris, builder);
                }
            } catch (SecurityException e) {
                Logger logger = ClassPath.logger;
                String valueOf = String.valueOf(file);
                String valueOf2 = String.valueOf(e);
                logger.warning(new StringBuilder(String.valueOf(valueOf).length() + 16 + String.valueOf(valueOf2).length()).append("Cannot access ").append(valueOf).append(": ").append(valueOf2).toString());
            }
        }

        private void scanJar(File file, Set<File> scannedUris, ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            try {
                JarFile jarFile = new JarFile(file);
                try {
                    UnmodifiableIterator<File> it = ClassPath.getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                    while (it.hasNext()) {
                        File path = it.next();
                        if (scannedUris.add(path.getCanonicalFile())) {
                            scan(path, scannedUris, builder);
                        }
                    }
                    scanJarFile(jarFile, builder);
                    try {
                        jarFile.close();
                    } catch (IOException e) {
                    }
                } catch (Throwable th) {
                    try {
                        jarFile.close();
                    } catch (IOException e2) {
                    }
                    throw th;
                }
            } catch (IOException e3) {
            }
        }

        private void scanJarFile(JarFile file, ImmutableSet.Builder<ResourceInfo> builder) {
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && !entry.getName().equals("META-INF/MANIFEST.MF")) {
                    builder.add((ImmutableSet.Builder<ResourceInfo>) ResourceInfo.of(new File(file.getName()), entry.getName(), this.classloader));
                }
            }
        }

        private void scanDirectory(File directory, ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            Set<File> currentPath = new HashSet<>();
            currentPath.add(directory.getCanonicalFile());
            scanDirectory(directory, "", currentPath, builder);
        }

        private void scanDirectory(File directory, String packagePrefix, Set<File> currentPath, ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            File[] files = directory.listFiles();
            if (files == null) {
                Logger logger = ClassPath.logger;
                String valueOf = String.valueOf(directory);
                logger.warning(new StringBuilder(String.valueOf(valueOf).length() + 22).append("Cannot read directory ").append(valueOf).toString());
                return;
            }
            for (File f : files) {
                String name = f.getName();
                if (f.isDirectory()) {
                    File deref = f.getCanonicalFile();
                    if (currentPath.add(deref)) {
                        scanDirectory(deref, new StringBuilder(String.valueOf(packagePrefix).length() + 1 + String.valueOf(name).length()).append(packagePrefix).append(name).append("/").toString(), currentPath, builder);
                        currentPath.remove(deref);
                    }
                } else {
                    String valueOf2 = String.valueOf(packagePrefix);
                    String valueOf3 = String.valueOf(name);
                    String resourceName = valueOf3.length() != 0 ? valueOf2.concat(valueOf3) : new String(valueOf2);
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        builder.add((ImmutableSet.Builder<ResourceInfo>) ResourceInfo.of(f, resourceName, this.classloader));
                    }
                }
            }
        }

        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof LocationInfo)) {
                return false;
            }
            LocationInfo that = (LocationInfo) obj;
            return this.home.equals(that.home) && this.classloader.equals(that.classloader);
        }

        public int hashCode() {
            return this.home.hashCode();
        }

        public String toString() {
            return this.home.toString();
        }
    }

    static ImmutableSet<File> getClassPathFromManifest(File jarFile, @CheckForNull Manifest manifest) {
        if (manifest == null) {
            return ImmutableSet.of();
        }
        ImmutableSet.Builder<File> builder = ImmutableSet.builder();
        String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute != null) {
            for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                try {
                    URL url = getClassPathEntry(jarFile, path);
                    if (url.getProtocol().equals("file")) {
                        builder.add((ImmutableSet.Builder<File>) toFile(url));
                    }
                } catch (MalformedURLException e) {
                    Logger logger2 = logger;
                    String valueOf = String.valueOf(path);
                    logger2.warning(valueOf.length() != 0 ? "Invalid Class-Path entry: ".concat(valueOf) : new String("Invalid Class-Path entry: "));
                }
            }
        }
        return builder.build();
    }

    static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
        LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
        ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll(getClassPathEntries(parent));
        }
        UnmodifiableIterator<URL> it = getClassLoaderUrls(classloader).iterator();
        while (it.hasNext()) {
            URL url = it.next();
            if (url.getProtocol().equals("file")) {
                File file = toFile(url);
                if (!entries.containsKey(file)) {
                    entries.put(file, classloader);
                }
            }
        }
        return ImmutableMap.copyOf((Map) entries);
    }

    private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classloader) {
        if (classloader instanceof URLClassLoader) {
            return ImmutableList.copyOf(((URLClassLoader) classloader).getURLs());
        }
        if (classloader.equals(ClassLoader.getSystemClassLoader())) {
            return parseJavaClassPath();
        }
        return ImmutableList.of();
    }

    static ImmutableList<URL> parseJavaClassPath() {
        ImmutableList.Builder<URL> urls = ImmutableList.builder();
        for (String entry : Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())) {
            try {
                try {
                    urls.add((ImmutableList.Builder<URL>) new File(entry).toURI().toURL());
                } catch (SecurityException e) {
                    urls.add((ImmutableList.Builder<URL>) new URL("file", (String) null, new File(entry).getAbsolutePath()));
                }
            } catch (MalformedURLException e2) {
                Logger logger2 = logger;
                Level level = Level.WARNING;
                String valueOf = String.valueOf(entry);
                logger2.log(level, valueOf.length() != 0 ? "malformed classpath entry: ".concat(valueOf) : new String("malformed classpath entry: "), (Throwable) e2);
            }
        }
        return urls.build();
    }

    static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
        return new URL(jarFile.toURI().toURL(), path);
    }

    static String getClassName(String filename) {
        int classNameEnd = filename.length() - CLASS_FILE_NAME_EXTENSION.length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }

    static File toFile(URL url) {
        Preconditions.checkArgument(url.getProtocol().equals("file"));
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            return new File(url.getPath());
        }
    }
}
