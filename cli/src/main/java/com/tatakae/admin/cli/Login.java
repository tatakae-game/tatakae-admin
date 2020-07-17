package com.tatakae.admin.cli;

import com.tatakae.admin.cli.menus.HomeMenu;
import com.tatakae.admin.core.models.Credentials;
import com.tatakae.admin.core.services.AuthService;
import java.util.Scanner;

import static java.lang.System.exit;

public class Login {

    public void display() {

        final int attempts = 3;

        for (int i = attempts; i > 0; --i) {
            var credentials = enterCredentials();

            try {
                AuthService.authenticate(credentials.getUsername(), credentials.getPassword());

                if (AuthService.isAuthed()) {
                    System.out.println("\nConnection successful.\n");
                    HomeMenu homeMenu = HomeMenu.getInstance(null);
                    homeMenu.run();
                    break;
                } else {
                    System.err.println("\nConnection failed. " + (i - 1) + " attempts remaining.\n");
                }
            } catch (Exception e) {
                System.err.println("Connection refused: Server disconnected.");
                separator();
            }
        }
        exit(0);
    }

    private static Credentials enterCredentials() {
        try {
            System.out.println("Enter your credentials:\n");

            final var console = System.console();
            String username;
            String password;

            if (console != null) {
                username = console.readLine("\tUsername: ");
                password = String.valueOf(console.readPassword("\tPassword: "));
            } else {
                Scanner scanner = new Scanner(System.in);

                System.out.print("\tUsername: ");
                username = scanner.next();

                System.out.print("\tPassword: ");
                password = scanner.next();
            }

            return new Credentials(username, password);

        } catch (Exception e) {
            e.getMessage();
            return new Credentials("", "");
        }
    }

    public void separator() {
        System.out.println("\n==============================\n");
    }

}
