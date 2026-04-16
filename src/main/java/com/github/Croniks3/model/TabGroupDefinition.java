package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class TabGroupDefinition {
    private final String sourceFilePath;
    private final TabGroupNameMatchRule nameMatchRule;
    private final List<TabGroupDirectoryRule> directoryRules;

    public TabGroupDefinition(
            @NotNull String sourceFilePath,
            @NotNull TabGroupNameMatchRule nameMatchRule,
            @NotNull List<TabGroupDirectoryRule> directoryRules
    ) {
        this.sourceFilePath = Objects.requireNonNull(sourceFilePath);
        this.nameMatchRule = Objects.requireNonNull(nameMatchRule);
        this.directoryRules = List.copyOf(Objects.requireNonNull(directoryRules));
    }

    public @NotNull String getSourceFilePath() {
        return sourceFilePath;
    }

    public @NotNull TabGroupNameMatchRule getNameMatchRule() {
        return nameMatchRule;
    }

    public @NotNull List<TabGroupDirectoryRule> getDirectoryRules() {
        return directoryRules;
    }

    public boolean hasActiveNameRule() {
        return nameMatchRule.isActive();
    }

    public boolean hasActiveDirectoryRules() {
        for (TabGroupDirectoryRule rule : directoryRules) {
            if (rule.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyActiveRule() {
        return hasActiveNameRule() || hasActiveDirectoryRules();
    }

    public @NotNull TabGroupDefinition withSourceFilePath(@NotNull String newSourceFilePath) {
        return new TabGroupDefinition(
                Objects.requireNonNull(newSourceFilePath),
                nameMatchRule,
                directoryRules
        );
    }
}