package com.github.Croniks3.logic;

import com.github.Croniks3.model.NameMatchRuleMode;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileChangeType;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupDefinition;
import com.github.Croniks3.model.TabGroupDirectoryRule;
import com.github.Croniks3.model.TabGroupKey;
import com.github.Croniks3.model.TabGroupNameMatchRule;
import com.github.Croniks3.model.TabGroupUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class TabGroupUpdatePolicy {
    private final TabGroupDirectoryRuleMatcher directoryRuleMatcher = new TabGroupDirectoryRuleMatcher();

    public @NotNull TabGroupUpdateAction decide(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(change);

        ProjectFileChangeType changeType = change.getType();

        if (changeType == ProjectFileChangeType.CREATED) {
            String newFilePath = change.getNewFilePath();
            if (newFilePath == null || newFilePath.isBlank()) {
                return TabGroupUpdateAction.NONE;
            }

            if (canPotentiallyMatchGroup(group, newFilePath)) {
                return TabGroupUpdateAction.REBUILD;
            }

            return TabGroupUpdateAction.NONE;
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
            String oldFilePath = change.getOldFilePath();
            String newFilePath = change.getNewFilePath();
            if (oldFilePath == null || oldFilePath.isBlank() || newFilePath == null || newFilePath.isBlank()) {
                return TabGroupUpdateAction.NONE;
            }

            if (isSourceFile(group, oldFilePath)) {
                return TabGroupUpdateAction.REBUILD;
            }

            if (containsFile(group, oldFilePath)) {
                return TabGroupUpdateAction.REBUILD;
            }

            if (canPotentiallyMatchGroup(group, newFilePath)) {
                return TabGroupUpdateAction.REBUILD;
            }

            return TabGroupUpdateAction.NONE;
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

    private boolean canPotentiallyMatchGroup(@NotNull TabGroup group, @NotNull String candidateFilePath) {
        TabGroupDefinition definition = group.getDefinition();

        if (!matchesDirectoryRules(definition, candidateFilePath)) {
            return false;
        }

        return matchesNameRule(definition, candidateFilePath);
    }

    private boolean matchesDirectoryRules(
            @NotNull TabGroupDefinition definition,
            @NotNull String candidateFilePath
    ) {
        if (!definition.hasActiveDirectoryRules()) {
            return true;
        }

        TabGroupKey candidateKey = tryCreateKeyFromPath(candidateFilePath);
        if (candidateKey == null) {
            return false;
        }

        // candidateKey нам здесь не нужен дальше, но сам факт валидного file name полезен
        for (TabGroupDirectoryRule rule : definition.getDirectoryRules()) {
            if (!rule.isActive()) {
                continue;
            }

            if (directoryRuleMatcher.matches(
                    new com.github.Croniks3.model.ProjectFileInfo(
                            candidateFilePath,
                            Path.of(candidateFilePath).getFileName().toString(),
                            candidateKey
                    ),
                    rule
            )) {
                return true;
            }
        }

        return false;
    }

    private boolean matchesNameRule(
            @NotNull TabGroupDefinition definition,
            @NotNull String candidateFilePath
    ) {
        TabGroupNameMatchRule rule = definition.getNameMatchRule();
        NameMatchRuleMode mode = rule.getMode();

        if (mode == NameMatchRuleMode.NONE) {
            return definition.hasActiveDirectoryRules();
        }

        TabGroupKey sourceKey = tryCreateKeyFromPath(definition.getSourceFilePath());
        TabGroupKey candidateKey = tryCreateKeyFromPath(candidateFilePath);
        if (sourceKey == null || candidateKey == null) {
            return false;
        }

        if (mode == NameMatchRuleMode.EXACT_NAME) {
            return sourceKey.getBaseName().equals(candidateKey.getBaseName());
        }

        if (mode == NameMatchRuleMode.CUSTOM_SUBSTRING) {
            String customSubstring = rule.getCustomSubstring();
            if (customSubstring == null || customSubstring.isBlank()) {
                return false;
            }

            if (!sourceKey.getBaseName().contains(customSubstring)) {
                return false;
            }

            return candidateKey.getBaseName().contains(customSubstring);
        }

        return false;
    }

    private @Nullable TabGroupKey tryCreateKeyFromPath(@NotNull String filePath) {
        Path fileNamePath = Path.of(filePath).getFileName();
        if (fileNamePath == null) {
            return null;
        }

        return TabGroupKey.tryCreate(fileNamePath.toString());
    }

    private @NotNull Path normalize(@NotNull String filePath) {
        return Path.of(filePath).normalize();
    }
}