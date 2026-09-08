package com.android.tools.smali.dexlib2.writer.util;

import com.android.tools.smali.dexlib2.base.value.BaseArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import com.android.tools.smali.dexlib2.util.EncodedValueUtils;
import com.android.tools.smali.util.AbstractForwardSequentialList;
import com.android.tools.smali.util.CollectionUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class StaticInitializerUtil {
    private static final Predicate<Field> HAS_INITIALIZER = new Predicate<Field>() { // from class: com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil.2
        @Override // com.google.common.base.Predicate
        public boolean apply(Field input) {
            EncodedValue encodedValue = input.getInitialValue();
            return (encodedValue == null || EncodedValueUtils.isDefaultValue(encodedValue)) ? false : true;
        }
    };
    private static final Function<Field, EncodedValue> GET_INITIAL_VALUE = new Function<Field, EncodedValue>() { // from class: com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil.3
        @Override // com.google.common.base.Function
        public EncodedValue apply(Field input) {
            EncodedValue initialValue = input.getInitialValue();
            if (initialValue == null) {
                return ImmutableEncodedValueFactory.defaultValueForType(input.getType());
            }
            return initialValue;
        }
    };

    @Nullable
    public static ArrayEncodedValue getStaticInitializers(@Nonnull final SortedSet<? extends Field> sortedStaticFields) {
        final int lastIndex = CollectionUtils.lastIndexOf(sortedStaticFields, HAS_INITIALIZER);
        if (lastIndex > -1) {
            return new BaseArrayEncodedValue() { // from class: com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil.1
                @Override // com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue
                @Nonnull
                public List<? extends EncodedValue> getValue() {
                    return new AbstractForwardSequentialList<EncodedValue>() { // from class: com.android.tools.smali.dexlib2.writer.util.StaticInitializerUtil.1.1
                        @Override // com.android.tools.smali.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                        @Nonnull
                        public Iterator<EncodedValue> iterator() {
                            return FluentIterable.from(sortedStaticFields).limit(lastIndex + 1).transform(StaticInitializerUtil.GET_INITIAL_VALUE).iterator();
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                        public int size() {
                            return lastIndex + 1;
                        }
                    };
                }
            };
        }
        return null;
    }
}
