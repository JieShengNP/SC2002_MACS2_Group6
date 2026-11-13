package restaurantrush.entity.staff;

import restaurantrush.entity.restaurant.Restaurant;

import java.util.Scanner;

public abstract class Staff {
    public abstract void performTask(Restaurant restaurant, Scanner scanner);

}