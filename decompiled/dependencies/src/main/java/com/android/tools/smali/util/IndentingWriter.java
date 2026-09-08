package com.android.tools.smali.util;

import java.io.IOException;
import java.io.Writer;

/* loaded from: classes.dex */
public class IndentingWriter extends Writer {
    private static final String newLine = System.getProperty("line.separator");
    protected final Writer writer;
    protected final char[] buffer = new char[24];
    protected int indentLevel = 0;
    private boolean beginningOfLine = true;

    public IndentingWriter(Writer writer) {
        this.writer = writer;
    }

    protected void writeIndent() throws IOException {
        for (int i = 0; i < this.indentLevel; i++) {
            this.writer.write(32);
        }
    }

    @Override // java.io.Writer
    public void write(int chr) throws IOException {
        if (chr == 10) {
            this.writer.write(newLine);
            this.beginningOfLine = true;
        } else {
            if (this.beginningOfLine) {
                writeIndent();
            }
            this.beginningOfLine = false;
            this.writer.write(chr);
        }
    }

    private void writeLine(char[] chars, int start, int len) throws IOException {
        if (this.beginningOfLine && len > 0) {
            writeIndent();
            this.beginningOfLine = false;
        }
        this.writer.write(chars, start, len);
    }

    private void writeLine(String str, int start, int len) throws IOException {
        if (this.beginningOfLine && len > 0) {
            writeIndent();
            this.beginningOfLine = false;
        }
        this.writer.write(str, start, len);
    }

    @Override // java.io.Writer
    public void write(char[] chars) throws IOException {
        write(chars, 0, chars.length);
    }

    @Override // java.io.Writer
    public void write(char[] chars, int start, int len) throws IOException {
        int end = start + len;
        int pos = start;
        while (pos < end) {
            if (chars[pos] == '\n') {
                writeLine(chars, start, pos - start);
                this.writer.write(newLine);
                this.beginningOfLine = true;
                pos++;
                start = pos;
            } else {
                pos++;
            }
        }
        writeLine(chars, start, pos - start);
    }

    @Override // java.io.Writer
    public void write(String s) throws IOException {
        write(s, 0, s.length());
    }

    @Override // java.io.Writer
    public void write(String str, int start, int len) throws IOException {
        int end = start + len;
        int pos = start;
        while (pos < end) {
            pos = str.indexOf(10, start);
            if (pos == -1 || pos >= end) {
                writeLine(str, start, end - start);
                return;
            }
            writeLine(str, start, pos - start);
            this.writer.write(newLine);
            this.beginningOfLine = true;
            start = pos + 1;
        }
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence charSequence) throws IOException {
        write(charSequence.toString());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence charSequence, int start, int len) throws IOException {
        write(charSequence.subSequence(start, len).toString());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        write(c);
        return this;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.writer.close();
    }

    public void indent(int indentAmount) {
        int i = this.indentLevel + indentAmount;
        this.indentLevel = i;
        if (i < 0) {
            this.indentLevel = 0;
        }
    }

    public void deindent(int indentAmount) {
        int i = this.indentLevel - indentAmount;
        this.indentLevel = i;
        if (i < 0) {
            this.indentLevel = 0;
        }
    }
}
