package com.softline.csrv.screen.main;

import io.jmix.ui.icon.Icons;


public enum SideMenuIcon implements Icons.Icon {

    ADMINISTRATION("font-icon:COGS", "administration"),
    BPM("font-icon:SITEMAP","bpm"),
    REPORT("font-icon:FILE_TEXT_O","reports"),
    NSI("font-icon:LIST", "nsi"),
    REQUEST("font-icon:TASKS", "request"),
    WIKI("font-icon:WIKIPEDIA_W", "wiki");

    protected String source;
    private final String menuId;

    SideMenuIcon(String source, String menuId) {
        this.source = source;
        this.menuId = menuId;
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public String iconName() {
        return name();
    }

    public String getMenuId() {
        return menuId;
    }
}
