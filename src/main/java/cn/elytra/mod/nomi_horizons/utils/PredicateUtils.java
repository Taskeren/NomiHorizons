package cn.elytra.mod.nomi_horizons.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class PredicateUtils {

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>) ALWAYS_FALSE;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>) ALWAYS_TRUE;
    }

    private static final Predicate<Object> ALWAYS_FALSE = new Predicate<>() {
        @Override
        public boolean test(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            Objects.requireNonNull(other);
            return other;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return ALWAYS_TRUE;
        }

        @Override
        public String toString() {
            return "PredicateAlwaysFalse";
        }

        @Override
        public int hashCode() {
            return 0;
        }
    };

    private static final Predicate<Object> ALWAYS_TRUE = new Predicate<>() {
        @Override
        public boolean test(Object o) {
            return true;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            Objects.requireNonNull(other);
            return other;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return ALWAYS_FALSE;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @Override
        public String toString() {
            return "PredicateAlwaysTrue";
        }

        @Override
        public int hashCode() {
            return 1;
        }
    };
}
