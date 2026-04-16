package com.github.Croniks3.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ActiveTabGroups {
    private final Map<TabGroupId, TabGroup> groupsById = new LinkedHashMap<>();

    public void add(@NotNull TabGroup group) {
        Objects.requireNonNull(group);
        groupsById.put(group.getId(), group);
    }

    public @Nullable TabGroup get(@NotNull TabGroupId groupId) {
        Objects.requireNonNull(groupId);
        return groupsById.get(groupId);
    }

    public boolean contains(@NotNull TabGroupId groupId) {
        Objects.requireNonNull(groupId);
        return groupsById.containsKey(groupId);
    }

    public void replace(@NotNull TabGroup group) {
        Objects.requireNonNull(group);
        groupsById.put(group.getId(), group);
    }

    public @Nullable TabGroup remove(@NotNull TabGroupId groupId) {
        Objects.requireNonNull(groupId);
        return groupsById.remove(groupId);
    }

    public @NotNull List<TabGroup> getAll() {
        return List.copyOf(groupsById.values());
    }

    public int size() {
        return groupsById.size();
    }

    public boolean isEmpty() {
        return groupsById.isEmpty();
    }

    public void clear() {
        groupsById.clear();
    }
}