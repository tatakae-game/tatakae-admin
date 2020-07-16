package com.tatakae.admin.cli.menus;

abstract class AbstractMenu implements MenuInterface {

    protected MenuInterface parent;
    protected Integer choice;

    protected AbstractMenu(MenuInterface parent) {
        this.parent = parent;
    }

    public boolean isValidChoice(final String choice, final Integer min, final Integer max) {
        try {
            int number = Integer.parseInt(choice);

            boolean isValid = (number >= min && number <= max) ;

            if (isValid) {
                this.choice = number;
            }

            return isValid;

        } catch (NumberFormatException e) {
            return wantToQuit(choice);
        }
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
