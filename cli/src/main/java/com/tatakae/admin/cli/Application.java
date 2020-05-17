package com.tatakae.admin.cli;

import com.tatakae.admin.core.Credentials;
import com.tatakae.admin.core.services.AuthService;

import java.util.Scanner;

class Application {

    public static void main(final String[] args) {
        for (int i = 5; i >= 0; --i) {
            var credentials = enterCredentials();

            try {
                AuthService.authenticate(credentials.getUsername(), credentials.getPassword());

                if (AuthService.isAuthed()) {
                    System.out.println("\nConnection successful.\n");
                    break;
                } else {
                    System.out.println("\nConnection failed. " + i + " attempts remaining.\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Credentials enterCredentials() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your credentials:\n");

        System.out.print("\tUsername: ");
        var username = scanner.next();

        System.out.print("\tPassword: ");
        var password = scanner.next();

        scanner.close();

        return new Credentials(username, password);
    }
}