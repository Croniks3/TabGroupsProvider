package com.github.Croniks3.logic;

import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileChangeType;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class TabGroupUpdatePolicy {

    public @NotNull TabGroupUpdateAction decide(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(change);

        ProjectFileChangeType changeType = change.getType();

        if (changeType == ProjectFileChangeType.CREATED) {
            return TabGroupUpdateAction.REBUILD;
        }

        if (changeType == ProjectFileChangeType.DELETED) {
            String oldFilePath = change.getOldFilePath();
            if (oldFilePath == null || oldFilePath.isBlank()) {
                return TabGroupUpdateAction.NONE;
            }

            if (isSourceFile(group, oldFilePath)) {
                return TabGroupUpdateAction.REMOVE_GROUP;
            }

            if (containsFile(group, oldFilePath)) {
                return TabGroupUpdateAction.REBUILD;
            }

            return TabGroupUpdateAction.NONE;
        }

        if (changeType == ProjectFileChangeType.MOVED || changeType == ProjectFileChangeType.RENAMED) {
            return TabGroupUpdateAction.REBUILD;
        }

        return TabGroupUpdateAction.NONE;
    }

    private boolean isSourceFile(@NotNull TabGroup group, @NotNull String filePath) {
        return normalize(group.getDefinition().getSourceFilePath())
                .equals(normalize(filePath));
    }

    private boolean containsFile(@NotNull TabGroup group, @NotNull String filePath) {
        List<String> filePaths = group.getFilePaths();
        Path normalizedTargetPath = normalize(filePath);

        for (String groupFilePath : filePaths) {
            if (normalize(groupFilePath).equals(normalizedTargetPath)) {
                return true;
            }
        }

        return false;
    }

    private @NotNull Path normalize(@NotNull String filePath) {
        return Path.of(filePath).normalize();
    }
}