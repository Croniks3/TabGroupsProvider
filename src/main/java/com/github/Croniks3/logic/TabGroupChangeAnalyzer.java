package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.enums.ProjectFileChangeType;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.enums.TabGroupChangeType;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class TabGroupChangeAnalyzer {
    private final TabGroupCandidateMatcher candidateMatcher = new TabGroupCandidateMatcher();

    public @NotNull TabGroupChangeType analyze(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(change);
        Objects.requireNonNull(groupedExtensionsRule);

        ProjectFileChangeType changeType = change.getType();

        if (changeType == ProjectFileChangeType.CREATED) {
            String newFilePath = change.getNewFilePath();
            if (newFilePath == null || newFilePath.isBlank()) {
                return TabGroupChangeType.NONE;
            }

            return matchesGroup(group, newFilePath, groupedExtensionsRule)
                    ? TabGroupChangeType.LIGHT_UPDATE
                    : TabGroupChangeType.NONE;
        }

        if (changeType == ProjectFileChangeType.DELETED) {
            String oldFilePath = change.getOldFilePath();
            if (oldFilePath == null || oldFilePath.isBlank()) {
                return TabGroupChangeType.NONE;
            }

            if (isSourceFile(group, oldFilePath)) {
                return TabGroupChangeType.REMOVE_GROUP;
            }

            if (containsFile(group, oldFilePath)) {
                return TabGroupChangeType.LIGHT_UPDATE;
            }

            return TabGroupChangeType.NONE;
        }

        if (changeType == ProjectFileChangeType.RENAMED || changeType == ProjectFileChangeType.MOVED) {
            String oldFilePath = change.getOldFilePath();
            String newFilePath = change.getNewFilePath();
            if (oldFilePath == null || oldFilePath.isBlank() || newFilePath == null || newFilePath.isBlank()) {
                return TabGroupChangeType.NONE;
            }

            if (isSourceFile(group, oldFilePath)) {
                return TabGroupChangeType.HEAVY_REBUILD;
            }

            boolean oldFileWasInGroup = containsFile(group, oldFilePath);
            boolean newFileMatchesGroup = matchesGroup(group, newFilePath, groupedExtensionsRule);

            if (oldFileWasInGroup || newFileMatchesGroup) {
                return TabGroupChangeType.LIGHT_UPDATE;
            }

            return TabGroupChangeType.NONE;
        }

        return TabGroupChangeType.NONE;
    }

    private boolean matchesGroup(
            @NotNull TabGroup group,
            @NotNull String candidateFilePath,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        ProjectFileInfo sourceFile = ProjectFileInfo.tryCreate(group.getDefinition().getSourceFilePath());
        ProjectFileInfo candidateFile = ProjectFileInfo.tryCreate(candidateFilePath);

        if (sourceFile == null || candidateFile == null) {
            return false;
        }

        return candidateMatcher.matches(
                sourceFile,
                candidateFile,
                group.getDefinition(),
                groupedExtensionsRule
        );
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