package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupUpdateAction;
import com.github.Croniks3.model.TabGroupUpdateResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class TabGroupLifecycleManager {
    private final TabGroupUpdatePolicy updatePolicy = new TabGroupUpdatePolicy();
    private final TabGroupBuilder groupBuilder = new TabGroupBuilder();

    public @NotNull TabGroupUpdateResult handleChange(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change,
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(change);
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(groupedExtensionsRule);

        TabGroupUpdateAction action = updatePolicy.decide(group, change);

        if (action == TabGroupUpdateAction.NONE) {
            return new TabGroupUpdateResult(TabGroupUpdateAction.NONE, null);
        }

        if (action == TabGroupUpdateAction.REMOVE_GROUP) {
            return new TabGroupUpdateResult(TabGroupUpdateAction.REMOVE_GROUP, null);
        }

        TabGroup rebuiltGroup = groupBuilder.rebuild(
                group,
                projectFiles,
                groupedExtensionsRule
        );

        return new TabGroupUpdateResult(TabGroupUpdateAction.REBUILD, rebuiltGroup);
    }
}