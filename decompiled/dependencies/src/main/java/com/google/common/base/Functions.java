package com.google.common.base;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class Functions {
    private Functions() {
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }

    /* loaded from: classes.dex */
    private enum ToStringFunction implements Function<Object, String> {
        INSTANCE;

        @Override // com.google.common.base.Function
        public String apply(Object o) {
            Preconditions.checkNotNull(o);
            return o.toString();
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Functions.toStringFunction()";
        }
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    /* loaded from: classes.dex */
    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        @Override // com.google.common.base.Function
        @CheckForNull
        public Object apply(@CheckForNull Object o) {
            return o;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Functions.identity()";
        }
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
        return new ForMapWithDefault(map, defaultValue);
    }

    /* loaded from: classes.dex */
    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public V apply(@ParametricNullness K k) {
            V v = this.map.get(k);
            Preconditions.checkArgument(v != null || this.map.containsKey(k), "Key '%s' not present in map", k);
            return (V) NullnessCasts.uncheckedCastNullableTToT(v);
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object o) {
            if (o instanceof FunctionForMapNoDefault) {
                FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault) o;
                return this.map.equals(that.map);
            }
            return false;
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.map);
            return new StringBuilder(String.valueOf(valueOf).length() + 18).append("Functions.forMap(").append(valueOf).append(")").toString();
        }
    }

    /* loaded from: classes.dex */
    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
            this.map = (Map) Preconditions.checkNotNull(map);
            this.defaultValue = defaultValue;
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public V apply(@ParametricNullness K k) {
            V v = this.map.get(k);
            if (v != null || this.map.containsKey(k)) {
                return (V) NullnessCasts.uncheckedCastNullableTToT(v);
            }
            return this.defaultValue;
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object o) {
            if (!(o instanceof ForMapWithDefault)) {
                return false;
            }
            ForMapWithDefault<?, ?> that = (ForMapWithDefault) o;
            return this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue);
        }

        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }

        public String toString() {
            String valueOf = String.valueOf(this.map);
            String valueOf2 = String.valueOf(this.defaultValue);
            return new StringBuilder(String.valueOf(valueOf).length() + 33 + String.valueOf(valueOf2).length()).append("Functions.forMap(").append(valueOf).append(", defaultValue=").append(valueOf2).append(")").toString();
        }
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
        return new FunctionComposition(g, f);
    }

    /* loaded from: classes.dex */
    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;
        private final Function<A, ? extends B> f;
        private final Function<B, C> g;

        public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
            this.g = (Function) Preconditions.checkNotNull(g);
            this.f = (Function) Preconditions.checkNotNull(f);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public C apply(@ParametricNullness A a) {
            return (C) this.g.apply(this.f.apply(a));
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof FunctionComposition)) {
                return false;
            }
            FunctionComposition<?, ?, ?> that = (FunctionComposition) obj;
            return this.f.equals(that.f) && this.g.equals(that.g);
        }

        public int hashCode() {
            return this.f.hashCode() ^ this.g.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.g);
            String valueOf2 = String.valueOf(this.f);
            return new StringBuilder(String.valueOf(valueOf).length() + 2 + String.valueOf(valueOf2).length()).append(valueOf).append("(").append(valueOf2).append(")").toString();
        }
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction(predicate);
    }

    /* loaded from: classes.dex */
    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.base.Function
        public /* bridge */ /* synthetic */ Boolean apply(@ParametricNullness Object obj) {
            return apply((PredicateFunction<T>) obj);
        }

        private PredicateFunction(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Function
        public Boolean apply(@ParametricNullness T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof PredicateFunction) {
                PredicateFunction<?> that = (PredicateFunction) obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.predicate);
            return new StringBuilder(String.valueOf(valueOf).length() + 24).append("Functions.forPredicate(").append(valueOf).append(")").toString();
        }
    }

    public static <E> Function<Object, E> constant(@ParametricNullness E value) {
        return new ConstantFunction(value);
    }

    /* loaded from: classes.dex */
    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;

        @ParametricNullness
        private final E value;

        public ConstantFunction(@ParametricNullness E value) {
            this.value = value;
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public E apply(@CheckForNull Object from) {
            return this.value;
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof ConstantFunction) {
                ConstantFunction<?> that = (ConstantFunction) obj;
                return Objects.equal(this.value, that.value);
            }
            return false;
        }

        public int hashCode() {
            E e = this.value;
            if (e == null) {
                return 0;
            }
            return e.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.value);
            return new StringBuilder(String.valueOf(valueOf).length() + 20).append("Functions.constant(").append(valueOf).append(")").toString();
        }
    }

    public static <F, T> Function<F, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction(supplier);
    }

    /* loaded from: classes.dex */
    private static class SupplierFunction<F, T> implements Function<F, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.base.Function
        @ParametricNullness
        public T apply(@ParametricNullness F input) {
            return this.supplier.get();
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof SupplierFunction) {
                SupplierFunction<?, ?> that = (SupplierFunction) obj;
                return this.supplier.equals(that.supplier);
            }
            return false;
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.supplier);
            return new StringBuilder(String.valueOf(valueOf).length() + 23).append("Functions.forSupplier(").append(valueOf).append(")").toString();
        }
    }
}
