package com.github.Croniks3.logic;

import com.github.Croniks3.model.NameMatchRuleMode;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroupNameMatchRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupNameRuleMatcher {

    public boolean matches(
            @NotNull ProjectFileInfo sourceFile,
            @NotNull ProjectFileInfo candidateFile,
            @NotNull TabGroupNameMatchRule rule
    ) {
        Objects.requireNonNull(sourceFile);
        Objects.requireNonNull(candidateFile);
        Objects.requireNonNull(rule);

        NameMatchRuleMode mode = rule.getMode();

        if (mode == NameMatchRuleMode.NONE) {
            return true;
        }

        if (mode == NameMatchRuleMode.EXACT_NAME) {
            return sourceFile.getBaseName().equals(candidateFile.getBaseName());
        }

        if (mode == NameMatchRuleMode.CUSTOM_SUBSTRING) {
            String customSubstring = rule.getCustomSubstring();
            if (isBlank(customSubstring)) {
                return false;
            }

            if (!sourceFile.getBaseName().contains(customSubstring)) {
                return false;
            }

            return candidateFile.getBaseName().contains(customSubstring);
        }

        return false;
    }

    private boolean isBlank(@Nullable String value) {
        return value == null || value.isBlank();
    }
}
