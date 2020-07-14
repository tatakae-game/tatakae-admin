package com.tatakae.admin.cli.menus;

abstract class AbstractMenu implements MenuInterface {

    protected MenuInterface parent;
    protected Integer choice;

    public Integer getChoice() {
        return this.choice;
    }

    public MenuInterface getParent() {
        return this.parent;
    }

    public void menuSeparator() {
        System.out.println("\n==============================\n");
    }

}
