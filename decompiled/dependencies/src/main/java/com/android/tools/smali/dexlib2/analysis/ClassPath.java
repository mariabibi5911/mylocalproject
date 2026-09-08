package com.android.tools.smali.dexlib2.analysis;

import androidx.exifinterface.media.ExifInterface;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.immutable.ImmutableDexFile;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ClassPath {
    public static final int NOT_ART = -1;
    public static final int NOT_SPECIFIED = -2;
    private final boolean checkPackagePrivateAccess;
    private final CacheLoader<String, TypeProto> classLoader;

    @Nonnull
    private List<ClassProvider> classProviders;
    private final Supplier<OdexedFieldInstructionMapper> fieldInstructionMapperSupplier;

    @Nonnull
    private LoadingCache<String, TypeProto> loadedClasses;
    public final int oatVersion;

    @Nonnull
    private final TypeProto unknownClass;

    public ClassPath(ClassProvider... classProviders) throws IOException {
        this(Arrays.asList(classProviders), false, -1);
    }

    public ClassPath(Iterable<ClassProvider> classProviders) throws IOException {
        this(classProviders, false, -1);
    }

    public ClassPath(@Nonnull Iterable<? extends ClassProvider> classProviders, boolean checkPackagePrivateAccess, int oatVersion) {
        CacheLoader<String, TypeProto> cacheLoader = new CacheLoader<String, TypeProto>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassPath.1
            @Override // com.google.common.cache.CacheLoader
            public TypeProto load(String type) throws Exception {
                if (type.charAt(0) == '[') {
                    return new ArrayProto(ClassPath.this, type);
                }
                return new ClassProto(ClassPath.this, type);
            }
        };
        this.classLoader = cacheLoader;
        this.loadedClasses = CacheBuilder.newBuilder().build(cacheLoader);
        this.fieldInstructionMapperSupplier = Suppliers.memoize(new Supplier<OdexedFieldInstructionMapper>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassPath.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Supplier
            public OdexedFieldInstructionMapper get() {
                return new OdexedFieldInstructionMapper(ClassPath.this.isArt());
            }
        });
        UnknownClassProto unknownClassProto = new UnknownClassProto(this);
        this.unknownClass = unknownClassProto;
        this.loadedClasses.put(unknownClassProto.getType(), unknownClassProto);
        this.checkPackagePrivateAccess = checkPackagePrivateAccess;
        this.oatVersion = oatVersion;
        loadPrimitiveType("Z");
        loadPrimitiveType("B");
        loadPrimitiveType(ExifInterface.LATITUDE_SOUTH);
        loadPrimitiveType("C");
        loadPrimitiveType("I");
        loadPrimitiveType("J");
        loadPrimitiveType("F");
        loadPrimitiveType("D");
        loadPrimitiveType("L");
        ArrayList newArrayList = Lists.newArrayList(classProviders);
        this.classProviders = newArrayList;
        newArrayList.add(getBasicClasses());
    }

    private void loadPrimitiveType(String type) {
        this.loadedClasses.put(type, new PrimitiveProto(this, type));
    }

    private static ClassProvider getBasicClasses() {
        return new DexClassProvider(new ImmutableDexFile(Opcodes.getDefault(), (Collection<? extends ClassDef>) ImmutableSet.of(new ReflectionClassDef(Class.class), new ReflectionClassDef(Cloneable.class), new ReflectionClassDef(Object.class), new ReflectionClassDef(Serializable.class), new ReflectionClassDef(String.class), new ReflectionClassDef(Throwable.class), new ReflectionClassDef[0])));
    }

    public boolean isArt() {
        return this.oatVersion != -1;
    }

    @Nonnull
    public TypeProto getClass(@Nonnull CharSequence type) {
        return this.loadedClasses.getUnchecked(type.toString());
    }

    @Nonnull
    public ClassDef getClassDef(String type) {
        for (ClassProvider provider : this.classProviders) {
            ClassDef classDef = provider.getClassDef(type);
            if (classDef != null) {
                return classDef;
            }
        }
        throw new UnresolvedClassException("Could not resolve class %s", type);
    }

    @Nonnull
    public TypeProto getUnknownClass() {
        return this.unknownClass;
    }

    public boolean shouldCheckPackagePrivateAccess() {
        return this.checkPackagePrivateAccess;
    }

    @Nonnull
    public OdexedFieldInstructionMapper getFieldInstructionMapper() {
        return this.fieldInstructionMapperSupplier.get();
    }
}
