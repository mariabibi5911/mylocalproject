package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.dexlib2.MethodHandleType;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.util.StringUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
/* loaded from: classes.dex */
public final class ReferenceUtil {
    public static String getMethodDescriptor(MethodReference methodReference) {
        return getMethodDescriptor(methodReference, false);
    }

    public static String getMethodDescriptor(MethodReference methodReference, boolean useImplicitReference) {
        StringBuilder sb = new StringBuilder();
        if (!useImplicitReference) {
            sb.append(methodReference.getDefiningClass());
            sb.append("->");
        }
        sb.append(methodReference.getName());
        sb.append('(');
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            sb.append(paramType);
        }
        sb.append(')');
        sb.append(methodReference.getReturnType());
        return sb.toString();
    }

    public static String getMethodProtoDescriptor(MethodProtoReference methodProtoReference) {
        StringWriter stringWriter = new StringWriter();
        try {
            writeMethodProtoDescriptor(stringWriter, methodProtoReference);
            return stringWriter.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeMethodProtoDescriptor(Writer writer, MethodProtoReference methodProtoReference) throws IOException {
        writer.write(40);
        for (CharSequence paramType : methodProtoReference.getParameterTypes()) {
            writer.write(paramType.toString());
        }
        writer.write(41);
        writer.write(methodProtoReference.getReturnType());
    }

    public static void writeMethodDescriptor(Writer writer, MethodReference methodReference) throws IOException {
        writeMethodDescriptor(writer, methodReference, false);
    }

    public static void writeMethodDescriptor(Writer writer, MethodReference methodReference, boolean useImplicitReference) throws IOException {
        if (!useImplicitReference) {
            writer.write(methodReference.getDefiningClass());
            writer.write("->");
        }
        writer.write(methodReference.getName());
        writer.write(40);
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            writer.write(paramType.toString());
        }
        writer.write(41);
        writer.write(methodReference.getReturnType());
    }

    public static String getFieldDescriptor(FieldReference fieldReference) {
        return getFieldDescriptor(fieldReference, false);
    }

    public static String getFieldDescriptor(FieldReference fieldReference, boolean useImplicitReference) {
        StringBuilder sb = new StringBuilder();
        if (!useImplicitReference) {
            sb.append(fieldReference.getDefiningClass());
            sb.append("->");
        }
        sb.append(fieldReference.getName());
        sb.append(':');
        sb.append(fieldReference.getType());
        return sb.toString();
    }

    public static String getShortFieldDescriptor(FieldReference fieldReference) {
        return fieldReference.getName() + ':' + fieldReference.getType();
    }

    public static void writeFieldDescriptor(Writer writer, FieldReference fieldReference) throws IOException {
        writeFieldDescriptor(writer, fieldReference, false);
    }

    public static void writeFieldDescriptor(Writer writer, FieldReference fieldReference, boolean implicitReference) throws IOException {
        if (!implicitReference) {
            writer.write(fieldReference.getDefiningClass());
            writer.write("->");
        }
        writer.write(fieldReference.getName());
        writer.write(58);
        writer.write(fieldReference.getType());
    }

    public static String getMethodHandleString(MethodHandleReference methodHandleReference) {
        StringWriter stringWriter = new StringWriter();
        try {
            writeMethodHandle(stringWriter, methodHandleReference);
            return stringWriter.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeMethodHandle(Writer writer, MethodHandleReference methodHandleReference) throws IOException {
        writer.write(MethodHandleType.toString(methodHandleReference.getMethodHandleType()));
        writer.write(64);
        Reference memberReference = methodHandleReference.getMemberReference();
        if (memberReference instanceof MethodReference) {
            writeMethodDescriptor(writer, (MethodReference) memberReference);
        } else {
            writeFieldDescriptor(writer, (FieldReference) memberReference);
        }
    }

    public static String getCallSiteString(CallSiteReference callSiteReference) {
        StringWriter stringWriter = new StringWriter();
        try {
            writeCallSite(stringWriter, callSiteReference);
            return stringWriter.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeCallSite(Writer writer, CallSiteReference callSiteReference) throws IOException {
        writer.write(callSiteReference.getName());
        writer.write(40);
        writer.write(34);
        StringUtils.writeEscapedString(writer, callSiteReference.getMethodName());
        writer.write(34);
        writer.write(", ");
        writeMethodProtoDescriptor(writer, callSiteReference.getMethodProto());
        for (EncodedValue encodedValue : callSiteReference.getExtraArguments()) {
            writer.write(", ");
            EncodedValueUtils.writeEncodedValue(writer, encodedValue);
        }
        writer.write(")@");
        MethodHandleReference methodHandle = callSiteReference.getMethodHandle();
        if (methodHandle.getMethodHandleType() != 4) {
            throw new IllegalArgumentException("The linker method handle for a call site must be of type invoke-static");
        }
        writeMethodDescriptor(writer, (MethodReference) callSiteReference.getMethodHandle().getMemberReference());
    }

    @Nullable
    public static String getReferenceString(@Nonnull Reference reference) {
        return getReferenceString(reference, null);
    }

    @Nullable
    public static String getReferenceString(@Nonnull Reference reference, @Nullable String containingClass) {
        if (reference instanceof StringReference) {
            return String.format("\"%s\"", StringUtils.escapeString(((StringReference) reference).getString()));
        }
        if (reference instanceof TypeReference) {
            return ((TypeReference) reference).getType();
        }
        if (reference instanceof FieldReference) {
            FieldReference fieldReference = (FieldReference) reference;
            boolean useImplicitReference = fieldReference.getDefiningClass().equals(containingClass);
            return getFieldDescriptor(fieldReference, useImplicitReference);
        }
        if (reference instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) reference;
            boolean useImplicitReference2 = methodReference.getDefiningClass().equals(containingClass);
            return getMethodDescriptor(methodReference, useImplicitReference2);
        }
        if (reference instanceof MethodProtoReference) {
            MethodProtoReference methodProtoReference = (MethodProtoReference) reference;
            return getMethodProtoDescriptor(methodProtoReference);
        }
        if (reference instanceof MethodHandleReference) {
            MethodHandleReference methodHandleReference = (MethodHandleReference) reference;
            return getMethodHandleString(methodHandleReference);
        }
        if (reference instanceof CallSiteReference) {
            CallSiteReference callSiteReference = (CallSiteReference) reference;
            return getCallSiteString(callSiteReference);
        }
        return null;
    }

    private ReferenceUtil() {
    }
}
