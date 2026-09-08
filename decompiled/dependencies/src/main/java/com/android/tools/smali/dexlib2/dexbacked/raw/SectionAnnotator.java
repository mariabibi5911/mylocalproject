package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AlignmentUtils;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class SectionAnnotator {

    @Nonnull
    public final DexAnnotator annotator;

    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int itemCount;
    protected Map<Integer, String> itemIdentities = Maps.newHashMap();
    public final int itemType;
    public final int sectionOffset;

    protected abstract void annotateItem(@Nonnull AnnotatedBytes annotatedBytes, int i, @Nullable String str);

    @Nonnull
    public abstract String getItemName();

    public SectionAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        this.annotator = annotator;
        DexBackedDexFile dexBackedDexFile = annotator.dexFile;
        this.dexFile = dexBackedDexFile;
        this.itemType = mapItem.getType();
        if (mapItem.getType() >= 4096) {
            this.sectionOffset = mapItem.getOffset() + dexBackedDexFile.getBaseDataOffset();
        } else {
            this.sectionOffset = mapItem.getOffset();
        }
        this.itemCount = mapItem.getItemCount();
    }

    public void annotateSection(@Nonnull AnnotatedBytes out) {
        out.moveTo(this.sectionOffset);
        annotateSectionInner(out, this.itemCount);
    }

    protected int getItemOffset(int itemIndex, int currentOffset) {
        return AlignmentUtils.alignOffset(currentOffset, getItemAlignment());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void annotateSectionInner(@Nonnull AnnotatedBytes out, int itemCount) {
        String itemName = getItemName();
        if (itemCount > 0) {
            out.annotate(0, "", new Object[0]);
            out.annotate(0, "-----------------------------", new Object[0]);
            out.annotate(0, "%s section", itemName);
            out.annotate(0, "-----------------------------", new Object[0]);
            out.annotate(0, "", new Object[0]);
            for (int i = 0; i < itemCount; i++) {
                out.moveTo(getItemOffset(i, out.getCursor()));
                String itemIdentity = getItemIdentity(out.getCursor());
                if (itemIdentity != null) {
                    out.annotate(0, "[%d] %s: %s", Integer.valueOf(i), itemName, itemIdentity);
                } else {
                    out.annotate(0, "[%d] %s", Integer.valueOf(i), itemName);
                }
                out.indent();
                annotateItem(out, i, itemIdentity);
                out.deindent();
            }
        }
    }

    @Nullable
    private String getItemIdentity(int itemOffset) {
        return this.itemIdentities.get(Integer.valueOf(itemOffset));
    }

    public void setItemIdentity(int itemOffset, String identity) {
        this.itemIdentities.put(Integer.valueOf(this.dexFile.getBaseDataOffset() + itemOffset), identity);
    }

    public int getItemAlignment() {
        return 1;
    }
}
