package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
final class MapRetrievalCache<K, V> extends MapIteratorCache<K, V> {

    @CheckForNull
    private volatile transient CacheEntry<K, V> cacheEntry1;

    @CheckForNull
    private volatile transient CacheEntry<K, V> cacheEntry2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MapRetrievalCache(Map<K, V> backingMap) {
        super(backingMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.MapIteratorCache
    @CheckForNull
    public V get(Object obj) {
        Preconditions.checkNotNull(obj);
        V value = getIfCached(obj);
        if (value != null) {
            return value;
        }
        V value2 = getWithoutCaching(obj);
        if (value2 != null) {
            addToCache(obj, value2);
        }
        return value2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.graph.MapIteratorCache
    @CheckForNull
    public V getIfCached(@CheckForNull Object obj) {
        V v = (V) super.getIfCached(obj);
        if (v != null) {
            return v;
        }
        CacheEntry<K, V> cacheEntry = this.cacheEntry1;
        if (cacheEntry != null && cacheEntry.key == obj) {
            return cacheEntry.value;
        }
        CacheEntry<K, V> cacheEntry2 = this.cacheEntry2;
        if (cacheEntry2 != null && cacheEntry2.key == obj) {
            addToCache(cacheEntry2);
            return cacheEntry2.value;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.graph.MapIteratorCache
    public void clearCache() {
        super.clearCache();
        this.cacheEntry1 = null;
        this.cacheEntry2 = null;
    }

    private void addToCache(K key, V value) {
        addToCache(new CacheEntry<>(key, value));
    }

    private void addToCache(CacheEntry<K, V> entry) {
        this.cacheEntry2 = this.cacheEntry1;
        this.cacheEntry1 = entry;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class CacheEntry<K, V> {
        final K key;
        final V value;

        CacheEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
