package com.android.tools.smali.dexlib2.dexbacked.raw;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.VerificationError;
import com.android.tools.smali.dexlib2.dexbacked.CDexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.dexbacked.instruction.DexBackedInstruction;
import com.android.tools.smali.dexlib2.dexbacked.raw.util.DexAnnotator;
import com.android.tools.smali.dexlib2.formatter.DexFormatter;
import com.android.tools.smali.dexlib2.iface.instruction.FieldOffsetInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.InlineIndexInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.ThreeRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.VerificationErrorInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.VtableIndexInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.util.AnnotatedBytes;
import com.android.tools.smali.util.ExceptionWithContext;
import com.android.tools.smali.util.NumberUtils;
import com.google.android.material.timepicker.TimeModel;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class CodeItem {
    public static final int DEBUG_INFO_OFFSET = 8;
    public static final int INSTRUCTION_COUNT_OFFSET = 12;
    public static final int INSTRUCTION_START_OFFSET = 16;
    public static final int INS_OFFSET = 2;
    public static final int OUTS_OFFSET = 4;
    public static final int REGISTERS_OFFSET = 0;
    public static final int TRIES_SIZE_OFFSET = 6;
    public static int CDEX_TRIES_SIZE_SHIFT = 0;
    public static int CDEX_OUTS_COUNT_SHIFT = 4;
    public static int CDEX_INS_COUNT_SHIFT = 8;
    public static int CDEX_REGISTER_COUNT_SHIFT = 12;
    public static int CDEX_INSTRUCTIONS_SIZE_AND_PREHEADER_FLAGS_OFFSET = 2;
    public static int CDEX_INSTRUCTIONS_SIZE_SHIFT = 5;
    public static int CDEX_PREHEADER_FLAGS_MASK = 31;
    public static int CDEX_PREHEADER_FLAG_REGISTER_COUNT = 1;
    public static int CDEX_PREHEADER_FLAG_INS_COUNT = 2;
    public static int CDEX_PREHEADER_FLAG_OUTS_COUNT = 4;
    public static int CDEX_PREHEADER_FLAG_TRIES_COUNT = 8;
    public static int CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE = 16;

    /* loaded from: classes.dex */
    public static class TryItem {
        public static final int CODE_UNIT_COUNT_OFFSET = 4;
        public static final int HANDLER_OFFSET = 6;
        public static final int ITEM_SIZE = 8;
        public static final int START_ADDRESS_OFFSET = 0;
    }

    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        if (annotator.dexFile instanceof CDexBackedDexFile) {
            return makeAnnotatorForCDex(annotator, mapItem);
        }
        return makeAnnotatorForDex(annotator, mapItem);
    }

    @Nonnull
    private static SectionAnnotator makeAnnotatorForDex(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new CodeItemAnnotator(annotator, mapItem);
    }

    @Nonnull
    private static SectionAnnotator makeAnnotatorForCDex(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new CodeItemAnnotator(annotator, mapItem) { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.CodeItem.1
            private List<Integer> sortedItems;

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                ArrayList arrayList = new ArrayList(this.itemIdentities.keySet());
                this.sortedItems = arrayList;
                arrayList.sort(new Comparator() { // from class: com.android.tools.smali.dexlib2.dexbacked.raw.CodeItem$1$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return ((Integer) obj).compareTo((Integer) obj2);
                    }
                });
                out.moveTo(this.sectionOffset);
                annotateSectionInner(out, this.itemIdentities.size());
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
            protected int getItemOffset(int itemIndex, int currentOffset) {
                return this.sortedItems.get(itemIndex).intValue();
            }

            @Override // com.android.tools.smali.dexlib2.dexbacked.raw.CodeItem.CodeItemAnnotator
            protected CodeItemAnnotator.PreInstructionInfo annotatePreInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, @Nullable String itemIdentity) {
                int totalInstructionsSize;
                int sizeFields = reader.readUshort();
                int triesCount = (sizeFields >> CodeItem.CDEX_TRIES_SIZE_SHIFT) & 15;
                int outsCount = (sizeFields >> CodeItem.CDEX_OUTS_COUNT_SHIFT) & 15;
                int insCount = (sizeFields >> CodeItem.CDEX_INS_COUNT_SHIFT) & 15;
                int registerCount = (sizeFields >> CodeItem.CDEX_REGISTER_COUNT_SHIFT) & 15;
                int startOffset = out.getCursor();
                out.annotate(2, "tries_size = %d", Integer.valueOf(triesCount));
                out.annotate(0, "outs_size = %d", Integer.valueOf(outsCount));
                out.annotate(0, "ins_size = %d", Integer.valueOf(insCount));
                out.annotate(0, "registers_size = %d", Integer.valueOf(registerCount));
                int instructionsSizeAndPreheaderFlags = reader.readUshort();
                int instructionsSize = instructionsSizeAndPreheaderFlags >> CodeItem.CDEX_INSTRUCTIONS_SIZE_SHIFT;
                out.annotate(2, "insns_size = %d", Integer.valueOf(instructionsSize));
                int instructionsStartOffset = out.getCursor();
                int preheaderOffset = startOffset;
                int totalTriesCount = triesCount;
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAGS_MASK) != 0) {
                    int preheaderCount = Integer.bitCount(instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAGS_MASK);
                    if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
                        preheaderCount++;
                    }
                    out.moveTo(startOffset - (preheaderCount * 2));
                    out.deindent();
                    out.annotate(0, "[preheader for next code_item]", new Object[0]);
                    out.indent();
                    out.moveTo(instructionsStartOffset);
                }
                if ((CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE & instructionsSizeAndPreheaderFlags) != 0) {
                    out.annotate(0, "insns_size_preheader_flag=1", new Object[0]);
                    int preheaderOffset2 = preheaderOffset - 2;
                    reader.setOffset(preheaderOffset2);
                    int extraInstructionsSize = reader.readUshort();
                    preheaderOffset = preheaderOffset2 - 2;
                    reader.setOffset(preheaderOffset);
                    int extraInstructionsSize2 = extraInstructionsSize + reader.readUshort();
                    out.moveTo(preheaderOffset);
                    int totalInstructionsSize2 = instructionsSize + extraInstructionsSize2;
                    out.annotate(2, "insns_size = %d + %d = %d", Integer.valueOf(instructionsSize), Integer.valueOf(extraInstructionsSize2), Integer.valueOf(instructionsSize + extraInstructionsSize2));
                    out.moveTo(instructionsStartOffset);
                    totalInstructionsSize = totalInstructionsSize2;
                } else {
                    totalInstructionsSize = instructionsSize;
                }
                if ((CodeItem.CDEX_PREHEADER_FLAG_REGISTER_COUNT & instructionsSizeAndPreheaderFlags) != 0) {
                    out.annotate(0, "registers_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraRegisterCount = reader.readUshort();
                    out.annotate(2, "registers_size = %d + %d = %d", Integer.valueOf(registerCount), Integer.valueOf(extraRegisterCount), Integer.valueOf(registerCount + extraRegisterCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((CodeItem.CDEX_PREHEADER_FLAG_INS_COUNT & instructionsSizeAndPreheaderFlags) != 0) {
                    out.annotate(0, "ins_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraInsCount = reader.readUshort();
                    out.annotate(2, "ins_size = %d + %d = %d", Integer.valueOf(insCount), Integer.valueOf(extraInsCount), Integer.valueOf(insCount + extraInsCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((CodeItem.CDEX_PREHEADER_FLAG_OUTS_COUNT & instructionsSizeAndPreheaderFlags) != 0) {
                    out.annotate(0, "outs_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraOutsCount = reader.readUshort();
                    out.annotate(2, "outs_size = %d + %d = %d", Integer.valueOf(outsCount), Integer.valueOf(extraOutsCount), Integer.valueOf(outsCount + extraOutsCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((CodeItem.CDEX_PREHEADER_FLAG_TRIES_COUNT & instructionsSizeAndPreheaderFlags) != 0) {
                    out.annotate(0, "tries_size_preheader_flag=1", new Object[0]);
                    int preheaderOffset3 = preheaderOffset - 2;
                    out.moveTo(preheaderOffset3);
                    reader.setOffset(preheaderOffset3);
                    int extraTriesCount = reader.readUshort();
                    totalTriesCount += extraTriesCount;
                    out.annotate(2, "tries_size = %d + %d = %d", Integer.valueOf(triesCount), Integer.valueOf(extraTriesCount), Integer.valueOf(triesCount + extraTriesCount));
                    out.moveTo(instructionsStartOffset);
                }
                reader.setOffset(instructionsStartOffset);
                return new CodeItemAnnotator.PreInstructionInfo(totalTriesCount, totalInstructionsSize);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CodeItemAnnotator extends SectionAnnotator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private SectionAnnotator debugInfoAnnotator;

        public CodeItemAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
            super(annotator, mapItem);
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
        @Nonnull
        public String getItemName() {
            return "code_item";
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
        public int getItemAlignment() {
            return 4;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: classes.dex */
        public class PreInstructionInfo {
            public int instructionSize;
            public int triesCount;

            public PreInstructionInfo(int triesCount, int instructionSize) {
                this.triesCount = triesCount;
                this.instructionSize = instructionSize;
            }
        }

        protected PreInstructionInfo annotatePreInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, @Nullable String itemIdentity) {
            int registers = reader.readUshort();
            out.annotate(2, "registers_size = %d", Integer.valueOf(registers));
            int inSize = reader.readUshort();
            out.annotate(2, "ins_size = %d", Integer.valueOf(inSize));
            int outSize = reader.readUshort();
            out.annotate(2, "outs_size = %d", Integer.valueOf(outSize));
            int triesCount = reader.readUshort();
            out.annotate(2, "tries_size = %d", Integer.valueOf(triesCount));
            int debugInfoOffset = reader.readInt();
            out.annotate(4, "debug_info_off = 0x%x", Integer.valueOf(debugInfoOffset));
            if (debugInfoOffset > 0) {
                addDebugInfoIdentity(debugInfoOffset, itemIdentity);
            }
            int instructionSize = reader.readSmallUint();
            out.annotate(4, "insns_size = 0x%x", Integer.valueOf(instructionSize));
            return new PreInstructionInfo(triesCount, instructionSize);
        }

        protected void annotateInstructions(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, int instructionSize) {
            out.annotate(0, "instructions:", new Object[0]);
            out.indent();
            out.setLimit(out.getCursor(), out.getCursor() + (instructionSize * 2));
            int end = reader.getOffset() + (instructionSize * 2);
            do {
                try {
                    try {
                    } catch (ExceptionWithContext ex) {
                        ex.printStackTrace(System.err);
                        out.annotate(0, "annotation error: %s", ex.getMessage());
                        out.moveTo(end);
                        reader.setOffset(end);
                    }
                    if (reader.getOffset() < end) {
                        Instruction instruction = DexBackedInstruction.readFrom(this.dexFile, reader);
                        if (reader.getOffset() > end) {
                            out.annotateTo(end, "truncated instruction", new Object[0]);
                            reader.setOffset(end);
                        } else {
                            switch (AnonymousClass2.$SwitchMap$com$android$tools$smali$dexlib2$Format[instruction.getOpcode().format.ordinal()]) {
                                case 1:
                                    annotateInstruction10x(out, instruction);
                                    break;
                                case 2:
                                    annotateInstruction35c(out, (Instruction35c) instruction);
                                    break;
                                case 3:
                                    annotateInstruction3rc(out, (Instruction3rc) instruction);
                                    break;
                                case 4:
                                    annotateArrayPayload(out, (ArrayPayload) instruction);
                                    break;
                                case 5:
                                    annotatePackedSwitchPayload(out, (PackedSwitchPayload) instruction);
                                    break;
                                case 6:
                                    annotateSparseSwitchPayload(out, (SparseSwitchPayload) instruction);
                                    break;
                                default:
                                    annotateDefaultInstruction(out, instruction);
                                    break;
                            }
                        }
                    } else {
                        return;
                    }
                } finally {
                    out.clearLimit();
                    out.deindent();
                }
            } while (reader.getOffset() == out.getCursor());
            throw new AssertionError();
        }

        protected void annotatePostInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, int triesCount) {
            if (triesCount > 0) {
                if (reader.getOffset() % 4 != 0) {
                    reader.readUshort();
                    out.annotate(2, "padding", new Object[0]);
                }
                out.annotate(0, "try_items:", new Object[0]);
                out.indent();
                for (int i = 0; i < triesCount; i++) {
                    try {
                        out.annotate(0, "try_item[%d]:", Integer.valueOf(i));
                        out.indent();
                        int startAddr = reader.readSmallUint();
                        out.annotate(4, "start_addr = 0x%x", Integer.valueOf(startAddr));
                        int instructionCount = reader.readUshort();
                        out.annotate(2, "insn_count = 0x%x", Integer.valueOf(instructionCount));
                        int handlerOffset = reader.readUshort();
                        out.annotate(2, "handler_off = 0x%x", Integer.valueOf(handlerOffset));
                        out.deindent();
                    } catch (Throwable th) {
                        throw th;
                    } finally {
                        out.deindent();
                    }
                }
                out.deindent();
                int handlerListCount = reader.readSmallUleb128();
                out.annotate(0, "encoded_catch_handler_list:", new Object[0]);
                out.annotateTo(reader.getOffset(), "size = %d", Integer.valueOf(handlerListCount));
                out.indent();
                for (int i2 = 0; i2 < handlerListCount; i2++) {
                    try {
                        out.annotate(0, "encoded_catch_handler[%d]", Integer.valueOf(i2));
                        out.indent();
                        try {
                            int handlerCount = reader.readSleb128();
                            out.annotateTo(reader.getOffset(), "size = %d", Integer.valueOf(handlerCount));
                            boolean hasCatchAll = handlerCount <= 0;
                            int handlerCount2 = Math.abs(handlerCount);
                            if (handlerCount2 != 0) {
                                out.annotate(0, "handlers:", new Object[0]);
                                out.indent();
                                for (int j = 0; j < handlerCount2; j++) {
                                    try {
                                        out.annotate(0, "encoded_type_addr_pair[%d]", Integer.valueOf(i2));
                                        out.indent();
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                    try {
                                        int typeIndex = reader.readSmallUleb128();
                                        try {
                                            out.annotateTo(reader.getOffset(), TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex), new Object[0]);
                                            int handlerAddress = reader.readSmallUleb128();
                                            out.annotateTo(reader.getOffset(), "addr = 0x%x", Integer.valueOf(handlerAddress));
                                            try {
                                                out.deindent();
                                            } catch (Throwable th3) {
                                                th = th3;
                                                out.deindent();
                                                throw th;
                                            }
                                        } catch (Throwable th4) {
                                            th = th4;
                                            out.deindent();
                                            throw th;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                    }
                                }
                                try {
                                    out.deindent();
                                } catch (Throwable th6) {
                                    th = th6;
                                    out.deindent();
                                    throw th;
                                }
                            }
                            if (hasCatchAll) {
                                int catchAllAddress = reader.readSmallUleb128();
                                out.annotateTo(reader.getOffset(), "catch_all_addr = 0x%x", Integer.valueOf(catchAllAddress));
                            }
                            try {
                                out.deindent();
                            } catch (Throwable th7) {
                                th = th7;
                                throw th;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                    }
                }
            }
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.raw.SectionAnnotator
        public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            try {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                PreInstructionInfo info = annotatePreInstructionFields(out, reader, itemIdentity);
                annotateInstructions(out, reader, info.instructionSize);
                annotatePostInstructionFields(out, reader, info.triesCount);
            } catch (ExceptionWithContext ex) {
                out.annotate(0, "annotation error: %s", ex.getMessage());
            }
        }

        private String formatRegister(int registerNum) {
            return String.format("v%d", Integer.valueOf(registerNum));
        }

        private void annotateInstruction10x(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
        }

        private void annotateInstruction35c(@Nonnull AnnotatedBytes out, @Nonnull Instruction35c instruction) {
            List<String> args = Lists.newArrayList();
            int registerCount = instruction.getRegisterCount();
            if (registerCount == 1) {
                args.add(formatRegister(instruction.getRegisterC()));
            } else if (registerCount == 2) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
            } else if (registerCount == 3) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
            } else if (registerCount == 4) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
                args.add(formatRegister(instruction.getRegisterF()));
            } else if (registerCount == 5) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
                args.add(formatRegister(instruction.getRegisterF()));
                args.add(formatRegister(instruction.getRegisterG()));
            }
            out.annotate(6, String.format("%s {%s}, %s", instruction.getOpcode().name, Joiner.on(", ").join(args), instruction.getReference()), new Object[0]);
        }

        private void annotateInstruction3rc(@Nonnull AnnotatedBytes out, @Nonnull Instruction3rc instruction) {
            int startRegister = instruction.getStartRegister();
            int endRegister = (instruction.getRegisterCount() + startRegister) - 1;
            out.annotate(6, String.format("%s {%s .. %s}, %s", instruction.getOpcode().name, formatRegister(startRegister), formatRegister(endRegister), instruction.getReference()), new Object[0]);
        }

        private void annotateDefaultInstruction(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            String referenceString;
            List<String> args = Lists.newArrayList();
            if (instruction instanceof OneRegisterInstruction) {
                args.add(formatRegister(((OneRegisterInstruction) instruction).getRegisterA()));
                if (instruction instanceof TwoRegisterInstruction) {
                    args.add(formatRegister(((TwoRegisterInstruction) instruction).getRegisterB()));
                    if (instruction instanceof ThreeRegisterInstruction) {
                        args.add(formatRegister(((ThreeRegisterInstruction) instruction).getRegisterC()));
                    }
                }
            } else if (instruction instanceof VerificationErrorInstruction) {
                String verificationError = VerificationError.getVerificationErrorName(((VerificationErrorInstruction) instruction).getVerificationError());
                if (verificationError != null) {
                    args.add(verificationError);
                } else {
                    args.add("invalid verification error type");
                }
            }
            if (instruction instanceof ReferenceInstruction) {
                ReferenceInstruction referenceInstruction = (ReferenceInstruction) instruction;
                Reference reference = ((ReferenceInstruction) instruction).getReference();
                if (referenceInstruction.getReferenceType() == 0) {
                    referenceString = DexFormatter.INSTANCE.getQuotedString((StringReference) reference);
                } else {
                    referenceString = referenceInstruction.getReference().toString();
                }
                args.add(referenceString);
            } else if (instruction instanceof OffsetInstruction) {
                int offset = ((OffsetInstruction) instruction).getCodeOffset();
                String sign = offset >= 0 ? "+" : "-";
                args.add(String.format("%s0x%x", sign, Integer.valueOf(Math.abs(offset))));
            } else if (instruction instanceof NarrowLiteralInstruction) {
                int value = ((NarrowLiteralInstruction) instruction).getNarrowLiteral();
                if (NumberUtils.isLikelyFloat(value)) {
                    args.add(String.format("%d # %f", Integer.valueOf(value), Float.valueOf(Float.intBitsToFloat(value))));
                } else {
                    args.add(String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(value)));
                }
            } else if (instruction instanceof WideLiteralInstruction) {
                long value2 = ((WideLiteralInstruction) instruction).getWideLiteral();
                if (NumberUtils.isLikelyDouble(value2)) {
                    args.add(String.format("%d # %f", Long.valueOf(value2), Double.valueOf(Double.longBitsToDouble(value2))));
                } else {
                    args.add(String.format(TimeModel.NUMBER_FORMAT, Long.valueOf(value2)));
                }
            } else if (instruction instanceof FieldOffsetInstruction) {
                int fieldOffset = ((FieldOffsetInstruction) instruction).getFieldOffset();
                args.add(String.format("field@0x%x", Integer.valueOf(fieldOffset)));
            } else if (instruction instanceof VtableIndexInstruction) {
                int vtableIndex = ((VtableIndexInstruction) instruction).getVtableIndex();
                args.add(String.format("vtable@%d", Integer.valueOf(vtableIndex)));
            } else if (instruction instanceof InlineIndexInstruction) {
                int inlineIndex = ((InlineIndexInstruction) instruction).getInlineIndex();
                args.add(String.format("inline@%d", Integer.valueOf(inlineIndex)));
            }
            out.annotate(instruction.getCodeUnits() * 2, "%s %s", instruction.getOpcode().name, Joiner.on(", ").join(args));
        }

        private void annotateArrayPayload(@Nonnull AnnotatedBytes out, @Nonnull ArrayPayload instruction) {
            List<Number> elements = instruction.getArrayElements();
            int elementWidth = instruction.getElementWidth();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "element_width = %d", Integer.valueOf(elementWidth));
            out.annotate(4, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() > 0) {
                out.annotate(0, "elements:", new Object[0]);
            }
            out.indent();
            if (elements.size() > 0) {
                for (int i = 0; i < elements.size(); i++) {
                    if (elementWidth == 8) {
                        long value = elements.get(i).longValue();
                        if (NumberUtils.isLikelyDouble(value)) {
                            out.annotate(elementWidth, "element[%d] = %d # %f", Integer.valueOf(i), Long.valueOf(value), Double.valueOf(Double.longBitsToDouble(value)));
                        } else {
                            out.annotate(elementWidth, "element[%d] = %d", Integer.valueOf(i), Long.valueOf(value));
                        }
                    } else {
                        int value2 = elements.get(i).intValue();
                        if (NumberUtils.isLikelyFloat(value2)) {
                            out.annotate(elementWidth, "element[%d] = %d # %f", Integer.valueOf(i), Integer.valueOf(value2), Float.valueOf(Float.intBitsToFloat(value2)));
                        } else {
                            out.annotate(elementWidth, "element[%d] = %d", Integer.valueOf(i), Integer.valueOf(value2));
                        }
                    }
                }
            }
            int i2 = out.getCursor();
            if (i2 % 2 != 0) {
                out.annotate(1, "padding", new Object[0]);
            }
            out.deindent();
            out.deindent();
        }

        private void annotatePackedSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull PackedSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() == 0) {
                out.annotate(4, "first_key", new Object[0]);
            } else {
                out.annotate(4, "first_key = %d", Integer.valueOf(elements.get(0).getKey()));
                out.annotate(0, "targets:", new Object[0]);
                out.indent();
                for (int i = 0; i < elements.size(); i++) {
                    out.annotate(4, "target[%d] = %d", Integer.valueOf(i), Integer.valueOf(elements.get(i).getOffset()));
                }
                out.deindent();
            }
            out.deindent();
        }

        private void annotateSparseSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull SparseSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() > 0) {
                out.annotate(0, "keys:", new Object[0]);
                out.indent();
                for (int i = 0; i < elements.size(); i++) {
                    out.annotate(4, "key[%d] = %d", Integer.valueOf(i), Integer.valueOf(elements.get(i).getKey()));
                }
                out.deindent();
                out.annotate(0, "targets:", new Object[0]);
                out.indent();
                for (int i2 = 0; i2 < elements.size(); i2++) {
                    out.annotate(4, "target[%d] = %d", Integer.valueOf(i2), Integer.valueOf(elements.get(i2).getOffset()));
                }
                out.deindent();
            }
            out.deindent();
        }

        private void addDebugInfoIdentity(int debugInfoOffset, String methodString) {
            SectionAnnotator sectionAnnotator = this.debugInfoAnnotator;
            if (sectionAnnotator != null) {
                sectionAnnotator.setItemIdentity(debugInfoOffset, methodString);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.tools.smali.dexlib2.dexbacked.raw.CodeItem$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$tools$smali$dexlib2$Format;

        static {
            int[] iArr = new int[Format.values().length];
            $SwitchMap$com$android$tools$smali$dexlib2$Format = iArr;
            try {
                iArr[Format.Format10x.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format35c.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.Format3rc.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.ArrayPayload.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.PackedSwitchPayload.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$tools$smali$dexlib2$Format[Format.SparseSwitchPayload.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }
}
