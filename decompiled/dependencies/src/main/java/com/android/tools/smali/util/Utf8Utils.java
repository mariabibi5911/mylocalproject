package com.android.tools.smali.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Utf8Utils {
    private static final ThreadLocal<char[]> localBuffer = new ThreadLocal<char[]>() { // from class: com.android.tools.smali.util.Utf8Utils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public char[] initialValue() {
            return new char[256];
        }
    };

    public static byte[] stringToUtf8Bytes(String string) {
        int len = string.length();
        byte[] bytes = new byte[len * 3];
        int outAt = 0;
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (c != 0 && c < 128) {
                bytes[outAt] = (byte) c;
                outAt++;
            } else if (c < 2048) {
                bytes[outAt] = (byte) (((c >> 6) & 31) | 192);
                bytes[outAt + 1] = (byte) (128 | (c & '?'));
                outAt += 2;
            } else {
                bytes[outAt] = (byte) (((c >> '\f') & 15) | 224);
                bytes[outAt + 1] = (byte) (((c >> 6) & 63) | 128);
                bytes[outAt + 2] = (byte) (128 | (c & '?'));
                outAt += 3;
            }
        }
        byte[] result = new byte[outAt];
        System.arraycopy(bytes, 0, result, 0, outAt);
        return result;
    }

    public static String utf8BytesToString(byte[] bytes, int start, int length) {
        char out;
        ThreadLocal<char[]> threadLocal = localBuffer;
        char[] chars = threadLocal.get();
        if (chars == null || chars.length < length) {
            chars = new char[length];
            threadLocal.set(chars);
        }
        int outAt = 0;
        int at = start;
        while (length > 0) {
            int v0 = bytes[at] & 255;
            switch (v0 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    length--;
                    if (v0 == 0) {
                        return throwBadUtf8(v0, at);
                    }
                    out = (char) v0;
                    at++;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    return throwBadUtf8(v0, at);
                case 12:
                case 13:
                    length -= 2;
                    if (length < 0) {
                        return throwBadUtf8(v0, at);
                    }
                    int v1 = bytes[at + 1] & 255;
                    if ((v1 & 192) != 128) {
                        return throwBadUtf8(v1, at + 1);
                    }
                    int value = ((v0 & 31) << 6) | (v1 & 63);
                    if (value != 0 && value < 128) {
                        return throwBadUtf8(v1, at + 1);
                    }
                    out = (char) value;
                    at += 2;
                    break;
                case 14:
                    length -= 3;
                    if (length < 0) {
                        return throwBadUtf8(v0, at);
                    }
                    int v12 = bytes[at + 1] & 255;
                    if ((v12 & 192) != 128) {
                        return throwBadUtf8(v12, at + 1);
                    }
                    int v2 = bytes[at + 2] & 255;
                    if ((v2 & 192) != 128) {
                        return throwBadUtf8(v2, at + 2);
                    }
                    int value2 = ((v0 & 15) << 12) | ((v12 & 63) << 6) | (v2 & 63);
                    if (value2 < 2048) {
                        return throwBadUtf8(v2, at + 2);
                    }
                    out = (char) value2;
                    at += 3;
                    break;
            }
            chars[outAt] = out;
            outAt++;
        }
        return new String(chars, 0, outAt);
    }

    public static String utf8BytesWithUtf16LengthToString(@Nonnull byte[] bytes, int start, int utf16Length) {
        return utf8BytesWithUtf16LengthToString(bytes, start, utf16Length, null);
    }

    public static String utf8BytesWithUtf16LengthToString(@Nonnull byte[] bytes, int start, int utf16Length, @Nullable int[] readLength) {
        char out;
        ThreadLocal<char[]> threadLocal = localBuffer;
        char[] chars = threadLocal.get();
        if (chars == null || chars.length < utf16Length) {
            chars = new char[utf16Length];
            threadLocal.set(chars);
        }
        int outAt = 0;
        int at = start;
        while (utf16Length > 0) {
            int v0 = bytes[at] & 255;
            switch (v0 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    if (v0 == 0) {
                        return throwBadUtf8(v0, at);
                    }
                    out = (char) v0;
                    at++;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    return throwBadUtf8(v0, at);
                case 12:
                case 13:
                    int v1 = bytes[at + 1] & 255;
                    if ((v1 & 192) != 128) {
                        return throwBadUtf8(v1, at + 1);
                    }
                    int value = ((v0 & 31) << 6) | (v1 & 63);
                    if (value != 0 && value < 128) {
                        return throwBadUtf8(v1, at + 1);
                    }
                    out = (char) value;
                    at += 2;
                    break;
                case 14:
                    int v12 = bytes[at + 1] & 255;
                    if ((v12 & 192) != 128) {
                        return throwBadUtf8(v12, at + 1);
                    }
                    int v2 = bytes[at + 2] & 255;
                    if ((v2 & 192) != 128) {
                        return throwBadUtf8(v2, at + 2);
                    }
                    int value2 = ((v0 & 15) << 12) | ((v12 & 63) << 6) | (v2 & 63);
                    if (value2 < 2048) {
                        return throwBadUtf8(v2, at + 2);
                    }
                    out = (char) value2;
                    at += 3;
                    break;
            }
            chars[outAt] = out;
            outAt++;
            utf16Length--;
        }
        if (readLength != null && readLength.length > 0) {
            readLength[0] = at - start;
            readLength[0] = at - start;
        }
        return new String(chars, 0, outAt);
    }

    private static String throwBadUtf8(int value, int offset) {
        throw new IllegalArgumentException("bad utf-8 byte " + Hex.u1(value) + " at offset " + Hex.u4(offset));
    }
}
