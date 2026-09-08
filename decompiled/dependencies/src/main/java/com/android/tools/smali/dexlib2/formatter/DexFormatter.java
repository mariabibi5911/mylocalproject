package com.android.tools.smali.dexlib2.formatter;

import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/* loaded from: classes.dex */
public class DexFormatter {
    public static final DexFormatter INSTANCE = new DexFormatter();

    public DexFormattedWriter getWriter(Writer writer) {
        return new DexFormattedWriter(writer);
    }

    public String getMethodDescriptor(MethodReference methodReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeMethodDescriptor(methodReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getShortMethodDescriptor(MethodReference methodReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeShortMethodDescriptor(methodReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getMethodProtoDescriptor(MethodProtoReference protoReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeMethodProtoDescriptor(protoReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getFieldDescriptor(FieldReference fieldReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeFieldDescriptor(fieldReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getShortFieldDescriptor(FieldReference fieldReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeShortFieldDescriptor(fieldReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getMethodHandle(MethodHandleReference methodHandleReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeMethodHandle(methodHandleReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getCallSite(CallSiteReference callSiteReference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeCallSite(callSiteReference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getType(CharSequence type) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeType(type);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getQuotedString(CharSequence string) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeQuotedString(string);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getEncodedValue(EncodedValue encodedValue) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeEncodedValue(encodedValue);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }

    public String getReference(Reference reference) {
        StringWriter writer = new StringWriter();
        try {
            getWriter(writer).writeReference(reference);
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException");
        }
    }
}
