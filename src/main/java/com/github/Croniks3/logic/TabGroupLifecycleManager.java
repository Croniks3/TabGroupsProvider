package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupChangeInfo;
import com.github.Croniks3.model.TabGroupDefinition;
import com.github.Croniks3.model.TabGroupHandleChangeResult;
import com.github.Croniks3.model.TabGroupLightUpdate;
import com.github.Croniks3.model.enums.ProjectFileChangeType;
import com.github.Croniks3.model.enums.TabGroupChangeType;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class TabGroupLifecycleManager {
    private final TabGroupChangeAnalyzer changeAnalyzer = new TabGroupChangeAnalyzer();
    private final TabGroupLightUpdater lightUpdater = new TabGroupLightUpdater();
    private final TabGroupBuilder groupBuilder = new TabGroupBuilder();

    public @NotNull TabGroupHandleChangeResult handleChange(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange fileChange,
            @NotNull List<ProjectFileInfo> projectFiles,
            @NotNull GroupedExtensionsRule groupedExtensionsRule,
            int maxFilesPerGroup
    ) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(fileChange);
        Objects.requireNonNull(projectFiles);
        Objects.requireNonNull(groupedExtensionsRule);

        TabGroupChangeInfo changeInfo = changeAnalyzer.analyze(
                group,
                fileChange,
                groupedExtensionsRule
        );

        TabGroupChangeType groupChangeType = changeInfo.getChangeType();

        if (groupChangeType == TabGroupChangeType.NONE) {
            return new TabGroupHandleChangeResult(TabGroupChangeType.NONE, null);
        }

        if (groupChangeType == TabGroupChangeType.REMOVE_GROUP) {
            return new TabGroupHandleChangeResult(TabGroupChangeType.REMOVE_GROUP, null);
        }

        if (groupChangeType == TabGroupChangeType.LIGHT_UPDATE) {
            TabGroupLightUpdate lightUpdate = Objects.requireNonNull(changeInfo.getLightUpdate());
            TabGroup updatedGroup = lightUpdater.apply(group, lightUpdate, maxFilesPerGroup);

            return new TabGroupHandleChangeResult(TabGroupChangeType.LIGHT_UPDATE, updatedGroup);
        }

        TabGroup groupForRebuild = prepareGroupForHeavyRebuild(group, fileChange);

        TabGroup rebuiltGroup = groupBuilder.rebuild(
                groupForRebuild,
                projectFiles,
                groupedExtensionsRule,
                maxFilesPerGroup
        );

        return new TabGroupHandleChangeResult(TabGroupChangeType.HEAVY_REBUILD, rebuiltGroup);
    }

    private @NotNull TabGroup prepareGroupForHeavyRebuild(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change
    ) {
        ProjectFileChangeType changeType = change.getType();
        if (changeType != ProjectFileChangeType.MOVED && changeType != ProjectFileChangeType.RENAMED) {
            return group;
        }

        String oldFilePath = change.getOldFilePath();
        String newFilePath = change.getNewFilePath();
        if (oldFilePath == null || oldFilePath.isBlank() || newFilePath == null || newFilePath.isBlank()) {
            return group;
        }

        if (!isSamePath(group.getDefinition().getSourceFilePath(), oldFilePath)) {
            return group;
        }

        TabGroupDefinition updatedDefinition = group.getDefinition().withSourceFilePath(newFilePath);

        return new TabGroup(
                group.getId(),
                updatedDefinition,
                group.getFilePaths()
        );
    }

    private boolean isSamePath(@NotNull String firstPath, @NotNull String secondPath) {
        return Path.of(firstPath).normalize().equals(Path.of(secondPath).normalize());
    }
}