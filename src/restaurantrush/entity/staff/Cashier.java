package restaurantrush.entity.staff;

import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

public class Cashier extends Staff {

    @Override
    public void performTask(Restaurant restaurant, RestaurantCLI cli) {
        restaurant.takeNextPayment();
    }
}