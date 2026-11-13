package restaurantrush.controller;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.customer.RegularCustomer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.entity.restaurant.Table;
import restaurantrush.entity.restaurant.WaitingQueue;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class TurnController {
    private int nextCustomerId = 1;
    private final GameController controller;
    private final Scanner scanner = new Scanner(System.in);

    public TurnController(GameController controller) {
        this.controller = controller;
    }

    public void nextTurn() {
        handleArrivals();
        displayStartTurnStats();
        handleWaiterAction();
        handleChefAction();
        handlePayments();
        updateSatisfaction();
        displayEndTurnStats();
        updateCustomerStatus();

    }

    private void displayStartTurnStats() {
        Restaurant restaurant = controller.getRestaurant();
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        System.out.printf("\n\nStart of Turn: %d/%d\n", controller.getTurnNumber(), GameController.MAX_TURNS);
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

    private void handleArrivals() {

        if ((controller.getTurnNumber() % 3) == 1) {
            Restaurant restaurant = controller.getRestaurant();
            WaitingQueue queue = restaurant.getWaitingQueue();
            Customer newCustomer = new RegularCustomer(nextCustomerId++, restaurant.getMenuItemList(), controller.random);
            queue.addCustomer(newCustomer);
            restaurant.getCustomers().add(newCustomer);
        }
    }

    private void handleWaiterAction() {
        Restaurant restaurant = controller.getRestaurant();
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        List<Order> orderList = restaurant.getActiveOrders();
        boolean canSeat = false, canTake = false, canServe = false;
        System.out.println("\nAvailable Waiter Actions: ");
        if (!restaurant.getAvailableTables().isEmpty()) {
            for (Customer customer : waitingQueue.getQueue()) {
                System.out.printf("-Waiter: Seat Customer %d\n", customer.getCustomerId());
                canSeat = true;
            }
        }
        for (Customer customer : restaurant.getCustomers()) {
            if (customer.getStatus() == CustomerStatus.SEATED) {
                System.out.printf("-Waiter: Take Order from Customer %d\n", customer.getCustomerId());
                canTake = true;
            }
        }
        for (Order order : orderList) {
            if (order.getStatus() == OrderStatus.READY) {
                System.out.printf("-Waiter: Serve Order to Customer %d\n", order.getCustomer().getCustomerId());
                canServe = true;
            }
        }
        System.out.println("-Waiter: Wait");
        List<String> actionsList = new ArrayList<>();
        if (canSeat) {
            actionsList.add("SEAT");
        }
        if (canTake) {
            actionsList.add("TAKE");
        }
        if (canServe) {
            actionsList.add("SERVE");
        }
        actionsList.add("WAIT");
        boolean waiterActed = false;
        while (!waiterActed) {
            System.out.printf("Choose Waiter Action (%s): ", String.join("/", actionsList));
            String choice = scanner.nextLine();
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("SEAT")) {
                    while (!waiterActed) {
                        HashMap<String, Customer> customerHashMap = new HashMap<>();
                        for (Customer customer : waitingQueue.getQueue()) {
                            System.out.printf("Customer ID: %d | Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
                            customerHashMap.put(String.valueOf(customer.getCustomerId()), customer);
                        }
                        System.out.printf("Choose customer ID (%s): ", String.join("/", customerHashMap.keySet()));
                        String seatChoice = scanner.nextLine();
                        if (customerHashMap.containsKey(seatChoice)) {
                            Customer selectedCustomer = customerHashMap.get(seatChoice);
                            restaurant.seatCustomer(selectedCustomer);
                            waiterActed = true;
                        }
                    }
                } else if (choice.equalsIgnoreCase("TAKE")){
                    while (!waiterActed) {
                        HashMap<String, Customer> customerHashMap = new HashMap<>();
                        for (Customer customer : restaurant.getCustomers()) {
                            if (customer.getStatus() == CustomerStatus.SEATED) {
                                System.out.printf("Customer ID: %d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                                customerHashMap.put(String.valueOf(customer.getCustomerId()), customer);
                            }
                        }
                        System.out.printf("Choose customer ID (%s): ", String.join("/", customerHashMap.keySet()));
                        String seatChoice = scanner.nextLine();
                        if (customerHashMap.containsKey(seatChoice)) {
                            Customer selectedCustomer = customerHashMap.get(seatChoice);
                            restaurant.takeOrder(selectedCustomer);
                            waiterActed = true;
                        }
                    }
                }  else if (choice.equalsIgnoreCase("SERVE")) {
                    while (!waiterActed) {
                        HashMap<String, Order> customerHashMap = new HashMap<>();
                        for (Order order : restaurant.getActiveOrders()) {
                            if (order.getStatus() == OrderStatus.READY) {
                                Customer customer = order.getCustomer();
                                System.out.printf("Customer ID: %d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                                customerHashMap.put(String.valueOf(customer.getCustomerId()), order);
                            }
                        }
                        System.out.printf("Choose customer ID (%s): ", String.join("/", customerHashMap.keySet()));
                        String seatChoice = scanner.nextLine();
                        if (customerHashMap.containsKey(seatChoice)) {
                            Order selectedOrder = customerHashMap.get(seatChoice);
                            restaurant.serveOrder(selectedOrder);
                            waiterActed = true;
                        }
                    }
                } else if (choice.equalsIgnoreCase("WAIT")){
                    waiterActed = true;
                }
            } else {
                System.out.println("Invalid Choice! Pick again: ");
            }
        }


    }

    private void handleChefAction() {
        Restaurant restaurant = controller.getRestaurant();
        List<Order> orderList = restaurant.getCookableOrders();
        boolean canCook = false;
        System.out.println("\nAvailable Chef Actions: ");
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("-Chef: Cook Customer ID: %d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                canCook = true;
            }
        }
        System.out.println("-Chef: Wait");
        List<String> actionsList = new ArrayList<>();
        if (canCook) {
            actionsList.add("COOK");
        }
        actionsList.add("WAIT");
        boolean chefActed = false;
        while (!chefActed) {
            System.out.printf("Choose Chef Action (%s): ", String.join("/", actionsList));
            String choice = scanner.nextLine();
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("COOK")) {
                    while (!chefActed) {
                        HashMap<String, Order> customerHashMap = new HashMap<>();
                        for (Order order : orderList) {
                            Customer customer = order.getCustomer();
                            System.out.printf("-Chef: Cook Customer ID: %d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                            customerHashMap.put(String.valueOf(customer.getCustomerId()), order);
                        }
                        System.out.printf("Choose customer ID (%s): ", String.join("/", customerHashMap.keySet()));
                        String seatChoice = scanner.nextLine();
                        if (customerHashMap.containsKey(seatChoice)) {
                            Order selectedOrder = customerHashMap.get(seatChoice);
                            restaurant.cookOrder(selectedOrder);
                            chefActed = true;
                        }
                    }
                } else if (choice.equalsIgnoreCase("WAIT")){
                    chefActed = true;
                }
            } else {
                System.out.println("Invalid Choice! Pick again: ");
            }
        }
    }

    private void handlePayments() {
        Restaurant restaurant = controller.getRestaurant();
        restaurant.takeNextPayment();
    }

    private void updateSatisfaction() {
        List<Customer> customerList = controller.getRestaurant().getCustomers();
        for (Customer customer : customerList) {
            CustomerStatus status = customer.getStatus();
            if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED) {
                customer.decreaseSatisfaction();

            }
        }
    }

    private void displayEndTurnStats() {
        Restaurant restaurant = controller.getRestaurant();
        // TODO: SHOW SERVED COUNT?
        System.out.printf("\nEnd of Turn: %d/%d\n", controller.getTurnNumber(), GameController.MAX_TURNS);
        List<Customer> leavingCustomers = restaurant.removeLeavingCustomers();
        System.out.printf("Unhappy departures: %s\n", leavingCustomers.isEmpty()? "N/A" : "");
        for (Customer customer: leavingCustomers){
            System.out.printf("-Customer ID %d has left.\n", customer.getCustomerId());
        }
        System.out.printf("Current Revenue: $%s\n", restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)));
        System.out.printf("Average Satisfaction of Paid Customers: %s\n", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        // TODO: Show turns used
    }



    private void updateCustomerStatus() {
        Restaurant restaurant = controller.getRestaurant();
        for (Customer customer: restaurant.getCustomers()){
            if (customer.getStatus() == CustomerStatus.SERVED){
                customer.setStatus(CustomerStatus.EATING);
            } else if (customer.getStatus() == CustomerStatus.EATING){
                customer.setStatus(CustomerStatus.READY_TO_PAY);
                restaurant.addToPaymentQueue(customer);
            }
        }
    }
}