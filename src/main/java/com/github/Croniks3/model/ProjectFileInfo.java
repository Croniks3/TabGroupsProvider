package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Objects;

public final class ProjectFileInfo {
    private final String filePath;
    private final String fileName;
    private final TabGroupKey key;

    public ProjectFileInfo(
            @NotNull String filePath,
            @NotNull String fileName,
            @NotNull TabGroupKey key
    ) {
        this.filePath = Objects.requireNonNull(filePath);
        this.fileName = Objects.requireNonNull(fileName);
        this.key = Objects.requireNonNull(key);
    }

    public @NotNull String getFilePath() {
        return filePath;
    }

    public @NotNull String getFileName() {
        return fileName;
    }

    public @NotNull String getBaseName() {
        return key.getBaseName();
    }

    public @NotNull String getExtension() {
        return key.getExtension();
    }

    public @NotNull TabGroupKey getKey() {
        return key;
    }

    public static @Nullable ProjectFileInfo tryCreate(@NotNull String filePath) {
        Objects.requireNonNull(filePath);

        Path path = Path.of(filePath);
        Path fileNamePath = path.getFileName();
        if (fileNamePath == null) {
            return null;
        }

        String fileName = fileNamePath.toString();
        TabGroupKey key = TabGroupKey.tryCreate(fileName);
        if (key == null) {
            return null;
        }

        return new ProjectFileInfo(filePath, fileName, key);
    }
}