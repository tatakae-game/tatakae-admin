package com.tatakae.admin.cli.menus;

import com.tatakae.admin.core.PluginEnvironment;
import com.tatakae.admin.core.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class HomeMenu extends AbstractMenu {
    private final PluginManager pluginManager;

    private Map<Integer, PluginEnvironment> plugins;

    private static HomeMenu instance = null;

    public static HomeMenu getInstance(MenuInterface parent) {
        if (instance == null) {
            return new HomeMenu(parent);
        }
        return instance;
    }


    private HomeMenu(MenuInterface parent) {
        super(parent);
        pluginManager = PluginManager.getInstance();
        plugins = new HashMap<>();
    }

    @Override
    public void run() {
        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("HOME");
            System.out.println("1 - Show tickets\n2 - Show plugins");

            int count = 2;
            plugins.entrySet().clear();

            for (final var env : pluginManager.environmentsActive) {
                count++;
                System.out.println(count + " - Launch: " + env.getPlugin().getName() + "\t");
                plugins.put(count, env);
            }

            System.out.println();
            System.out.print("Choose your action: ");

            choice = sc.next();
            isValid = isValidChoice(choice, 1, count);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }
        } while (!isValid);

        loadChoice(this.choice);
        run();
    }

    @Override
    public void loadChoice(Integer choice) {
        if (choice >= 3) {
            plugins.get(choice).getPlugin().startCLI();
            return;
        }
        switch (choice) {
            case 1:
                final var ticketsMenu = TicketsMenu.getInstance(this);
                ticketsMenu.run();
                break;
            case 2:
                final var pluginsMenu = PluginsMenu.getInstance(this);
                pluginsMenu.run();
                break;
            default:
                menuSeparator();
                System.out.println("See you soon!");
                exit(0);
        }
    }
}
