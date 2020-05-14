package com.tatakae.admin.cli;

import com.tatakae.admin.core.Credentials;
import com.tatakae.admin.core.services.AuthService;

import java.util.Scanner;

class Application {

    public static void main(final String[] args) {
        try {
            int counter = 5;
            do {
                System.out.println("Enter your credentials:\n");
                var credentials = enterCredentials();

                var user = AuthService.authenticate(credentials.getUsername(), credentials.getPassword());

                if (AuthService.isAuthed()) {
                    System.out.println("\nConnection successful.\n");
                } else {
                    counter--;
                    System.out.println("\nConnection failed. " + counter + " attempts remaining.\n");
                }
            } while (!AuthService.isAuthed() && counter > 0 );

            System.exit(0);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Credentials enterCredentials() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\tUsername: ");
        var username = scanner.next();
        System.out.print("\tPassword: ");
        var password = scanner.next();

        return new Credentials(username, password);
    }
}