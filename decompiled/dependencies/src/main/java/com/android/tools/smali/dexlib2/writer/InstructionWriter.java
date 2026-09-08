package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.instruction.DualReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction10x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11n;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction11x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction12x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20bc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction20t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21ih;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21lh;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22b;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22cs;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22s;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction23x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction30t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31i;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction31t;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction32x;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35mi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35ms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rmi;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rms;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction45cc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction4rcc;
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction51l;
import com.android.tools.smali.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class InstructionWriter<StringRef extends StringReference, TypeRef extends TypeReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ProtoRefKey extends MethodProtoReference, MethodHandleKey extends MethodHandleReference, CallSiteKey extends CallSiteReference> {

    @Nonnull
    private final CallSiteSection<CallSiteKey, ?> callSiteSection;

    @Nonnull
    private final FieldSection<?, ?, FieldRefKey, ?> fieldSection;

    @Nonnull
    private final MethodHandleSection<MethodHandleKey, ?, ?> methodHandleSection;

    @Nonnull
    private final MethodSection<?, ?, ?, MethodRefKey, ?> methodSection;

    @Nonnull
    private final Opcodes opcodes;

    @Nonnull
    private final ProtoSection<?, ?, ProtoRefKey, ?> protoSection;

    @Nonnull
    private final StringSection<?, StringRef> stringSection;
    private final Comparator<SwitchElement> switchElementComparator = new Comparator<SwitchElement>() { // from class: com.android.tools.smali.dexlib2.writer.InstructionWriter.1
        @Override // java.util.Comparator
        public int compare(SwitchElement element1, SwitchElement element2) {
            return Ints.compare(element1.getKey(), element2.getKey());
        }
    };

    @Nonnull
    private final TypeSection<?, ?, TypeRef> typeSection;

    @Nonnull
    private final DexDataWriter writer;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nonnull
    public static <StringRef extends StringReference, TypeRef extends TypeReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ProtoRefKey extends MethodProtoReference, MethodHandleKey extends MethodHandleReference, CallSiteKey extends CallSiteReference> InstructionWriter<StringRef, TypeRef, FieldRefKey, MethodRefKey, ProtoRefKey, MethodHandleKey, CallSiteKey> makeInstructionWriter(@Nonnull Opcodes opcodes, @Nonnull DexDataWriter writer, @Nonnull StringSection<?, StringRef> stringSection, @Nonnull TypeSection<?, ?, TypeRef> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, @Nonnull ProtoSection<?, ?, ProtoRefKey, ?> protoSection, @Nonnull MethodHandleSection<MethodHandleKey, ?, ?> methodHandleSection, @Nonnull CallSiteSection<CallSiteKey, ?> callSiteSection) {
        return new InstructionWriter<>(opcodes, writer, stringSection, typeSection, fieldSection, methodSection, protoSection, methodHandleSection, callSiteSection);
    }

    InstructionWriter(@Nonnull Opcodes opcodes, @Nonnull DexDataWriter writer, @Nonnull StringSection<?, StringRef> stringSection, @Nonnull TypeSection<?, ?, TypeRef> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, @Nonnull ProtoSection<?, ?, ProtoRefKey, ?> protoSection, @Nonnull MethodHandleSection<MethodHandleKey, ?, ?> methodHandleSection, @Nonnull CallSiteSection<CallSiteKey, ?> callSiteSection) {
        this.opcodes = opcodes;
        this.writer = writer;
        this.stringSection = stringSection;
        this.typeSection = typeSection;
        this.fieldSection = fieldSection;
        this.methodSection = methodSection;
        this.protoSection = protoSection;
        this.methodHandleSection = methodHandleSection;
        this.callSiteSection = callSiteSection;
    }

    private short getOpcodeValue(Opcode opcode) {
        Short value = this.opcodes.getOpcodeValue(opcode);
        if (value == null) {
            throw new ExceptionWithContext("Instruction %s is invalid for api %d", opcode.name, Integer.valueOf(this.opcodes.api));
        }
        return value.shortValue();
    }

    public void write(@Nonnull Instruction10t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction10x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction11n instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getNarrowLiteral()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction11x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction12x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction20bc instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getVerificationError());
            this.writer.writeUshort(getReferenceIndex(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction20t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(0);
            this.writer.writeShort(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction21c instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeUshort(getReferenceIndex(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction21ih instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeShort(instruction.getHatLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction21lh instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeShort(instruction.getHatLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction21s instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeShort(instruction.getNarrowLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction21t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeShort(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22b instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.write(instruction.getRegisterB());
            this.writer.write(instruction.getNarrowLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22c instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
            this.writer.writeUshort(getReferenceIndex(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22cs instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
            this.writer.writeUshort(instruction.getFieldOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22s instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
            this.writer.writeShort(instruction.getNarrowLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
            this.writer.writeShort(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction22x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeUshort(instruction.getRegisterB());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction23x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.write(instruction.getRegisterB());
            this.writer.write(instruction.getRegisterC());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction30t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(0);
            this.writer.writeInt(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction31c instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeInt(getReferenceIndex(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction31i instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeInt(instruction.getNarrowLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction31t instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeInt(instruction.getCodeOffset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction32x instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(0);
            this.writer.writeUshort(instruction.getRegisterA());
            this.writer.writeUshort(instruction.getRegisterB());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction35c instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
            this.writer.writeUshort(getReferenceIndex(instruction));
            this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
            this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction35mi instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
            this.writer.writeUshort(instruction.getInlineIndex());
            this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
            this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction35ms instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
            this.writer.writeUshort(instruction.getVtableIndex());
            this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
            this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction3rc instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterCount());
            this.writer.writeUshort(getReferenceIndex(instruction));
            this.writer.writeUshort(instruction.getStartRegister());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction3rmi instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterCount());
            this.writer.writeUshort(instruction.getInlineIndex());
            this.writer.writeUshort(instruction.getStartRegister());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction3rms instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterCount());
            this.writer.writeUshort(instruction.getVtableIndex());
            this.writer.writeUshort(instruction.getStartRegister());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction45cc instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
            this.writer.writeUshort(getReferenceIndex(instruction));
            this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
            this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
            this.writer.writeUshort(getReference2Index(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction4rcc instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterCount());
            this.writer.writeUshort(getReferenceIndex(instruction));
            this.writer.writeUshort(instruction.getStartRegister());
            this.writer.writeUshort(getReference2Index(instruction));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull Instruction51l instruction) {
        try {
            this.writer.write(getOpcodeValue(instruction.getOpcode()));
            this.writer.write(instruction.getRegisterA());
            this.writer.writeLong(instruction.getWideLiteral());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull ArrayPayload instruction) {
        try {
            this.writer.writeUshort(getOpcodeValue(instruction.getOpcode()));
            this.writer.writeUshort(instruction.getElementWidth());
            List<Number> elements = instruction.getArrayElements();
            this.writer.writeInt(elements.size());
            switch (instruction.getElementWidth()) {
                case 1:
                    for (Number element : elements) {
                        this.writer.write(element.byteValue());
                    }
                    break;
                case 2:
                    for (Number element2 : elements) {
                        this.writer.writeShort(element2.shortValue());
                    }
                    break;
                case 4:
                    for (Number element3 : elements) {
                        this.writer.writeInt(element3.intValue());
                    }
                    break;
                case 8:
                    for (Number element4 : elements) {
                        this.writer.writeLong(element4.longValue());
                    }
                    break;
            }
            if ((this.writer.getPosition() & 1) != 0) {
                this.writer.write(0);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull SparseSwitchPayload instruction) {
        try {
            this.writer.writeUbyte(0);
            this.writer.writeUbyte(getOpcodeValue(instruction.getOpcode()) >> 8);
            List<? extends SwitchElement> elements = Ordering.from(this.switchElementComparator).immutableSortedCopy(instruction.getSwitchElements());
            this.writer.writeUshort(elements.size());
            for (SwitchElement element : elements) {
                this.writer.writeInt(element.getKey());
            }
            for (SwitchElement element2 : elements) {
                this.writer.writeInt(element2.getOffset());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(@Nonnull PackedSwitchPayload instruction) {
        try {
            this.writer.writeUbyte(0);
            this.writer.writeUbyte(getOpcodeValue(instruction.getOpcode()) >> 8);
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            this.writer.writeUshort(elements.size());
            if (elements.size() == 0) {
                this.writer.writeInt(0);
                return;
            }
            this.writer.writeInt(elements.get(0).getKey());
            for (SwitchElement element : elements) {
                this.writer.writeInt(element.getOffset());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int packNibbles(int a, int b) {
        return (b << 4) | a;
    }

    private int getReferenceIndex(ReferenceInstruction referenceInstruction) {
        return getReferenceIndex(referenceInstruction.getReferenceType(), referenceInstruction.getReference());
    }

    private int getReference2Index(DualReferenceInstruction referenceInstruction) {
        return getReferenceIndex(referenceInstruction.getReferenceType2(), referenceInstruction.getReference2());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getReferenceIndex(int i, Reference reference) {
        switch (i) {
            case 0:
                return this.stringSection.getItemIndex((StringSection<?, StringRef>) reference);
            case 1:
                return this.typeSection.getItemIndex((TypeSection<?, ?, TypeRef>) reference);
            case 2:
                return this.fieldSection.getItemIndex((FieldReference) reference);
            case 3:
                return this.methodSection.getItemIndex((MethodReference) reference);
            case 4:
                return this.protoSection.getItemIndex((MethodProtoReference) reference);
            case 5:
                return this.callSiteSection.getItemIndex((CallSiteReference) reference);
            case 6:
                return this.methodHandleSection.getItemIndex((MethodHandleReference) reference);
            default:
                throw new ExceptionWithContext("Unknown reference type: %d", Integer.valueOf(i));
        }
    }
}
