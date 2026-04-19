package com.github.Croniks3.logic;

import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupLightUpdate;
import com.github.Croniks3.model.enums.TabGroupLightUpdateType;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TabGroupLightUpdater {

    private final TabGroupFilesLimiter filesLimiter = new TabGroupFilesLimiter();

    public @NotNull TabGroup apply(
            @NotNull TabGroup group,
            @NotNull TabGroupLightUpdate update,
            int maxFilesPerGroup
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(update);

        TabGroupLightUpdateType updateType = update.getType();

        if (updateType == TabGroupLightUpdateType.NONE) {
            return group;
        }

        if (updateType == TabGroupLightUpdateType.ADD_FILE) {
            String newFilePath = update.getNewFilePath();
            if (newFilePath == null || newFilePath.isBlank()) {
                return group;
            }
            if (containsPath(group.getFilePaths(), newFilePath)) {
                return group;
            }

            List<String> updatedFilePaths = new ArrayList<>(group.getFilePaths());
            updatedFilePaths.add(newFilePath);

            List<String> limitedFilePaths = filesLimiter.applyLimit(updatedFilePaths, maxFilesPerGroup);
            return new TabGroup(group.getId(), group.getDefinition(), limitedFilePaths);
        }

        if (updateType == TabGroupLightUpdateType.REMOVE_FILE) {
            String oldFilePath = update.getOldFilePath();
            if (oldFilePath == null || oldFilePath.isBlank()) {
                return group;
            }

            List<String> updatedFilePaths = new ArrayList<>();
            boolean changed = false;

            for (String filePath : group.getFilePaths()) {
                if (isSamePath(filePath, oldFilePath)) {
                    changed = true;
                    continue;
                }
                updatedFilePaths.add(filePath);
            }

            if (!changed) {
                return group;
            }

            return new TabGroup(group.getId(), group.getDefinition(), updatedFilePaths);
        }

        if (updateType == TabGroupLightUpdateType.REPLACE_FILE_PATH) {
            String oldFilePath = update.getOldFilePath();
            String newFilePath = update.getNewFilePath();

            if (oldFilePath == null || oldFilePath.isBlank() ||
                    newFilePath == null || newFilePath.isBlank()) {
                return group;
            }

            List<String> updatedFilePaths = new ArrayList<>();
            boolean replaced = false;

            for (String filePath : group.getFilePaths()) {
                if (isSamePath(filePath, oldFilePath)) {
                    replaced = true;
                    if (!containsPath(updatedFilePaths, newFilePath)) {
                        updatedFilePaths.add(newFilePath);
                    }
                    continue;
                }

                if (!containsPath(updatedFilePaths, filePath)) {
                    updatedFilePaths.add(filePath);
                }
            }

            if (!replaced) {
                return group;
            }

            return new TabGroup(group.getId(), group.getDefinition(), updatedFilePaths);
        }

        return group;
    }

    private boolean containsPath(@NotNull List<String> filePaths, @NotNull String targetPath) {
        for (String filePath : filePaths) {
            if (isSamePath(filePath, targetPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSamePath(@NotNull String firstPath, @NotNull String secondPath) {
        return Path.of(firstPath).normalize().equals(Path.of(secondPath).normalize());
    }
}