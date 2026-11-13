package restaurantrush.view;

import restaurantrush.controller.GameController;
import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.entity.restaurant.Table;
import restaurantrush.entity.restaurant.WaitingQueue;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RestaurantCLI {

    private final Scanner scanner = new Scanner(System.in);

    public String getPrompt(String message) {
        System.out.printf(message);
        return scanner.nextLine();
    }

    public void displayMessage(String message) {
        System.out.print(message);
    }

    public void displayStartTurnStats(Restaurant restaurant, int turnNumber) {
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        System.out.printf("\n====================\nStart of Turn: %d/%d\n", turnNumber, GameController.MAX_TURNS);
        System.out.printf("Current Revenue: $%s\n", restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)));
        System.out.printf("Average Satisfaction of Paid Customers: %s\n", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        System.out.printf("Waiting Queue: %s\n", (waitingQueue.isEmpty() ? "None" : ""));
        for (Customer customer : waitingQueue.getQueue()) {
            System.out.printf("-Customer #%d: Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
        }

        System.out.printf("Occupied Tables: %s\n", restaurant.getOccupiedTableCount() == 0 ? "None" : restaurant.getOccupiedTableCount());
        for (Table table : restaurant.getTables()) {
            if (table.isOccupied()) {
                Customer customer = table.getSeatCustomer();
                System.out.printf("-Table #%d: | Customer #%d: Status: %s | Satisfaction: %d\n", table.getTableNumber(), customer.getCustomerId(), customer.getStatus(), customer.getSatisfaction());
            }
        }

        List<Order> orderList = restaurant.getActiveOrders();
        System.out.printf("Active Orders: %s\n", orderList.isEmpty() ? "None" : orderList.size());
        for (Order order : orderList) {
            System.out.printf("-Order: %s | Customer: %d | Status: %s ", order.getMenuItem().getName(), order.getCustomer().getCustomerId(), order.getStatus().name());
            if (order.getStatus() == OrderStatus.PREPARING) {
                System.out.printf("Preparing %d/%d\n", order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
            } else {
                System.out.println();
            }
        }

    }

    public void displayEndTurnStats(Restaurant restaurant, int turnNumber) {
        System.out.printf("\nEnd of Turn: %d/%d\n", turnNumber, GameController.MAX_TURNS);
        System.out.printf("Total Served Count: %d\n", restaurant.getServedCount());
        List<Customer> leavingCustomers = restaurant.removeLeavingCustomers();
        System.out.printf("Unhappy departures: %s\n", leavingCustomers.isEmpty() ? "N/A" : "");
        for (Customer customer : leavingCustomers) {
            System.out.printf("-Customer #%d has left.\n", customer.getCustomerId());
        }
        System.out.printf("Current Revenue: $%s\n", restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)));
        System.out.printf("Average Satisfaction of Paid Customers: %s\n", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        System.out.printf("Turns used: %d\n====================\n", turnNumber);
    }

    public void displayEndGameResult(Restaurant restaurant) {
        System.out.println("\nEND OF RESTAURANT RUSH\n");
        if (restaurant.getPaidCustomer().size() >= 4 && restaurant.getRevenue() >= 4500 && restaurant.getPaidAverageSatisfaction() >= 60) {
            System.out.println("Victory! You met all 3 targets!");
        } else {
            System.out.println("Defeat! You did not meet all 3 targets!");
        }
        System.out.printf("Stats: \n-Paid Customers: %d\n-Revenue: $%s\n-Average Satisfaction: %.2f", restaurant.getPaidCustomer().size(), restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)), restaurant.getPaidAverageSatisfaction());

    }

    public void displayInvalidChoice() {
        System.out.println("Invalid Choice! Pick again.");
    }

    public void displayChefActions(List<Order> orderList) {
        System.out.println("\nAvailable Chef Actions: ");
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("-Chef: Cook Customer #%d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
            }
        }
        System.out.println("-Chef: Wait");
    }

    public Order selectChefOrder(List<Order> orderList) {
        while (true) {
            List<String> choices = new ArrayList<>();
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("-Chef: Cook Customer #%d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                choices.add(String.valueOf(customer.getCustomerId()));
            }
            String customerChoice = getPrompt(String.format("Choose customer #(%s): ", String.join("/", choices)));
            for (Order order : orderList) {
                if (String.valueOf(order.getCustomer().getCustomerId()).equals(customerChoice)) {
                    return order;
                }
            }
            displayInvalidChoice();
        }
    }

    public void displayWaiterActions(List<Customer> waitingQueue, List<Customer> seatedCustomers, List<Order> readyOrders, boolean haveEmptySeat) {
        System.out.println("\nAvailable Waiter Actions: ");
        if (haveEmptySeat) {
            for (Customer customer : waitingQueue) {
                System.out.printf("-Waiter: Seat Customer %d\n", customer.getCustomerId());
            }
        }
        for (Customer customer : seatedCustomers) {
            System.out.printf("-Waiter: Take Order from Customer %d\n", customer.getCustomerId());

        }
        for (Order order : readyOrders) {
            System.out.printf("-Waiter: Serve Order to Customer %d\n", order.getCustomer().getCustomerId());
        }
        System.out.println("-Waiter: Wait");
    }

    public Customer selectWaiterSeat(List<Customer> waitingList) {
        while (true) {
            List<String> choices = new ArrayList<>();
            for (Customer customer : waitingList) {
                System.out.printf("Customer #%d | Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
                choices.add(String.valueOf(customer.getCustomerId()));
            }
            String seatChoice = getPrompt(String.format("Choose customer #(%s): ", String.join("/", choices)));
            for (Customer customer : waitingList) {
                if (String.valueOf(customer.getCustomerId()).equals(seatChoice)) {
                    return customer;
                }
            }
            displayInvalidChoice();
        }
    }

    public Customer selectWaiterTake(List<Customer> seatedList) {
        while (true) {
            List<String> choices = new ArrayList<>();
            for (Customer customer : seatedList) {
                if (customer.getStatus() == CustomerStatus.SEATED) {
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    choices.add(String.valueOf(customer.getCustomerId()));
                }
            }
            String seatChoice = getPrompt(String.format("Choose customer #(%s): ", String.join("/", choices)));
            for (Customer customer : seatedList) {
                if (String.valueOf(customer.getCustomerId()).equals(seatChoice)) {
                    return customer;
                }
            }
            displayInvalidChoice();
        }
    }
    public Order selectWaiterServe(List<Order> readyOrderList){
        while (true) {
            List<String> choices = new ArrayList<>();
            for (Order order : readyOrderList) {
                if (order.getStatus() == OrderStatus.READY) {
                    Customer customer = order.getCustomer();
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    choices.add(String.valueOf(customer.getCustomerId()));
                }
            }
            String seatChoice = getPrompt(String.format("Choose customer #(%s): ", String.join("/", choices)));
            for (Order order : readyOrderList) {
                if (String.valueOf(order.getCustomer().getCustomerId()).equals(seatChoice)) {
                    return order;
                }
            }
            displayInvalidChoice();
        }
    }
}
