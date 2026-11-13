package restaurantrush.entity.staff;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.entity.restaurant.WaitingQueue;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Waiter extends Staff {

    @Override
    public void performTask(Restaurant restaurant, Scanner scanner) {
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
        while (true) {
            System.out.printf("Choose Waiter Action (%s): ", String.join("/", actionsList));
            String choice = scanner.nextLine();
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("SEAT")) {
                    seatCustomer(restaurant, scanner);
                    return;
                } else if (choice.equalsIgnoreCase("TAKE")){
                    takeCustomerOrder(restaurant, scanner);
                    return;
                }  else if (choice.equalsIgnoreCase("SERVE")) {
                    serveCustomerOrder(restaurant, scanner);
                    return;
                } else if (choice.equalsIgnoreCase("WAIT")){
                    return;
                }
            } else {
                System.out.println("Invalid Choice! Pick again: ");
            }
        }
    }

    private void seatCustomer(Restaurant restaurant, Scanner scanner){
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        while (true) {
            HashMap<String, Customer> customerHashMap = new HashMap<>();
            for (Customer customer : waitingQueue.getQueue()) {
                System.out.printf("Customer #%d | Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
                customerHashMap.put(String.valueOf(customer.getCustomerId()), customer);
            }
            System.out.printf("Choose customer #(%s): ", String.join("/", customerHashMap.keySet()));
            String seatChoice = scanner.nextLine();
            if (customerHashMap.containsKey(seatChoice)) {
                Customer selectedCustomer = customerHashMap.get(seatChoice);
                restaurant.seatCustomer(selectedCustomer);
                return;
            }
        }
    }

    private void takeCustomerOrder(Restaurant restaurant, Scanner scanner){
        while (true) {
            HashMap<String, Customer> customerHashMap = new HashMap<>();
            for (Customer customer : restaurant.getCustomers()) {
                if (customer.getStatus() == CustomerStatus.SEATED) {
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    customerHashMap.put(String.valueOf(customer.getCustomerId()), customer);
                }
            }
            System.out.printf("Choose customer #(%s): ", String.join("/", customerHashMap.keySet()));
            String seatChoice = scanner.nextLine();
            if (customerHashMap.containsKey(seatChoice)) {
                Customer selectedCustomer = customerHashMap.get(seatChoice);
                restaurant.takeOrder(selectedCustomer);
                return;
            }
        }
    }

    private void serveCustomerOrder(Restaurant restaurant, Scanner scanner){
        while (true) {
            HashMap<String, Order> customerHashMap = new HashMap<>();
            for (Order order : restaurant.getActiveOrders()) {
                if (order.getStatus() == OrderStatus.READY) {
                    Customer customer = order.getCustomer();
                    System.out.printf("Customer #%d | Order: %s\n", customer.getCustomerId(), customer.getMenuItem().getName());
                    customerHashMap.put(String.valueOf(customer.getCustomerId()), order);
                }
            }
            System.out.printf("Choose customer #(%s): ", String.join("/", customerHashMap.keySet()));
            String seatChoice = scanner.nextLine();
            if (customerHashMap.containsKey(seatChoice)) {
                Order selectedOrder = customerHashMap.get(seatChoice);
                restaurant.serveOrder(selectedOrder);
                return;
            }
        }
    }
}