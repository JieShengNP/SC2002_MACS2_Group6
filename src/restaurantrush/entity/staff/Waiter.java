package restaurantrush.entity.staff;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

import java.util.ArrayList;
import java.util.List;

public class Waiter extends Staff {

    @Override
    public void performTask(Restaurant restaurant, RestaurantCLI cli) {
        List<Customer> waitingList = restaurant.getWaitingQueue().getQueue();
        List<Customer> seatedList = restaurant.getSeatedCustomers();
        List<Order> readyOrders = restaurant.getReadyOrders();

        boolean canSeat = (!waitingList.isEmpty() && !restaurant.getAvailableTables().isEmpty());
        cli.displayWaiterActions(waitingList, seatedList, readyOrders, canSeat);

        List<String> actionsList = new ArrayList<>();
        if (canSeat) {
            actionsList.add("SEAT");
        }
        if (!seatedList.isEmpty()) {
            actionsList.add("TAKE");
        }
        if (!readyOrders.isEmpty()) {
            actionsList.add("SERVE");
        }
        actionsList.add("WAIT");

        while (true) {
            String choice = cli.getPrompt(String.format("Choose Waiter Action (%s): ", String.join("/", actionsList)));
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("SEAT")) {
                    seatCustomer(restaurant, cli);
                    return;
                } else if (choice.equalsIgnoreCase("TAKE")) {
                    takeCustomerOrder(restaurant, cli);
                    return;
                } else if (choice.equalsIgnoreCase("SERVE")) {
                    serveCustomerOrder(restaurant, cli);
                    return;
                } else if (choice.equalsIgnoreCase("WAIT")) {
                    return;
                }
            } else {
                cli.displayInvalidChoice();
            }
        }
    }

    private void seatCustomer(Restaurant restaurant, RestaurantCLI cli) {
        List<Customer> waitingList = restaurant.getWaitingQueue().getQueue();
        Customer selectedCustomer = cli.selectWaiterSeat(waitingList);
        restaurant.seatCustomer(selectedCustomer);
    }

    private void takeCustomerOrder(Restaurant restaurant, RestaurantCLI cli) {
        List<Customer> seatedList = restaurant.getSeatedCustomers();
        Customer selectedCustomer = cli.selectWaiterTake(seatedList);
        restaurant.takeOrder(selectedCustomer);
    }

    private void serveCustomerOrder(Restaurant restaurant, RestaurantCLI cli) {
        List<Order> readyOrders = restaurant.getReadyOrders();
        Order selectedOrder = cli.selectWaiterServe(readyOrders);
        restaurant.serveOrder(selectedOrder);
    }
}