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

        Path normalizedSourcePath = Path.of(definition.getSourceFilePath()).normalize();

        ProjectFileInfo sourceFile = null;
        List<ProjectFileInfo> candidatesBeforeSource = new ArrayList<>();
        List<ProjectFileInfo> result = new ArrayList<>();

        for (ProjectFileInfo projectFile : projectFiles) {
            Path normalizedProjectFilePath = Path.of(projectFile.getFilePath()).normalize();

            if (normalizedProjectFilePath.equals(normalizedSourcePath)) {
                sourceFile = projectFile;

                if (candidateMatcher.matches(sourceFile, sourceFile, definition, groupedExtensionsRule)) {
                    result.add(sourceFile);
                }

                for (ProjectFileInfo candidateFile : candidatesBeforeSource) {
                    if (candidateMatcher.matches(sourceFile, candidateFile, definition, groupedExtensionsRule)) {
                        result.add(candidateFile);
                    }
                }

                candidatesBeforeSource.clear();
                continue;
            }

            if (sourceFile == null) {
                candidatesBeforeSource.add(projectFile);
                continue;
            }

            if (candidateMatcher.matches(sourceFile, projectFile, definition, groupedExtensionsRule)) {
                result.add(projectFile);
            }
        }

        if (sourceFile == null) {
            return List.of();
        }

        return List.copyOf(result);
    }
}