package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TabGroupBuilder {
    private final TabGroupFilesCollector filesCollector = new TabGroupFilesCollector();
    private final TabGroupFilesLimiter filesLimiter = new TabGroupFilesLimiter();

    public @NotNull TabGroup build(
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull TabGroupDefinition definition,
            @NotNull GroupedExtensionsRule groupedExtensionsRule,
            int maxFilesPerGroup
    ) {
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(definition);
        Objects.requireNonNull(groupedExtensionsRule);

        List<ProjectFileInfo> matchedFiles = filesCollector.collectMatchedFiles(
                projectFiles,
                definition,
                groupedExtensionsRule
        );

        List<String> filePaths = new ArrayList<>(matchedFiles.size());
        for (ProjectFileInfo matchedFile : matchedFiles) {
            filePaths.add(matchedFile.getFilePath());
        }

        List<String> limitedFilePaths = filesLimiter.applyLimit(filePaths, maxFilesPerGroup);

        return new TabGroup(definition, limitedFilePaths);
    }

    public @NotNull TabGroup rebuild(
            @NotNull TabGroup existingGroup,
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull GroupedExtensionsRule groupedExtensionsRule,
            int maxFilesPerGroup
    ) {
        Objects.requireNonNull(existingGroup);
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(groupedExtensionsRule);

        List<ProjectFileInfo> matchedFiles = filesCollector.collectMatchedFiles(
                projectFiles,
                existingGroup.getDefinition(),
                groupedExtensionsRule
        );

        List<String> filePaths = new ArrayList<>(matchedFiles.size());
        for (ProjectFileInfo matchedFile : matchedFiles) {
            filePaths.add(matchedFile.getFilePath());
        }

        List<String> limitedFilePaths = filesLimiter.applyLimit(filePaths, maxFilesPerGroup);

        return new TabGroup(existingGroup.getId(), existingGroup.getDefinition(), limitedFilePaths);
    }
}