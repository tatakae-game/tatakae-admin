package com.tatakae.admin.cli;

import com.tatakae.admin.core.Auth;
import com.tatakae.admin.core.User;

class Application {

    public static void main(final String[] args) {
        var user = new User();
        do {
            System.out.println("Enter your credentials:\n");
            user = user.login();
        } while (!Auth.authed);

        System.exit(0);
    }
}
