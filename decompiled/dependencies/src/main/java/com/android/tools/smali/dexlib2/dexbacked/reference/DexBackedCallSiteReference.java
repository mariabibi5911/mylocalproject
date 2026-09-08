package com.android.tools.smali.dexlib2.dexbacked.reference;

import com.android.tools.smali.dexlib2.base.reference.BaseCallSiteReference;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.util.EncodedArrayItemIterator;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexBackedCallSiteReference extends BaseCallSiteReference {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public final int callSiteIdOffset;
    public final int callSiteIndex;
    private int callSiteOffset = -1;

    @Nonnull
    public final DexBackedDexFile dexFile;

    public DexBackedCallSiteReference(DexBackedDexFile dexFile, int callSiteIndex) {
        this.dexFile = dexFile;
        this.callSiteIndex = callSiteIndex;
        this.callSiteIdOffset = dexFile.getCallSiteSection().getOffset(callSiteIndex);
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getName() {
        return String.format("call_site_%d", Integer.valueOf(this.callSiteIndex));
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodHandleReference getMethodHandle() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        EncodedValue encodedValue = getCallSiteIterator().getNextOrNull();
        if (encodedValue == null) {
            throw new AssertionError();
        }
        if (encodedValue.getValueType() != 22) {
            throw new ExceptionWithContext("Invalid encoded value type (%d) for the first item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
        }
        return ((MethodHandleEncodedValue) encodedValue).getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public String getMethodName() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        iter.skipNext();
        EncodedValue encodedValue = iter.getNextOrNull();
        if (encodedValue == null) {
            throw new AssertionError();
        }
        if (encodedValue.getValueType() != 23) {
            throw new ExceptionWithContext("Invalid encoded value type (%d) for the second item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
        }
        return ((StringEncodedValue) encodedValue).getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public MethodProtoReference getMethodProto() {
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        iter.skipNext();
        iter.skipNext();
        EncodedValue encodedValue = iter.getNextOrNull();
        if (encodedValue == null) {
            throw new AssertionError();
        }
        if (encodedValue.getValueType() != 21) {
            throw new ExceptionWithContext("Invalid encoded value type (%d) for the second item in call site %d", Integer.valueOf(encodedValue.getValueType()), Integer.valueOf(this.callSiteIndex));
        }
        return ((MethodTypeEncodedValue) encodedValue).getValue();
    }

    @Override // com.android.tools.smali.dexlib2.iface.reference.CallSiteReference
    @Nonnull
    public List<? extends EncodedValue> getExtraArguments() {
        List<EncodedValue> values = Lists.newArrayList();
        EncodedArrayItemIterator iter = getCallSiteIterator();
        if (iter.getItemCount() < 3) {
            throw new ExceptionWithContext("Invalid call site item: must contain at least 3 entries.", new Object[0]);
        }
        if (iter.getItemCount() == 3) {
            return values;
        }
        iter.skipNext();
        iter.skipNext();
        iter.skipNext();
        for (EncodedValue item = iter.getNextOrNull(); item != null; item = iter.getNextOrNull()) {
            values.add(item);
        }
        return values;
    }

    private EncodedArrayItemIterator getCallSiteIterator() {
        return EncodedArrayItemIterator.newOrEmpty(this.dexFile, getCallSiteOffset());
    }

    private int getCallSiteOffset() {
        if (this.callSiteOffset < 0) {
            this.callSiteOffset = this.dexFile.getBuffer().readSmallUint(this.callSiteIdOffset);
        }
        return this.callSiteOffset;
    }

    @Override // com.android.tools.smali.dexlib2.base.reference.BaseReference, com.android.tools.smali.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        int i = this.callSiteIndex;
        if (i < 0 || i >= this.dexFile.getCallSiteSection().size()) {
            throw new Reference.InvalidReferenceException("callsite@" + this.callSiteIndex);
        }
    }
}
