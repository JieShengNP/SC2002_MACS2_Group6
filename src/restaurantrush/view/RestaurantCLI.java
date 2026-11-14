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

    private static final String LINE = "========================================";
    private static final String DIVIDER = "----------------------------------------";
    private final Scanner scanner = new Scanner(System.in);

    public String getPrompt(String message) {
        System.out.printf(message);
        return scanner.nextLine();
    }

    private void displayHeader(String title) {
        int width = LINE.length();
        int padding = (width - title.length()) / 2;

        System.out.println("\n" + LINE);
        System.out.printf("%" + (padding + title.length()) + "s%n", title);
        System.out.println(LINE);
    }

    private void displaySection(String title) {
        System.out.println("\n" + title.toUpperCase());
        System.out.println(DIVIDER);
    }

    private void displayStatistic(String label, String value) {
        System.out.printf("%-27s %s%n", label + ":", value);
    }

    public void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    public void displayStartTurnStats(Restaurant restaurant, int turnNumber) {
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        displayHeader(String.format("TURN %d / %d", turnNumber, GameController.MAX_TURNS));
        displaySection("Restaurant Overview");
        displayStatistic("Current Revenue", "$" + (restaurant.getRevenue() % 100 == 0 ? String.valueOf((restaurant.getRevenue() / 100)) : String.format("%.2f", (restaurant.getRevenue() * 0.01f))));
        displayStatistic("Average Paid Satisfaction", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));

        displaySection(String.format("Waiting Queue (%d)", waitingQueue.getQueue().size()));
        if (waitingQueue.isEmpty()) {
            System.out.println("No customers waiting.");
        } else {
            for (Customer customer : waitingQueue.getQueue()) {
                System.out.printf("Customer #%d | Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
            }
        }

        List<Table> occupiedTables = restaurant.getOccupiedTables();
        displaySection(String.format("Occupied Tables (%d)", occupiedTables.size()));
        if (occupiedTables.isEmpty()) {
            System.out.println("No occupied tables.");
        } else {
            for (Table table : occupiedTables) {
                Customer customer = table.getSeatCustomer();
                System.out.printf("Table #%d | Customer #%d | Satisfaction: %d | Status: %s\n", table.getTableNumber(), customer.getCustomerId(), customer.getSatisfaction(), customer.getStatus());
            }
        }

        List<Order> orderList = restaurant.getActiveOrders();
        displaySection(String.format("Active Orders (%d)", orderList.size()));
        if (orderList.isEmpty()) {
            System.out.println("No active orders.");
        } else {
            for (Order order : orderList) {
                System.out.printf("Customer: %d | Order: %s | %s ", order.getCustomer().getCustomerId(), order.getMenuItem().getName(), order.getStatus().name());
                if (order.getStatus() == OrderStatus.PREPARING) {
                    System.out.printf("(%d/%d)\n", order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                } else {
                    System.out.println();
                }
            }
        }

    }

    public void displayEndTurnStats(Restaurant restaurant, int turnNumber) {
        displayHeader(String.format("END OF TURN %d / %d", turnNumber, GameController.MAX_TURNS));
        displaySection("Turn Summary");
        displayStatistic("Customers Served", String.valueOf(restaurant.getPaidCustomer().size()));
        List<Customer> leavingCustomers = restaurant.removeLeavingCustomers();
        displayStatistic("Unhappy Departures", leavingCustomers.isEmpty() ? "0" : String.valueOf(leavingCustomers.size()));
        displayStatistic("Current Revenue", "$" + (restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f))));
        displayStatistic("Average Paid Satisfaction", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        displayStatistic("Turns Used", String.valueOf(turnNumber));
        displaySection("Customers Who Left");
        if (leavingCustomers.isEmpty()) {
            System.out.println("No customers left.");
        } else {
            for (Customer customer : leavingCustomers) {
                System.out.printf("Customer #%d | Satisfaction: %d", customer.getCustomerId(), customer.getSatisfaction());
            }
        }
        System.out.println(LINE);
    }

    public void displayEndGameResult(Restaurant restaurant) {
        int paidCustomers = restaurant.getPaidCustomer().size();
        int revenue = restaurant.getRevenue();
        double satisfaction = restaurant.getPaidAverageSatisfaction();

        boolean customersTarget = paidCustomers >= 4;
        boolean revenueTarget = revenue >= 4500;
        boolean satisfactionTarget = !Double.isNaN(satisfaction)
                && satisfaction >= 60;

        boolean victory = customersTarget
                && revenueTarget
                && satisfactionTarget;

        displayHeader("RESTAURANT RUSH - GAME OVER");

        System.out.println(victory
                ? "CONGRATULATIONS! YOU WON!"
                : "DEFEAT! YOU DID NOT MEET ALL TARGETS!");

        displaySection("Final Results");

        displayStatistic("Paid Customers", paidCustomers + " / 4");

        displayStatistic("Total Revenue",
                String.format("$%.2f / $45.00", revenue / 100.0));

        displayStatistic("Average Satisfaction",
                Double.isNaN(satisfaction)
                        ? "N/A"
                        : String.format("%.2f / 60.00", satisfaction));

        displaySection("Targets");

        System.out.printf("[%s] Paid at least 4 customers%n",
                customersTarget ? "PASS" : "FAIL");

        System.out.printf("[%s] Earned at least $45.00%n",
                revenueTarget ? "PASS" : "FAIL");

        System.out.printf("[%s] Average satisfaction >= 60%n",
                satisfactionTarget ? "PASS" : "FAIL");

        displayHeader(victory ? "FINAL RESULT: VICTORY" : "FINAL RESULT: DEFEAT");
    }

    public void displayInvalidChoice() {
        System.out.println("Invalid Choice! Pick again.");
    }

    public void displayChefActions(List<Order> orderList) {
        displayHeader("CHEF ACTIONS");
        if (!orderList.isEmpty()) {
            displaySection("Cookable Orders");
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("Customer #%d | %s | Preparation: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
            }
        } else {
            System.out.println("No orders available for cooking.");
        }
        System.out.println(DIVIDER);
    }

    public Order selectChefOrder(List<Order> orderList) {
        while (true) {
            displayHeader("SELECT ORDER TO COOK");
            List<String> choices = new ArrayList<>();
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("Customer #%d | %s | Preparation: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                choices.add(String.valueOf(customer.getCustomerId()));
            }
            System.out.println(DIVIDER);
            String customerChoice = getPrompt(String.format("Available Choices: %s\n\nChoose Customer: ", String.join(" / ", choices)));
            for (Order order : orderList) {
                if (String.valueOf(order.getCustomer().getCustomerId()).equals(customerChoice)) {
                    return order;
                }
            }
            displayInvalidChoice();
        }
    }

    public void displayWaiterActions(List<Customer> waitingQueue, List<Customer> seatedCustomers, List<Order> readyOrders, boolean haveEmptySeat) {
        displayHeader("WAITER ACTIONS");
        if (haveEmptySeat) {
            displaySection("Seat Customer");
            for (Customer customer : waitingQueue) {
                System.out.printf("-Waiter: Seat Customer %d\n", customer.getCustomerId());
            }
        }
        if (!seatedCustomers.isEmpty()) {
            displaySection("Take Order");
            for (Customer customer : seatedCustomers) {
                System.out.printf("-Waiter: Take Order from Customer %d\n", customer.getCustomerId());

            }
        }
        if (!readyOrders.isEmpty()) {
            displaySection("Serve Order");
            for (Order order : readyOrders) {
                System.out.printf("-Waiter: Serve Order to Customer %d\n", order.getCustomer().getCustomerId());
            }
        }
//        System.out.println("-Waiter: Wait");
        System.out.println(DIVIDER);
    }

    public Customer selectWaiterSeat(List<Customer> waitingList) {
        while (true) {
            displayHeader("SELECT CUSTOMER TO SEAT");
            List<String> choices = new ArrayList<>();
            for (Customer customer : waitingList) {
                System.out.printf("Customer #%d | Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
                choices.add(String.valueOf(customer.getCustomerId()));
            }
            System.out.println(DIVIDER);
            String seatChoice = getPrompt(String.format("Available Choices: %s\n\nChoose Customer: ", String.join("/", choices)));
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
            displayHeader("SELECT CUSTOMER TO TAKE ORDER");
            List<String> choices = new ArrayList<>();
            for (Customer customer : seatedList) {
                if (customer.getStatus() == CustomerStatus.SEATED) {
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    choices.add(String.valueOf(customer.getCustomerId()));
                }
            }
            System.out.println(DIVIDER);
            String seatChoice = getPrompt(String.format("Available Choices: %s\n\nChoose Customer: ", String.join("/", choices)));
            for (Customer customer : seatedList) {
                if (String.valueOf(customer.getCustomerId()).equals(seatChoice)) {
                    return customer;
                }
            }
            displayInvalidChoice();
        }
    }

    public Order selectWaiterServe(List<Order> readyOrderList) {
        while (true) {
            displayHeader("SELECT CUSTOMER TO SERVE TO");
            List<String> choices = new ArrayList<>();
            for (Order order : readyOrderList) {
                if (order.getStatus() == OrderStatus.READY) {
                    Customer customer = order.getCustomer();
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    choices.add(String.valueOf(customer.getCustomerId()));
                }
            }
            System.out.println(DIVIDER);
            String seatChoice = getPrompt(String.format("Available Choices: %s\n\nChoose Customer: ", String.join("/", choices)));
            for (Order order : readyOrderList) {
                if (String.valueOf(order.getCustomer().getCustomerId()).equals(seatChoice)) {
                    return order;
                }
            }
            displayInvalidChoice();
        }
    }
}
