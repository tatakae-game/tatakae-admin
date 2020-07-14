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

    public boolean wantToQuit(final String choice) {
        boolean isValid = choice.equals("q") || choice.equals("Q") || choice.equals("quit")
                || choice.equals("Quit") || choice.equals("QUIT");

        if (isValid) {
            this.choice = -1;
        }

        return isValid;
    }

    public void displayTitle(final String title) {
        menuSeparator();
        System.out.print("=== " + title + " ===\t");
        System.out.println("( q | Q | quit | Quit | QUIT --> Exit application )\n");
    }
}
