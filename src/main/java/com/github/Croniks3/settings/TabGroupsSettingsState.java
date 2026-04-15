package com.github.Croniks3.settings;

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
}
