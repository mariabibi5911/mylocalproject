package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DebugInfoItem {
    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.DebugInfoItem.1
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "debug_info_item";
            }

            /* JADX WARN: Failed to find 'out' block for switch in B:10:0x008a. Please report as an issue. */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r5v0 */
            /* JADX WARN: Type inference failed for: r5v13 */
            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                int lineStart = reader.readBigUleb128();
                int i = 1;
                int i2 = 0;
                out.annotateTo(reader.getOffset(), "line_start = %d", Long.valueOf(lineStart & 4294967295L));
                int parametersSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "parameters_size = %d", Integer.valueOf(parametersSize));
                if (parametersSize > 0) {
                    out.annotate(0, "parameters:", new Object[0]);
                    out.indent();
                    for (int i3 = 0; i3 < parametersSize; i3++) {
                        int paramaterIndex = reader.readSmallUleb128() - 1;
                        out.annotateTo(reader.getOffset(), "%s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, paramaterIndex, true));
                    }
                    out.deindent();
                }
                out.annotate(0, "debug opcodes:", new Object[0]);
                out.indent();
                int codeAddress = 0;
                int lineNumber = lineStart;
                while (true) {
                    int opcode = reader.readUbyte();
                    switch (opcode) {
                        case 0:
                            break;
                        case 1:
                            out.annotateTo(reader.getOffset(), "DBG_ADVANCE_PC", new Object[0]);
                            out.indent();
                            int addressDiff = reader.readSmallUleb128();
                            codeAddress += addressDiff;
                            out.annotateTo(reader.getOffset(), "addr_diff = +0x%x: 0x%x", Integer.valueOf(addressDiff), Integer.valueOf(codeAddress));
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 2:
                            out.annotateTo(reader.getOffset(), "DBG_ADVANCE_LINE", new Object[0]);
                            out.indent();
                            int lineDiff = reader.readSleb128();
                            lineNumber += lineDiff;
                            out.annotateTo(reader.getOffset(), "line_diff = +%d: %d", Integer.valueOf(Math.abs(lineDiff)), Integer.valueOf(lineNumber));
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 3:
                            out.annotateTo(reader.getOffset(), "DBG_START_LOCAL", new Object[0]);
                            out.indent();
                            int registerNum = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "register_num = v%d", Integer.valueOf(registerNum));
                            int nameIndex = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex, true));
                            int typeIndex = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "type_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex));
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 4:
                            out.annotateTo(reader.getOffset(), "DBG_START_LOCAL_EXTENDED", new Object[i2]);
                            out.indent();
                            int registerNum2 = reader.readSmallUleb128();
                            int offset = reader.getOffset();
                            Object[] objArr = new Object[i];
                            objArr[i2] = Integer.valueOf(registerNum2);
                            out.annotateTo(offset, "register_num = v%d", objArr);
                            int nameIndex2 = reader.readSmallUleb128() - i;
                            int offset2 = reader.getOffset();
                            Object[] objArr2 = new Object[i];
                            objArr2[0] = StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex2, i);
                            out.annotateTo(offset2, "name_idx = %s", objArr2);
                            int typeIndex2 = reader.readSmallUleb128() - i;
                            int offset3 = reader.getOffset();
                            Object[] objArr3 = new Object[i];
                            objArr3[0] = TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex2);
                            out.annotateTo(offset3, "type_idx = %s", objArr3);
                            int sigIndex = reader.readSmallUleb128() - i;
                            int offset4 = reader.getOffset();
                            Object[] objArr4 = new Object[i];
                            objArr4[0] = StringIdItem.getOptionalReferenceAnnotation(this.dexFile, sigIndex, i);
                            out.annotateTo(offset4, "sig_idx = %s", objArr4);
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 5:
                            int registerNum3 = reader.getOffset();
                            out.annotateTo(registerNum3, "DBG_END_LOCAL", new Object[i2]);
                            out.indent();
                            int registerNum4 = reader.readSmallUleb128();
                            int offset5 = reader.getOffset();
                            Object[] objArr5 = new Object[i];
                            objArr5[i2] = Integer.valueOf(registerNum4);
                            out.annotateTo(offset5, "register_num = v%d", objArr5);
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 6:
                            out.annotateTo(reader.getOffset(), "DBG_RESTART_LOCAL", new Object[i2]);
                            out.indent();
                            int registerNum5 = reader.readSmallUleb128();
                            int offset6 = reader.getOffset();
                            Object[] objArr6 = new Object[i];
                            objArr6[i2] = Integer.valueOf(registerNum5);
                            out.annotateTo(offset6, "register_num = v%d", objArr6);
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        case 7:
                            out.annotateTo(reader.getOffset(), "DBG_SET_PROLOGUE_END", new Object[i2]);
                            i = 1;
                            i2 = 0;
                        case 8:
                            int nameIdx = reader.getOffset();
                            out.annotateTo(nameIdx, "DBG_SET_EPILOGUE_BEGIN", new Object[i2]);
                            i = 1;
                            i2 = 0;
                        case 9:
                            out.annotateTo(reader.getOffset(), "DBG_SET_FILE", new Object[i2]);
                            out.indent();
                            int nameIdx2 = reader.readSmallUleb128() - i;
                            int offset7 = reader.getOffset();
                            Object[] objArr7 = new Object[i];
                            objArr7[i2] = StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIdx2);
                            out.annotateTo(offset7, "name_idx = %s", objArr7);
                            out.deindent();
                            i = 1;
                            i2 = 0;
                        default:
                            int adjusted = opcode - 10;
                            int addressDiff2 = adjusted / 15;
                            int lineDiff2 = (adjusted % 15) - 4;
                            codeAddress += addressDiff2;
                            lineNumber += lineDiff2;
                            out.annotateTo(reader.getOffset(), "address_diff = +0x%x:0x%x, line_diff = +%d:%d, ", Integer.valueOf(addressDiff2), Integer.valueOf(codeAddress), Integer.valueOf(lineDiff2), Integer.valueOf(lineNumber));
                            i = 1;
                            i2 = 0;
                    }
                    out.annotateTo(reader.getOffset(), "DBG_END_SEQUENCE", new Object[0]);
                    out.deindent();
                    return;
                }
            }
        };
    }
}
