package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.CDexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBuffer;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class CdexDebugOffsetTable {
    @Nonnull
    public static void annotate(@Nonnull DexAnnotator annotator, DexBuffer buffer) {
        int i;
        DexReader reader = buffer.readerAt(annotator.getCursor());
        SectionAnnotator debugInfoAnnotator = annotator.getAnnotator(ItemType.DEBUG_INFO_ITEM);
        int methodCount = annotator.dexFile.getMethodSection().size();
        for (int methodIndex = 0; methodIndex < methodCount; methodIndex += 16) {
            char c = 2;
            annotator.annotate(0, "Offset chuck for methods %d-%d", Integer.valueOf(methodIndex), Integer.valueOf(Math.min(methodIndex + 16, methodCount)));
            annotator.indent();
            int bitmask = (reader.readUbyte() << 8) | reader.readUbyte();
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            while (true) {
                if (i2 >= 16) {
                    break;
                }
                sb.append((bitmask >> i2) & 1);
                i2++;
            }
            annotator.annotate(2, "bitmask: 0b%s", sb.reverse());
            int debugOffset = ((CDexBackedDexFile) annotator.dexFile).getDebugInfoBase();
            int i3 = 0;
            for (i = 16; i3 < i; i = 16) {
                if ((bitmask & 1) != 0) {
                    int offsetDelta = reader.readBigUleb128();
                    debugOffset += offsetDelta;
                    int offset = reader.getOffset();
                    Object[] objArr = new Object[3];
                    objArr[0] = Integer.valueOf(methodIndex + i3);
                    objArr[1] = Integer.valueOf(offsetDelta);
                    objArr[c] = Integer.valueOf(debugOffset);
                    annotator.annotateTo(offset, "[method_id: %d]: offset_delta: %d  (offset=0x%x)", objArr);
                    debugInfoAnnotator.setItemIdentity(debugOffset, annotator.dexFile.getMethodSection().get(methodIndex + i3).toString());
                }
                bitmask >>= 1;
                i3++;
                c = 2;
            }
            annotator.deindent();
        }
    }
}
