package com.predicate;

@FunctionalInterface
public interface Predicate<T> {
    public boolean test(T t);

    public default Predicate<T> and(Predicate<T> other) {
        return t -> this.test(t) && other.test(t);
    }

    public default Predicate<T> or(Predicate<T> other) {
        return t -> this.test(t) || other.test(t);
    }

    public static <T> Predicate<T> isEqualsTo(T value) {
        return t -> t.equals(value);
    }
}