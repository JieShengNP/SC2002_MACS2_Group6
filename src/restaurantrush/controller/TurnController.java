package restaurantrush.controller;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.customer.RegularCustomer;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.entity.restaurant.WaitingQueue;
import restaurantrush.entity.staff.Staff;
import restaurantrush.view.RestaurantCLI;

import java.util.List;
import java.util.Random;

public class TurnController {
    private int nextCustomerId = 1;
    private final Restaurant restaurant;
    private final RestaurantCLI cli;
    private final Random random;

    public TurnController(Restaurant restaurant, RestaurantCLI cli, Random random) {
        this.restaurant = restaurant;
        this.cli = cli;
        this.random = random;
    }

    public void nextTurn(int turnNumber) {
        handleArrivals(turnNumber);
        cli.displayStartTurnStats(restaurant, turnNumber);
        handleStaffActions();
        updateSatisfaction();
        cli.displayEndTurnStats(restaurant, turnNumber);
        updateCustomerStatus();

    }


    private void handleArrivals(int turnNumber) {
        if ((turnNumber % 3) == 1) {
            WaitingQueue queue = restaurant.getWaitingQueue();
            Customer newCustomer = new RegularCustomer(nextCustomerId++, restaurant.getMenuItemList(), random);
            queue.addCustomer(newCustomer);
            restaurant.getCustomers().add(newCustomer);
        }
    }

    private void handleStaffActions() {
        for (Staff staff : restaurant.getStaffList()){
            staff.performTask(restaurant, cli);
        }
    }

    private void updateSatisfaction() {
        List<Customer> customerList = restaurant.getCustomers();
        for (Customer customer : customerList) {
            customer.updateSatisfaction();
        }
    }


    private void updateCustomerStatus() {
        for (Customer customer : restaurant.getCustomers()) {
            if (customer.updateServePayState()){
                restaurant.addToPaymentQueue(customer);
            }
        }
    }
}