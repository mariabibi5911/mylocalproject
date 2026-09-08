package com.google.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes.dex */
public final class Predicates {
    private Predicates() {
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }

    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return new NotPredicate(predicate);
    }

    public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate(defensiveCopy(components));
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T>... components) {
        return new AndPredicate(defensiveCopy(components));
    }

    public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
        return new AndPredicate(asList((Predicate) Preconditions.checkNotNull(first), (Predicate) Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate(defensiveCopy(components));
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<? super T>... components) {
        return new OrPredicate(defensiveCopy(components));
    }

    public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
        return new OrPredicate(asList((Predicate) Preconditions.checkNotNull(first), (Predicate) Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> equalTo(@ParametricNullness T target) {
        if (target == null) {
            return isNull();
        }
        return new IsEqualToPredicate(target).withNarrowedType();
    }

    public static <T> Predicate<T> instanceOf(Class<?> clazz) {
        return new InstanceOfPredicate(clazz);
    }

    public static Predicate<Class<?>> subtypeOf(Class<?> clazz) {
        return new SubtypeOfPredicate(clazz);
    }

    public static <T> Predicate<T> in(Collection<? extends T> target) {
        return new InPredicate(target);
    }

    public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
        return new CompositionPredicate(predicate, function);
    }

    public static Predicate<CharSequence> containsPattern(String pattern) {
        return new ContainsPatternFromStringPredicate(pattern);
    }

    public static Predicate<CharSequence> contains(Pattern pattern) {
        return new ContainsPatternPredicate(new JdkPattern(pattern));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum ObjectPredicate implements Predicate<Object> {
        ALWAYS_TRUE { // from class: com.google.common.base.Predicates.ObjectPredicate.1
            @Override // com.google.common.base.Predicate
            public boolean apply(@CheckForNull Object o) {
                return true;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        },
        ALWAYS_FALSE { // from class: com.google.common.base.Predicates.ObjectPredicate.2
            @Override // com.google.common.base.Predicate
            public boolean apply(@CheckForNull Object o) {
                return false;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        },
        IS_NULL { // from class: com.google.common.base.Predicates.ObjectPredicate.3
            @Override // com.google.common.base.Predicate
            public boolean apply(@CheckForNull Object o) {
                return o == null;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.isNull()";
            }
        },
        NOT_NULL { // from class: com.google.common.base.Predicates.ObjectPredicate.4
            @Override // com.google.common.base.Predicate
            public boolean apply(@CheckForNull Object o) {
                return o != null;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "Predicates.notNull()";
            }
        };

        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }

    /* loaded from: classes.dex */
    private static class NotPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Predicate<T> predicate;

        NotPredicate(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T t) {
            return !this.predicate.apply(t);
        }

        public int hashCode() {
            return ~this.predicate.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof NotPredicate) {
                NotPredicate<?> that = (NotPredicate) obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }

        public String toString() {
            String valueOf = String.valueOf(this.predicate);
            return new StringBuilder(String.valueOf(valueOf).length() + 16).append("Predicates.not(").append(valueOf).append(")").toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AndPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final List<? extends Predicate<? super T>> components;

        private AndPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T t) {
            for (int i = 0; i < this.components.size(); i++) {
                if (!this.components.get(i).apply(t)) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof AndPredicate) {
                AndPredicate<?> that = (AndPredicate) obj;
                return this.components.equals(that.components);
            }
            return false;
        }

        public String toString() {
            return Predicates.toStringHelper("and", this.components);
        }
    }

    /* loaded from: classes.dex */
    private static class OrPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final List<? extends Predicate<? super T>> components;

        private OrPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T t) {
            for (int i = 0; i < this.components.size(); i++) {
                if (this.components.get(i).apply(t)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof OrPredicate) {
                OrPredicate<?> that = (OrPredicate) obj;
                return this.components.equals(that.components);
            }
            return false;
        }

        public String toString() {
            return Predicates.toStringHelper("or", this.components);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String toStringHelper(String methodName, Iterable<?> components) {
        StringBuilder builder = new StringBuilder("Predicates.").append(methodName).append('(');
        boolean first = true;
        for (Object o : components) {
            if (!first) {
                builder.append(',');
            }
            builder.append(o);
            first = false;
        }
        return builder.append(')').toString();
    }

    /* loaded from: classes.dex */
    private static class IsEqualToPredicate implements Predicate<Object>, Serializable {
        private static final long serialVersionUID = 0;
        private final Object target;

        private IsEqualToPredicate(Object target) {
            this.target = target;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@CheckForNull Object o) {
            return this.target.equals(o);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                IsEqualToPredicate that = (IsEqualToPredicate) obj;
                return this.target.equals(that.target);
            }
            return false;
        }

        public String toString() {
            String valueOf = String.valueOf(this.target);
            return new StringBuilder(String.valueOf(valueOf).length() + 20).append("Predicates.equalTo(").append(valueOf).append(")").toString();
        }

        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InstanceOfPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<?> clazz;

        private InstanceOfPredicate(Class<?> clazz) {
            this.clazz = (Class) Preconditions.checkNotNull(clazz);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T o) {
            return this.clazz.isInstance(o);
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof InstanceOfPredicate)) {
                return false;
            }
            InstanceOfPredicate<?> that = (InstanceOfPredicate) obj;
            return this.clazz == that.clazz;
        }

        public String toString() {
            String name = this.clazz.getName();
            return new StringBuilder(String.valueOf(name).length() + 23).append("Predicates.instanceOf(").append(name).append(")").toString();
        }
    }

    /* loaded from: classes.dex */
    private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<?> clazz;

        private SubtypeOfPredicate(Class<?> clazz) {
            this.clazz = (Class) Preconditions.checkNotNull(clazz);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(Class<?> input) {
            return this.clazz.isAssignableFrom(input);
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof SubtypeOfPredicate)) {
                return false;
            }
            SubtypeOfPredicate that = (SubtypeOfPredicate) obj;
            return this.clazz == that.clazz;
        }

        public String toString() {
            String name = this.clazz.getName();
            return new StringBuilder(String.valueOf(name).length() + 22).append("Predicates.subtypeOf(").append(name).append(")").toString();
        }
    }

    /* loaded from: classes.dex */
    private static class InPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Collection<?> target;

        private InPredicate(Collection<?> target) {
            this.target = (Collection) Preconditions.checkNotNull(target);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T t) {
            try {
                return this.target.contains(t);
            } catch (ClassCastException | NullPointerException e) {
                return false;
            }
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (obj instanceof InPredicate) {
                InPredicate<?> that = (InPredicate) obj;
                return this.target.equals(that.target);
            }
            return false;
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.target);
            return new StringBuilder(String.valueOf(valueOf).length() + 15).append("Predicates.in(").append(valueOf).append(")").toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
        private static final long serialVersionUID = 0;
        final Function<A, ? extends B> f;
        final Predicate<B> p;

        private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
            this.p = (Predicate) Preconditions.checkNotNull(p);
            this.f = (Function) Preconditions.checkNotNull(f);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness A a) {
            return this.p.apply(this.f.apply(a));
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof CompositionPredicate)) {
                return false;
            }
            CompositionPredicate<?, ?> that = (CompositionPredicate) obj;
            return this.f.equals(that.f) && this.p.equals(that.p);
        }

        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }

        public String toString() {
            String valueOf = String.valueOf(this.p);
            String valueOf2 = String.valueOf(this.f);
            return new StringBuilder(String.valueOf(valueOf).length() + 2 + String.valueOf(valueOf2).length()).append(valueOf).append("(").append(valueOf2).append(")").toString();
        }
    }

    /* loaded from: classes.dex */
    private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable {
        private static final long serialVersionUID = 0;
        final CommonPattern pattern;

        ContainsPatternPredicate(CommonPattern pattern) {
            this.pattern = (CommonPattern) Preconditions.checkNotNull(pattern);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(CharSequence t) {
            return this.pattern.matcher(t).find();
        }

        public int hashCode() {
            return Objects.hashCode(this.pattern.pattern(), Integer.valueOf(this.pattern.flags()));
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object obj) {
            if (!(obj instanceof ContainsPatternPredicate)) {
                return false;
            }
            ContainsPatternPredicate that = (ContainsPatternPredicate) obj;
            return Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern.flags() == that.pattern.flags();
        }

        public String toString() {
            String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
            return new StringBuilder(String.valueOf(patternString).length() + 21).append("Predicates.contains(").append(patternString).append(")").toString();
        }
    }

    /* loaded from: classes.dex */
    private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate {
        private static final long serialVersionUID = 0;

        ContainsPatternFromStringPredicate(String string) {
            super(Platform.compilePattern(string));
        }

        @Override // com.google.common.base.Predicates.ContainsPatternPredicate
        public String toString() {
            String pattern = this.pattern.pattern();
            return new StringBuilder(String.valueOf(pattern).length() + 28).append("Predicates.containsPattern(").append(pattern).append(")").toString();
        }
    }

    private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
        return Arrays.asList(first, second);
    }

    private static <T> List<T> defensiveCopy(T... array) {
        return defensiveCopy(Arrays.asList(array));
    }

    static <T> List<T> defensiveCopy(Iterable<T> iterable) {
        ArrayList arrayList = new ArrayList();
        for (T element : iterable) {
            arrayList.add(Preconditions.checkNotNull(element));
        }
        return arrayList;
    }
}
