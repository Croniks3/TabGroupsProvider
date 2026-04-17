package com.github.Croniks3.logic;

import com.github.Croniks3.model.GroupedExtensionsRule;
import com.github.Croniks3.model.ProjectFileChange;
import com.github.Croniks3.model.ProjectFileChangeType;
import com.github.Croniks3.model.ProjectFileInfo;
import com.github.Croniks3.model.TabGroup;
import com.github.Croniks3.model.TabGroupDefinition;
import com.github.Croniks3.model.TabGroupUpdateAction;
import com.github.Croniks3.model.TabGroupUpdateResult;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
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

        if(hasGroupSourceFilePathChanged(group, change)) {
            String newFilePath = Objects.requireNonNull(change.getNewFilePath());
            TabGroupDefinition updatedDefinition = group.getDefinition().withSourceFilePath(newFilePath);
            group = new TabGroup(
                    group.getId(),
                    updatedDefinition,
                    group.getFilePaths()
            );
        }

        // Тут возможно не нужно передавать все файлы,
        // а лучше запрашивать у API IDE индексы тех файлов
        // которые изменились, формировать из них список и передавать.
        // Или делать это уровнем выше в момент вызова handleChange().
        TabGroup rebuiltGroup = groupBuilder.rebuild(
                group,
                projectFiles,
                groupedExtensionsRule
        );

        return new TabGroupUpdateResult(TabGroupUpdateAction.REBUILD, rebuiltGroup);
    }

    private boolean hasGroupSourceFilePathChanged(
            @NotNull TabGroup group,
            @NotNull ProjectFileChange change
    ){
        ProjectFileChangeType changeType = change.getType();
        if (changeType != ProjectFileChangeType.MOVED && changeType != ProjectFileChangeType.RENAMED) {
            return false;
        }

        String oldFilePath = change.getOldFilePath();
        String newFilePath = change.getNewFilePath();
        if (oldFilePath == null || oldFilePath.isBlank() || newFilePath == null || newFilePath.isBlank()) {
            return false;
        }

        return isSamePath(group.getDefinition().getSourceFilePath(), oldFilePath);
    }

    private boolean isSamePath(@NotNull String firstPath, @NotNull String secondPath) {
        return Path.of(firstPath).normalize().equals(Path.of(secondPath).normalize());
    }
}