package restaurantrush.entity.staff;

import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

import java.util.ArrayList;
import java.util.List;

public class Chef extends Staff {

    @Override
    public void performTask(Restaurant restaurant, RestaurantCLI cli) {
        List<Order> orderList = restaurant.getCookableOrders();
        cli.displayChefActions(orderList);
        List<String> actionsList = new ArrayList<>();
        if (!orderList.isEmpty()) {
            actionsList.add("COOK");
        }
        actionsList.add("WAIT");
        while (true) {
            String choice = cli.getPrompt(String.format("Choose Chef Action (%s): ", String.join("/", actionsList)));
            if (actionsList.stream().anyMatch(choice::equalsIgnoreCase)) {
                if (choice.equalsIgnoreCase("COOK")) {
                    cookOrder(restaurant, cli);
                    return;
                } else if (choice.equalsIgnoreCase("WAIT")){
                    return;
                }
            } else {
                cli.displayInvalidChoice();
            }
        }
    }

    private void cookOrder(Restaurant restaurant, RestaurantCLI cli){
        List<Order> orderList = restaurant.getCookableOrders();
        Order selectedOrder = cli.selectChefOrder(orderList);
        restaurant.cookOrder(selectedOrder);
    }

}