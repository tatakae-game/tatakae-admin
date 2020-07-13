package com.tatakae.admin.cli;

import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.services.UserService;

class Application {

    public static void main(final String[] args) {

        final var token = LocalDataManager.getToken();

        if (!token.isEmpty() && !token.isBlank()) {
            final var user = UserService.getUserByToken(token);

            if (user.getId().isEmpty() || user.getId().isBlank()) {
                final var loginMenu = new Login();
                loginMenu.display();
            } else {
                System.out.println("/!\\ TODO /!\\");
            }
        } else {
            final var loginMenu = new Login();
            loginMenu.display();
        }
    }
}