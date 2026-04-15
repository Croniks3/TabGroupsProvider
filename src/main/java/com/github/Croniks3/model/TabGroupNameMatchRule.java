package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupNameMatchRule implements TabGroupRule {
    private final NameMatchRuleMode mode;
    private final String customSubstring;

    public TabGroupNameMatchRule(@NotNull NameMatchRuleMode mode, @Nullable String customSubstring) {
        this.mode = Objects.requireNonNull(mode);
        this.customSubstring = customSubstring;
    }

    public @NotNull NameMatchRuleMode getMode() {
        return mode;
    }

    public @Nullable String getCustomSubstring() {
        return customSubstring;
    }

    public boolean isActive() {
        return mode != NameMatchRuleMode.NONE;
    }

    public boolean requiresCustomSubstring() {
        return mode == NameMatchRuleMode.CUSTOM_SUBSTRING;
    }
}
