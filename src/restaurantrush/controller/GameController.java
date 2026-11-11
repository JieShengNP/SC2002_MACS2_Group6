package restaurantrush.controller;

import restaurantrush.entity.restaurant.Restaurant;

import java.util.Random;

public class GameController {

    public static final int MAX_TURNS = 18;
    public static final long GAME_SEED = 2002L;
    protected Random random;

    private final Restaurant restaurant;
    private int turnNumber = 0;

    public GameController() {
        restaurant = new Restaurant();
        random = new Random(GAME_SEED);
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