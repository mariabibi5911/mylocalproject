package com.android.tools.smali.dexlib2.immutable;

import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.util.FieldUtil;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.util.ImmutableConverter;
import com.android.tools.smali.util.ImmutableUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableClassDef extends BaseTypeReference implements ClassDef {
    private static final ImmutableConverter<ImmutableClassDef, ClassDef> CONVERTER = new ImmutableConverter<ImmutableClassDef, ClassDef>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableClassDef.3
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull ClassDef item) {
            return item instanceof ImmutableClassDef;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableClassDef makeImmutable(@Nonnull ClassDef item) {
            return ImmutableClassDef.of(item);
        }
    };
    protected final int accessFlags;

    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotation> annotations;

    @Nonnull
    protected final ImmutableSortedSet<? extends ImmutableMethod> directMethods;

    @Nonnull
    protected final ImmutableSortedSet<? extends ImmutableField> instanceFields;

    @Nonnull
    protected final ImmutableList<String> interfaces;

    @Nullable
    protected final String sourceFile;

    @Nonnull
    protected final ImmutableSortedSet<? extends ImmutableField> staticFields;

    @Nullable
    protected final String superclass;

    @Nonnull
    protected final String type;

    @Nonnull
    protected final ImmutableSortedSet<? extends ImmutableMethod> virtualMethods;

    public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable Collection<String> interfaces, @Nullable String sourceFile, @Nullable Collection<? extends Annotation> annotations, @Nullable Iterable<? extends Field> fields, @Nullable Iterable<? extends Method> methods) {
        fields = fields == null ? ImmutableList.of() : fields;
        methods = methods == null ? ImmutableList.of() : methods;
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = interfaces == null ? ImmutableList.of() : ImmutableList.copyOf((Collection) interfaces);
        this.sourceFile = sourceFile;
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.staticFields = ImmutableField.immutableSetOf(Iterables.filter(fields, FieldUtil.FIELD_IS_STATIC));
        this.instanceFields = ImmutableField.immutableSetOf(Iterables.filter(fields, FieldUtil.FIELD_IS_INSTANCE));
        this.directMethods = ImmutableMethod.immutableSetOf(Iterables.filter(methods, MethodUtil.METHOD_IS_DIRECT));
        this.virtualMethods = ImmutableMethod.immutableSetOf(Iterables.filter(methods, MethodUtil.METHOD_IS_VIRTUAL));
    }

    public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable Collection<String> interfaces, @Nullable String sourceFile, @Nullable Collection<? extends Annotation> annotations, @Nullable Iterable<? extends Field> staticFields, @Nullable Iterable<? extends Field> instanceFields, @Nullable Iterable<? extends Method> directMethods, @Nullable Iterable<? extends Method> virtualMethods) {
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = interfaces == null ? ImmutableList.of() : ImmutableList.copyOf((Collection) interfaces);
        this.sourceFile = sourceFile;
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.staticFields = ImmutableField.immutableSetOf(staticFields);
        this.instanceFields = ImmutableField.immutableSetOf(instanceFields);
        this.directMethods = ImmutableMethod.immutableSetOf(directMethods);
        this.virtualMethods = ImmutableMethod.immutableSetOf(virtualMethods);
    }

    public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable ImmutableList<String> interfaces, @Nullable String sourceFile, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable ImmutableSortedSet<? extends ImmutableField> staticFields, @Nullable ImmutableSortedSet<? extends ImmutableField> instanceFields, @Nullable ImmutableSortedSet<? extends ImmutableMethod> directMethods, @Nullable ImmutableSortedSet<? extends ImmutableMethod> virtualMethods) {
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = ImmutableUtils.nullToEmptyList(interfaces);
        this.sourceFile = sourceFile;
        this.annotations = ImmutableUtils.nullToEmptySet(annotations);
        this.staticFields = ImmutableUtils.nullToEmptySortedSet(staticFields);
        this.instanceFields = ImmutableUtils.nullToEmptySortedSet(instanceFields);
        this.directMethods = ImmutableUtils.nullToEmptySortedSet(directMethods);
        this.virtualMethods = ImmutableUtils.nullToEmptySortedSet(virtualMethods);
    }

    public static ImmutableClassDef of(ClassDef classDef) {
        if (classDef instanceof ImmutableClassDef) {
            return (ImmutableClassDef) classDef;
        }
        return new ImmutableClassDef(classDef.getType(), classDef.getAccessFlags(), classDef.getSuperclass(), classDef.getInterfaces(), classDef.getSourceFile(), classDef.getAnnotations(), classDef.getStaticFields(), classDef.getInstanceFields(), classDef.getDirectMethods(), classDef.getVirtualMethods());
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        return this.superclass;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public ImmutableList<String> getInterfaces() {
        return this.interfaces;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        return this.sourceFile;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
        return this.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public ImmutableSet<? extends ImmutableField> getStaticFields() {
        return this.staticFields;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public ImmutableSet<? extends ImmutableField> getInstanceFields() {
        return this.instanceFields;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public ImmutableSet<? extends ImmutableMethod> getDirectMethods() {
        return this.directMethods;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public ImmutableSet<? extends ImmutableMethod> getVirtualMethods() {
        return this.virtualMethods;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<? extends ImmutableField> getFields() {
        return new AbstractCollection<ImmutableField>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableClassDef.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<ImmutableField> iterator() {
                return Iterators.concat(ImmutableClassDef.this.staticFields.iterator(), ImmutableClassDef.this.instanceFields.iterator());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return ImmutableClassDef.this.staticFields.size() + ImmutableClassDef.this.instanceFields.size();
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<? extends ImmutableMethod> getMethods() {
        return new AbstractCollection<ImmutableMethod>() { // from class: com.android.tools.smali.dexlib2.immutable.ImmutableClassDef.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<ImmutableMethod> iterator() {
                return Iterators.concat(ImmutableClassDef.this.directMethods.iterator(), ImmutableClassDef.this.virtualMethods.iterator());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return ImmutableClassDef.this.directMethods.size() + ImmutableClassDef.this.virtualMethods.size();
            }
        };
    }

    @Nonnull
    public static ImmutableSet<ImmutableClassDef> immutableSetOf(@Nullable Iterable<? extends ClassDef> iterable) {
        return CONVERTER.toSet(iterable);
    }
}
