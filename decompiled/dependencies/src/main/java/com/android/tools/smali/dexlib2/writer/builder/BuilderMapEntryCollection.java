package com.android.tools.smali.dexlib2.writer.builder;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public abstract class BuilderMapEntryCollection<Key> extends AbstractCollection<Map.Entry<Key, Integer>> {

    @Nonnull
    private final Collection<Key> keys;

    protected abstract int getValue(@Nonnull Key key);

    protected abstract int setValue(@Nonnull Key key, int i);

    public BuilderMapEntryCollection(@Nonnull Collection<Key> keys) {
        this.keys = keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MapEntry implements Map.Entry<Key, Integer> {

        @Nonnull
        private Key key;

        private MapEntry() {
        }

        @Override // java.util.Map.Entry
        @Nonnull
        public Key getKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public Integer getValue() {
            return Integer.valueOf(BuilderMapEntryCollection.this.getValue(this.key));
        }

        @Override // java.util.Map.Entry
        public Integer setValue(Integer value) {
            return Integer.valueOf(BuilderMapEntryCollection.this.setValue(this.key, value.intValue()));
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    @Nonnull
    public Iterator<Map.Entry<Key, Integer>> iterator() {
        final Iterator<Key> iter = this.keys.iterator();
        return new Iterator<Map.Entry<Key, Integer>>() { // from class: com.android.tools.smali.dexlib2.writer.builder.BuilderMapEntryCollection.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<Key, Integer> next() {
                BuilderMapEntryCollection<Key>.MapEntry entry = new MapEntry();
                ((MapEntry) entry).key = iter.next();
                return entry;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.keys.size();
    }
}
