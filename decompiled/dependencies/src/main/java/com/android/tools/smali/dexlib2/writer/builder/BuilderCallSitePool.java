package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.writer.CallSiteSection;
import com.android.tools.smali.dexlib2.writer.builder.BuilderEncodedValues;
import com.android.tools.smali.dexlib2.writer.util.CallSiteUtil;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderCallSitePool extends BaseBuilderPool implements CallSiteSection<BuilderCallSiteReference, BuilderEncodedValues.BuilderArrayEncodedValue> {

    @Nonnull
    private final ConcurrentMap<CallSiteReference, BuilderCallSiteReference> internedItems;

    public BuilderCallSitePool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderCallSiteReference internCallSite(@Nonnull CallSiteReference callSiteReference) {
        BuilderCallSiteReference internedCallSite = this.internedItems.get(callSiteReference);
        if (internedCallSite != null) {
            return internedCallSite;
        }
        BuilderEncodedValues.BuilderArrayEncodedValue encodedCallSite = ((BuilderEncodedArrayPool) this.dexBuilder.encodedArraySection).internArrayEncodedValue(CallSiteUtil.getEncodedCallSite(callSiteReference));
        BuilderCallSiteReference internedCallSite2 = new BuilderCallSiteReference(callSiteReference.getName(), encodedCallSite);
        BuilderCallSiteReference existing = this.internedItems.putIfAbsent(internedCallSite2, internedCallSite2);
        return existing == null ? internedCallSite2 : existing;
    }

    @Override // com.android.tools.smali.dexlib2.writer.CallSiteSection
    public BuilderEncodedValues.BuilderArrayEncodedValue getEncodedCallSite(BuilderCallSiteReference callSiteReference) {
        return callSiteReference.encodedCallSite;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderCallSiteReference builderCallSite) {
        return builderCallSite.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderCallSiteReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderCallSiteReference>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderCallSitePool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderCallSiteReference builderCallSiteReference) {
                return builderCallSiteReference.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderCallSiteReference builderCallSiteReference, int value) {
                int prev = builderCallSiteReference.index;
                builderCallSiteReference.index = value;
                return prev;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }
}
