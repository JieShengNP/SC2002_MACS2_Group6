package restaurantrush.entity.staff;

import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

public abstract class Staff {
    public abstract void performTask(Restaurant restaurant, RestaurantCLI cli);

}