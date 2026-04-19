package com.github.Croniks3.model;

import com.github.Croniks3.model.enums.TabGroupChangeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupHandleChangeResult {
    private final TabGroupChangeType changeType;
    private final TabGroup updatedGroup;

    public TabGroupHandleChangeResult(
            @NotNull TabGroupChangeType changeType,
            @Nullable TabGroup updatedGroup
    ) {
        this.changeType = Objects.requireNonNull(changeType);
        this.updatedGroup = updatedGroup;
    }

    public @NotNull TabGroupChangeType getChangeType() {
        return changeType;
    }

    public @Nullable TabGroup getUpdatedGroup() {
        return updatedGroup;
    }

    public boolean hasUpdatedGroup() {
        return updatedGroup != null;
    }
}