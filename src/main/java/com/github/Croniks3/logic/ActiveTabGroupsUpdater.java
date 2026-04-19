package com.github.Croniks3.logic;

import com.github.Croniks3.model.ActiveTabGroups;
import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupHandleChangeResult;
import com.github.Croniks3.model.enums.TabGroupChangeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ActiveTabGroupsUpdater {
    private final TabGroupLifecycleManager lifecycleManager = new TabGroupLifecycleManager();

    public void handleChange(
            @NotNull ActiveTabGroups activeTabGroups,
            @NotNull ProjectFileChange fileChange,
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull GroupedExtensionsRule groupedExtensionsRule
    ) {
        Objects.requireNonNull(activeTabGroups);
        Objects.requireNonNull(fileChange);
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(groupedExtensionsRule);

        List<TabGroup> groupsSnapshot = activeTabGroups.getAll();

        for (TabGroup group : groupsSnapshot) {
            TabGroupHandleChangeResult result = lifecycleManager.handleChange(
                    group,
                    fileChange,
                    projectFiles,
                    groupedExtensionsRule
            );

            TabGroupChangeType changeType = result.getChangeType();

            if (changeType == TabGroupChangeType.NONE) {
                continue;
            }

            if (changeType == TabGroupChangeType.REMOVE_GROUP) {
                activeTabGroups.remove(group.getId());
                continue;
            }

            TabGroup updatedGroup = result.getUpdatedGroup();
            if (updatedGroup != null) {
                activeTabGroups.replace(updatedGroup);
            }
        }
    }
}