package com.github.Croniks3.model;

import com.github.Croniks3.model.enums.TabGroupChangeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TabGroupChangeInfo {
    private final TabGroupChangeType changeType;
    private final TabGroupLightUpdate lightUpdate;

    public TabGroupChangeInfo(){
        changeType = TabGroupChangeType.NONE;
        lightUpdate = null;
    }

    public TabGroupChangeInfo(
            @NotNull TabGroupChangeType changeType,
            @Nullable TabGroupLightUpdate lightUpdate
    ) {
        this.changeType = Objects.requireNonNull(changeType);
        this.lightUpdate = lightUpdate;
    }

    public @NotNull TabGroupChangeType getChangeType() {
        return changeType;
    }

    public @Nullable TabGroupLightUpdate getLightUpdate() {
        return lightUpdate;
    }

    public boolean hasLightUpdate() {
        return lightUpdate != null;
    }
}