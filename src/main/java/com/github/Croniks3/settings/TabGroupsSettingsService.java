package com.github.Croniks3.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.APP)
@State(
        name = "TabGroupsSettings",
        storages = @Storage("TabGroupsProvider.xml")
)
public final class TabGroupsSettingsService implements PersistentStateComponent<TabGroupsSettingsState> {

    private TabGroupsSettingsState state = new TabGroupsSettingsState();

    public static TabGroupsSettingsService getInstance() {
        return ApplicationManager.getApplication().getService(TabGroupsSettingsService.class);
    }

    @Override
    public @Nullable TabGroupsSettingsState getState() {
        return state;
    }
    
    @Override
    public void loadState(@NotNull TabGroupsSettingsState state) {
        this.state = state;
    }
}