package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class TabGroup {
    private final TabGroupId id;
    private final TabGroupDefinition definition;
    private final List<String> filePaths;

    public TabGroup(
            @NotNull TabGroupDefinition definition,
            @NotNull List<String> filePaths
    ) {
        this(TabGroupId.create(), definition, filePaths);
    }

    public TabGroup(
            @NotNull TabGroupId id,
            @NotNull TabGroupDefinition definition,
            @NotNull List<String> filePaths
    ) {
        this.id = Objects.requireNonNull(id);
        this.definition = Objects.requireNonNull(definition);
        this.filePaths = List.copyOf(Objects.requireNonNull(filePaths));
    }

    public @NotNull TabGroupId getId() {
        return id;
    }

    public @NotNull TabGroupDefinition getDefinition() {
        return definition;
    }

    public @NotNull List<String> getFilePaths() {
        return filePaths;
    }

    public int getFilesCount() {
        return filePaths.size();
    }

    public boolean isEmpty() {
        return filePaths.isEmpty();
    }
}