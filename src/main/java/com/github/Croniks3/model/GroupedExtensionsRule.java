package com.github.Croniks3.model;

import com.github.Croniks3.settings.TabGroupsSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class GroupedExtensionsRule {
    private final Set<String> groupedExtensions;

    public GroupedExtensionsRule(@NotNull List<String> groupedExtensions) {
        Objects.requireNonNull(groupedExtensions);

        this.groupedExtensions = new LinkedHashSet<>();
        for (String extension : groupedExtensions) {
            String normalizedExtension = normalizeExtension(extension);
            if (!normalizedExtension.isEmpty()) {
                this.groupedExtensions.add(normalizedExtension);
            }
        }
    }

    public static @NotNull GroupedExtensionsRule fromState(@NotNull TabGroupsSettingsState state) {
        Objects.requireNonNull(state);
        return new GroupedExtensionsRule(state.groupedExtensions);
    }

    public boolean isGroupedExtension(@NotNull String extension) {
        Objects.requireNonNull(extension);
        return groupedExtensions.contains(normalizeExtension(extension));
    }

    public @NotNull Set<String> getGroupedExtensions() {
        return Set.copyOf(groupedExtensions);
    }

    private static @NotNull String normalizeExtension(@Nullable String extension) {
        if (extension == null) {
            return "";
        }

        String result = extension.trim().toLowerCase(Locale.ROOT);

        while (result.startsWith(".")) {
            result = result.substring(1);
        }

        return result;
    }
}