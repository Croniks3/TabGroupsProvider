package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public final class TabGroupKey {
    private final String baseName;
    private final String extension;

    public TabGroupKey(@NotNull String baseName, @NotNull String extension) {
        this.baseName = Objects.requireNonNull(baseName);
        this.extension = Objects.requireNonNull(extension);
    }

    public @NotNull String getBaseName() {
        return baseName;
    }

    public @NotNull String getExtension() {
        return extension;
    }

    public static TabGroupKey tryCreate(@NotNull String fileName) {
        Objects.requireNonNull(fileName);

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex <= 0 || lastDotIndex == fileName.length() - 1) {
            return null;
        }

        String baseName = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex + 1).toLowerCase(Locale.ROOT);

        return new TabGroupKey(baseName, extension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabGroupKey that)) return false;
        return baseName.equals(that.baseName) && extension.equals(that.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseName, extension);
    }

    @Override
    public String toString() {
        return "TabGroupKey{" +
                "baseName='" + baseName + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}