package restaurantrush.entity.staff;

import restaurantrush.entity.restaurant.Restaurant;

import java.util.Scanner;

public class Cashier extends Staff {

    @Override
    public void performTask(Restaurant restaurant, Scanner scanner) {
        restaurant.takeNextPayment();
    }
}