package com.comparator;

import java.util.function.Function;

@FunctionalInterface
public interface Comparator<T> {
    public int compare(T left, T right);

    public default Comparator<T> thenComparing(Comparator<T> cmp) {
        return (left, right) -> compare(left, right) == 0 ? cmp.compare(left, right) : compare(left, right);
    }

    public default Comparator<T> thenComparing(Function<T, Comparable> func) {
        return thenComparing(Comparator.comparing(func));
    }

    public static <T> Comparator<T> comparing(Function<T, Comparable> func) {
        return (left, right) -> func.apply(left).compareTo(func.apply(right));
    }
}