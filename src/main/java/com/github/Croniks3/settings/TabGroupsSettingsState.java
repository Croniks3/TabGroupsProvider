package com.github.Croniks3.settings;

import com.github.Croniks3.model.TabGroupDirectoryRule;
import com.github.Croniks3.model.enums.NameMatchRuleMode;

import java.util.ArrayList;
import java.util.List;

public class TabGroupsSettingsState {
    public List<String> groupedExtensions = new ArrayList<>(List.of(
            "h",
            "hpp",
            "hxx",
            "hh",
            "c",
            "cpp",
            "cxx",
            "cc"
    ));

    public boolean enableCppNavigationLogic = true;

    public int maxFilesPerGroup = 30;

    public boolean autoCreateGroupForNewTabs = false;
    public boolean autoSaveGroupToCacheOnClose = false;

    public NameMatchRuleMode defaultNameMatchRuleMode = NameMatchRuleMode.NONE;
    public String defaultCustomSubstring = "";

    public List<TabGroupDirectoryRule> defaultDirectoryRules = new ArrayList<>();
}