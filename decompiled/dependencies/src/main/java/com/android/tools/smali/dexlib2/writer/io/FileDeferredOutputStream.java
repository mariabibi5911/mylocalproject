package com.android.tools.smali.dexlib2.writer.io;

import com.google.common.io.ByteStreams;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class FileDeferredOutputStream extends DeferredOutputStream {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    @Nonnull
    private final File backingFile;

    @Nonnull
    private final NakedBufferedOutputStream output;
    private int writtenBytes;

    public FileDeferredOutputStream(@Nonnull File backingFile) throws FileNotFoundException {
        this(backingFile, 4096);
    }

    public FileDeferredOutputStream(@Nonnull File backingFile, int bufferSize) throws FileNotFoundException {
        this.backingFile = backingFile;
        this.output = new NakedBufferedOutputStream(new FileOutputStream(backingFile), bufferSize);
    }

    @Override // com.android.tools.smali.dexlib2.writer.io.DeferredOutputStream
    public void writeTo(@Nonnull OutputStream dest) throws IOException {
        byte[] outBuf = this.output.getBuffer();
        int count = this.output.getCount();
        this.output.resetBuffer();
        this.output.close();
        if (count != this.writtenBytes) {
            InputStream fis = new FileInputStream(this.backingFile);
            ByteStreams.copy(fis, dest);
            this.backingFile.delete();
        }
        dest.write(outBuf, 0, count);
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.output.write(i);
        this.writtenBytes++;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes) throws IOException {
        this.output.write(bytes);
        this.writtenBytes += bytes.length;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes, int off, int len) throws IOException {
        this.output.write(bytes, off, len);
        this.writtenBytes += len;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.output.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.output.close();
    }

    /* loaded from: classes.dex */
    private static class NakedBufferedOutputStream extends BufferedOutputStream {
        public NakedBufferedOutputStream(OutputStream outputStream) {
            super(outputStream);
        }

        public NakedBufferedOutputStream(OutputStream outputStream, int i) {
            super(outputStream, i);
        }

        public int getCount() {
            return this.count;
        }

        public void resetBuffer() {
            this.count = 0;
        }

        public byte[] getBuffer() {
            return this.buf;
        }
    }

    @Nonnull
    public static DeferredOutputStreamFactory getFactory(@Nullable File containingDirectory) {
        return getFactory(containingDirectory, 4096);
    }

    @Nonnull
    public static DeferredOutputStreamFactory getFactory(@Nullable final File containingDirectory, final int bufferSize) {
        return new DeferredOutputStreamFactory() { // from class: com.android.tools.smali.dexlib2.writer.io.FileDeferredOutputStream.1
            @Override // com.android.tools.smali.dexlib2.writer.io.DeferredOutputStreamFactory
            public DeferredOutputStream makeDeferredOutputStream() throws IOException {
                File tempFile = File.createTempFile("dexlibtmp", null, containingDirectory);
                return new FileDeferredOutputStream(tempFile, bufferSize);
            }
        };
    }
}
