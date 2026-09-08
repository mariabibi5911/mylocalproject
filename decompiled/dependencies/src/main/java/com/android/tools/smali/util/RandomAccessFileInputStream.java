package com.android.tools.smali.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class RandomAccessFileInputStream extends InputStream {
    private int filePosition;

    @Nonnull
    private final RandomAccessFile raf;

    public RandomAccessFileInputStream(@Nonnull RandomAccessFile raf, int filePosition) {
        this.filePosition = filePosition;
        this.raf = raf;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        this.raf.seek(this.filePosition);
        this.filePosition++;
        return this.raf.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes) throws IOException {
        this.raf.seek(this.filePosition);
        int bytesRead = this.raf.read(bytes);
        this.filePosition += bytesRead;
        return bytesRead;
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes, int offset, int length) throws IOException {
        this.raf.seek(this.filePosition);
        int bytesRead = this.raf.read(bytes, offset, length);
        this.filePosition += bytesRead;
        return bytesRead;
    }

    @Override // java.io.InputStream
    public long skip(long l) throws IOException {
        int skipBytes = Math.min((int) l, available());
        this.filePosition += skipBytes;
        return skipBytes;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return ((int) this.raf.length()) - this.filePosition;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }
}
