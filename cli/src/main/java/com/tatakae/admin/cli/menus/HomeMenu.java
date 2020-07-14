package com.tatakae.admin.cli.menus;

import java.util.Scanner;

import static java.lang.System.exit;

public class HomeMenu implements Menu {

    private int choice;

    @Override
    public void display() {
        System.out.println("\n=== HOME === \n");
        String choice;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("1 - Show tickets\t2 - Show plugins\n\n(q) / (Q) / (quit) / (Quit) / (QUIT) --> Quit\n");
            System.out.print("Choose your action (must be a number): ");

            choice = sc.next();

        } while (!isValidChoice(choice));

        if (wantToQuit(choice)) {
            System.out.println("\nSee you soon!");
            exit(0);
        } else {
            switch (this.choice) {
                case 1:
                    System.out.println("case 1: load tickets menu");
                    break;
                case 2:
                    System.out.println("case 2: load plugins menu");
                    break;
            }
        }
    }

    @Override
    public int getChoice() {
        return this.choice;
    }

    @Override
    public boolean isValidChoice(String choice) {
        try {
            int number = Integer.parseInt(choice);

            boolean isValid = (number >= 0 && number <= 3) ;

            if (isValid) {
                this.choice = number;
            }

            return isValid;

        } catch (NumberFormatException e) {
            return wantToQuit(choice);
        }
    }

    public boolean wantToQuit(final String choice) {
        return choice.equals("q") || choice.equals("Q") || choice.equals("quit")
                || choice.equals("Quit") || choice.equals("QUIT");
    }
}
