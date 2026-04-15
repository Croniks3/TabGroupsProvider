package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroupDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TabGroupCandidateMatcher {
    private final TabGroupNameRuleMatcher nameRuleMatcher = new TabGroupNameRuleMatcher();
    private final TabGroupDirectoryRulesMatcher directoryRulesMatcher = new TabGroupDirectoryRulesMatcher();

    public boolean matches(
            @NotNull ProjectFileInfo sourceFile,
            @NotNull ProjectFileInfo candidateFile,
            @NotNull TabGroupDefinition definition,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        Objects.requireNonNull(sourceFile);
        Objects.requireNonNull(candidateFile);
        Objects.requireNonNull(definition);
        Objects.requireNonNull(groupedExtensionsRule);

        if (!groupedExtensionsRule.isGroupedExtension(sourceFile.getExtension())) {
            return false;
        }

        if (!groupedExtensionsRule.isGroupedExtension(candidateFile.getExtension())) {
            return false;
        }

        boolean matchesName = nameRuleMatcher.matches(
                sourceFile,
                candidateFile,
                definition.getNameMatchRule()
        );

        boolean matchesDirectory = directoryRulesMatcher.matches(
                candidateFile,
                definition.getDirectoryRules()
        );

        return matchesName && matchesDirectory;
    }
}