package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class TabGroupId {
    private final String value;

    public TabGroupId(@NotNull String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static @NotNull TabGroupId create() {
        return new TabGroupId(UUID.randomUUID().toString());
    }

    public @NotNull String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabGroupId that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TabGroupId{" +
                "value='" + value + '\'' +
                '}';
    }
}