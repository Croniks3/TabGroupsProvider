package com.github.Croniks3.logic;

import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroupDirectoryRule;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public final class TabGroupDirectoryRuleMatcher {

    public boolean matches(
            @NotNull ProjectFileInfo candidateFile,
            @NotNull TabGroupDirectoryRule rule
    ) {
        Objects.requireNonNull(candidateFile);
        Objects.requireNonNull(rule);

        if (!rule.isActive()) {
            return true;
        }

        Path candidatePath = Path.of(candidateFile.getFilePath()).normalize();
        Path candidateDirectory = candidatePath.getParent();
        if (candidateDirectory == null) {
            return false;
        }

        Path ruleDirectory = Path.of(rule.getDirectoryPath()).normalize();

        if (rule.isIncludeSubdirectories()) {
            return candidateDirectory.startsWith(ruleDirectory);
        }

        return candidateDirectory.equals(ruleDirectory);
    }
}