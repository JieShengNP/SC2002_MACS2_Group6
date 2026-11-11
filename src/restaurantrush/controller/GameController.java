package restaurantrush.controller;

import restaurantrush.entity.restaurant.Restaurant;

public class GameController {

    public static final int MAX_TURNS = 18;

    private Restaurant restaurant;
    private int turnNumber = 0;

    public GameController() {
        restaurant = new Restaurant();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void start() {
        TurnController turnController = new TurnController(this);
        for (int i = 1; i <= MAX_TURNS; i++) {
            turnNumber = i;
            turnController.nextTurn();
        }
    }

}