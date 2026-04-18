package com.github.Croniks3.model;

import com.github.Croniks3.model.enums.TabGroupUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupUpdateResult {
    private final TabGroupUpdateAction action;
    private final TabGroup updatedGroup;

    public TabGroupUpdateResult(
            @NotNull TabGroupUpdateAction action,
            @Nullable TabGroup updatedGroup
    ) {
        this.action = Objects.requireNonNull(action);
        this.updatedGroup = updatedGroup;
    }

    public @NotNull TabGroupUpdateAction getAction() {
        return action;
    }

    public @Nullable TabGroup getUpdatedGroup() {
        return updatedGroup;
    }

    public boolean hasUpdatedGroup() {
        return updatedGroup != null;
    }
}
