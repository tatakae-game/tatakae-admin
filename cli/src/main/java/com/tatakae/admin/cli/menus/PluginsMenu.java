package com.tatakae.admin.cli.menus;

import com.tatakae.admin.core.PluginManager;

import java.io.File;
import java.util.*;

import static java.lang.System.exit;

public class PluginsMenu extends AbstractMenu {

    private final PluginManager pluginManager;

    private Map<String, String> pluginMap;

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
    }

    @Override
    public void run() {
        String choice;
        boolean isValid;

        Map<String, String> tmp = new HashMap<>();
        for(final var entry : pluginManager.environments.entrySet()) {
            tmp.put(entry.getKey().getPlugin().getName(), entry.getValue());
        }

        pluginMap = new TreeMap<>(tmp);

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
            case 3:
                showPluginDetails();
                break;
            case 4:
                importPlugin();
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
        for (final var entry : pluginMap.entrySet()) {
            final var status = entry.getValue().equals("Enable") ? "Inactive" : "Active";

            System.out.print(count + " | ");
            System.out.print(entry.getKey());
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
            System.err.println("Error: Invalid choice.");
            return;
        }

        int count = 1;

        for (final var entry : pluginMap.entrySet()) {
            if (count == this.choice) {
                pluginManager.findAndMovePlugin(entry.getKey(), entry.getValue());
                return;
            }
            count++;
        }
    }

    private void showPluginDetails() {
        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.print("Choose a plugin: ");

        choice = sc.next();
        isValid = isValidChoice(choice, 1, pluginManager.environments.size());

        if (!isValid) {
            menuSeparator();
            System.err.println("Error: Invalid choice.");
            return;
        }

        int count = 1;

        for (final var entry : pluginMap.entrySet()) {
            if (count == this.choice) {
                for (final var env : pluginManager.environments.entrySet())
                    if (entry.getKey().equals(env.getKey().getPlugin().getName())) {
                        System.out.println("[NAME]: " + entry.getKey());
                        System.out.println("[DESCRIPTION]: " + env.getKey().getPlugin().getDescription());
                    }
                return;
            }
            count++;
        }
    }

    private void importPlugin() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter plugin file location: ");

        String s = scanner.next();

        if (s.isEmpty() || s.isBlank()) {
            System.err.println("Error: Path cannot be empty");
            return;
        }

        boolean success = pluginManager.importPlugin(new File(s));

        if (success) {
            System.out.println("\nNew plugin added.");
        } else {
            System.err.println("Possible error:");
            System.err.println("\t- The file chosen is not a valid plugin.");
            System.err.println("\t- The plugin is already present.");
            System.err.println("\t- The file does not exist.\n");
        }
    }
}
