package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class MapItem {
    public static final int ITEM_SIZE = 12;
    public static final int OFFSET_OFFSET = 8;
    public static final int SIZE_OFFSET = 4;
    public static final int TYPE_OFFSET = 0;
    private final DexBackedDexFile dexFile;
    private final int offset;

    public MapItem(DexBackedDexFile dexFile, int offset) {
        this.dexFile = dexFile;
        this.offset = offset;
    }

    public int getType() {
        return this.dexFile.getDataBuffer().readUshort(this.offset + 0);
    }

    @Nonnull
    public String getName() {
        return ItemType.getItemTypeName(getType());
    }

    public int getItemCount() {
        return this.dexFile.getDataBuffer().readSmallUint(this.offset + 4);
    }

    public int getOffset() {
        return this.dexFile.getDataBuffer().readSmallUint(this.offset + 8);
    }

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.MapItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "map_item";
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int itemType = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "type = 0x%x: %s", Integer.valueOf(itemType), ItemType.getItemTypeName(itemType));
                out.annotate(2, "unused", new Object[0]);
                int size = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(size));
                int offset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "offset = 0x%x", Integer.valueOf(offset));
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                out.moveTo(this.sectionOffset);
                int mapItemCount = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(mapItemCount));
                super.annotateSectionInner(out, mapItemCount);
            }
        };
    }
}
