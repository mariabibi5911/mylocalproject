package com.android.tools.smali.dexlib2.writer.util;

import com.android.tools.smali.dexlib2.base.value.BaseArrayEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodHandleEncodedValue;
import com.android.tools.smali.dexlib2.base.value.BaseMethodTypeEncodedValue;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue;
import com.android.tools.smali.dexlib2.iface.value.EncodedValue;
import com.android.tools.smali.dexlib2.immutable.value.ImmutableStringEncodedValue;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CallSiteUtil {
    public static ArrayEncodedValue getEncodedCallSite(final CallSiteReference callSiteReference) {
        return new BaseArrayEncodedValue() { // from class: com.android.tools.smali.dexlib2.writer.util.CallSiteUtil.1
            @Override // com.android.tools.smali.dexlib2.iface.value.ArrayEncodedValue
            @Nonnull
            public List<? extends EncodedValue> getValue() {
                List<EncodedValue> encodedCallSite = Lists.newArrayList();
                encodedCallSite.add(new BaseMethodHandleEncodedValue() { // from class: com.android.tools.smali.dexlib2.writer.util.CallSiteUtil.1.1
                    @Override // com.android.tools.smali.dexlib2.iface.value.MethodHandleEncodedValue
                    @Nonnull
                    public MethodHandleReference getValue() {
                        return CallSiteReference.this.getMethodHandle();
                    }
                });
                encodedCallSite.add(new ImmutableStringEncodedValue(CallSiteReference.this.getMethodName()));
                encodedCallSite.add(new BaseMethodTypeEncodedValue() { // from class: com.android.tools.smali.dexlib2.writer.util.CallSiteUtil.1.2
                    @Override // com.android.tools.smali.dexlib2.iface.value.MethodTypeEncodedValue
                    @Nonnull
                    public MethodProtoReference getValue() {
                        return CallSiteReference.this.getMethodProto();
                    }
                });
                encodedCallSite.addAll(CallSiteReference.this.getExtraArguments());
                return encodedCallSite;
            }
        };
    }
}
