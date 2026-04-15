package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TabGroupDirectoryRule implements TabGroupRule {
    private final String directoryPath;
    private final boolean includeSubdirectories;

    public TabGroupDirectoryRule(@NotNull String directoryPath, boolean includeSubdirectories) {
        this.directoryPath = Objects.requireNonNull(directoryPath);
        this.includeSubdirectories = includeSubdirectories;
    }

    public @NotNull String getDirectoryPath() {
        return directoryPath;
    }

    public boolean isIncludeSubdirectories() {
        return includeSubdirectories;
    }

    public boolean isActive() {
        return !directoryPath.isBlank();
    }
}
