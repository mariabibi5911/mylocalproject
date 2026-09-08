package com.android.tools.smali.dexlib2.dexbacked.util;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedMethod;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedMethodImplementation;
import com.android.tools.smali.dexlib2.dexbacked.DexReader;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import com.android.tools.smali.dexlib2.iface.debug.LocalInfo;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableEndLocal;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableEpilogueBegin;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableLineNumber;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutablePrologueEnd;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableRestartLocal;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableSetSourceFile;
import com.android.tools.smali.dexlib2.immutable.debug.ImmutableStartLocal;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class DebugInfo implements Iterable<DebugItem> {
    @Nonnull
    public abstract Iterator<String> getParameterNames(@Nullable DexReader dexReader);

    public abstract int getSize();

    public static DebugInfo newOrEmpty(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
        if (debugInfoOffset == 0) {
            return EmptyDebugInfo.INSTANCE;
        }
        return new DebugInfoImpl(dexFile, debugInfoOffset, methodImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class EmptyDebugInfo extends DebugInfo {
        public static final EmptyDebugInfo INSTANCE = new EmptyDebugInfo();

        private EmptyDebugInfo() {
        }

        @Override // java.lang.Iterable
        @Nonnull
        public Iterator<DebugItem> iterator() {
            return ImmutableSet.of().iterator();
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo
        @Nonnull
        public Iterator<String> getParameterNames(@Nullable DexReader reader) {
            return ImmutableSet.of().iterator();
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo
        public int getSize() {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DebugInfoImpl extends DebugInfo {
        private static final LocalInfo EMPTY_LOCAL_INFO = new LocalInfo() { // from class: com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.1
            @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getName() {
                return null;
            }

            @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getType() {
                return null;
            }

            @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getSignature() {
                return null;
            }
        };
        private final int debugInfoOffset;

        @Nonnull
        public final DexBackedDexFile dexFile;

        @Nonnull
        private final DexBackedMethodImplementation methodImpl;

        public DebugInfoImpl(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
            this.dexFile = dexFile;
            this.debugInfoOffset = debugInfoOffset;
            this.methodImpl = methodImpl;
        }

        @Override // java.lang.Iterable
        @Nonnull
        public Iterator<DebugItem> iterator() {
            LocalInfo currentLocal;
            String type;
            DexReader reader = this.dexFile.getDataBuffer().readerAt(this.debugInfoOffset);
            int lineNumberStart = reader.readBigUleb128();
            int registerCount = this.methodImpl.getRegisterCount();
            int lastInstructionAddress = this.methodImpl.getInstructionsSize() - ((Instruction) Iterators.getLast(this.methodImpl.getInstructions().iterator())).getCodeUnits();
            LocalInfo[] locals = new LocalInfo[registerCount];
            Arrays.fill(locals, EMPTY_LOCAL_INFO);
            DexBackedMethod method = this.methodImpl.method;
            Iterator<? extends MethodParameter> parameterIterator = new ParameterIterator(method.getParameterTypes(), method.getParameterAnnotations(), getParameterNames(reader));
            int parameterIndex = 0;
            if (!AccessFlags.STATIC.isSet(this.methodImpl.method.getAccessFlags())) {
                int parameterIndex2 = 0 + 1;
                locals[0] = new LocalInfo() { // from class: com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.2
                    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
                    public String getName() {
                        return "this";
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
                    public String getType() {
                        return DebugInfoImpl.this.methodImpl.method.getDefiningClass();
                    }

                    @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
                    public String getSignature() {
                        return null;
                    }
                };
                parameterIndex = parameterIndex2;
            }
            while (parameterIterator.hasNext()) {
                locals[parameterIndex] = parameterIterator.next();
                parameterIndex++;
            }
            if (parameterIndex < registerCount) {
                int localIndex = registerCount - 1;
                while (true) {
                    parameterIndex--;
                    if (parameterIndex <= -1 || ((type = (currentLocal = locals[parameterIndex]).getType()) != null && ((type.equals("J") || type.equals("D")) && localIndex - 1 == parameterIndex))) {
                        break;
                    }
                    locals[localIndex] = currentLocal;
                    locals[parameterIndex] = EMPTY_LOCAL_INFO;
                    localIndex--;
                }
            }
            return new VariableSizeLookaheadIterator<DebugItem>(this.dexFile.getDataBuffer(), reader.getOffset(), lineNumberStart, lastInstructionAddress, locals) { // from class: com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.3
                private int codeAddress = 0;
                private int lineNumber;
                final /* synthetic */ int val$lastInstructionAddress;
                final /* synthetic */ int val$lineNumberStart;
                final /* synthetic */ LocalInfo[] val$locals;

                {
                    this.val$lineNumberStart = lineNumberStart;
                    this.val$lastInstructionAddress = lastInstructionAddress;
                    this.val$locals = locals;
                    this.lineNumber = lineNumberStart;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Removed duplicated region for block: B:30:0x0080  */
                /* JADX WARN: Removed duplicated region for block: B:43:0x00a0  */
                /* JADX WARN: Removed duplicated region for block: B:46:0x00bc  */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                @Nullable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public DebugItem readNextItem(@Nonnull DexReader reader2) {
                    LocalInfo localInfo;
                    LocalInfo localInfo2;
                    while (this.codeAddress <= this.val$lastInstructionAddress) {
                        int next = reader2.readUbyte();
                        switch (next) {
                            case 0:
                                return endOfData();
                            case 1:
                                int addressDiff = reader2.readSmallUleb128();
                                this.codeAddress += addressDiff;
                                break;
                            case 2:
                                int lineDiff = reader2.readSleb128();
                                this.lineNumber += lineDiff;
                                break;
                            case 3:
                                int register = reader2.readSmallUleb128();
                                String name = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                String type2 = DebugInfoImpl.this.dexFile.getTypeSection().getOptional(reader2.readSmallUleb128() - 1);
                                ImmutableStartLocal startLocal = new ImmutableStartLocal(this.codeAddress, register, name, type2, null);
                                if (register >= 0) {
                                    LocalInfo[] localInfoArr = this.val$locals;
                                    if (register < localInfoArr.length) {
                                        localInfoArr[register] = startLocal;
                                    }
                                }
                                return startLocal;
                            case 4:
                                int register2 = reader2.readSmallUleb128();
                                String name2 = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                String type3 = DebugInfoImpl.this.dexFile.getTypeSection().getOptional(reader2.readSmallUleb128() - 1);
                                String signature = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                ImmutableStartLocal startLocal2 = new ImmutableStartLocal(this.codeAddress, register2, name2, type3, signature);
                                if (register2 >= 0) {
                                    LocalInfo[] localInfoArr2 = this.val$locals;
                                    if (register2 < localInfoArr2.length) {
                                        localInfoArr2[register2] = startLocal2;
                                    }
                                }
                                return startLocal2;
                            case 5:
                                int register3 = reader2.readSmallUleb128();
                                boolean replaceLocalInTable = true;
                                if (register3 >= 0) {
                                    LocalInfo[] localInfoArr3 = this.val$locals;
                                    if (register3 < localInfoArr3.length) {
                                        localInfo = localInfoArr3[register3];
                                        if (localInfo instanceof EndLocal) {
                                            localInfo = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                            replaceLocalInTable = false;
                                        }
                                        ImmutableEndLocal endLocal = new ImmutableEndLocal(this.codeAddress, register3, localInfo.getName(), localInfo.getType(), localInfo.getSignature());
                                        if (replaceLocalInTable) {
                                            this.val$locals[register3] = endLocal;
                                        }
                                        return endLocal;
                                    }
                                }
                                localInfo = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                replaceLocalInTable = false;
                                if (localInfo instanceof EndLocal) {
                                }
                                ImmutableEndLocal endLocal2 = new ImmutableEndLocal(this.codeAddress, register3, localInfo.getName(), localInfo.getType(), localInfo.getSignature());
                                if (replaceLocalInTable) {
                                }
                                return endLocal2;
                            case 6:
                                int register4 = reader2.readSmallUleb128();
                                if (register4 >= 0) {
                                    LocalInfo[] localInfoArr4 = this.val$locals;
                                    if (register4 < localInfoArr4.length) {
                                        localInfo2 = localInfoArr4[register4];
                                        ImmutableRestartLocal restartLocal = new ImmutableRestartLocal(this.codeAddress, register4, localInfo2.getName(), localInfo2.getType(), localInfo2.getSignature());
                                        if (register4 >= 0) {
                                            LocalInfo[] localInfoArr5 = this.val$locals;
                                            if (register4 < localInfoArr5.length) {
                                                localInfoArr5[register4] = restartLocal;
                                            }
                                        }
                                        return restartLocal;
                                    }
                                }
                                localInfo2 = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                ImmutableRestartLocal restartLocal2 = new ImmutableRestartLocal(this.codeAddress, register4, localInfo2.getName(), localInfo2.getType(), localInfo2.getSignature());
                                if (register4 >= 0) {
                                }
                                return restartLocal2;
                            case 7:
                                return new ImmutablePrologueEnd(this.codeAddress);
                            case 8:
                                return new ImmutableEpilogueBegin(this.codeAddress);
                            case 9:
                                String sourceFile = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                return new ImmutableSetSourceFile(this.codeAddress, sourceFile);
                            default:
                                int adjusted = next - 10;
                                int i = this.codeAddress + (adjusted / 15);
                                this.codeAddress = i;
                                this.lineNumber += (adjusted % 15) - 4;
                                if (i > this.val$lastInstructionAddress) {
                                    return endOfData();
                                }
                                return new ImmutableLineNumber(this.codeAddress, this.lineNumber);
                        }
                    }
                    return endOfData();
                }
            };
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo
        @Nonnull
        public VariableSizeIterator<String> getParameterNames(@Nullable DexReader reader) {
            if (reader == null) {
                reader = this.dexFile.getDataBuffer().readerAt(this.debugInfoOffset);
                reader.skipUleb128();
            }
            int parameterNameCount = reader.readSmallUleb128();
            return new VariableSizeIterator<String>(reader, parameterNameCount) { // from class: com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.4
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.android.tools.smali.dexlib2.dexbacked.util.VariableSizeIterator
                public String readNextItem(@Nonnull DexReader reader2, int index) {
                    return DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                }
            };
        }

        @Override // com.android.tools.smali.dexlib2.dexbacked.util.DebugInfo
        public int getSize() {
            Iterator<DebugItem> iter = iterator();
            while (iter.hasNext()) {
                iter.next();
            }
            return ((VariableSizeLookaheadIterator) iter).getReaderOffset() - this.debugInfoOffset;
        }
    }
}
