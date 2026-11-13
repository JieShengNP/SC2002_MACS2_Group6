package restaurantrush.entity.staff;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Chef extends Staff {

    @Override
    public void performTask(Restaurant restaurant, Scanner scanner) {
        List<Order> orderList = restaurant.getCookableOrders();
        boolean canCook = false;
        System.out.println("\nAvailable Chef Actions: ");
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("-Chef: Cook Customer #%d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                canCook = true;
            }
        }
        System.out.println("-Chef: Wait");
        List<String> actionsList = new ArrayList<>();
        if (canCook) {
            actionsList.add("COOK");
        }
        actionsList.add("WAIT");
        while (true) {
            System.out.printf("Choose Chef Action (%s): ", String.join("/", actionsList));
            String choice = scanner.nextLine();
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("COOK")) {
                    cookOrder(restaurant, scanner);
                    return;
                } else if (choice.equalsIgnoreCase("WAIT")){
                    return;
                }
            } else {
                System.out.println("Invalid Choice! Pick again: ");
            }
        }
    }

    private void cookOrder(Restaurant restaurant, Scanner scanner){
        List<Order> orderList = restaurant.getCookableOrders();
        while (true) {
            HashMap<String, Order> customerHashMap = new HashMap<>();
            for (Order order : orderList) {
                Customer customer = order.getCustomer();
                System.out.printf("-Chef: Cook Customer #%d's %s: %d/%d\n", customer.getCustomerId(), order.getMenuItem().getName(), order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
                customerHashMap.put(String.valueOf(customer.getCustomerId()), order);
            }
            System.out.printf("Choose customer #(%s): ", String.join("/", customerHashMap.keySet()));
            String seatChoice = scanner.nextLine();
            if (customerHashMap.containsKey(seatChoice)) {
                Order selectedOrder = customerHashMap.get(seatChoice);
                restaurant.cookOrder(selectedOrder);
                return;
            }
        }
    }

}