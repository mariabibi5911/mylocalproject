package com.android.tools.smali.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public final class TwoColumnOutput {
    private String[] leftLines;
    private final int leftWidth;
    private final Writer out;
    private String[] rightLines;
    private final int rightWidth;
    private final String spacer;

    public TwoColumnOutput(@Nonnull Writer out, int leftWidth, int rightWidth, @Nonnull String spacer) {
        this.leftLines = null;
        this.rightLines = null;
        if (leftWidth < 1) {
            throw new IllegalArgumentException("leftWidth < 1");
        }
        if (rightWidth < 1) {
            throw new IllegalArgumentException("rightWidth < 1");
        }
        this.out = out;
        this.leftWidth = leftWidth;
        this.rightWidth = rightWidth;
        this.spacer = spacer;
    }

    public TwoColumnOutput(OutputStream out, int leftWidth, int rightWidth, String spacer) {
        this(new OutputStreamWriter(out), leftWidth, rightWidth, spacer);
    }

    public void write(String left, String right) throws IOException {
        this.leftLines = StringWrapper.wrapString(left, this.leftWidth, this.leftLines);
        String[] wrapString = StringWrapper.wrapString(right, this.rightWidth, this.rightLines);
        this.rightLines = wrapString;
        int leftCount = this.leftLines.length;
        int rightCount = wrapString.length;
        int i = 0;
        while (true) {
            if (i < leftCount || i < rightCount) {
                String leftLine = null;
                String rightLine = null;
                if (i < leftCount && (leftLine = this.leftLines[i]) == null) {
                    leftCount = i;
                }
                if (i < rightCount && (rightLine = this.rightLines[i]) == null) {
                    rightCount = i;
                }
                if (leftLine != null || rightLine != null) {
                    int written = 0;
                    if (leftLine != null) {
                        this.out.write(leftLine);
                        written = leftLine.length();
                    }
                    int remaining = this.leftWidth - written;
                    if (remaining > 0) {
                        writeSpaces(this.out, remaining);
                    }
                    this.out.write(this.spacer);
                    if (rightLine != null) {
                        this.out.write(rightLine);
                    }
                    this.out.write(10);
                }
                i++;
            } else {
                return;
            }
        }
    }

    private static void writeSpaces(Writer out, int amt) throws IOException {
        while (amt > 0) {
            out.write(32);
            amt--;
        }
    }
}
