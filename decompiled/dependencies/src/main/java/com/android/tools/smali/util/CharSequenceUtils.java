package com.android.tools.smali.util;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.util.List;

/* loaded from: classes.dex */
public class CharSequenceUtils {
    private static final Function<Object, String> TO_STRING = Functions.toStringFunction();

    public static int listHashCode(List<? extends CharSequence> list) {
        return Lists.transform(list, TO_STRING).hashCode();
    }

    public static boolean listEquals(List<? extends CharSequence> list1, List<? extends CharSequence> list2) {
        Function<Object, String> function = TO_STRING;
        return Lists.transform(list1, function).equals(Lists.transform(list2, function));
    }
}
