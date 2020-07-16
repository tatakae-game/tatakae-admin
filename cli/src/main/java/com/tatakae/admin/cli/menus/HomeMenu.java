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
        super(parent);
    }

    @Override
    public void run() {
        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("HOME");
            System.out.println("1 - Show tickets\t2 - Show plugins\n");
            System.out.print("Choose your action: ");

            choice = sc.next();
            isValid = isValidChoice(choice, 1, 2);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }
        } while (!isValid);

        loadChoice(this.choice);
    }

    @Override
    public void loadChoice(Integer choice) {
        switch (choice) {
            case 1:
                final var ticketsMenu = TicketsMenu.getInstance(this);
                ticketsMenu.run();
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
}
