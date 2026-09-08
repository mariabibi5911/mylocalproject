package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodHandleReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.writer.MethodHandleSection;
import com.android.tools.smali.util.ExceptionWithContext;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class BuilderMethodHandlePool extends BaseBuilderPool implements MethodHandleSection<BuilderMethodHandleReference, BuilderFieldReference, BuilderMethodReference> {

    @Nonnull
    private final ConcurrentMap<MethodHandleReference, BuilderMethodHandleReference> internedItems;

    public BuilderMethodHandlePool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    public BuilderMethodHandleReference internMethodHandle(MethodHandleReference methodHandleReference) {
        BuilderReference memberReference;
        BuilderMethodHandleReference internedMethodHandle = this.internedItems.get(methodHandleReference);
        if (internedMethodHandle != null) {
            return internedMethodHandle;
        }
        switch (methodHandleReference.getMethodHandleType()) {
            case 0:
            case 1:
            case 2:
            case 3:
                memberReference = this.dexBuilder.internFieldReference((FieldReference) methodHandleReference.getMemberReference());
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                memberReference = this.dexBuilder.internMethodReference((MethodReference) methodHandleReference.getMemberReference());
                break;
            default:
                throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleReference.getMethodHandleType()));
        }
        BuilderMethodHandleReference internedMethodHandle2 = new BuilderMethodHandleReference(methodHandleReference.getMethodHandleType(), memberReference);
        BuilderMethodHandleReference prev = this.internedItems.putIfAbsent(internedMethodHandle2, internedMethodHandle2);
        return prev == null ? internedMethodHandle2 : prev;
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodHandleSection
    public BuilderFieldReference getFieldReference(BuilderMethodHandleReference methodHandleReference) {
        return (BuilderFieldReference) methodHandleReference.getMemberReference();
    }

    @Override // com.android.tools.smali.dexlib2.writer.MethodHandleSection
    public BuilderMethodReference getMethodReference(BuilderMethodHandleReference methodHandleReference) {
        return (BuilderMethodReference) methodHandleReference.getMemberReference();
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderMethodHandleReference builderMethodHandleReference) {
        return builderMethodHandleReference.index;
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderMethodHandleReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderMethodHandleReference>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderMethodHandlePool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderMethodHandleReference builderMethodHandleReference) {
                return builderMethodHandleReference.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderMethodHandleReference builderMethodHandleReference, int value) {
                int prev = builderMethodHandleReference.index;
                builderMethodHandleReference.index = value;
                return prev;
            }
        };
    }

    @Override // com.android.tools.smali.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }
}
