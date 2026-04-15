package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroupDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TabGroupFilesCollector {
    private final TabGroupCandidateMatcher candidateMatcher = new TabGroupCandidateMatcher();

    public @NotNull List<ProjectFileInfo> collectMatchedFiles(
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull TabGroupDefinition definition,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(definition);
        Objects.requireNonNull(groupedExtensionsRule);

        ProjectFileInfo sourceFile = findSourceFile(projectFiles, definition.getSourceFilePath());
        if (sourceFile == null) {
            return List.of();
        }

        List<ProjectFileInfo> result = new ArrayList<>();

        for (ProjectFileInfo candidateFile : projectFiles) {
            if (candidateMatcher.matches(sourceFile, candidateFile, definition, groupedExtensionsRule)) {
                result.add(candidateFile);
            }
        }

        return List.copyOf(result);
    }

    private @Nullable ProjectFileInfo findSourceFile(
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull String sourceFilePath
    ) {
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(sourceFilePath);

        Path normalizedSourcePath = Path.of(sourceFilePath).normalize();

        for (ProjectFileInfo projectFile : projectFiles) {
            Path normalizedProjectFilePath = Path.of(projectFile.getFilePath()).normalize();
            if (normalizedProjectFilePath.equals(normalizedSourcePath)) {
                return projectFile;
            }
        }

        return null;
    }
}