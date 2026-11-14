package restaurantrush.entity.staff;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

import java.util.ArrayList;
import java.util.List;

public class Host extends Staff{
    @Override
    public void performTask(Restaurant restaurant, RestaurantCLI cli) {
        List<Customer> waitingList = restaurant.getWaitingQueue().getQueue();

        boolean canSeat = (!waitingList.isEmpty() && !restaurant.getAvailableTables().isEmpty());
        cli.displayHostActions(waitingList, canSeat);

        List<String> actionsList = new ArrayList<>();
        if (canSeat) {
            actionsList.add("SEAT");
        }
        actionsList.add("WAIT");

        while (true) {
            String choice = cli.getPrompt(String.format("Available Actions: %s\n\nChoose Host Action: ", String.join(" / ", actionsList)));
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("SEAT")) {
                    seatCustomer(restaurant, cli);
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
}
