package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class SyntheticAccessorResolver {
    public static final int ADD_ASSIGNMENT = 7;
    public static final int AND_ASSIGNMENT = 12;
    public static final int DIV_ASSIGNMENT = 10;
    public static final int GETTER = 1;
    public static final int METHOD = 0;
    public static final int MUL_ASSIGNMENT = 9;
    public static final int OR_ASSIGNMENT = 13;
    public static final int POSTFIX_DECREMENT = 5;
    public static final int POSTFIX_INCREMENT = 3;
    public static final int PREFIX_DECREMENT = 6;
    public static final int PREFIX_INCREMENT = 4;
    public static final int REM_ASSIGNMENT = 11;
    public static final int SETTER = 2;
    public static final int SHL_ASSIGNMENT = 15;
    public static final int SHR_ASSIGNMENT = 16;
    public static final int SUB_ASSIGNMENT = 8;
    public static final int USHR_ASSIGNMENT = 17;
    public static final int XOR_ASSIGNMENT = 14;
    private final Map<String, ClassDef> classDefMap;
    private final Map<MethodReference, AccessedMember> resolvedAccessors = Maps.newConcurrentMap();
    private final SyntheticAccessorFSM syntheticAccessorFSM;

    public SyntheticAccessorResolver(@Nonnull Opcodes opcodes, @Nonnull Iterable<? extends ClassDef> classDefs) {
        this.syntheticAccessorFSM = new SyntheticAccessorFSM(opcodes);
        ImmutableMap.Builder<String, ClassDef> builder = ImmutableMap.builder();
        for (ClassDef classDef : classDefs) {
            builder.put(classDef.getType(), classDef);
        }
        this.classDefMap = builder.build();
    }

    public static boolean looksLikeSyntheticAccessor(String methodName) {
        return methodName.startsWith("access$");
    }

    @Nullable
    public AccessedMember getAccessedMember(@Nonnull MethodReference methodReference) {
        List<Instruction> instructions;
        int accessType;
        AccessedMember accessedMember = this.resolvedAccessors.get(methodReference);
        if (accessedMember != null) {
            return accessedMember;
        }
        String type = methodReference.getDefiningClass();
        ClassDef classDef = this.classDefMap.get(type);
        if (classDef == null) {
            return null;
        }
        Method matchedMethod = null;
        MethodImplementation matchedMethodImpl = null;
        Iterator<? extends Method> it = classDef.getMethods().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Method method = it.next();
            MethodImplementation methodImpl = method.getImplementation();
            if (methodImpl != null && methodReferenceEquals(method, methodReference)) {
                matchedMethod = method;
                matchedMethodImpl = methodImpl;
                break;
            }
        }
        if (matchedMethod == null || !AccessFlags.SYNTHETIC.isSet(matchedMethod.getAccessFlags()) || (accessType = this.syntheticAccessorFSM.test((instructions = ImmutableList.copyOf(matchedMethodImpl.getInstructions())))) < 0) {
            return null;
        }
        AccessedMember member = new AccessedMember(accessType, ((ReferenceInstruction) instructions.get(0)).getReference());
        this.resolvedAccessors.put(methodReference, member);
        return member;
    }

    /* loaded from: classes.dex */
    public static class AccessedMember {

        @Nonnull
        public final Reference accessedMember;
        public final int accessedMemberType;

        public AccessedMember(int accessedMemberType, @Nonnull Reference accessedMember) {
            this.accessedMemberType = accessedMemberType;
            this.accessedMember = accessedMember;
        }
    }

    private static boolean methodReferenceEquals(@Nonnull MethodReference ref1, @Nonnull MethodReference ref2) {
        return ref1.getName().equals(ref2.getName()) && ref1.getReturnType().equals(ref2.getReturnType()) && ref1.getParameterTypes().equals(ref2.getParameterTypes());
    }
}
