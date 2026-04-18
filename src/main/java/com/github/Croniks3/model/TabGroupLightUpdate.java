package com.github.Croniks3.model;

import com.github.Croniks3.model.enums.TabGroupLightUpdateType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupLightUpdate {
    private final TabGroupLightUpdateType type;
    private final String oldFilePath;
    private final String newFilePath;

    public TabGroupLightUpdate(
            @NotNull TabGroupLightUpdateType type,
            @Nullable String oldFilePath,
            @Nullable String newFilePath
    ) {
        this.type = Objects.requireNonNull(type);
        this.oldFilePath = oldFilePath;
        this.newFilePath = newFilePath;
    }

    public @NotNull TabGroupLightUpdateType getType() {
        return type;
    }

    public @Nullable String getOldFilePath() {
        return oldFilePath;
    }

    public @Nullable String getNewFilePath() {
        return newFilePath;
    }

    public boolean hasOldFilePath() {
        return oldFilePath != null && !oldFilePath.isBlank();
    }

    public boolean hasNewFilePath() {
        return newFilePath != null && !newFilePath.isBlank();
    }
}