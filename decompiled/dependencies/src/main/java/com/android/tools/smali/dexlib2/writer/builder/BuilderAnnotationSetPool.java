package com.android.tools.smali.dexlib2.writer.builder;

import com.android.tools.smali.dexlib2.iface.Annotation;
import com.android.tools.smali.dexlib2.writer.AnnotationSetSection;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BuilderAnnotationSetPool extends BaseBuilderPool implements AnnotationSetSection<BuilderAnnotation, BuilderAnnotationSet> {

    @Nonnull
    private final ConcurrentMap<Set<? extends Annotation>, BuilderAnnotationSet> internedItems;

    public BuilderAnnotationSetPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderAnnotationSet internAnnotationSet(@Nullable Set<? extends Annotation> annotations) {
        if (annotations == null) {
            return BuilderAnnotationSet.EMPTY;
        }
        BuilderAnnotationSet ret = this.internedItems.get(annotations);
        if (ret != null) {
            return ret;
        }
        BuilderAnnotationSet annotationSet = new BuilderAnnotationSet(ImmutableSet.copyOf(Iterators.transform(annotations.iterator(), new Function<Annotation, BuilderAnnotation>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderAnnotationSetPool.1
            @Override // com.google.common.base.Function
            @Nullable
            public BuilderAnnotation apply(Annotation input) {
                return ((BuilderAnnotationPool) BuilderAnnotationSetPool.this.dexBuilder.annotationSection).internAnnotation(input);
            }
        })));
        BuilderAnnotationSet ret2 = this.internedItems.putIfAbsent(annotationSet, annotationSet);
        return ret2 == null ? annotationSet : ret2;
    }

    @Override // com.android.tools.smali.dexlib2.writer.AnnotationSetSection
    @Nonnull
    public Collection<? extends BuilderAnnotation> getAnnotations(@Nonnull BuilderAnnotationSet key) {
        return key.annotations;
    }

    @Override // com.android.tools.smali.dexlib2.writer.NullableOffsetSection
    public int getNullableItemOffset(@Nullable BuilderAnnotationSet key) {
        if (key == null) {
            return 0;
        }
        return key.offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    public int getItemOffset(@Nonnull BuilderAnnotationSet key) {
        return key.offset;
    }

    @Override // com.android.tools.smali.dexlib2.writer.OffsetSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderAnnotationSet, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderAnnotationSet>(this.internedItems.values()) { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderAnnotationSetPool.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderAnnotationSet key) {
                return key.offset;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderAnnotationSet key, int value) {
                int prev = key.offset;
                key.offset = value;
                return prev;
            }
        };
    }
}
