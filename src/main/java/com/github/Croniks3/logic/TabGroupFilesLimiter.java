package com.github.Croniks3.logic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TabGroupFilesLimiter {

    public @NotNull List<String> applyLimit(
            @NotNull List<String> filePaths,
            int maxFilesPerGroup
    ) {
        Objects.requireNonNull(filePaths);

        if (maxFilesPerGroup <= 0) {
            return List.of();
        }

        if (filePaths.size() <= maxFilesPerGroup) {
            return List.copyOf(filePaths);
        }

        List<String> limitedFilePaths = new ArrayList<>(maxFilesPerGroup);
        for (int i = 0; i < filePaths.size() && i < maxFilesPerGroup; i++) {
            limitedFilePaths.add(filePaths.get(i));
        }

        return List.copyOf(limitedFilePaths);
    }
}