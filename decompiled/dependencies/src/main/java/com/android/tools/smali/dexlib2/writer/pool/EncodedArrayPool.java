package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.writer.EncodedArraySection;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class EncodedArrayPool extends BaseOffsetPool<ArrayEncodedValue> implements EncodedArraySection<ArrayEncodedValue, EncodedValue> {
    public EncodedArrayPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull ArrayEncodedValue arrayEncodedValue) {
        Integer prev = (Integer) this.internedItems.put(arrayEncodedValue, 0);
        if (prev == null) {
            for (EncodedValue value : arrayEncodedValue.getValue()) {
                this.dexPool.internEncodedValue(value);
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.EncodedArraySection
    public List<? extends EncodedValue> getEncodedValueList(ArrayEncodedValue arrayEncodedValue) {
        return arrayEncodedValue.getValue();
    }
}
