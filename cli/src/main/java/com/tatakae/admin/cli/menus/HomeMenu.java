package com.tatakae.admin.cli.menus;

import java.util.Scanner;

import static java.lang.System.exit;

public class HomeMenu extends AbstractMenu {

    private static HomeMenu instance = null;

    public static HomeMenu getInstance(MenuInterface parent) {
        if (instance == null) {
            return new HomeMenu(parent);
        }
        return instance;
    }


    private HomeMenu(MenuInterface parent) {
        this.parent = parent;
    }

    @Override
    public void display() {
        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("HOME");
            System.out.println("1 - Show tickets\t2 - Show plugins\n");
            System.out.print("Choose your action (must be a number): ");

            choice = sc.next();
            isValid = isValidChoice(choice);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }

        } while (!isValid);

        sc.close();

        switch (this.getChoice()) {
            case 0:
                parent.display();
                break;
            case 1:
                System.out.println("case 1: load tickets menu");
                break;
            case 2:
                System.out.println("case 2: load plugins menu");
                break;
            default:
                menuSeparator();
                System.out.println("See you soon!");
                exit(0);
        }
    }

    @Override
    public boolean isValidChoice(String choice) {
        try {
            int number = Integer.parseInt(choice);

            boolean isValid = (number >= 1 && number <= 3) ;

            if (isValid) {
                this.choice = number;
            }

            return isValid;

        } catch (NumberFormatException e) {
            return wantToQuit(choice);
        }
    }
}
