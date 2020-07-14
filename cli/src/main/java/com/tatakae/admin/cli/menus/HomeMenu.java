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
        System.out.println("\n=== HOME === \n");
        String choice;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("0 - Previous\t1 - Show tickets\t2 - Show plugins\n");
            System.out.println("(q) / (Q) / (quit) / (Quit) / (QUIT) --> Quit\n");
            System.out.print("Choose your action (must be a number): ");

            choice = sc.next();

        } while (!isValidChoice(choice));

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

    public boolean wantToQuit(final String choice) {
        boolean isValid = choice.equals("q") || choice.equals("Q") || choice.equals("quit")
                || choice.equals("Quit") || choice.equals("QUIT");

        if (isValid) {
            this.choice = -1;
        }

        return isValid;
    }
}
