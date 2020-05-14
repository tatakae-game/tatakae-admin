package com.tatakae.admin.cli;

import com.tatakae.admin.core.services.AuthService;
import com.tatakae.admin.core.User;

class Application {

    public static void main(final String[] args) {
        int counter = 5;
        do {
            System.out.println("Enter your credentials:\n");
            User.login();

            if (AuthService.isAuthed()) {
                counter--;
                System.out.println("\nConnection failed. " + counter + " attempts remaining.\n");
            } else {
                System.out.println("\nConnection successful.\n");
            }
        } while (!AuthService.isAuthed() && counter > 0 );

        System.exit(0);
    }
}
