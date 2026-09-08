package com.android.tools.smali.util;

import java.io.IOException;
import java.io.Writer;

/* loaded from: classes.dex */
public class StringUtils {
    @Deprecated
    public static void writeEscapedChar(Writer writer, char c) throws IOException {
        if (c >= ' ' && c < 127) {
            if (c == '\'' || c == '\"' || c == '\\') {
                writer.write(92);
            }
            writer.write(c);
            return;
        }
        if (c <= 127) {
            switch (c) {
                case '\t':
                    writer.write("\\t");
                    return;
                case '\n':
                    writer.write("\\n");
                    return;
                case '\r':
                    writer.write("\\r");
                    return;
            }
        }
        writer.write("\\u");
        writer.write(Character.forDigit(c >> '\f', 16));
        writer.write(Character.forDigit((c >> '\b') & 15, 16));
        writer.write(Character.forDigit((c >> 4) & 15, 16));
        writer.write(Character.forDigit(c & 15, 16));
    }

    @Deprecated
    public static void writeEscapedString(Writer writer, String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c >= ' ' && c < 127) {
                if (c == '\'' || c == '\"' || c == '\\') {
                    writer.write(92);
                }
                writer.write(c);
            } else {
                if (c <= 127) {
                    switch (c) {
                        case '\t':
                            writer.write("\\t");
                            break;
                        case '\n':
                            writer.write("\\n");
                            break;
                        case '\r':
                            writer.write("\\r");
                            break;
                    }
                }
                writer.write("\\u");
                writer.write(Character.forDigit(c >> '\f', 16));
                writer.write(Character.forDigit((c >> '\b') & 15, 16));
                writer.write(Character.forDigit((c >> 4) & 15, 16));
                writer.write(Character.forDigit(c & 15, 16));
            }
        }
    }

    public static String escapeString(String value) {
        int len = value.length();
        StringBuilder sb = new StringBuilder((len * 3) / 2);
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c >= ' ' && c < 127) {
                if (c == '\'' || c == '\"' || c == '\\') {
                    sb.append('\\');
                }
                sb.append(c);
            } else {
                if (c <= 127) {
                    switch (c) {
                        case '\t':
                            sb.append("\\t");
                            break;
                        case '\n':
                            sb.append("\\n");
                            break;
                        case '\r':
                            sb.append("\\r");
                            break;
                    }
                }
                sb.append("\\u");
                sb.append(Character.forDigit(c >> '\f', 16));
                sb.append(Character.forDigit((c >> '\b') & 15, 16));
                sb.append(Character.forDigit((c >> 4) & 15, 16));
                sb.append(Character.forDigit(c & 15, 16));
            }
        }
        return sb.toString();
    }
}
