package com.android.tools.smali.dexlib2.util;

import androidx.core.view.InputDeviceCompat;
import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.VerificationError;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.reference.CallSiteReference;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodProtoReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class Preconditions {
    public static void checkFormat(Opcode opcode, Format expectedFormat) {
        if (opcode.format != expectedFormat) {
            throw new IllegalArgumentException(String.format("Invalid opcode %s for %s", opcode.name, expectedFormat.name()));
        }
    }

    public static int checkNibbleRegister(int register) {
        if ((register & (-16)) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v15, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkByteRegister(int register) {
        if ((register & InputDeviceCompat.SOURCE_ANY) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v255, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkShortRegister(int register) {
        if (((-65536) & register) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v65535, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkNibbleLiteral(int literal) {
        if (literal < -8 || literal > 7) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -8 and 7, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkByteLiteral(int literal) {
        if (literal < -128 || literal > 127) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -128 and 127, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkShortLiteral(int literal) {
        if (literal < -32768 || literal > 32767) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -32768 and 32767, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkIntegerHatLiteral(int literal) {
        if ((65535 & literal) != 0) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 16 bits must be zeroed out.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static long checkLongHatLiteral(long literal) {
        if ((281474976710655L & literal) != 0) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 48 bits must be zeroed out.", Long.valueOf(literal)));
        }
        return literal;
    }

    public static int checkByteCodeOffset(int offset) {
        if (offset < -128 || offset > 127) {
            throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -128 and 127, inclusive.", Integer.valueOf(offset)));
        }
        return offset;
    }

    public static int checkShortCodeOffset(int offset) {
        if (offset < -32768 || offset > 32767) {
            throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -32768 and 32767, inclusive.", Integer.valueOf(offset)));
        }
        return offset;
    }

    public static int check35cAnd45ccRegisterCount(int registerCount) {
        if (registerCount < 0 || registerCount > 5) {
            throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 5, inclusive.", Integer.valueOf(registerCount)));
        }
        return registerCount;
    }

    public static int checkRegisterRangeCount(int registerCount) {
        if ((registerCount & InputDeviceCompat.SOURCE_ANY) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 255, inclusive.", Integer.valueOf(registerCount)));
        }
        return registerCount;
    }

    public static void checkValueArg(int valueArg, int maxValue) {
        if (valueArg > maxValue) {
            if (maxValue == 0) {
                throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0", Integer.valueOf(valueArg)));
            }
            throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0..%d, inclusive", Integer.valueOf(valueArg), Integer.valueOf(maxValue)));
        }
    }

    public static int checkFieldOffset(int fieldOffset) {
        if (fieldOffset < 0 || fieldOffset > 65535) {
            throw new IllegalArgumentException(String.format("Invalid field offset: 0x%x. Must be between 0x0000 and 0xFFFF inclusive", Integer.valueOf(fieldOffset)));
        }
        return fieldOffset;
    }

    public static int checkVtableIndex(int vtableIndex) {
        if (vtableIndex < 0 || vtableIndex > 65535) {
            throw new IllegalArgumentException(String.format("Invalid vtable index: %d. Must be between 0 and 65535, inclusive", Integer.valueOf(vtableIndex)));
        }
        return vtableIndex;
    }

    public static int checkInlineIndex(int inlineIndex) {
        if (inlineIndex < 0 || inlineIndex > 65535) {
            throw new IllegalArgumentException(String.format("Invalid inline index: %d. Must be between 0 and 65535, inclusive", Integer.valueOf(inlineIndex)));
        }
        return inlineIndex;
    }

    public static int checkVerificationError(int verificationError) {
        if (!VerificationError.isValidVerificationError(verificationError)) {
            throw new IllegalArgumentException(String.format("Invalid verification error value: %d. Must be between 1 and 9, inclusive", Integer.valueOf(verificationError)));
        }
        return verificationError;
    }

    public static <C extends Collection<? extends SwitchElement>> C checkSequentialOrderedKeys(C elements) {
        Integer previousKey = null;
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            SwitchElement element = (SwitchElement) it.next();
            int key = element.getKey();
            if (previousKey != null && previousKey.intValue() + 1 != key) {
                throw new IllegalArgumentException("SwitchElement set is not sequential and ordered");
            }
            previousKey = Integer.valueOf(key);
        }
        return elements;
    }

    public static int checkArrayPayloadElementWidth(int elementWidth) {
        switch (elementWidth) {
            case 1:
            case 2:
            case 4:
            case 8:
                return elementWidth;
            default:
                throw new IllegalArgumentException(String.format("Not a valid element width: %d", Integer.valueOf(elementWidth)));
        }
    }

    public static <L extends List<? extends Number>> L checkArrayPayloadElements(int elementWidth, L elements) {
        long maxValue;
        long minValue;
        if (elementWidth != 2) {
            long maxValue2 = (1 << ((elementWidth * 8) - 1)) - 1;
            maxValue = maxValue2;
            minValue = (-maxValue2) - 1;
        } else {
            maxValue = 65535;
            minValue = -32768;
        }
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            Number element = (Number) it.next();
            if (element.longValue() < minValue || element.longValue() > maxValue) {
                throw new IllegalArgumentException(String.format("%d does not fit into a %d-byte signed integer", Long.valueOf(element.longValue()), Integer.valueOf(elementWidth)));
            }
        }
        return elements;
    }

    public static <T extends Reference> T checkReference(int referenceType, T reference) {
        switch (referenceType) {
            case 0:
                if (!(reference instanceof StringReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a string reference");
                }
                return reference;
            case 1:
                if (!(reference instanceof TypeReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a type reference");
                }
                return reference;
            case 2:
                if (!(reference instanceof FieldReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a field reference");
                }
                return reference;
            case 3:
                if (!(reference instanceof MethodReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a method reference");
                }
                return reference;
            case 4:
                if (!(reference instanceof MethodProtoReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a method proto reference");
                }
                return reference;
            case 5:
                if (!(reference instanceof CallSiteReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a call site reference");
                }
                return reference;
            case 6:
                if (!(reference instanceof MethodHandleReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a method handle reference");
                }
                return reference;
            default:
                throw new IllegalArgumentException(String.format("Not a valid reference type: %d", Integer.valueOf(referenceType)));
        }
    }
}
