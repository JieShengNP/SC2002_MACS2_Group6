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
        end();
    }

    private void end(){
        System.out.println("\nEND OF RESTAURANT RUSH\n");
        if (restaurant.getPaidCustomer().size() >= 4 && restaurant.getRevenue() >= 4500 && restaurant.getPaidAverageSatisfaction() >= 60){
            System.out.println("Victory! You met all 3 targets!");
        } else {
            System.out.println("Defeat! You did not meet all 3 targets!");
        }
        System.out.printf("Stats: \n-Paid Customers: %d\n-Revenue: %s\n-Average Satisfaction: %.2f", restaurant.getPaidCustomer().size(), restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)), restaurant.getPaidAverageSatisfaction());
    }

}