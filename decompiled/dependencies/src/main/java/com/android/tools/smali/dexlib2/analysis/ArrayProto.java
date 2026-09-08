package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import com.android.tools.smali.dexlib2.util.TypeUtils;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.base.Strings;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ArrayProto implements TypeProto {
    private static final String BRACKETS = Strings.repeat("[", 256);
    protected final ClassPath classPath;
    protected final int dimensions;
    protected final String elementType;

    public ArrayProto(@Nonnull ClassPath classPath, @Nonnull String type) {
        this.classPath = classPath;
        int i = 0;
        while (type.charAt(i) == '[') {
            i++;
            if (i == type.length()) {
                throw new ExceptionWithContext("Invalid array type: %s", type);
            }
        }
        if (i == 0) {
            throw new ExceptionWithContext("Invalid array type: %s", type);
        }
        this.dimensions = i;
        this.elementType = type.substring(i);
    }

    public String toString() {
        return getType();
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return makeArrayType(this.elementType, this.dimensions);
    }

    public int getDimensions() {
        return this.dimensions;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean isInterface() {
        return false;
    }

    @Nonnull
    public String getElementType() {
        return this.elementType;
    }

    @Nonnull
    public String getImmediateElementType() {
        int i = this.dimensions;
        if (i > 1) {
            return makeArrayType(this.elementType, i - 1);
        }
        return this.elementType;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean implementsInterface(@Nonnull String iface) {
        return iface.equals("Ljava/lang/Cloneable;") || iface.equals("Ljava/io/Serializable;");
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return "Ljava/lang/Object;";
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        if (other instanceof ArrayProto) {
            if (TypeUtils.isPrimitiveType(getElementType()) || TypeUtils.isPrimitiveType(((ArrayProto) other).getElementType())) {
                int dimensions = this.dimensions;
                if (dimensions == ((ArrayProto) other).dimensions && getElementType().equals(((ArrayProto) other).getElementType())) {
                    return this;
                }
                return this.classPath.getClass("Ljava/lang/Object;");
            }
            int i = this.dimensions;
            if (i == ((ArrayProto) other).dimensions) {
                TypeProto thisClass = this.classPath.getClass(this.elementType);
                TypeProto otherClass = this.classPath.getClass(((ArrayProto) other).elementType);
                TypeProto mergedClass = thisClass.getCommonSuperclass(otherClass);
                if (thisClass == mergedClass) {
                    return this;
                }
                if (otherClass == mergedClass) {
                    return other;
                }
                return this.classPath.getClass(makeArrayType(mergedClass.getType(), this.dimensions));
            }
            int dimensions2 = Math.min(i, ((ArrayProto) other).dimensions);
            return this.classPath.getClass(makeArrayType("Ljava/lang/Object;", dimensions2));
        }
        if (other instanceof ClassProto) {
            try {
                if (other.isInterface()) {
                    if (implementsInterface(other.getType())) {
                        return other;
                    }
                }
            } catch (UnresolvedClassException e) {
            }
            return this.classPath.getClass("Ljava/lang/Object;");
        }
        return other.getCommonSuperclass(this);
    }

    @Nonnull
    private static String makeArrayType(@Nonnull String elementType, int dimensions) {
        return BRACKETS.substring(0, dimensions) + elementType;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        if (fieldOffset == 8) {
            return new ImmutableFieldReference(getType(), "length", "int");
        }
        return null;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public Method getMethodByVtableIndex(int vtableIndex) {
        return this.classPath.getClass("Ljava/lang/Object;").getMethodByVtableIndex(vtableIndex);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public int findMethodIndexInVtable(@Nonnull MethodReference method) {
        return this.classPath.getClass("Ljava/lang/Object;").findMethodIndexInVtable(method);
    }
}
