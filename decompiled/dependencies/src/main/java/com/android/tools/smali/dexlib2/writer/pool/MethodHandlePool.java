package com.android.tools.smali.dexlib2.writer.pool;

import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.writer.MethodHandleSection;
import com.android.tools.smali.util.ExceptionWithContext;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class MethodHandlePool extends BaseIndexPool<MethodHandleReference> implements MethodHandleSection<MethodHandleReference, FieldReference, MethodReference> {
    public MethodHandlePool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(MethodHandleReference methodHandleReference) {
        Integer prev = (Integer) this.internedItems.put(methodHandleReference, 0);
        if (prev == null) {
            switch (methodHandleReference.getMethodHandleType()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    ((FieldPool) this.dexPool.fieldSection).intern((FieldReference) methodHandleReference.getMemberReference());
                    return;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    ((MethodPool) this.dexPool.methodSection).intern((MethodReference) methodHandleReference.getMemberReference());
                    return;
                default:
                    throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleReference.getMethodHandleType()));
            }
        }
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodHandleSection
    public FieldReference getFieldReference(MethodHandleReference methodHandleReference) {
        return (FieldReference) methodHandleReference.getMemberReference();
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodHandleSection
    public MethodReference getMethodReference(MethodHandleReference methodHandleReference) {
        return (MethodReference) methodHandleReference.getMemberReference();
    }
}
