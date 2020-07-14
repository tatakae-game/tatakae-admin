package com.tatakae.admin.cli.menus;

abstract class AbstractMenu implements MenuInterface {

    protected final MenuInterface parent;
    protected Integer choice;

    public AbstractMenu(MenuInterface parent) {
        this.parent = parent;
    }

    public Integer getChoice() {
        return this.choice;
    }

    public MenuInterface getParent() {
        return this.parent;
    }

}
