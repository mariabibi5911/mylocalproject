package com.android.tools.smali.dexlib2.immutable.util;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import com.android.tools.smali.dexlib2.immutable.ImmutableAnnotation;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class ParamUtil {
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0004. Please report as an issue. */
    public static int findTypeEnd(@Nonnull String str, int index) {
        char c = str.charAt(index);
        switch (c) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /* 66 */:
            case ConstraintLayout.LayoutParams.Table.GUIDELINE_USE_RTL /* 67 */:
            case HeaderItem.TYPE_START_OFFSET /* 68 */:
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return index + 1;
            case HeaderItem.PROTO_START_OFFSET /* 76 */:
                while (true) {
                    int index2 = index + 1;
                    if (str.charAt(index) == 59) {
                        return index2;
                    }
                    index = index2;
                }
            case '[':
                while (true) {
                    int index3 = index + 1;
                    if (str.charAt(index) == 91) {
                        return findTypeEnd(str, index3);
                    }
                    index = index3;
                }
            default:
                throw new IllegalArgumentException(String.format("Param string \"%s\" contains invalid type prefix: %s", str, Character.toString(c)));
        }
    }

    @Nonnull
    public static Iterable<ImmutableMethodParameter> parseParamString(@Nonnull final String params) {
        return new Iterable<ImmutableMethodParameter>() { // from class: com.android.tools.smali.dexlib2.immutable.util.ParamUtil.1
            @Override // java.lang.Iterable
            public Iterator<ImmutableMethodParameter> iterator() {
                return new Iterator<ImmutableMethodParameter>() { // from class: com.android.tools.smali.dexlib2.immutable.util.ParamUtil.1.1
                    private int index = 0;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.index < params.length();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImmutableMethodParameter next() {
                        int end = ParamUtil.findTypeEnd(params, this.index);
                        String ret = params.substring(this.index, end);
                        this.index = end;
                        return new ImmutableMethodParameter(ret, (ImmutableSet<? extends ImmutableAnnotation>) null, (String) null);
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
