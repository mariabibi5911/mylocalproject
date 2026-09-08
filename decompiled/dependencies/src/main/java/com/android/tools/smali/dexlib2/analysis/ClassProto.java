package com.android.tools.smali.dexlib2.analysis;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.analysis.util.TypeProtoUtils;
import com.android.tools.smali.dexlib2.base.reference.BaseMethodReference;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.util.AlignmentUtils;
import com.android.tools.smali.dexlib2.util.MethodUtil;
import com.android.tools.smali.util.ExceptionWithContext;
import com.android.tools.smali.util.SparseArray;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ClassProto implements TypeProto {
    private static final byte OTHER = 2;
    private static final byte REFERENCE = 0;
    private static final byte WIDE = 1;

    @Nonnull
    protected final ClassPath classPath;

    @Nonnull
    protected final String type;
    protected boolean vtableFullyResolved = true;
    protected boolean interfacesFullyResolved = true;
    protected Set<String> unresolvedInterfaces = null;

    @Nonnull
    private final Supplier<ClassDef> classDefSupplier = Suppliers.memoize(new Supplier<ClassDef>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Supplier
        public ClassDef get() {
            return ClassProto.this.classPath.getClassDef(ClassProto.this.type);
        }
    });

    @Nonnull
    private final Supplier<LinkedHashMap<String, ClassDef>> preDefaultMethodInterfaceSupplier = Suppliers.memoize(new Supplier<LinkedHashMap<String, ClassDef>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.2
        @Override // com.google.common.base.Supplier
        public LinkedHashMap<String, ClassDef> get() {
            Set<String> unresolvedInterfaces = new HashSet<>(0);
            LinkedHashMap<String, ClassDef> interfaces = Maps.newLinkedHashMap();
            try {
                for (String interfaceType : ClassProto.this.getClassDef().getInterfaces()) {
                    if (!interfaces.containsKey(interfaceType)) {
                        try {
                            ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType);
                            interfaces.put(interfaceType, interfaceDef);
                        } catch (UnresolvedClassException e) {
                            interfaces.put(interfaceType, null);
                            unresolvedInterfaces.add(interfaceType);
                            ClassProto.this.interfacesFullyResolved = false;
                        }
                        ClassProto interfaceProto = (ClassProto) ClassProto.this.classPath.getClass(interfaceType);
                        for (String superInterface : interfaceProto.getInterfaces().keySet()) {
                            if (!interfaces.containsKey(superInterface)) {
                                interfaces.put(superInterface, interfaceProto.getInterfaces().get(superInterface));
                            }
                        }
                        if (!interfaceProto.interfacesFullyResolved) {
                            unresolvedInterfaces.addAll(interfaceProto.getUnresolvedInterfaces());
                            ClassProto.this.interfacesFullyResolved = false;
                        }
                    }
                }
            } catch (UnresolvedClassException e2) {
                interfaces.put(ClassProto.this.type, null);
                unresolvedInterfaces.add(ClassProto.this.type);
                ClassProto.this.interfacesFullyResolved = false;
            }
            if (ClassProto.this.isInterface() && !interfaces.containsKey(ClassProto.this.getType())) {
                interfaces.put(ClassProto.this.getType(), null);
            }
            String superclass = ClassProto.this.getSuperclass();
            if (superclass != null) {
                try {
                    ClassProto superclassProto = (ClassProto) ClassProto.this.classPath.getClass(superclass);
                    for (String superclassInterface : superclassProto.getInterfaces().keySet()) {
                        if (!interfaces.containsKey(superclassInterface)) {
                            interfaces.put(superclassInterface, null);
                        }
                    }
                    if (!superclassProto.interfacesFullyResolved) {
                        unresolvedInterfaces.addAll(superclassProto.getUnresolvedInterfaces());
                        ClassProto.this.interfacesFullyResolved = false;
                    }
                } catch (UnresolvedClassException e3) {
                    unresolvedInterfaces.add(superclass);
                    ClassProto.this.interfacesFullyResolved = false;
                }
            }
            if (unresolvedInterfaces.size() > 0) {
                ClassProto.this.unresolvedInterfaces = unresolvedInterfaces;
            }
            return interfaces;
        }
    });

    @Nonnull
    private final Supplier<LinkedHashMap<String, ClassDef>> postDefaultMethodInterfaceSupplier = Suppliers.memoize(new Supplier<LinkedHashMap<String, ClassDef>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.3
        @Override // com.google.common.base.Supplier
        public LinkedHashMap<String, ClassDef> get() {
            Set<String> unresolvedInterfaces = new HashSet<>(0);
            LinkedHashMap<String, ClassDef> interfaces = Maps.newLinkedHashMap();
            String superclass = ClassProto.this.getSuperclass();
            if (superclass != null) {
                ClassProto superclassProto = (ClassProto) ClassProto.this.classPath.getClass(superclass);
                for (String superclassInterface : superclassProto.getInterfaces().keySet()) {
                    interfaces.put(superclassInterface, null);
                }
                if (!superclassProto.interfacesFullyResolved) {
                    unresolvedInterfaces.addAll(superclassProto.getUnresolvedInterfaces());
                    ClassProto.this.interfacesFullyResolved = false;
                }
            }
            try {
                for (String interfaceType : ClassProto.this.getClassDef().getInterfaces()) {
                    if (!interfaces.containsKey(interfaceType)) {
                        ClassProto interfaceProto = (ClassProto) ClassProto.this.classPath.getClass(interfaceType);
                        try {
                            for (Map.Entry<String, ClassDef> entry : interfaceProto.getInterfaces().entrySet()) {
                                if (!interfaces.containsKey(entry.getKey())) {
                                    interfaces.put(entry.getKey(), entry.getValue());
                                }
                            }
                        } catch (UnresolvedClassException e) {
                            interfaces.put(interfaceType, null);
                            unresolvedInterfaces.add(interfaceType);
                            ClassProto.this.interfacesFullyResolved = false;
                        }
                        if (!interfaceProto.interfacesFullyResolved) {
                            unresolvedInterfaces.addAll(interfaceProto.getUnresolvedInterfaces());
                            ClassProto.this.interfacesFullyResolved = false;
                        }
                        try {
                            ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType);
                            interfaces.put(interfaceType, interfaceDef);
                        } catch (UnresolvedClassException e2) {
                            interfaces.put(interfaceType, null);
                            unresolvedInterfaces.add(interfaceType);
                            ClassProto.this.interfacesFullyResolved = false;
                        }
                    }
                }
            } catch (UnresolvedClassException e3) {
                interfaces.put(ClassProto.this.type, null);
                unresolvedInterfaces.add(ClassProto.this.type);
                ClassProto.this.interfacesFullyResolved = false;
            }
            if (unresolvedInterfaces.size() > 0) {
                ClassProto.this.unresolvedInterfaces = unresolvedInterfaces;
            }
            return interfaces;
        }
    });

    @Nonnull
    private final Supplier<SparseArray<FieldReference>> dalvikInstanceFieldsSupplier = Suppliers.memoize(new Supplier<SparseArray<FieldReference>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.4
        static final /* synthetic */ boolean $assertionsDisabled = false;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Supplier
        public SparseArray<FieldReference> get() {
            int fieldIndexMod;
            SparseArray<FieldReference> superFields;
            int fieldOffset;
            ArrayList<Field> fields = getSortedInstanceFields(ClassProto.this.getClassDef());
            int fieldCount = fields.size();
            byte[] fieldTypes = new byte[fields.size()];
            for (int i = 0; i < fieldCount; i++) {
                fieldTypes[i] = ClassProto.getFieldType(fields.get(i));
            }
            int i2 = fields.size();
            int back = i2 - 1;
            int front = 0;
            while (front < fieldCount) {
                if (fieldTypes[front] != 0) {
                    while (true) {
                        if (back <= front) {
                            break;
                        }
                        if (fieldTypes[back] == 0) {
                            swap(fieldTypes, fields, front, back);
                            back--;
                            break;
                        }
                        back--;
                    }
                }
                if (fieldTypes[front] != 0) {
                    break;
                }
                front++;
            }
            int startFieldOffset = 8;
            String superclassType = ClassProto.this.getSuperclass();
            ClassProto superclass = null;
            if (superclassType != null) {
                superclass = (ClassProto) ClassProto.this.classPath.getClass(superclassType);
                startFieldOffset = superclass.getNextFieldOffset();
            }
            if (startFieldOffset % 8 == 0) {
                fieldIndexMod = 0;
            } else {
                fieldIndexMod = 1;
            }
            if (front < fieldCount && front % 2 != fieldIndexMod) {
                if (fieldTypes[front] == 1) {
                    int back2 = fieldCount - 1;
                    while (true) {
                        if (back2 <= front) {
                            break;
                        }
                        if (fieldTypes[back2] == 2) {
                            swap(fieldTypes, fields, front, back2);
                            front++;
                            break;
                        }
                        back2--;
                    }
                } else {
                    front++;
                }
            }
            int back3 = fieldCount - 1;
            while (front < fieldCount) {
                if (fieldTypes[front] != 1) {
                    while (true) {
                        if (back3 <= front) {
                            break;
                        }
                        if (fieldTypes[back3] == 1) {
                            swap(fieldTypes, fields, front, back3);
                            back3--;
                            break;
                        }
                        back3--;
                    }
                }
                if (fieldTypes[front] != 1) {
                    break;
                }
                front++;
            }
            if (superclass != null) {
                superFields = superclass.getInstanceFields();
            } else {
                superFields = new SparseArray<>();
            }
            int superFieldCount = superFields.size();
            int totalFieldCount = superFieldCount + fieldCount;
            SparseArray<FieldReference> instanceFields = new SparseArray<>(totalFieldCount);
            if (superclass != null && superFieldCount > 0) {
                for (int i3 = 0; i3 < superFieldCount; i3++) {
                    instanceFields.append(superFields.keyAt(i3), superFields.valueAt(i3));
                }
                int fieldOffset2 = instanceFields.keyAt(superFieldCount - 1);
                FieldReference lastSuperField = superFields.valueAt(superFieldCount - 1);
                char fieldType = lastSuperField.getType().charAt(0);
                if (fieldType == 'J' || fieldType == 'D') {
                    fieldOffset = fieldOffset2 + 8;
                } else {
                    fieldOffset = fieldOffset2 + 4;
                }
            } else {
                fieldOffset = 8;
            }
            boolean gotDouble = false;
            int i4 = 0;
            while (i4 < fieldCount) {
                FieldReference field = fields.get(i4);
                ArrayList<Field> fields2 = fields;
                int fieldCount2 = fieldCount;
                if (fieldTypes[i4] == 1 && !gotDouble) {
                    if (fieldOffset % 8 != 0) {
                        if (fieldOffset % 8 != 4) {
                            throw new AssertionError();
                        }
                        fieldOffset += 4;
                    }
                    gotDouble = true;
                }
                instanceFields.append(fieldOffset, field);
                if (fieldTypes[i4] == 1) {
                    fieldOffset += 8;
                } else {
                    fieldOffset += 4;
                }
                i4++;
                fields = fields2;
                fieldCount = fieldCount2;
            }
            return instanceFields;
        }

        @Nonnull
        private ArrayList<Field> getSortedInstanceFields(@Nonnull ClassDef classDef) {
            ArrayList<Field> fields = Lists.newArrayList(classDef.getInstanceFields());
            Collections.sort(fields);
            return fields;
        }

        private void swap(byte[] fieldTypes, List<Field> fields, int position1, int position2) {
            byte tempType = fieldTypes[position1];
            fieldTypes[position1] = fieldTypes[position2];
            fieldTypes[position2] = tempType;
            Field tempField = fields.set(position1, fields.get(position2));
            fields.set(position2, tempField);
        }
    });

    @Nonnull
    private final Supplier<SparseArray<FieldReference>> artInstanceFieldsSupplier = Suppliers.memoize(new Supplier<SparseArray<FieldReference>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.5
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Supplier
        public SparseArray<FieldReference> get() {
            PriorityQueue<FieldGap> gaps = new PriorityQueue<>();
            SparseArray<FieldReference> linkedFields = new SparseArray<>();
            ArrayList<Field> fields = getSortedInstanceFields(ClassProto.this.getClassDef());
            int fieldOffset = 0;
            String superclassType = ClassProto.this.getSuperclass();
            if (superclassType != null) {
                ClassProto superclass = (ClassProto) ClassProto.this.classPath.getClass(superclassType);
                SparseArray<FieldReference> superFields = superclass.getInstanceFields();
                FieldReference field = null;
                int lastOffset = 0;
                for (int i = 0; i < superFields.size(); i++) {
                    int offset = superFields.keyAt(i);
                    field = superFields.valueAt(i);
                    linkedFields.put(offset, field);
                    lastOffset = offset;
                }
                if (field != null) {
                    fieldOffset = lastOffset + getFieldSize(field);
                }
            }
            Iterator<Field> it = fields.iterator();
            while (it.hasNext()) {
                Field field2 = it.next();
                int fieldSize = getFieldSize(field2);
                if (!AlignmentUtils.isAligned(fieldOffset, fieldSize)) {
                    int oldOffset = fieldOffset;
                    fieldOffset = AlignmentUtils.alignOffset(fieldOffset, fieldSize);
                    addFieldGap(oldOffset, fieldOffset, gaps);
                }
                FieldGap gap = gaps.peek();
                if (gap != null && gap.size >= fieldSize) {
                    gaps.poll();
                    linkedFields.put(gap.offset, field2);
                    if (gap.size > fieldSize) {
                        addFieldGap(gap.offset + fieldSize, gap.offset + gap.size, gaps);
                    }
                } else {
                    linkedFields.append(fieldOffset, field2);
                    fieldOffset += fieldSize;
                }
            }
            return linkedFields;
        }

        private void addFieldGap(int gapStart, int gapEnd, @Nonnull PriorityQueue<FieldGap> gaps) {
            int offset = gapStart;
            while (offset < gapEnd) {
                int remaining = gapEnd - offset;
                if (remaining >= 4 && offset % 4 == 0) {
                    gaps.add(FieldGap.newFieldGap(offset, 4, ClassProto.this.classPath.oatVersion));
                    offset += 4;
                } else if (remaining >= 2 && offset % 2 == 0) {
                    gaps.add(FieldGap.newFieldGap(offset, 2, ClassProto.this.classPath.oatVersion));
                    offset += 2;
                } else {
                    gaps.add(FieldGap.newFieldGap(offset, 1, ClassProto.this.classPath.oatVersion));
                    offset++;
                }
            }
        }

        @Nonnull
        private ArrayList<Field> getSortedInstanceFields(@Nonnull ClassDef classDef) {
            ArrayList<Field> fields = Lists.newArrayList(classDef.getInstanceFields());
            Collections.sort(fields, new Comparator<Field>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.5.1
                @Override // java.util.Comparator
                public int compare(Field field1, Field field2) {
                    int result = Ints.compare(getFieldSortOrder(field1), getFieldSortOrder(field2));
                    if (result != 0) {
                        return result;
                    }
                    int result2 = field1.getName().compareTo(field2.getName());
                    if (result2 != 0) {
                        return result2;
                    }
                    return field1.getType().compareTo(field2.getType());
                }
            });
            return fields;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getFieldSortOrder(@Nonnull FieldReference field) {
            switch (field.getType().charAt(0)) {
                case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
                    return 8;
                case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
                    return 5;
                case HeaderItem.TYPE_START_OFFSET /* 68 */:
                    return 2;
                case 'F':
                    return 4;
                case 'I':
                    return 3;
                case 'J':
                    return 1;
                case HeaderItem.PROTO_START_OFFSET /* 76 */:
                case '[':
                    return 0;
                case 'S':
                    return 6;
                case 'Z':
                    return 7;
                default:
                    throw new ExceptionWithContext("Invalid field type: %s", field.getType());
            }
        }

        private int getFieldSize(@Nonnull FieldReference field) {
            return ClassProto.getTypeSize(field.getType().charAt(0));
        }
    });

    @Nonnull
    private final Supplier<List<Method>> preDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.6
        @Override // com.google.common.base.Supplier
        public List<Method> get() {
            List<Method> vtable = Lists.newArrayList();
            try {
                String superclassType = ClassProto.this.getSuperclass();
                if (superclassType != null) {
                    ClassProto superclass = (ClassProto) ClassProto.this.classPath.getClass(superclassType);
                    vtable.addAll(superclass.getVtable());
                    if (!superclass.vtableFullyResolved) {
                        ClassProto.this.vtableFullyResolved = false;
                        return vtable;
                    }
                }
                if (!ClassProto.this.isInterface()) {
                    ClassProto classProto = ClassProto.this;
                    classProto.addToVtable(classProto.getClassDef().getVirtualMethods(), vtable, true, true);
                    Iterable<ClassDef> interfaces = ClassProto.this.getDirectInterfaces();
                    for (ClassDef interfaceDef : interfaces) {
                        List<Method> interfaceMethods = Lists.newArrayList();
                        for (Method interfaceMethod : interfaceDef.getVirtualMethods()) {
                            interfaceMethods.add(new ReparentedMethod(interfaceMethod, ClassProto.this.type));
                        }
                        ClassProto.this.addToVtable(interfaceMethods, vtable, false, true);
                    }
                }
                return vtable;
            } catch (UnresolvedClassException e) {
                vtable.addAll(((ClassProto) ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
                ClassProto.this.vtableFullyResolved = false;
                return vtable;
            }
        }
    });

    @Nonnull
    private final Supplier<List<Method>> buggyPostDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.7
        @Override // com.google.common.base.Supplier
        public List<Method> get() {
            Method oldVtableMethod;
            String superclassType;
            List<String> interfaces;
            String interfaceType;
            List<Method> vtable = Lists.newArrayList();
            try {
                String superclassType2 = ClassProto.this.getSuperclass();
                if (superclassType2 != null) {
                    ClassProto superclass = (ClassProto) ClassProto.this.classPath.getClass(superclassType2);
                    vtable.addAll(superclass.getVtable());
                    if (!superclass.vtableFullyResolved) {
                        ClassProto.this.vtableFullyResolved = false;
                        return vtable;
                    }
                }
                if (!ClassProto.this.isInterface()) {
                    ClassProto classProto = ClassProto.this;
                    classProto.addToVtable(classProto.getClassDef().getVirtualMethods(), vtable, true, true);
                    List<String> interfaces2 = Lists.newArrayList(ClassProto.this.getInterfaces().keySet());
                    List<Method> defaultMethods = Lists.newArrayList();
                    List<Method> defaultConflictMethods = Lists.newArrayList();
                    List<Method> mirandaMethods = Lists.newArrayList();
                    final HashMap<MethodReference, Integer> methodOrder = Maps.newHashMap();
                    for (int i = interfaces2.size() - 1; i >= 0; i--) {
                        String interfaceType2 = interfaces2.get(i);
                        ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType2);
                        for (Method interfaceMethod : interfaceDef.getVirtualMethods()) {
                            int vtableIndex = ClassProto.this.findMethodIndexInVtableReverse(vtable, interfaceMethod);
                            if (vtableIndex < 0) {
                                oldVtableMethod = null;
                            } else {
                                Method oldVtableMethod2 = vtable.get(vtableIndex);
                                oldVtableMethod = oldVtableMethod2;
                            }
                            int j = 0;
                            while (j < vtable.size()) {
                                Method candidate = vtable.get(j);
                                if (!MethodUtil.methodSignaturesMatch(candidate, interfaceMethod)) {
                                    superclassType = superclassType2;
                                    interfaces = interfaces2;
                                    interfaceType = interfaceType2;
                                } else {
                                    superclassType = superclassType2;
                                    if (!ClassProto.this.classPath.shouldCheckPackagePrivateAccess()) {
                                        interfaces = interfaces2;
                                        interfaceType = interfaceType2;
                                    } else {
                                        interfaces = interfaces2;
                                        interfaceType = interfaceType2;
                                        if (!AnalyzedMethodUtil.canAccess(ClassProto.this, candidate, true, false, false)) {
                                        }
                                    }
                                    if (ClassProto.this.interfaceMethodOverrides(interfaceMethod, candidate)) {
                                        vtable.set(j, interfaceMethod);
                                    }
                                }
                                j++;
                                interfaceType2 = interfaceType;
                                superclassType2 = superclassType;
                                interfaces2 = interfaces;
                            }
                            String superclassType3 = superclassType2;
                            List<String> interfaces3 = interfaces2;
                            String interfaceType3 = interfaceType2;
                            if (vtableIndex < 0 || ClassProto.this.isOverridableByDefaultMethod(vtable.get(vtableIndex))) {
                                int defaultMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultMethods, interfaceMethod);
                                if (defaultMethodIndex < 0) {
                                    int defaultConflictMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultConflictMethods, interfaceMethod);
                                    if (defaultConflictMethodIndex < 0) {
                                        int mirandaMethodIndex = ClassProto.this.findMethodIndexInVtable(mirandaMethods, interfaceMethod);
                                        if (mirandaMethodIndex >= 0) {
                                            if (AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                                interfaceType2 = interfaceType3;
                                                superclassType2 = superclassType3;
                                                interfaces2 = interfaces3;
                                            } else {
                                                ClassProto existingInterface = (ClassProto) ClassProto.this.classPath.getClass(mirandaMethods.get(mirandaMethodIndex).getDefiningClass());
                                                if (!existingInterface.implementsInterface(interfaceMethod.getDefiningClass())) {
                                                    Method oldMethod = mirandaMethods.remove(mirandaMethodIndex);
                                                    int methodOrderValue = methodOrder.get(oldMethod).intValue();
                                                    methodOrder.put(interfaceMethod, Integer.valueOf(methodOrderValue));
                                                    defaultMethods.add(interfaceMethod);
                                                }
                                                interfaceType2 = interfaceType3;
                                                superclassType2 = superclassType3;
                                                interfaces2 = interfaces3;
                                            }
                                        } else {
                                            if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                                Method oldVtableMethod3 = oldVtableMethod;
                                                if (oldVtableMethod3 != null && !ClassProto.this.interfaceMethodOverrides(interfaceMethod, oldVtableMethod3)) {
                                                    interfaceType2 = interfaceType3;
                                                    superclassType2 = superclassType3;
                                                    interfaces2 = interfaces3;
                                                } else {
                                                    defaultMethods.add(interfaceMethod);
                                                    methodOrder.put(interfaceMethod, Integer.valueOf(methodOrder.size()));
                                                }
                                            } else if (oldVtableMethod == null) {
                                                mirandaMethods.add(interfaceMethod);
                                                methodOrder.put(interfaceMethod, Integer.valueOf(methodOrder.size()));
                                            }
                                            interfaceType2 = interfaceType3;
                                            superclassType2 = superclassType3;
                                            interfaces2 = interfaces3;
                                        }
                                    } else {
                                        interfaceType2 = interfaceType3;
                                        superclassType2 = superclassType3;
                                        interfaces2 = interfaces3;
                                    }
                                } else if (AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                    interfaceType2 = interfaceType3;
                                    superclassType2 = superclassType3;
                                    interfaces2 = interfaces3;
                                } else {
                                    ClassProto existingInterface2 = (ClassProto) ClassProto.this.classPath.getClass(defaultMethods.get(defaultMethodIndex).getDefiningClass());
                                    if (!existingInterface2.implementsInterface(interfaceMethod.getDefiningClass())) {
                                        Method removedMethod = defaultMethods.remove(defaultMethodIndex);
                                        defaultConflictMethods.add(removedMethod);
                                    }
                                    interfaceType2 = interfaceType3;
                                    superclassType2 = superclassType3;
                                    interfaces2 = interfaces3;
                                }
                            } else {
                                interfaceType2 = interfaceType3;
                                superclassType2 = superclassType3;
                                interfaces2 = interfaces3;
                            }
                        }
                    }
                    Comparator<MethodReference> comparator = new Comparator<MethodReference>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.7.1
                        @Override // java.util.Comparator
                        public int compare(MethodReference o1, MethodReference o2) {
                            return Ints.compare(((Integer) methodOrder.get(o1)).intValue(), ((Integer) methodOrder.get(o2)).intValue());
                        }
                    };
                    Collections.sort(mirandaMethods, comparator);
                    Collections.sort(defaultMethods, comparator);
                    Collections.sort(defaultConflictMethods, comparator);
                    vtable.addAll(mirandaMethods);
                    vtable.addAll(defaultMethods);
                    vtable.addAll(defaultConflictMethods);
                }
                return vtable;
            } catch (UnresolvedClassException e) {
                vtable.addAll(((ClassProto) ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
                ClassProto.this.vtableFullyResolved = false;
                return vtable;
            }
        }
    });

    @Nonnull
    private final Supplier<List<Method>> postDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.8
        @Override // com.google.common.base.Supplier
        public List<Method> get() {
            String superclassType;
            List<Method> vtable = Lists.newArrayList();
            try {
                String superclassType2 = ClassProto.this.getSuperclass();
                if (superclassType2 != null) {
                    ClassProto superclass = (ClassProto) ClassProto.this.classPath.getClass(superclassType2);
                    vtable.addAll(superclass.getVtable());
                    if (!superclass.vtableFullyResolved) {
                        ClassProto.this.vtableFullyResolved = false;
                        return vtable;
                    }
                }
                if (!ClassProto.this.isInterface()) {
                    ClassProto classProto = ClassProto.this;
                    classProto.addToVtable(classProto.getClassDef().getVirtualMethods(), vtable, true, true);
                    Iterable<ClassDef> interfaces = Lists.reverse(Lists.newArrayList(ClassProto.this.getDirectInterfaces()));
                    List<Method> defaultMethods = Lists.newArrayList();
                    List<Method> defaultConflictMethods = Lists.newArrayList();
                    List<Method> mirandaMethods = Lists.newArrayList();
                    final HashMap<MethodReference, Integer> methodOrder = Maps.newHashMap();
                    for (ClassDef interfaceDef : interfaces) {
                        for (Method interfaceMethod : interfaceDef.getVirtualMethods()) {
                            int vtableIndex = ClassProto.this.findMethodIndexInVtable(vtable, interfaceMethod);
                            if (vtableIndex >= 0) {
                                if (!ClassProto.this.interfaceMethodOverrides(interfaceMethod, vtable.get(vtableIndex))) {
                                    superclassType = superclassType2;
                                } else {
                                    vtable.set(vtableIndex, interfaceMethod);
                                    superclassType = superclassType2;
                                }
                            } else {
                                int defaultMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultMethods, interfaceMethod);
                                if (defaultMethodIndex < 0) {
                                    int defaultConflictMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultConflictMethods, interfaceMethod);
                                    if (defaultConflictMethodIndex < 0) {
                                        int mirandaMethodIndex = ClassProto.this.findMethodIndexInVtable(mirandaMethods, interfaceMethod);
                                        if (mirandaMethodIndex >= 0) {
                                            String superclassType3 = superclassType2;
                                            AccessFlags accessFlags = AccessFlags.ABSTRACT;
                                            int defaultConflictMethodIndex2 = interfaceMethod.getAccessFlags();
                                            if (accessFlags.isSet(defaultConflictMethodIndex2)) {
                                                superclassType2 = superclassType3;
                                            } else {
                                                ClassProto existingInterface = (ClassProto) ClassProto.this.classPath.getClass(mirandaMethods.get(mirandaMethodIndex).getDefiningClass());
                                                if (!existingInterface.implementsInterface(interfaceMethod.getDefiningClass())) {
                                                    Method oldMethod = mirandaMethods.remove(mirandaMethodIndex);
                                                    int methodOrderValue = methodOrder.get(oldMethod).intValue();
                                                    methodOrder.put(interfaceMethod, Integer.valueOf(methodOrderValue));
                                                    defaultMethods.add(interfaceMethod);
                                                }
                                                superclassType2 = superclassType3;
                                            }
                                        } else {
                                            superclassType = superclassType2;
                                            if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                                defaultMethods.add(interfaceMethod);
                                                methodOrder.put(interfaceMethod, Integer.valueOf(methodOrder.size()));
                                            } else {
                                                mirandaMethods.add(interfaceMethod);
                                                methodOrder.put(interfaceMethod, Integer.valueOf(methodOrder.size()));
                                            }
                                        }
                                    }
                                } else if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                    ClassProto existingInterface2 = (ClassProto) ClassProto.this.classPath.getClass(defaultMethods.get(defaultMethodIndex).getDefiningClass());
                                    if (!existingInterface2.implementsInterface(interfaceMethod.getDefiningClass())) {
                                        Method removedMethod = defaultMethods.remove(defaultMethodIndex);
                                        defaultConflictMethods.add(removedMethod);
                                    }
                                }
                            }
                            superclassType2 = superclassType;
                        }
                    }
                    Comparator<MethodReference> comparator = new Comparator<MethodReference>() { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.8.1
                        @Override // java.util.Comparator
                        public int compare(MethodReference o1, MethodReference o2) {
                            return Ints.compare(((Integer) methodOrder.get(o1)).intValue(), ((Integer) methodOrder.get(o2)).intValue());
                        }
                    };
                    Collections.sort(defaultMethods, comparator);
                    Collections.sort(defaultConflictMethods, comparator);
                    Collections.sort(mirandaMethods, comparator);
                    ClassProto.this.addToVtable(defaultMethods, vtable, false, false);
                    ClassProto.this.addToVtable(defaultConflictMethods, vtable, false, false);
                    ClassProto.this.addToVtable(mirandaMethods, vtable, false, false);
                }
                return vtable;
            } catch (UnresolvedClassException e) {
                vtable.addAll(((ClassProto) ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
                ClassProto.this.vtableFullyResolved = false;
                return vtable;
            }
        }
    });

    public ClassProto(@Nonnull ClassPath classPath, @Nonnull String type) {
        if (type.charAt(0) != 'L') {
            throw new ExceptionWithContext("Cannot construct ClassProto for non reference type: %s", type);
        }
        this.classPath = classPath;
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Nonnull
    public ClassDef getClassDef() {
        return this.classDefSupplier.get();
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean isInterface() {
        ClassDef classDef = getClassDef();
        return (classDef.getAccessFlags() & AccessFlags.INTERFACE.getValue()) != 0;
    }

    @Nonnull
    protected LinkedHashMap<String, ClassDef> getInterfaces() {
        if (!this.classPath.isArt() || this.classPath.oatVersion < 72) {
            return this.preDefaultMethodInterfaceSupplier.get();
        }
        return this.postDefaultMethodInterfaceSupplier.get();
    }

    @Nonnull
    protected Set<String> getUnresolvedInterfaces() {
        Set<String> set = this.unresolvedInterfaces;
        if (set == null) {
            return ImmutableSet.of();
        }
        return set;
    }

    @Nonnull
    protected Iterable<ClassDef> getDirectInterfaces() {
        Iterable<ClassDef> directInterfaces = FluentIterable.from(getInterfaces().values()).filter(Predicates.notNull());
        if (!this.interfacesFullyResolved) {
            throw new UnresolvedClassException("Interfaces for class %s not fully resolved: %s", getType(), Joiner.on(',').join(getUnresolvedInterfaces()));
        }
        return directInterfaces;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public boolean implementsInterface(@Nonnull String iface) {
        if (getInterfaces().containsKey(iface)) {
            return true;
        }
        if (this.interfacesFullyResolved) {
            return false;
        }
        throw new UnresolvedClassException("Interfaces for class %s not fully resolved", getType());
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return getClassDef().getSuperclass();
    }

    private boolean checkInterface(@Nonnull ClassProto other) {
        boolean isResolved = true;
        boolean isInterface = true;
        try {
            isInterface = isInterface();
        } catch (UnresolvedClassException e) {
            isResolved = false;
        }
        if (isInterface) {
            try {
                if (other.implementsInterface(getType())) {
                    return true;
                }
                return false;
            } catch (UnresolvedClassException ex) {
                if (isResolved) {
                    throw ex;
                }
                return false;
            }
        }
        return false;
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        if (!(other instanceof ClassProto)) {
            return other.getCommonSuperclass(this);
        }
        if (this == other || getType().equals(other.getType())) {
            return this;
        }
        if (getType().equals("Ljava/lang/Object;")) {
            return this;
        }
        if (other.getType().equals("Ljava/lang/Object;")) {
            return other;
        }
        boolean gotException = false;
        try {
            if (checkInterface((ClassProto) other)) {
                return this;
            }
        } catch (UnresolvedClassException e) {
            gotException = true;
        }
        try {
            if (((ClassProto) other).checkInterface(this)) {
                return other;
            }
        } catch (UnresolvedClassException e2) {
            gotException = true;
        }
        if (gotException) {
            return this.classPath.getUnknownClass();
        }
        List<TypeProto> thisChain = Lists.newArrayList(this);
        Iterables.addAll(thisChain, TypeProtoUtils.getSuperclassChain(this));
        List<TypeProto> otherChain = Lists.newArrayList(other);
        Iterables.addAll(otherChain, TypeProtoUtils.getSuperclassChain(other));
        List<TypeProto> thisChain2 = Lists.reverse(thisChain);
        List<TypeProto> otherChain2 = Lists.reverse(otherChain);
        for (int i = Math.min(thisChain2.size(), otherChain2.size()) - 1; i >= 0; i--) {
            TypeProto typeProto = thisChain2.get(i);
            if (typeProto.getType().equals(otherChain2.get(i).getType())) {
                return typeProto;
            }
        }
        return this.classPath.getUnknownClass();
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        if (getInstanceFields().size() == 0) {
            return null;
        }
        return getInstanceFields().get(fieldOffset);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    @Nullable
    public Method getMethodByVtableIndex(int vtableIndex) {
        List<Method> vtable = getVtable();
        if (vtableIndex < 0 || vtableIndex >= vtable.size()) {
            return null;
        }
        return vtable.get(vtableIndex);
    }

    @Override // com.android.tools.smali.dexlib2.analysis.TypeProto
    public int findMethodIndexInVtable(@Nonnull MethodReference method) {
        return findMethodIndexInVtable(getVtable(), method);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findMethodIndexInVtable(@Nonnull List<Method> vtable, MethodReference method) {
        for (int i = 0; i < vtable.size(); i++) {
            Method candidate = vtable.get(i);
            if (MethodUtil.methodSignaturesMatch(candidate, method) && (!this.classPath.shouldCheckPackagePrivateAccess() || AnalyzedMethodUtil.canAccess(this, candidate, true, false, false))) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findMethodIndexInVtableReverse(@Nonnull List<Method> vtable, MethodReference method) {
        for (int i = vtable.size() - 1; i >= 0; i--) {
            Method candidate = vtable.get(i);
            if (MethodUtil.methodSignaturesMatch(candidate, method) && (!this.classPath.shouldCheckPackagePrivateAccess() || AnalyzedMethodUtil.canAccess(this, candidate, true, false, false))) {
                return i;
            }
        }
        return -1;
    }

    @Nonnull
    public SparseArray<FieldReference> getInstanceFields() {
        if (this.classPath.isArt()) {
            return this.artInstanceFieldsSupplier.get();
        }
        return this.dalvikInstanceFieldsSupplier.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class FieldGap implements Comparable<FieldGap> {
        public final int offset;
        public final int size;

        public static FieldGap newFieldGap(int offset, int size, int oatVersion) {
            if (oatVersion >= 67) {
                return new FieldGap(offset, size) { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.FieldGap.1
                    @Override // java.lang.Comparable
                    public int compareTo(@Nonnull FieldGap o) {
                        int result = Ints.compare(o.size, this.size);
                        if (result != 0) {
                            return result;
                        }
                        return Ints.compare(this.offset, o.offset);
                    }
                };
            }
            return new FieldGap(offset, size) { // from class: com.android.tools.smali.dexlib2.analysis.ClassProto.FieldGap.2
                @Override // java.lang.Comparable
                public int compareTo(@Nonnull FieldGap o) {
                    int result = Ints.compare(this.size, o.size);
                    if (result != 0) {
                        return result;
                    }
                    return Ints.compare(o.offset, this.offset);
                }
            };
        }

        private FieldGap(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNextFieldOffset() {
        SparseArray<FieldReference> instanceFields = getInstanceFields();
        if (instanceFields.size() == 0) {
            return this.classPath.isArt() ? 0 : 8;
        }
        int lastItemIndex = instanceFields.size() - 1;
        int fieldOffset = instanceFields.keyAt(lastItemIndex);
        FieldReference lastField = instanceFields.valueAt(lastItemIndex);
        if (this.classPath.isArt()) {
            return getTypeSize(lastField.getType().charAt(0)) + fieldOffset;
        }
        switch (lastField.getType().charAt(0)) {
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
            case 'J':
                return fieldOffset + 8;
            default:
                return fieldOffset + 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getTypeSize(char type) {
        switch (type) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
            case 'Z':
                return 1;
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
            case 'S':
                return 2;
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
            case 'J':
                return 8;
            case 'F':
            case 'I':
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return 4;
            default:
                throw new ExceptionWithContext("Invalid type: %s", Character.valueOf(type));
        }
    }

    @Nonnull
    public List<Method> getVtable() {
        if (!this.classPath.isArt() || this.classPath.oatVersion < 72) {
            return this.preDefaultMethodVtableSupplier.get();
        }
        if (this.classPath.oatVersion < 87) {
            return this.buggyPostDefaultMethodVtableSupplier.get();
        }
        return this.postDefaultMethodVtableSupplier.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addToVtable(@Nonnull Iterable<? extends Method> localMethods, @Nonnull List<Method> vtable, boolean replaceExisting, boolean sort) {
        if (sort) {
            ArrayList<Method> methods = Lists.newArrayList(localMethods);
            Collections.sort(methods);
            localMethods = methods;
        }
        for (Method virtualMethod : localMethods) {
            int vtableIndex = findMethodIndexInVtable(vtable, virtualMethod);
            if (vtableIndex >= 0) {
                if (replaceExisting) {
                    vtable.set(vtableIndex, virtualMethod);
                }
            } else {
                vtable.add(virtualMethod);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte getFieldType(@Nonnull FieldReference field) {
        switch (field.getType().charAt(0)) {
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
            case 'J':
                return (byte) 1;
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
            case '[':
                return (byte) 0;
            default:
                return (byte) 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOverridableByDefaultMethod(@Nonnull Method method) {
        ClassProto classProto = (ClassProto) this.classPath.getClass(method.getDefiningClass());
        return classProto.isInterface();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean interfaceMethodOverrides(@Nonnull Method method, @Nonnull Method method2) {
        ClassProto classProto = (ClassProto) this.classPath.getClass(method2.getDefiningClass());
        if (classProto.isInterface()) {
            ClassProto targetClassProto = (ClassProto) this.classPath.getClass(method.getDefiningClass());
            return targetClassProto.implementsInterface(method2.getDefiningClass());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ReparentedMethod extends BaseMethodReference implements Method {
        private final String definingClass;
        private final Method method;

        public ReparentedMethod(Method method, String definingClass) {
            this.method = method;
            this.definingClass = definingClass;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return this.definingClass;
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.method.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference
        @Nonnull
        public List<? extends CharSequence> getParameterTypes() {
            return this.method.getParameterTypes();
        }

        @Override // com.android.tools.smali.dexlib2.iface.reference.MethodReference, com.android.tools.smali.dexlib2.iface.Method
        @Nonnull
        public String getReturnType() {
            return this.method.getReturnType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Method
        @Nonnull
        public List<? extends MethodParameter> getParameters() {
            return this.method.getParameters();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        public int getAccessFlags() {
            return this.method.getAccessFlags();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Annotatable
        @Nonnull
        public Set<? extends Annotation> getAnnotations() {
            return this.method.getAnnotations();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Method, com.android.tools.smali.dexlib2.iface.Member
        @Nonnull
        public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
            return this.method.getHiddenApiRestrictions();
        }

        @Override // com.android.tools.smali.dexlib2.iface.Method
        @Nullable
        public MethodImplementation getImplementation() {
            return this.method.getImplementation();
        }
    }
}
