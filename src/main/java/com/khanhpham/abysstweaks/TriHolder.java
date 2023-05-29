package com.khanhpham.abysstweaks;

public record  TriHolder<A, B, C>(A first, B second, C third) {
    public static <A, B, C> TriHolder<A, B, C> of(A first, B second, C third) {
        return new TriHolder<>(first, second, third);
    }
}
