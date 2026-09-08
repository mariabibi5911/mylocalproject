package com.android.tools.smali.dexlib2.util;

import com.android.tools.smali.util.ExceptionWithContext;
import com.android.tools.smali.util.Hex;
import com.android.tools.smali.util.TwoColumnOutput;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnnotatedBytes {
    private int cursor;
    private int indentLevel;
    private int outputWidth;

    @Nonnull
    private TreeMap<Integer, AnnotationEndpoint> annotatations = Maps.newTreeMap();
    private int hexCols = 8;
    private int startLimit = -1;
    private int endLimit = -1;

    public AnnotatedBytes(int width) {
        this.outputWidth = width;
    }

    public void moveTo(int offset) {
        this.cursor = offset;
    }

    public void moveBy(int offset) {
        this.cursor += offset;
    }

    public void annotateTo(int offset, @Nonnull String msg, Object... formatArgs) {
        annotate(offset - this.cursor, msg, formatArgs);
    }

    public void annotate(int length, @Nonnull String msg, Object... formatArgs) {
        String formattedMsg;
        AnnotationItem existingRangeAnnotation;
        Map.Entry<Integer, AnnotationEndpoint> nextEntry;
        int i;
        int i2;
        int i3 = this.startLimit;
        if (i3 != -1 && (i = this.endLimit) != -1 && ((i2 = this.cursor) < i3 || i2 >= i)) {
            throw new ExceptionWithContext("Annotating outside the parent bounds", new Object[0]);
        }
        if (formatArgs != null && formatArgs.length > 0) {
            formattedMsg = String.format(msg, formatArgs);
        } else {
            formattedMsg = msg;
        }
        int i4 = this.cursor;
        int exclusiveEndOffset = i4 + length;
        AnnotationEndpoint endPoint = null;
        AnnotationEndpoint startPoint = this.annotatations.get(Integer.valueOf(i4));
        if (startPoint == null) {
            Map.Entry<Integer, AnnotationEndpoint> previousEntry = this.annotatations.lowerEntry(Integer.valueOf(this.cursor));
            if (previousEntry != null) {
                AnnotationEndpoint previousAnnotations = previousEntry.getValue();
                AnnotationItem previousRangeAnnotation = previousAnnotations.rangeAnnotation;
                if (previousRangeAnnotation != null) {
                    int i5 = this.cursor;
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(i5, Integer.valueOf(i5 + length), formattedMsg), formatAnnotation(previousEntry.getKey().intValue(), previousRangeAnnotation.annotation));
                }
            }
        } else if (length > 0 && (existingRangeAnnotation = startPoint.rangeAnnotation) != null) {
            int i6 = this.cursor;
            throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(i6, Integer.valueOf(i6 + length), formattedMsg), formatAnnotation(this.cursor, existingRangeAnnotation.annotation));
        }
        if (length > 0 && (nextEntry = this.annotatations.higherEntry(Integer.valueOf(this.cursor))) != null) {
            int nextKey = nextEntry.getKey().intValue();
            if (nextKey < exclusiveEndOffset) {
                AnnotationEndpoint nextEndpoint = nextEntry.getValue();
                AnnotationItem nextRangeAnnotation = nextEndpoint.rangeAnnotation;
                if (nextRangeAnnotation != null) {
                    int i7 = this.cursor;
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(i7, Integer.valueOf(i7 + length), formattedMsg), formatAnnotation(nextKey, nextRangeAnnotation.annotation));
                }
                if (nextEndpoint.pointAnnotations.size() > 0) {
                    int i8 = this.cursor;
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(i8, Integer.valueOf(i8 + length), formattedMsg), formatAnnotation(nextKey, Integer.valueOf(nextKey), nextEndpoint.pointAnnotations.get(0).annotation));
                }
                int i9 = this.cursor;
                throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation endpoint at %d", formatAnnotation(i9, Integer.valueOf(i9 + length), formattedMsg), Integer.valueOf(nextKey));
            }
            if (nextKey == exclusiveEndOffset) {
                endPoint = nextEntry.getValue();
            }
        }
        if (startPoint == null) {
            startPoint = new AnnotationEndpoint();
            this.annotatations.put(Integer.valueOf(this.cursor), startPoint);
        }
        if (length == 0) {
            startPoint.pointAnnotations.add(new AnnotationItem(this.indentLevel, formattedMsg));
        } else {
            startPoint.rangeAnnotation = new AnnotationItem(this.indentLevel, formattedMsg);
            if (endPoint == null) {
                this.annotatations.put(Integer.valueOf(exclusiveEndOffset), new AnnotationEndpoint());
            }
        }
        this.cursor += length;
    }

    private String formatAnnotation(int offset, String annotationMsg) {
        Integer endOffset = this.annotatations.higherKey(Integer.valueOf(offset));
        return formatAnnotation(offset, endOffset, annotationMsg);
    }

    private String formatAnnotation(int offset, Integer endOffset, String annotationMsg) {
        return endOffset != null ? String.format("[0x%x, 0x%x) \"%s\"", Integer.valueOf(offset), endOffset, annotationMsg) : String.format("[0x%x, ) \"%s\"", Integer.valueOf(offset), annotationMsg);
    }

    public void indent() {
        this.indentLevel++;
    }

    public void deindent() {
        int i = this.indentLevel - 1;
        this.indentLevel = i;
        if (i < 0) {
            this.indentLevel = 0;
        }
    }

    public int getCursor() {
        return this.cursor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnnotationEndpoint {

        @Nonnull
        public final List<AnnotationItem> pointAnnotations;

        @Nullable
        public AnnotationItem rangeAnnotation;

        private AnnotationEndpoint() {
            this.pointAnnotations = Lists.newArrayList();
            this.rangeAnnotation = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnnotationItem {
        public final String annotation;
        public final int indentLevel;

        public AnnotationItem(int indentLevel, String annotation) {
            this.indentLevel = indentLevel;
            this.annotation = annotation;
        }
    }

    public int getAnnotationWidth() {
        int i = this.hexCols;
        int leftWidth = (i * 2) + 8 + (i / 2);
        return this.outputWidth - leftWidth;
    }

    public void writeAnnotations(Writer out, byte[] data, int offset) throws IOException {
        String right;
        int rightWidth = getAnnotationWidth();
        int leftWidth = (this.outputWidth - rightWidth) - 1;
        String padding = Strings.repeat(" ", 1000);
        TwoColumnOutput twoc = new TwoColumnOutput(out, leftWidth, rightWidth, "|");
        Integer[] keys = (Integer[]) this.annotatations.keySet().toArray(new Integer[this.annotatations.size()]);
        AnnotationEndpoint[] values = new AnnotationEndpoint[this.annotatations.size()];
        AnnotationEndpoint[] values2 = (AnnotationEndpoint[]) this.annotatations.values().toArray(values);
        for (int i = 0; i < keys.length - 1; i++) {
            int rangeStart = keys[i].intValue();
            int rangeEnd = keys[i + 1].intValue();
            AnnotationEndpoint annotations = values2[i];
            for (Iterator<AnnotationItem> it = annotations.pointAnnotations.iterator(); it.hasNext(); it = it) {
                AnnotationItem pointAnnotation = it.next();
                String paddingSub = padding.substring(0, pointAnnotation.indentLevel * 2);
                twoc.write("", paddingSub + pointAnnotation.annotation);
            }
            AnnotationItem rangeAnnotation = annotations.rangeAnnotation;
            if (rangeAnnotation != null) {
                String right2 = padding.substring(0, rangeAnnotation.indentLevel * 2);
                right = right2 + rangeAnnotation.annotation;
            } else {
                right = "";
            }
            String right3 = right;
            String left = Hex.dump(data, rangeStart + offset, rangeEnd - rangeStart, rangeStart + offset, this.hexCols, 6);
            twoc.write(left, right3);
        }
        int lastKey = keys[keys.length - 1].intValue();
        if (lastKey < data.length) {
            String left2 = Hex.dump(data, lastKey + offset, (data.length - offset) - lastKey, lastKey + offset, this.hexCols, 6);
            twoc.write(left2, "");
        }
    }

    public void setLimit(int start, int end) {
        this.startLimit = start;
        this.endLimit = end;
    }

    public void clearLimit() {
        this.startLimit = -1;
        this.endLimit = -1;
    }
}
