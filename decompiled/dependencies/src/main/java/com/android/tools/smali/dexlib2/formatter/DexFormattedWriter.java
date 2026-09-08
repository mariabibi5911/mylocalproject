package com.android.tools.smali.dexlib2.formatter;

import com.android.tools.smali.dexlib2.MethodHandleType;
import com.android.tools.smali.dexlib2.iface.AnnotationElement;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.dexlib2.iface.value.AnnotationEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.CharEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EnumEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FieldEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.LongEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.TypeEncodedValue;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/* loaded from: classes.dex */
public class DexFormattedWriter extends Writer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    protected final Writer writer;

    public DexFormattedWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeMethodDescriptor(MethodReference methodReference) throws IOException {
        writeType(methodReference.getDefiningClass());
        this.writer.write("->");
        writeSimpleName(methodReference.getName());
        this.writer.write(40);
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(methodReference.getReturnType());
    }

    public void writeShortMethodDescriptor(MethodReference methodReference) throws IOException {
        writeSimpleName(methodReference.getName());
        this.writer.write(40);
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(methodReference.getReturnType());
    }

    public void writeMethodProtoDescriptor(MethodProtoReference protoReference) throws IOException {
        this.writer.write(40);
        for (CharSequence paramType : protoReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(protoReference.getReturnType());
    }

    public void writeFieldDescriptor(FieldReference fieldReference) throws IOException {
        writeType(fieldReference.getDefiningClass());
        this.writer.write("->");
        writeSimpleName(fieldReference.getName());
        this.writer.write(58);
        writeType(fieldReference.getType());
    }

    public void writeShortFieldDescriptor(FieldReference fieldReference) throws IOException {
        writeSimpleName(fieldReference.getName());
        this.writer.write(58);
        writeType(fieldReference.getType());
    }

    public void writeMethodHandle(MethodHandleReference methodHandleReference) throws IOException {
        this.writer.write(MethodHandleType.toString(methodHandleReference.getMethodHandleType()));
        this.writer.write(64);
        Reference memberReference = methodHandleReference.getMemberReference();
        if (memberReference instanceof MethodReference) {
            writeMethodDescriptor((MethodReference) memberReference);
        } else {
            writeFieldDescriptor((FieldReference) memberReference);
        }
    }

    public void writeCallSite(CallSiteReference callSiteReference) throws IOException {
        writeSimpleName(callSiteReference.getName());
        this.writer.write(40);
        writeQuotedString(callSiteReference.getMethodName());
        this.writer.write(", ");
        writeMethodProtoDescriptor(callSiteReference.getMethodProto());
        for (EncodedValue encodedValue : callSiteReference.getExtraArguments()) {
            this.writer.write(", ");
            writeEncodedValue(encodedValue);
        }
        this.writer.write(")@");
        MethodHandleReference methodHandle = callSiteReference.getMethodHandle();
        if (methodHandle.getMethodHandleType() != 4) {
            throw new IllegalArgumentException("The linker method handle for a call site must be of type invoke-static");
        }
        writeMethodDescriptor((MethodReference) callSiteReference.getMethodHandle().getMemberReference());
    }

    public void writeType(CharSequence type) throws IOException {
        for (int i = 0; i < type.length(); i++) {
            char c = type.charAt(i);
            if (c == 'L') {
                writeClass(type.subSequence(i, type.length()));
                return;
            }
            if (c == '[') {
                this.writer.write(c);
            } else {
                if (c == 'Z' || c == 'B' || c == 'S' || c == 'C' || c == 'I' || c == 'J' || c == 'F' || c == 'D' || c == 'V') {
                    this.writer.write(c);
                    if (i != type.length() - 1) {
                        throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
                    }
                    return;
                }
                throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
            }
        }
        throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
    }

    protected void writeClass(CharSequence type) throws IOException {
        if (type.charAt(0) == 'L') {
            this.writer.write(type.charAt(0));
            int startIndex = 1;
            int i = 1;
            while (true) {
                if (i >= type.length()) {
                    break;
                }
                char c = type.charAt(i);
                if (c == '/') {
                    if (i == startIndex) {
                        throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
                    }
                    writeSimpleName(type.subSequence(startIndex, i));
                    this.writer.write(type.charAt(i));
                    startIndex = i + 1;
                } else if (c == ';') {
                    if (i == startIndex) {
                        throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
                    }
                    writeSimpleName(type.subSequence(startIndex, i));
                    this.writer.write(type.charAt(i));
                }
                i++;
            }
            if (i != type.length() - 1 || type.charAt(i) != ';') {
                throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
            }
            return;
        }
        throw new AssertionError();
    }

    protected void writeSimpleName(CharSequence simpleName) throws IOException {
        this.writer.append(simpleName);
    }

    public void writeQuotedString(CharSequence charSequence) throws IOException {
        this.writer.write(34);
        String string = charSequence.toString();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c >= ' ' && c < 127) {
                if (c == '\'' || c == '\"' || c == '\\') {
                    this.writer.write(92);
                }
                this.writer.write(c);
            } else {
                if (c <= 127) {
                    switch (c) {
                        case '\t':
                            this.writer.write("\\t");
                            break;
                        case '\n':
                            this.writer.write("\\n");
                            break;
                        case '\r':
                            this.writer.write("\\r");
                            break;
                    }
                }
                this.writer.write("\\u");
                this.writer.write(Character.forDigit(c >> '\f', 16));
                this.writer.write(Character.forDigit((c >> '\b') & 15, 16));
                this.writer.write(Character.forDigit((c >> 4) & 15, 16));
                this.writer.write(Character.forDigit(c & 15, 16));
            }
        }
        this.writer.write(34);
    }

    public void writeEncodedValue(EncodedValue encodedValue) throws IOException {
        switch (encodedValue.getValueType()) {
            case 0:
                this.writer.write(String.format("0x%x", Byte.valueOf(((ByteEncodedValue) encodedValue).getValue())));
                return;
            case 1:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            case 20:
            default:
                throw new IllegalArgumentException("Unknown encoded value type");
            case 2:
                this.writer.write(String.format("0x%x", Short.valueOf(((ShortEncodedValue) encodedValue).getValue())));
                return;
            case 3:
                this.writer.write(String.format("0x%x", Integer.valueOf(((CharEncodedValue) encodedValue).getValue())));
                return;
            case 4:
                this.writer.write(String.format("0x%x", Integer.valueOf(((IntEncodedValue) encodedValue).getValue())));
                return;
            case 6:
                this.writer.write(String.format("0x%x", Long.valueOf(((LongEncodedValue) encodedValue).getValue())));
                return;
            case 16:
                this.writer.write(Float.toString(((FloatEncodedValue) encodedValue).getValue()));
                return;
            case 17:
                this.writer.write(Double.toString(((DoubleEncodedValue) encodedValue).getValue()));
                return;
            case 21:
                writeMethodProtoDescriptor(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                writeMethodHandle(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                writeQuotedString(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                writeType(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                writeFieldDescriptor(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                writeMethodDescriptor(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                writeFieldDescriptor(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                writeArray((ArrayEncodedValue) encodedValue);
                return;
            case 29:
                writeAnnotation((AnnotationEncodedValue) encodedValue);
                return;
            case 30:
                this.writer.write("null");
                return;
            case 31:
                this.writer.write(Boolean.toString(((BooleanEncodedValue) encodedValue).getValue()));
                return;
        }
    }

    protected void writeAnnotation(AnnotationEncodedValue annotation) throws IOException {
        this.writer.write("Annotation[");
        writeType(annotation.getType());
        Set<? extends AnnotationElement> elements = annotation.getElements();
        for (AnnotationElement element : elements) {
            this.writer.write(", ");
            writeSimpleName(element.getName());
            this.writer.write(61);
            writeEncodedValue(element.getValue());
        }
        this.writer.write(93);
    }

    protected void writeArray(ArrayEncodedValue array) throws IOException {
        this.writer.write("Array[");
        boolean first = true;
        for (EncodedValue element : array.getValue()) {
            if (first) {
                first = false;
            } else {
                this.writer.write(", ");
            }
            writeEncodedValue(element);
        }
        this.writer.write(93);
    }

    public void writeReference(Reference reference) throws IOException {
        if (reference instanceof StringReference) {
            writeQuotedString((StringReference) reference);
            return;
        }
        if (reference instanceof TypeReference) {
            writeType((TypeReference) reference);
            return;
        }
        if (reference instanceof FieldReference) {
            writeFieldDescriptor((FieldReference) reference);
            return;
        }
        if (reference instanceof MethodReference) {
            writeMethodDescriptor((MethodReference) reference);
            return;
        }
        if (reference instanceof MethodProtoReference) {
            writeMethodProtoDescriptor((MethodProtoReference) reference);
        } else if (reference instanceof MethodHandleReference) {
            writeMethodHandle((MethodHandleReference) reference);
        } else {
            if (reference instanceof CallSiteReference) {
                writeCallSite((CallSiteReference) reference);
                return;
            }
            throw new IllegalArgumentException(String.format("Not a known reference type: %s", reference.getClass()));
        }
    }

    @Override // java.io.Writer
    public void write(int c) throws IOException {
        this.writer.write(c);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws IOException {
        this.writer.write(cbuf);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        this.writer.write(str);
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        this.writer.write(str, off, len);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws IOException {
        return this.writer.append(csq);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return this.writer.append(csq, start, end);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        return this.writer.append(c);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.writer.close();
    }
}
