package com.tatakae.admin.cli;

import com.tatakae.admin.cli.menus.HomeMenu;
import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.services.UserService;

import static java.lang.System.exit;

class Application {

    public static void main(final String[] args) {

        final var token = LocalDataManager.getToken();

        if (!token.isEmpty() && !token.isBlank()) {
            final var user = UserService.getUserByToken(token);

            if (user.getId().isEmpty() || user.getId().isBlank()) {
                final var loginStep = new Login();
                loginStep.display();
            } else {
                final var homeMenu = new HomeMenu();
                homeMenu.display();
            }
        } else {
            final var loginMenu = new Login();
            loginMenu.display();
        }
        exit(0);
    }
}