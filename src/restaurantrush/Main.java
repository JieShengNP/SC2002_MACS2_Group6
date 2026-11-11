package restaurantrush;

import restaurantrush.controller.GameController;

public class Main {

    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.start();
    }

}