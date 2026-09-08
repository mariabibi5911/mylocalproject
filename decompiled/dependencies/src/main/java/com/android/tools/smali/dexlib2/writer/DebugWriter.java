package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.util.ExceptionWithContext;
import java.io.IOException;
import java.lang.CharSequence;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DebugWriter<StringKey extends CharSequence, TypeKey extends CharSequence> {
    private static final int FIRST_SPECIAL = 10;
    private static final int LINE_BASE = -4;
    private static final int LINE_RANGE = 15;
    private int currentAddress;
    private int currentLine;

    @Nonnull
    private final StringSection<StringKey, ?> stringSection;

    @Nonnull
    private final TypeSection<StringKey, TypeKey, ?> typeSection;

    @Nonnull
    private final DexDataWriter writer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DebugWriter(@Nonnull StringSection<StringKey, ?> stringSection, @Nonnull TypeSection<StringKey, TypeKey, ?> typeSection, @Nonnull DexDataWriter writer) {
        this.stringSection = stringSection;
        this.typeSection = typeSection;
        this.writer = writer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset(int startLine) {
        this.currentAddress = 0;
        this.currentLine = startLine;
    }

    public void writeStartLocal(int codeAddress, int register, @Nullable StringKey name, @Nullable TypeKey type, @Nullable StringKey signature) throws IOException {
        int nameIndex = this.stringSection.getNullableItemIndex(name);
        int typeIndex = this.typeSection.getNullableItemIndex(type);
        int signatureIndex = this.stringSection.getNullableItemIndex(signature);
        writeAdvancePC(codeAddress);
        if (signatureIndex == -1) {
            this.writer.write(3);
            this.writer.writeUleb128(register);
            this.writer.writeUleb128(nameIndex + 1);
            this.writer.writeUleb128(typeIndex + 1);
            return;
        }
        this.writer.write(4);
        this.writer.writeUleb128(register);
        this.writer.writeUleb128(nameIndex + 1);
        this.writer.writeUleb128(typeIndex + 1);
        this.writer.writeUleb128(signatureIndex + 1);
    }

    public void writeEndLocal(int codeAddress, int register) throws IOException {
        writeAdvancePC(codeAddress);
        this.writer.write(5);
        this.writer.writeUleb128(register);
    }

    public void writeRestartLocal(int codeAddress, int register) throws IOException {
        writeAdvancePC(codeAddress);
        this.writer.write(6);
        this.writer.writeUleb128(register);
    }

    public void writePrologueEnd(int codeAddress) throws IOException {
        writeAdvancePC(codeAddress);
        this.writer.write(7);
    }

    public void writeEpilogueBegin(int codeAddress) throws IOException {
        writeAdvancePC(codeAddress);
        this.writer.write(8);
    }

    public void writeLineNumber(int codeAddress, int lineNumber) throws IOException {
        int lineDelta = lineNumber - this.currentLine;
        int addressDelta = codeAddress - this.currentAddress;
        if (addressDelta < 0) {
            throw new ExceptionWithContext("debug info items must have non-decreasing code addresses", new Object[0]);
        }
        if (lineDelta < -4 || lineDelta > 10) {
            writeAdvanceLine(lineNumber);
            lineDelta = 0;
        }
        if ((lineDelta < 2 && addressDelta > 16) || (lineDelta > 1 && addressDelta > 15)) {
            writeAdvancePC(codeAddress);
            addressDelta = 0;
        }
        writeSpecialOpcode(lineDelta, addressDelta);
    }

    public void writeSetSourceFile(int codeAddress, @Nullable StringKey sourceFile) throws IOException {
        writeAdvancePC(codeAddress);
        this.writer.write(9);
        this.writer.writeUleb128(this.stringSection.getNullableItemIndex(sourceFile) + 1);
    }

    private void writeAdvancePC(int address) throws IOException {
        int addressDelta = address - this.currentAddress;
        if (addressDelta > 0) {
            this.writer.write(1);
            this.writer.writeUleb128(addressDelta);
            this.currentAddress = address;
        }
    }

    private void writeAdvanceLine(int line) throws IOException {
        int lineDelta = line - this.currentLine;
        if (lineDelta != 0) {
            this.writer.write(2);
            this.writer.writeSleb128(lineDelta);
            this.currentLine = line;
        }
    }

    private void writeSpecialOpcode(int lineDelta, int addressDelta) throws IOException {
        this.writer.write((byte) ((addressDelta * 15) + 10 + lineDelta + 4));
        this.currentLine += lineDelta;
        this.currentAddress += addressDelta;
    }
}
