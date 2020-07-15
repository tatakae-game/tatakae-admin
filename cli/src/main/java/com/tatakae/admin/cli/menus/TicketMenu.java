package com.tatakae.admin.cli.menus;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.models.User;
import com.tatakae.admin.core.services.RoomService;
import com.tatakae.admin.core.services.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class TicketMenu extends AbstractMenu {

    private final Room ticket;

    private Map<Integer, String> statusOptions;

    private Map<Integer, User> assignedToOptions;

    private static TicketMenu instance = null;

    public static TicketMenu getInstance(MenuInterface parent, final Room ticket) {
        if (instance == null) {
            return new TicketMenu(parent, ticket);
        }
        return instance;
    }

    protected TicketMenu(MenuInterface parent, final Room ticket) {
        super(parent);
        this.ticket = ticket;
        this.statusOptions = new HashMap<>();
        this.assignedToOptions = new HashMap<>();

        this.statusOptions.put(1, "opened");
        this.statusOptions.put(2, "in progress");
        this.statusOptions.put(3, "closed");
    }

    @Override
    public void run() {

        String choice;
        boolean isValid;

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("TICKET N°: " + this.ticket.getId());

            final var assignedTo = ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : "";
            final var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            System.out.println("Name: " + ticket.getName());
            System.out.println("Status: " + ticket.getStatus());
            System.out.println("Assigned to: " + assignedTo);
            System.out.println("Created: " + ticket.getCreated().format(dateTimeFormatter));
            System.out.println();

            System.out.println("0 - Previous\t1 - Update 'status' field\t" +
                    "2 - Update 'assigned to' field\t3 - Open Chat\n");

            System.out.print("Choose your action: ");

            choice = sc.next();
            isValid = isValidChoice(choice, 0, 3);

            if (!isValid) {
                menuSeparator();
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }
        } while (!isValid);

        loadChoice(this.choice);

    }

    @Override
    public void loadChoice(Integer choice) {
        switch (choice) {
            case 0:
                parent.run();
                break;
            case 1:
                updateStatus();
                break;
            case 2:
                System.out.println("case 2: update assigned to field");
                updateAssignedTo();
                break;
            case 3:
                System.out.println("case 3: open chat room");
                break;
            default:
                menuSeparator();
                System.out.println("See you soon!");
                exit(0);
        }
    }

    private void updateStatus() {
        String choice;
        Scanner sc = new Scanner(System.in);
        boolean isValid;

        do {
            System.out.println("\n0 - Cancel\t1 - Opened\t2 - In progress\t3 - Closed\n");
            System.out.print("Choose your action: ");

            choice = sc.next();

            isValid = isValidChoice(choice, 0, 3);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
                menuSeparator();
            }
        } while (!isValid);

        if (statusOptions.containsKey(this.choice)) {
            try {
                boolean success = RoomService.updateStatus(ticket.getId(), statusOptions.get(this.choice));

                if (success) {
                    this.ticket.setStatus(statusOptions.get(this.choice));
                }

            } catch (FailedParsingJsonException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        run();
    }

    private void updateAssignedTo() {
        String choice;
        Scanner sc = new Scanner(System.in);
        boolean isValid;

        ArrayList<User> admins;
        try {
            admins = UserService.getAdministrators();
        } catch (FailedParsingJsonException e) {
            admins = new ArrayList<>();
        }

        for (int i = 0; i < admins.size(); i++) {
            assignedToOptions.put(i + 1, admins.get(i));
        }

        do {
            System.out.print("0 - Cancel\t");

            for (final var entry : assignedToOptions.entrySet()) {
                System.out.print(entry.getKey() + " - " + entry.getValue().getUsername() + "\t");
            }

            System.out.println("\n");
            System.out.print("Choose your action: ");

            choice = sc.next();

            isValid = isValidChoice(choice, 0, admins.size());

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
                menuSeparator();
            }
        } while (!isValid);

        if (assignedToOptions.containsKey(this.choice)) {
            try {
                boolean success = RoomService
                        .updateAssignedTo(ticket.getId(), assignedToOptions.get(this.choice).getId());

                if (success) {
                    this.ticket.setAssignedTo(assignedToOptions.get(this.choice));
                }

            } catch (FailedParsingJsonException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        run();
    }
}
