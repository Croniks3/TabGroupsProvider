package com.github.Croniks3.settings;

import com.github.Croniks3.model.TabGroupDefinition;
import com.github.Croniks3.model.TabGroupDirectoryRule;
import com.github.Croniks3.model.TabGroupNameMatchRule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DefaultTabGroupDefinitionFactory {

    public @NotNull TabGroupDefinition createForSourceFile(
            @NotNull String sourceFilePath,
            @NotNull TabGroupsSettingsState settingsState
    ) {
        Objects.requireNonNull(sourceFilePath);
        Objects.requireNonNull(settingsState);

        TabGroupNameMatchRule nameMatchRule = new TabGroupNameMatchRule(
                settingsState.defaultNameMatchRuleMode,
                settingsState.defaultCustomSubstring
        );

        List<TabGroupDirectoryRule> directoryRules = new ArrayList<>();
        for (TabGroupDirectoryRule rule : settingsState.defaultDirectoryRules) {
            directoryRules.add(new TabGroupDirectoryRule(
                    rule.getDirectoryPath(),
                    rule.isIncludeSubdirectories()
            ));
        }

        return new TabGroupDefinition(
                sourceFilePath,
                nameMatchRule,
                directoryRules
        );
    }
}