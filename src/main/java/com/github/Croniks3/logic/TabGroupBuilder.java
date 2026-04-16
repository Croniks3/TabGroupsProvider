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

    public @NotNull TabGroup build(
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull TabGroupDefinition definition,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
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

        return new TabGroup(definition, filePaths);
    }
}