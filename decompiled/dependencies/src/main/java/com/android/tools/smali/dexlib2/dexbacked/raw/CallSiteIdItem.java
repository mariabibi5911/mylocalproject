package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.dexbacked.value.DexBackedArrayEncodedValue;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class CallSiteIdItem {
    public static final int ITEM_SIZE = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.CallSiteIdItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "call_site_id_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int callSiteOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                DexBackedArrayEncodedValue arrayEncodedValue = new DexBackedArrayEncodedValue(this.dexFile, this.dexFile.getDataBuffer().readerAt(callSiteOffset));
                out.annotate(4, "call_site_id_item[0x%x] = %s", Integer.valueOf(callSiteOffset), arrayEncodedValue);
            }
        };
    }
}
