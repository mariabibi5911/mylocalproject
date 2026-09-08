package com.google.common.io;

import com.google.common.base.Ascii;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public abstract class BaseEncoding {
    private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
    private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
    private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");

    public abstract boolean canDecode(CharSequence charSequence);

    abstract int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException;

    public abstract InputStream decodingStream(Reader reader);

    abstract void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException;

    public abstract OutputStream encodingStream(Writer writer);

    public abstract BaseEncoding lowerCase();

    abstract int maxDecodedSize(int i);

    abstract int maxEncodedSize(int i);

    public abstract BaseEncoding omitPadding();

    public abstract BaseEncoding upperCase();

    public abstract BaseEncoding withPadChar(char c);

    public abstract BaseEncoding withSeparator(String str, int i);

    BaseEncoding() {
    }

    /* loaded from: classes7.dex */
    public static final class DecodingException extends IOException {
        DecodingException(String message) {
            super(message);
        }

        DecodingException(Throwable cause) {
            super(cause);
        }
    }

    public String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    }

    public final String encode(byte[] bytes, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        StringBuilder result = new StringBuilder(maxEncodedSize(len));
        try {
            encodeTo(result, bytes, off, len);
            return result.toString();
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }
    }

    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink() { // from class: com.google.common.io.BaseEncoding.1
            @Override // com.google.common.io.ByteSink
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] result, int length) {
        if (length == result.length) {
            return result;
        }
        byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }

    public final byte[] decode(CharSequence chars) {
        try {
            return decodeChecked(chars);
        } catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }

    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        CharSequence chars2 = trimTrailingPadding(chars);
        byte[] tmp = new byte[maxDecodedSize(chars2.length())];
        int len = decodeTo(tmp, chars2);
        return extract(tmp, len);
    }

    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource() { // from class: com.google.common.io.BaseEncoding.2
            @Override // com.google.common.io.ByteSource
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }

    CharSequence trimTrailingPadding(CharSequence chars) {
        return (CharSequence) Preconditions.checkNotNull(chars);
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class Alphabet {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String name, char[] chars) {
            this.name = (String) Preconditions.checkNotNull(name);
            this.chars = (char[]) Preconditions.checkNotNull(chars);
            try {
                int log2 = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
                this.bitsPerChar = log2;
                int gcd = Math.min(8, Integer.lowestOneBit(log2));
                try {
                    this.charsPerChunk = 8 / gcd;
                    this.bytesPerChunk = log2 / gcd;
                    this.mask = chars.length - 1;
                    byte[] decodabet = new byte[128];
                    Arrays.fill(decodabet, (byte) -1);
                    for (int i = 0; i < chars.length; i++) {
                        char c = chars[i];
                        boolean z = false;
                        Preconditions.checkArgument(c < decodabet.length, "Non-ASCII character: %s", c);
                        if (decodabet[c] == -1) {
                            z = true;
                        }
                        Preconditions.checkArgument(z, "Duplicate character: %s", c);
                        decodabet[c] = (byte) i;
                    }
                    this.decodabet = decodabet;
                    boolean[] validPadding = new boolean[this.charsPerChunk];
                    for (int i2 = 0; i2 < this.bytesPerChunk; i2++) {
                        validPadding[IntMath.divide(i2 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                    }
                    this.validPadding = validPadding;
                } catch (ArithmeticException e) {
                    String valueOf = String.valueOf(new String(chars));
                    throw new IllegalArgumentException(valueOf.length() != 0 ? "Illegal alphabet ".concat(valueOf) : new String("Illegal alphabet "), e);
                }
            } catch (ArithmeticException e2) {
                throw new IllegalArgumentException(new StringBuilder(35).append("Illegal alphabet length ").append(chars.length).toString(), e2);
            }
        }

        char encode(int bits) {
            return this.chars[bits];
        }

        boolean isValidPaddingStartPosition(int index) {
            return this.validPadding[index % this.charsPerChunk];
        }

        boolean canDecode(char ch) {
            return ch <= 127 && this.decodabet[ch] != -1;
        }

        int decode(char ch) throws DecodingException {
            if (ch > 127) {
                String valueOf = String.valueOf(Integer.toHexString(ch));
                throw new DecodingException(valueOf.length() != 0 ? "Unrecognized character: 0x".concat(valueOf) : new String("Unrecognized character: 0x"));
            }
            int result = this.decodabet[ch];
            if (result == -1) {
                if (ch <= ' ' || ch == 127) {
                    String valueOf2 = String.valueOf(Integer.toHexString(ch));
                    throw new DecodingException(valueOf2.length() != 0 ? "Unrecognized character: 0x".concat(valueOf2) : new String("Unrecognized character: 0x"));
                }
                throw new DecodingException(new StringBuilder(25).append("Unrecognized character: ").append(ch).toString());
            }
            return result;
        }

        private boolean hasLowerCase() {
            for (char c : this.chars) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char c : this.chars) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }

        Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] upperCased = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr = this.chars;
                if (i < cArr.length) {
                    upperCased[i] = Ascii.toUpperCase(cArr[i]);
                    i++;
                } else {
                    return new Alphabet(String.valueOf(this.name).concat(".upperCase()"), upperCased);
                }
            }
        }

        Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] lowerCased = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr = this.chars;
                if (i < cArr.length) {
                    lowerCased[i] = Ascii.toLowerCase(cArr[i]);
                    i++;
                } else {
                    return new Alphabet(String.valueOf(this.name).concat(".lowerCase()"), lowerCased);
                }
            }
        }

        public boolean matches(char c) {
            byte[] bArr = this.decodabet;
            return c < bArr.length && bArr[c] != -1;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(@CheckForNull Object other) {
            if (other instanceof Alphabet) {
                Alphabet that = (Alphabet) other;
                return Arrays.equals(this.chars, that.chars);
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes7.dex */
    public static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;

        @CheckForNull
        @LazyInit
        private transient BaseEncoding lowerCase;

        @CheckForNull
        final Character paddingChar;

        @CheckForNull
        @LazyInit
        private transient BaseEncoding upperCase;

        StandardBaseEncoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        StandardBaseEncoding(Alphabet alphabet, @CheckForNull Character paddingChar) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar.charValue()), "Padding character %s was already in alphabet", paddingChar);
            this.paddingChar = paddingChar;
        }

        @Override // com.google.common.io.BaseEncoding
        int maxEncodedSize(int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        @Override // com.google.common.io.BaseEncoding
        public OutputStream encodingStream(final Writer out) {
            Preconditions.checkNotNull(out);
            return new OutputStream() { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.1
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                @Override // java.io.OutputStream
                public void write(int b) throws IOException {
                    int i = this.bitBuffer << 8;
                    this.bitBuffer = i;
                    this.bitBuffer = i | (b & 255);
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        int charIndex = (this.bitBuffer >> (this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar)) & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        this.writtenChars++;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }

                @Override // java.io.OutputStream, java.io.Flushable
                public void flush() throws IOException {
                    out.flush();
                }

                @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        int charIndex = (this.bitBuffer << (StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength)) & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        this.writtenChars++;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    out.close();
                }
            };
        }

        @Override // com.google.common.io.BaseEncoding
        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int i = 0;
            while (i < len) {
                encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
                i += this.alphabet.bytesPerChunk;
            }
        }

        void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            Preconditions.checkArgument(len <= this.alphabet.bytesPerChunk);
            long bitBuffer = 0;
            for (int i = 0; i < len; i++) {
                bitBuffer = (bitBuffer | (bytes[off + i] & 255)) << 8;
            }
            int i2 = len + 1;
            int bitOffset = (i2 * 8) - this.alphabet.bitsPerChar;
            int bitsProcessed = 0;
            while (bitsProcessed < len * 8) {
                int charIndex = this.alphabet.mask & ((int) (bitBuffer >>> (bitOffset - bitsProcessed)));
                target.append(this.alphabet.encode(charIndex));
                bitsProcessed += this.alphabet.bitsPerChar;
            }
            if (this.paddingChar != null) {
                while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
                    target.append(this.paddingChar.charValue());
                    bitsProcessed += this.alphabet.bitsPerChar;
                }
            }
        }

        @Override // com.google.common.io.BaseEncoding
        int maxDecodedSize(int chars) {
            return (int) (((this.alphabet.bitsPerChar * chars) + 7) / 8);
        }

        @Override // com.google.common.io.BaseEncoding
        CharSequence trimTrailingPadding(CharSequence chars) {
            Preconditions.checkNotNull(chars);
            Character ch = this.paddingChar;
            if (ch == null) {
                return chars;
            }
            char padChar = ch.charValue();
            int l = chars.length() - 1;
            while (l >= 0 && chars.charAt(l) == padChar) {
                l--;
            }
            return chars.subSequence(0, l + 1);
        }

        @Override // com.google.common.io.BaseEncoding
        public boolean canDecode(CharSequence chars) {
            Preconditions.checkNotNull(chars);
            CharSequence chars2 = trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars2.length())) {
                return false;
            }
            for (int i = 0; i < chars2.length(); i++) {
                if (!this.alphabet.canDecode(chars2.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.BaseEncoding
        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            CharSequence chars2 = trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars2.length())) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars2.length()).toString());
            }
            int bytesWritten = 0;
            int charIdx = 0;
            while (charIdx < chars2.length()) {
                long chunk = 0;
                int charsProcessed = 0;
                for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
                    chunk <<= this.alphabet.bitsPerChar;
                    if (charIdx + i < chars2.length()) {
                        chunk |= this.alphabet.decode(chars2.charAt(charsProcessed + charIdx));
                        charsProcessed++;
                    }
                }
                int minOffset = (this.alphabet.bytesPerChunk * 8) - (this.alphabet.bitsPerChar * charsProcessed);
                int offset = (this.alphabet.bytesPerChunk - 1) * 8;
                while (offset >= minOffset) {
                    target[bytesWritten] = (byte) ((chunk >>> offset) & 255);
                    offset -= 8;
                    bytesWritten++;
                }
                charIdx += this.alphabet.charsPerChunk;
            }
            return bytesWritten;
        }

        @Override // com.google.common.io.BaseEncoding
        public InputStream decodingStream(final Reader reader) {
            Preconditions.checkNotNull(reader);
            return new InputStream() { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.2
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;

                @Override // java.io.InputStream
                public int read() throws IOException {
                    while (true) {
                        int readChar = reader.read();
                        if (readChar == -1) {
                            if (this.hitPadding || StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                                return -1;
                            }
                            throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(this.readChars).toString());
                        }
                        this.readChars++;
                        char ch = (char) readChar;
                        if (StandardBaseEncoding.this.paddingChar != null && StandardBaseEncoding.this.paddingChar.charValue() == ch) {
                            if (this.hitPadding || (this.readChars != 1 && StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                                this.hitPadding = true;
                            }
                        } else {
                            if (this.hitPadding) {
                                throw new DecodingException(new StringBuilder(61).append("Expected padding character but found '").append(ch).append("' at index ").append(this.readChars).toString());
                            }
                            int i = this.bitBuffer << StandardBaseEncoding.this.alphabet.bitsPerChar;
                            this.bitBuffer = i;
                            this.bitBuffer = i | StandardBaseEncoding.this.alphabet.decode(ch);
                            int i2 = this.bitBufferLength + StandardBaseEncoding.this.alphabet.bitsPerChar;
                            this.bitBufferLength = i2;
                            if (i2 >= 8) {
                                int i3 = i2 - 8;
                                this.bitBufferLength = i3;
                                return (this.bitBuffer >> i3) & 255;
                            }
                        }
                    }
                    throw new DecodingException(new StringBuilder(41).append("Padding cannot start at index ").append(this.readChars).toString());
                }

                @Override // java.io.InputStream
                public int read(byte[] buf, int off, int len) throws IOException {
                    Preconditions.checkPositionIndexes(off, off + len, buf.length);
                    int i = off;
                    while (i < off + len) {
                        int b = read();
                        if (b == -1) {
                            int read = i - off;
                            if (read == 0) {
                                return -1;
                            }
                            return read;
                        }
                        buf[i] = (byte) b;
                        i++;
                    }
                    return i - off;
                }

                @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    reader.close();
                }
            };
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : newInstance(this.alphabet, null);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char padChar) {
            Character ch;
            if (8 % this.alphabet.bitsPerChar == 0 || ((ch = this.paddingChar) != null && ch.charValue() == padChar)) {
                return this;
            }
            return newInstance(this.alphabet, Character.valueOf(padChar));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            int i = 0;
            while (true) {
                if (i >= separator.length()) {
                    break;
                }
                Preconditions.checkArgument(!this.alphabet.matches(separator.charAt(i)), "Separator (%s) cannot contain alphabet characters", separator);
                i++;
            }
            Character ch = this.paddingChar;
            if (ch != null) {
                Preconditions.checkArgument(separator.indexOf(ch.charValue()) < 0, "Separator (%s) cannot contain padding character", separator);
            }
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                Alphabet upper = this.alphabet.upperCase();
                BaseEncoding result2 = upper == this.alphabet ? this : newInstance(upper, this.paddingChar);
                this.upperCase = result2;
                return result2;
            }
            return result;
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                Alphabet lower = this.alphabet.lowerCase();
                BaseEncoding result2 = lower == this.alphabet ? this : newInstance(lower, this.paddingChar);
                this.lowerCase = result2;
                return result2;
            }
            return result;
        }

        BaseEncoding newInstance(Alphabet alphabet, @CheckForNull Character paddingChar) {
            return new StandardBaseEncoding(alphabet, paddingChar);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                } else {
                    builder.append(".withPadChar('").append(this.paddingChar).append("')");
                }
            }
            return builder.toString();
        }

        public boolean equals(@CheckForNull Object other) {
            if (!(other instanceof StandardBaseEncoding)) {
                return false;
            }
            StandardBaseEncoding that = (StandardBaseEncoding) other;
            return this.alphabet.equals(that.alphabet) && Objects.equal(this.paddingChar, that.paddingChar);
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }
    }

    /* loaded from: classes7.dex */
    static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        Base16Encoding(String name, String alphabetChars) {
            this(new Alphabet(name, alphabetChars.toCharArray()));
        }

        private Base16Encoding(Alphabet alphabet) {
            super(alphabet, null);
            this.encoding = new char[512];
            Preconditions.checkArgument(alphabet.chars.length == 16);
            for (int i = 0; i < 256; i++) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 256] = alphabet.encode(i & 15);
            }
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            for (int i = 0; i < len; i++) {
                int b = bytes[off + i] & 255;
                target.append(this.encoding[b]);
                target.append(this.encoding[b | 256]);
            }
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            if (chars.length() % 2 == 1) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars.length()).toString());
            }
            int bytesWritten = 0;
            int i = 0;
            while (i < chars.length()) {
                int decoded = (this.alphabet.decode(chars.charAt(i)) << 4) | this.alphabet.decode(chars.charAt(i + 1));
                target[bytesWritten] = (byte) decoded;
                i += 2;
                bytesWritten++;
            }
            return bytesWritten;
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        BaseEncoding newInstance(Alphabet alphabet, @CheckForNull Character paddingChar) {
            return new Base16Encoding(alphabet);
        }
    }

    /* loaded from: classes7.dex */
    static final class Base64Encoding extends StandardBaseEncoding {
        Base64Encoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        private Base64Encoding(Alphabet alphabet, @CheckForNull Character paddingChar) {
            super(alphabet, paddingChar);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int i = off;
            int remaining = len;
            while (remaining >= 3) {
                int i2 = i + 1;
                int i3 = i2 + 1;
                int chunk = ((bytes[i] & 255) << 16) | ((bytes[i2] & 255) << 8) | (bytes[i3] & 255);
                target.append(this.alphabet.encode(chunk >>> 18));
                target.append(this.alphabet.encode((chunk >>> 12) & 63));
                target.append(this.alphabet.encode((chunk >>> 6) & 63));
                target.append(this.alphabet.encode(chunk & 63));
                remaining -= 3;
                i = i3 + 1;
            }
            int remaining2 = off + len;
            if (i < remaining2) {
                encodeChunkTo(target, bytes, i, (off + len) - i);
            }
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            CharSequence chars2 = trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars2.length())) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars2.length()).toString());
            }
            int chunk = 0;
            int chunk2 = 0;
            while (chunk2 < chars2.length()) {
                int i = chunk2 + 1;
                int i2 = i + 1;
                int chunk3 = (this.alphabet.decode(chars2.charAt(chunk2)) << 18) | (this.alphabet.decode(chars2.charAt(i)) << 12);
                int bytesWritten = chunk + 1;
                target[chunk] = (byte) (chunk3 >>> 16);
                if (i2 >= chars2.length()) {
                    chunk = bytesWritten;
                    chunk2 = i2;
                } else {
                    int i3 = i2 + 1;
                    int chunk4 = (this.alphabet.decode(chars2.charAt(i2)) << 6) | chunk3;
                    int chunk5 = bytesWritten + 1;
                    target[bytesWritten] = (byte) ((chunk4 >>> 8) & 255);
                    if (i3 >= chars2.length()) {
                        chunk = chunk5;
                        chunk2 = i3;
                    } else {
                        target[chunk5] = (byte) ((chunk4 | this.alphabet.decode(chars2.charAt(i3))) & 255);
                        chunk = chunk5 + 1;
                        chunk2 = i3 + 1;
                    }
                }
            }
            return chunk;
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        BaseEncoding newInstance(Alphabet alphabet, @CheckForNull Character paddingChar) {
            return new Base64Encoding(alphabet, paddingChar);
        }
    }

    static Reader ignoringReader(final Reader delegate, final String toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new Reader() { // from class: com.google.common.io.BaseEncoding.3
            @Override // java.io.Reader
            public int read() throws IOException {
                int readChar;
                do {
                    readChar = delegate.read();
                    if (readChar == -1) {
                        break;
                    }
                } while (toIgnore.indexOf((char) readChar) >= 0);
                return readChar;
            }

            @Override // java.io.Reader
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    static Appendable separatingAppendable(Appendable delegate, String separator, int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new Appendable(afterEveryChars, delegate, separator) { // from class: com.google.common.io.BaseEncoding.4
            int charsUntilSeparator;
            final /* synthetic */ int val$afterEveryChars;
            final /* synthetic */ Appendable val$delegate;
            final /* synthetic */ String val$separator;

            {
                this.val$afterEveryChars = afterEveryChars;
                this.val$delegate = delegate;
                this.val$separator = separator;
                this.charsUntilSeparator = afterEveryChars;
            }

            @Override // java.lang.Appendable
            public Appendable append(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    this.val$delegate.append(this.val$separator);
                    this.charsUntilSeparator = this.val$afterEveryChars;
                }
                this.val$delegate.append(c);
                this.charsUntilSeparator--;
                return this;
            }

            @Override // java.lang.Appendable
            public Appendable append(@CheckForNull CharSequence chars, int off, int len) {
                throw new UnsupportedOperationException();
            }

            @Override // java.lang.Appendable
            public Appendable append(@CheckForNull CharSequence chars) {
                throw new UnsupportedOperationException();
            }
        };
    }

    static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
        final Appendable separatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
        return new Writer() { // from class: com.google.common.io.BaseEncoding.5
            @Override // java.io.Writer
            public void write(int c) throws IOException {
                separatingAppendable.append((char) c);
            }

            @Override // java.io.Writer
            public void write(char[] chars, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override // java.io.Writer, java.io.Flushable
            public void flush() throws IOException {
                delegate.flush();
            }

            @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    /* loaded from: classes7.dex */
    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;

        SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(delegate);
            this.separator = (String) Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        CharSequence trimTrailingPadding(CharSequence chars) {
            return this.delegate.trimTrailingPadding(chars);
        }

        @Override // com.google.common.io.BaseEncoding
        int maxEncodedSize(int bytes) {
            int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return (this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR)) + unseparatedSize;
        }

        @Override // com.google.common.io.BaseEncoding
        public OutputStream encodingStream(Writer output) {
            return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
        }

        @Override // com.google.common.io.BaseEncoding
        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
        }

        @Override // com.google.common.io.BaseEncoding
        int maxDecodedSize(int chars) {
            return this.delegate.maxDecodedSize(chars);
        }

        @Override // com.google.common.io.BaseEncoding
        public boolean canDecode(CharSequence chars) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < chars.length(); i++) {
                char c = chars.charAt(i);
                if (this.separator.indexOf(c) < 0) {
                    builder.append(c);
                }
            }
            return this.delegate.canDecode(builder);
        }

        @Override // com.google.common.io.BaseEncoding
        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            StringBuilder stripped = new StringBuilder(chars.length());
            for (int i = 0; i < chars.length(); i++) {
                char c = chars.charAt(i);
                if (this.separator.indexOf(c) < 0) {
                    stripped.append(c);
                }
            }
            return this.delegate.decodeTo(target, stripped);
        }

        @Override // com.google.common.io.BaseEncoding
        public InputStream decodingStream(Reader reader) {
            return this.delegate.decodingStream(ignoringReader(reader, this.separator));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            String valueOf = String.valueOf(this.delegate);
            String str = this.separator;
            return new StringBuilder(String.valueOf(valueOf).length() + 31 + String.valueOf(str).length()).append(valueOf).append(".withSeparator(\"").append(str).append("\", ").append(this.afterEveryChars).append(")").toString();
        }
    }
}
