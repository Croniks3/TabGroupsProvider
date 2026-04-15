package com.github.Croniks3.logic;

import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroupDirectoryRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class TabGroupDirectoryRulesMatcher {
    private final TabGroupDirectoryRuleMatcher singleRuleMatcher = new TabGroupDirectoryRuleMatcher();

    public boolean matches(
            @NotNull ProjectFileInfo candidateFile,
            @NotNull List<TabGroupDirectoryRule> rules
    ) {
        Objects.requireNonNull(candidateFile);
        Objects.requireNonNull(rules);

        boolean hasActiveRules = false;

        for (TabGroupDirectoryRule rule : rules) {
            if (!rule.isActive()) {
                continue;
            }

            hasActiveRules = true;

            if (singleRuleMatcher.matches(candidateFile, rule)) {
                return true;
            }
        }

        return !hasActiveRules;
    }
}