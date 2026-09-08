package com.android.tools.smali.util;

import java.io.PrintStream;
import java.text.BreakIterator;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class StringWrapper {
    public static Iterable<String> wrapStringOnBreaks(@Nonnull final String string, final int maxWidth) {
        final BreakIterator breakIterator = BreakIterator.getLineInstance();
        breakIterator.setText(string);
        return new Iterable<String>() { // from class: com.android.tools.smali.util.StringWrapper.1
            @Override // java.lang.Iterable
            public Iterator<String> iterator() {
                return new Iterator<String>() { // from class: com.android.tools.smali.util.StringWrapper.1.1
                    private String nextLine;
                    private int currentLineStart = 0;
                    private boolean nextLineSet = false;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        if (!this.nextLineSet) {
                            calculateNext();
                        }
                        return this.nextLine != null;
                    }

                    private void calculateNext() {
                        int lineEnd;
                        int lineEnd2 = this.currentLineStart;
                        do {
                            lineEnd2 = breakIterator.following(lineEnd2);
                            if (lineEnd2 == -1) {
                                lineEnd = breakIterator.last();
                                if (lineEnd <= this.currentLineStart) {
                                    this.nextLine = null;
                                    this.nextLineSet = true;
                                    return;
                                }
                            } else if (lineEnd2 - this.currentLineStart > maxWidth) {
                                lineEnd = breakIterator.preceding(lineEnd2);
                                int i = this.currentLineStart;
                                if (lineEnd <= i) {
                                    lineEnd = i + maxWidth;
                                }
                            }
                            this.nextLine = string.substring(this.currentLineStart, lineEnd);
                            this.nextLineSet = true;
                            this.currentLineStart = lineEnd;
                            return;
                        } while (string.charAt(lineEnd2 - 1) != '\n');
                        this.nextLine = string.substring(this.currentLineStart, lineEnd2 - 1);
                        this.nextLineSet = true;
                        this.currentLineStart = lineEnd2;
                    }

                    @Override // java.util.Iterator
                    public String next() {
                        String ret = this.nextLine;
                        this.nextLine = null;
                        this.nextLineSet = false;
                        return ret;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static String[] wrapString(@Nonnull String str, int maxWidth, @Nullable String[] output) {
        if (output == null) {
            output = new String[(int) (((str.length() / maxWidth) * 1.5d) + 1.0d)];
        }
        int lineStart = 0;
        int arrayIndex = 0;
        int i = 0;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (c == '\n') {
                output = addString(output, str.substring(lineStart, i), arrayIndex);
                lineStart = i + 1;
                arrayIndex++;
            } else if (i - lineStart == maxWidth) {
                output = addString(output, str.substring(lineStart, i), arrayIndex);
                lineStart = i;
                arrayIndex++;
            }
            i++;
        }
        if (lineStart != i || i == 0) {
            output = addString(output, str.substring(lineStart), arrayIndex, output.length + 1);
            arrayIndex++;
        }
        if (arrayIndex < output.length) {
            output[arrayIndex] = null;
        }
        return output;
    }

    private static String[] addString(@Nonnull String[] arr, String str, int index) {
        if (index >= arr.length) {
            arr = enlargeArray(arr, (int) Math.ceil((arr.length + 1) * 1.5d));
        }
        arr[index] = str;
        return arr;
    }

    private static String[] addString(@Nonnull String[] arr, String str, int index, int newLength) {
        if (index >= arr.length) {
            arr = enlargeArray(arr, newLength);
        }
        arr[index] = str;
        return arr;
    }

    private static String[] enlargeArray(String[] arr, int newLength) {
        String[] newArr = new String[newLength];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static void printWrappedString(@Nonnull PrintStream stream, @Nonnull String string, int maxWidth) {
        for (String str : wrapStringOnBreaks(string, maxWidth)) {
            stream.println(str);
        }
    }
}
