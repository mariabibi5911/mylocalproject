package com.android.tools.smali.dexlib2.analysis.reflection;

import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils;
import com.android.tools.smali.dexlib2.base.reference.BaseTypeReference;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ReflectionClassDef extends BaseTypeReference implements ClassDef {
    private static final int DIRECT_MODIFIERS = 10;
    private final Class cls;

    public ReflectionClassDef(Class cls) {
        this.cls = cls;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.cls.getModifiers();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        if (Modifier.isInterface(this.cls.getModifiers())) {
            return "Ljava/lang/Object;";
        }
        Class superClass = this.cls.getSuperclass();
        if (superClass == null) {
            return null;
        }
        return ReflectionUtils.javaToDexName(superClass.getName());
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public List<String> getInterfaces() {
        return ImmutableList.copyOf(Iterators.transform(Iterators.forArray(this.cls.getInterfaces()), new Function<Class, String>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.1
            @Override // com.google.common.base.Function
            @Nullable
            public String apply(@Nullable Class input) {
                if (input == null) {
                    return null;
                }
                return ReflectionUtils.javaToDexName(input.getName());
            }
        }));
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef, com.android.tools.smali.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return ImmutableSet.of();
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends Field> getStaticFields() {
        return new Iterable<Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.2
            @Override // java.lang.Iterable
            @Nonnull
            public Iterator<Field> iterator() {
                Iterator<java.lang.reflect.Field> staticFields = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), new Predicate<java.lang.reflect.Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.2.1
                    @Override // com.google.common.base.Predicate
                    public boolean apply(@Nullable java.lang.reflect.Field input) {
                        return input != null && Modifier.isStatic(input.getModifiers());
                    }
                });
                return Iterators.transform(staticFields, new Function<java.lang.reflect.Field, Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.2.2
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Field apply(@Nullable java.lang.reflect.Field input) {
                        return new ReflectionField(input);
                    }
                });
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends Field> getInstanceFields() {
        return new Iterable<Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.3
            @Override // java.lang.Iterable
            @Nonnull
            public Iterator<Field> iterator() {
                Iterator<java.lang.reflect.Field> staticFields = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), new Predicate<java.lang.reflect.Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.3.1
                    @Override // com.google.common.base.Predicate
                    public boolean apply(@Nullable java.lang.reflect.Field input) {
                        return (input == null || Modifier.isStatic(input.getModifiers())) ? false : true;
                    }
                });
                return Iterators.transform(staticFields, new Function<java.lang.reflect.Field, Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.3.2
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Field apply(@Nullable java.lang.reflect.Field input) {
                        return new ReflectionField(input);
                    }
                });
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Set<? extends Field> getFields() {
        return new AbstractSet<Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.4
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            @Nonnull
            public Iterator<Field> iterator() {
                return Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), new Function<java.lang.reflect.Field, Field>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.4.1
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Field apply(@Nullable java.lang.reflect.Field input) {
                        return new ReflectionField(input);
                    }
                });
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ReflectionClassDef.this.cls.getDeclaredFields().length;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends Method> getDirectMethods() {
        return new Iterable<Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.5
            @Override // java.lang.Iterable
            @Nonnull
            public Iterator<Method> iterator() {
                Iterator<Method> constructorIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredConstructors()), new Function<Constructor, Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.5.1
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Method apply(@Nullable Constructor input) {
                        return new ReflectionConstructor(input);
                    }
                });
                Iterator<java.lang.reflect.Method> directMethods = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), new Predicate<java.lang.reflect.Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.5.2
                    @Override // com.google.common.base.Predicate
                    public boolean apply(@Nullable java.lang.reflect.Method input) {
                        return (input == null || (input.getModifiers() & 10) == 0) ? false : true;
                    }
                });
                Iterator<Method> methodIterator = Iterators.transform(directMethods, new Function<java.lang.reflect.Method, Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.5.3
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Method apply(@Nullable java.lang.reflect.Method input) {
                        return new ReflectionMethod(input);
                    }
                });
                return Iterators.concat(constructorIterator, methodIterator);
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Iterable<? extends Method> getVirtualMethods() {
        return new Iterable<Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.6
            @Override // java.lang.Iterable
            @Nonnull
            public Iterator<Method> iterator() {
                Iterator<java.lang.reflect.Method> directMethods = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), new Predicate<java.lang.reflect.Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.6.1
                    @Override // com.google.common.base.Predicate
                    public boolean apply(@Nullable java.lang.reflect.Method input) {
                        return input != null && (input.getModifiers() & 10) == 0;
                    }
                });
                return Iterators.transform(directMethods, new Function<java.lang.reflect.Method, Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.6.2
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Method apply(@Nullable java.lang.reflect.Method input) {
                        return new ReflectionMethod(input);
                    }
                });
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public Set<? extends Method> getMethods() {
        return new AbstractSet<Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.7
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            @Nonnull
            public Iterator<Method> iterator() {
                Iterator<Method> constructorIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredConstructors()), new Function<Constructor, Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.7.1
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Method apply(@Nullable Constructor input) {
                        return new ReflectionConstructor(input);
                    }
                });
                Iterator<Method> methodIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), new Function<java.lang.reflect.Method, Method>() { // from class: com.android.tools.smali.dexlib2.analysis.reflection.ReflectionClassDef.7.2
                    @Override // com.google.common.base.Function
                    @Nullable
                    public Method apply(@Nullable java.lang.reflect.Method input) {
                        return new ReflectionMethod(input);
                    }
                });
                return Iterators.concat(constructorIterator, methodIterator);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ReflectionClassDef.this.cls.getDeclaredMethods().length + ReflectionClassDef.this.cls.getDeclaredConstructors().length;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.TypeReference, com.android.tools.smali.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return ReflectionUtils.javaToDexName(this.cls.getName());
    }
}
