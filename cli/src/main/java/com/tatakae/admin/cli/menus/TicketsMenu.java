package com.tatakae.admin.cli.menus;

import com.tatakae.admin.cli.TicketType;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.services.RoomService;
import com.tatakae.admin.core.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class TicketsMenu extends AbstractMenu {

    private static TicketsMenu instance = null;

    private ArrayList<Room> tickets;

    private Map<Integer, Room> displayedTickets;

    public static TicketsMenu getInstance(MenuInterface parent) {
        if (instance == null) {
            instance =  new TicketsMenu(parent);
        }
        return instance;
    }


    private TicketsMenu(MenuInterface parent) {
        super(parent);
    }

    @Override
    public void run() {
        String choice;
        boolean isValid;

        loadOpenedTickets();

        Scanner sc = new Scanner(System.in);

        do {
            displayTitle("TICKETS");
            System.out.println("0 - Previous\t1 - Opened\t2 - In progress\t3 - Closed\t4 - Assigned to me\n");
            System.out.print("Choose your action: ");

            choice = sc.next();
            isValid = isValidChoice(choice, 0, 4);

            if (!isValid) {
                System.err.println("Error: Choice invalid, please enter a valid one.");
            }
        } while (!isValid);

        loadChoice(this.choice);

        if (this.choice != 0) {
            System.out.println();

            if (!displayedTickets.entrySet().isEmpty()) {
                System.out.print("Do you want to open one of the tickets? (y/n) ");
                final var c = sc.next();

                if (isYes(c)) {
                    System.out.print("Enter the ticket number you want to open: ");

                    int ticketKey;
                    try {
                        ticketKey = Integer.parseInt(sc.next());
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Choice invalid, please enter a valid one.");
                        ticketKey = -1;
                    }

                    if (displayedTickets.containsKey(ticketKey)) {
                        final var ticketMenu = TicketMenu.getInstance(this, displayedTickets.get(ticketKey));
                        ticketMenu.run();
                    }
                }
            } else {
                System.out.println("No ticket found.");
            }
            run();
        }
    }

    private void setDisplayedTickets(final String type) {
        int count = 1;

        displayedTickets = new HashMap<>();

        if (!type.equals(TicketType.ASSIGNED_TO_ME.name().toLowerCase())) {
            for (var ticket : tickets) {
                if (ticket.getStatus().equals(type)) {
                    displayedTickets.put(count, ticket);
                    count++;
                }
            }
        } else {
            final var currentUser = UserService.getUserByToken(LocalDataManager.getToken());

            for (var ticket : tickets) {
                if (ticket.getAssignedTo() != null) {
                    if (ticket.getAssignedTo().getId().equals(currentUser.getId())) {
                        displayedTickets.put(count, ticket);
                        count++;
                    }
                }
            }
        }
    }

    private void display() {
        System.out.println();

        for (final var entry : displayedTickets.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().getName());
        }
    }

    private void loadOpenedTickets() {
        try {
            this.tickets = RoomService.getAllTickets();

            final String ticketType = TicketType.OPENED.name().toLowerCase();

            setDisplayedTickets(ticketType);

        } catch (FailedParsingJsonException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadInProgressTickets() {
        try {
            this.tickets = RoomService.getAllTickets();
            final String ticketType = TicketType.IN_PROGRESS.name().toLowerCase().replace("_", " ");
            setDisplayedTickets(ticketType);

        } catch (FailedParsingJsonException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadClosedTickets() {
        try {
            this.tickets = RoomService.getAllTickets();
            final String ticketType = TicketType.CLOSED.name().toLowerCase();

            setDisplayedTickets(ticketType);

        } catch (FailedParsingJsonException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadMyTickets() {
        try {
            this.tickets = RoomService.getAllTickets();

            final String ticketType = TicketType.ASSIGNED_TO_ME.name().toLowerCase();

            setDisplayedTickets(ticketType);

        } catch (FailedParsingJsonException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void loadChoice(Integer choice) {
        switch (choice) {
            case 0:
                parent.run();
                break;
            case 1:
                loadOpenedTickets();
                break;
            case 2:
                loadInProgressTickets();
                break;
            case 3:
                loadClosedTickets();
                break;
            case 4:
                loadMyTickets();
                break;
            default:
                menuSeparator();
                System.out.println("See you soon!");
                exit(0);
        }
        display();
    }

    private boolean isYes(final String choice) {
        return choice.equals("y")
                || choice.equals("Y")
                || choice.equals("yes")
                || choice.equals("Yes")
                || choice.equals("YES");
    }
}
