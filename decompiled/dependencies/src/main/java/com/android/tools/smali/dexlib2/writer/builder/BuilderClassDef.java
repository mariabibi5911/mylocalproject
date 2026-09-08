package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BuilderClassDef extends BaseTypeReference implements ClassDef {
    final int accessFlags;

    @Nonnull
    final BuilderAnnotationSet annotations;

    @Nonnull
    final SortedSet<BuilderMethod> directMethods;

    @Nonnull
    final SortedSet<BuilderField> instanceFields;

    @Nonnull
    final BuilderTypeList interfaces;

    @Nullable
    final BuilderStringReference sourceFile;

    @Nonnull
    final SortedSet<BuilderField> staticFields;

    @Nullable
    final BuilderEncodedValues.BuilderArrayEncodedValue staticInitializers;

    @Nullable
    final BuilderTypeReference superclass;

    @Nonnull
    final BuilderTypeReference type;

    @Nonnull
    final SortedSet<BuilderMethod> virtualMethods;
    int classDefIndex = -1;
    int annotationDirectoryOffset = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderClassDef(@Nonnull BuilderTypeReference type, int accessFlags, @Nullable BuilderTypeReference superclass, @Nonnull BuilderTypeList interfaces, @Nullable BuilderStringReference sourceFile, @Nonnull BuilderAnnotationSet annotations, @Nullable SortedSet<BuilderField> staticFields, @Nullable SortedSet<BuilderField> instanceFields, @Nullable Iterable<? extends BuilderMethod> methods, @Nullable BuilderEncodedValues.BuilderArrayEncodedValue staticInitializers) {
        methods = methods == null ? ImmutableList.of() : methods;
        staticFields = staticFields == null ? ImmutableSortedSet.of() : staticFields;
        instanceFields = instanceFields == null ? ImmutableSortedSet.of() : instanceFields;
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.sourceFile = sourceFile;
        this.annotations = annotations;
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.directMethods = ImmutableSortedSet.copyOf(Iterables.filter(methods, MethodUtil.METHOD_IS_DIRECT));
        this.virtualMethods = ImmutableSortedSet.copyOf(Iterables.filter(methods, MethodUtil.METHOD_IS_VIRTUAL));
        this.staticInitializers = staticInitializers;
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        BuilderTypeReference builderTypeReference = this.superclass;
        if (builderTypeReference == null) {
            return null;
        }
        return builderTypeReference.getType();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        BuilderStringReference builderStringReference = this.sourceFile;
        if (builderStringReference == null) {
            return null;
        }
        return builderStringReference.getString();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderField> getStaticFields() {
        return this.staticFields;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderField> getInstanceFields() {
        return this.instanceFields;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderMethod> getDirectMethods() {
        return this.directMethods;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderMethod> getVirtualMethods() {
        return this.virtualMethods;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public List<String> getInterfaces() {
        return Lists.transform(this.interfaces, Functions.toStringFunction());
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<BuilderField> getFields() {
        return new AbstractCollection<BuilderField>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassDef.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<BuilderField> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.staticFields.iterator(), BuilderClassDef.this.instanceFields.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return BuilderClassDef.this.staticFields.size() + BuilderClassDef.this.instanceFields.size();
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<BuilderMethod> getMethods() {
        return new AbstractCollection<BuilderMethod>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderClassDef.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<BuilderMethod> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.directMethods.iterator(), BuilderClassDef.this.virtualMethods.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return BuilderClassDef.this.directMethods.size() + BuilderClassDef.this.virtualMethods.size();
            }
        };
    }
}
