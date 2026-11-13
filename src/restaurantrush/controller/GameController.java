package restaurantrush.controller;

import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.view.RestaurantCLI;

import java.util.Random;

public class GameController {

    public static final int MAX_TURNS = 18;
    public static final long GAME_SEED = 2002L;
    protected Random random;

    private final Restaurant restaurant;
    private final RestaurantCLI cli;

    public GameController() {
        restaurant = new Restaurant();
        random = new Random(GAME_SEED);
        cli = new RestaurantCLI();
    }

    public void start() {
        TurnController turnController = new TurnController(restaurant, cli, random);
        for (int i = 1; i <= MAX_TURNS; i++) {
            turnController.nextTurn(i);
        }
        end();
    }

    private void end(){
        cli.displayEndGameResult(restaurant);
    }

}