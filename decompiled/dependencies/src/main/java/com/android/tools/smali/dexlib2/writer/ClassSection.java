package com.android.tools.smali.dexlib2.writer;

import com.android.tools.smali.dexlib2.HiddenApiRestriction;
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation;
import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import java.io.IOException;
import java.lang.CharSequence;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ClassSection<StringKey extends CharSequence, TypeKey extends CharSequence, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedArrayKey> extends IndexSection<ClassKey> {
    int getAccessFlags(@Nonnull ClassKey classkey);

    int getAnnotationDirectoryOffset(@Nonnull ClassKey classkey);

    int getAnnotationSetRefListOffset(@Nonnull MethodKey methodkey);

    @Nullable
    AnnotationSetKey getClassAnnotations(@Nonnull ClassKey classkey);

    @Nullable
    Map.Entry<? extends ClassKey, Integer> getClassEntryByType(@Nullable TypeKey typekey);

    int getCodeItemOffset(@Nonnull MethodKey methodkey);

    @Nullable
    Iterable<? extends DebugItem> getDebugItems(@Nonnull MethodKey methodkey);

    @Nullable
    TypeKey getExceptionType(@Nonnull ExceptionHandler exceptionHandler);

    int getFieldAccessFlags(@Nonnull FieldKey fieldkey);

    @Nullable
    AnnotationSetKey getFieldAnnotations(@Nonnull FieldKey fieldkey);

    @Nonnull
    Set<HiddenApiRestriction> getFieldHiddenApiRestrictions(@Nonnull FieldKey fieldkey);

    @Nullable
    Iterable<? extends Instruction> getInstructions(@Nonnull MethodKey methodkey);

    @Nullable
    TypeListKey getInterfaces(@Nonnull ClassKey classkey);

    int getMethodAccessFlags(@Nonnull MethodKey methodkey);

    @Nullable
    AnnotationSetKey getMethodAnnotations(@Nonnull MethodKey methodkey);

    @Nonnull
    Set<HiddenApiRestriction> getMethodHiddenApiRestrictions(@Nonnull MethodKey methodkey);

    @Nullable
    List<? extends AnnotationSetKey> getParameterAnnotations(@Nonnull MethodKey methodkey);

    @Nullable
    Iterable<? extends StringKey> getParameterNames(@Nonnull MethodKey methodkey);

    int getRegisterCount(@Nonnull MethodKey methodkey);

    @Nonnull
    Collection<? extends ClassKey> getSortedClasses();

    @Nonnull
    Collection<? extends MethodKey> getSortedDirectMethods(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedInstanceFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends MethodKey> getSortedMethods(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedStaticFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends MethodKey> getSortedVirtualMethods(@Nonnull ClassKey classkey);

    @Nullable
    StringKey getSourceFile(@Nonnull ClassKey classkey);

    @Nullable
    EncodedArrayKey getStaticInitializers(@Nonnull ClassKey classkey);

    @Nullable
    TypeKey getSuperclass(@Nonnull ClassKey classkey);

    @Nonnull
    List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull MethodKey methodkey);

    @Nonnull
    TypeKey getType(@Nonnull ClassKey classkey);

    @Nonnull
    MutableMethodImplementation makeMutableMethodImplementation(@Nonnull MethodKey methodkey);

    void setAnnotationDirectoryOffset(@Nonnull ClassKey classkey, int i);

    void setAnnotationSetRefListOffset(@Nonnull MethodKey methodkey, int i);

    void setCodeItemOffset(@Nonnull MethodKey methodkey, int i);

    void writeDebugItem(@Nonnull DebugWriter<StringKey, TypeKey> debugWriter, DebugItem debugItem) throws IOException;
}
