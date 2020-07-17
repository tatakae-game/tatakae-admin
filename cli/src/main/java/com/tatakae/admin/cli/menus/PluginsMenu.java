package com.tatakae.admin.cli.menus;

import com.tatakae.admin.core.PluginEnvironment;
import com.tatakae.admin.core.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class PluginsMenu extends AbstractMenu {

    private final PluginManager pluginManager;

    private final Map<PluginEnvironment, String> environmentsMap;

    private static PluginsMenu instance = null;

    public static PluginsMenu getInstance(MenuInterface parent) {
        if (instance == null) {
            return new PluginsMenu(parent);
        }
        return instance;
    }

    private PluginsMenu(MenuInterface parent) {
        super(parent);
        pluginManager = PluginManager.getInstance();
        environmentsMap = new HashMap<>();
    }

    @Override
    public void run() {
        String choice;
        boolean isValid;

        environmentsMap.entrySet().clear();

        for(final var entry : pluginManager.environments.entrySet()) {
            environmentsMap.put(entry.getKey(), entry.getValue());
        }

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("PLUGINS");
            System.out.println("0 - Previous\t1 - Show plugins list\t2 - Enable / Disable a plugin\t3 - Show plugin details\t4 - Import a plugin\n");
            System.out.print("Choose your action: ");

            choice = sc.next();
            isValid = isValidChoice(choice, 0, 4);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }
        } while (!isValid);

        loadChoice(this.choice);
        run();
    }

    @Override
    public void loadChoice(Integer choice) {
        switch (choice) {
            case 0:
                this.parent.run();
                break;
            case 1:
                showPluginsList();
                break;
            case 2:
                activateDeactivatePlugin();
                break;
            default:
                menuSeparator();
                System.out.println("See you soon!");
                exit(0);
        }
    }

    private void showPluginsList() {
        int count = 1;

        System.out.println("N° | Name | Status");
        for (final var entry : pluginManager.environments.entrySet()) {
            final var status = entry.getValue().equals("Enable") ? "Inactive" : "Active";

            System.out.print(count + " | ");
            System.out.print(entry.getKey().getPlugin().getName());
            System.out.println(" | [" + status + "]");
            count++;
        }
    }

    public void activateDeactivatePlugin() {
        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.print("Choose a plugin: ");

        choice = sc.next();
        isValid = isValidChoice(choice, 1, pluginManager.environments.size());

        if (!isValid) {
            menuSeparator();
            System.err.println("Error: Plugin chosen invalid.");
            return;
        }

        int count = 1;

        for (final var entry : environmentsMap.entrySet()) {
            if (count == this.choice) {
                pluginManager.findAndMovePlugin(entry.getKey().getPlugin().getName(), entry.getValue());
                return;
            }
            count++;
        }
    }
}
