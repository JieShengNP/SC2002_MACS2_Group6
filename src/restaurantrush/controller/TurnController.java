package restaurantrush.controller;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.customer.RegularCustomer;
import restaurantrush.entity.restaurant.Order;
import restaurantrush.entity.restaurant.Restaurant;
import restaurantrush.entity.restaurant.Table;
import restaurantrush.entity.restaurant.WaitingQueue;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.List;
import java.util.Scanner;

public class TurnController {
    private int nextCustomerId = 1;
    private final GameController controller;
    private final Scanner scanner = new Scanner(System.in);

    public TurnController(GameController controller) {
        this.controller = controller;
    }

    public void nextTurn() {
        handleArrivals();
        displayStartTurnStats();
        handleStaffActions();
        updateSatisfaction();
        displayEndTurnStats();
        updateCustomerStatus();

    }

    private void displayStartTurnStats() {
        Restaurant restaurant = controller.getRestaurant();
        WaitingQueue waitingQueue = restaurant.getWaitingQueue();
        System.out.printf("\n====================\nStart of Turn: %d/%d\n", controller.getTurnNumber(), GameController.MAX_TURNS);
        System.out.printf("Current Revenue: $%s\n", restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)));
        System.out.printf("Average Satisfaction of Paid Customers: %s\n", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        System.out.printf("Waiting Queue: %s\n", (waitingQueue.isEmpty() ? "None" : ""));
        for (Customer customer : waitingQueue.getQueue()) {
            System.out.printf("-Customer #%d: Satisfaction: %d\n", customer.getCustomerId(), customer.getSatisfaction());
        }

        System.out.printf("Occupied Tables: %s\n", restaurant.getOccupiedTableCount() == 0 ? "None" : restaurant.getOccupiedTableCount());
        for (Table table : restaurant.getTables()) {
            if (table.isOccupied()) {
                Customer customer = table.getSeatCustomer();
                System.out.printf("-Table #%d: | Customer #%d: Status: %s | Satisfaction: %d\n", table.getTableNumber(), customer.getCustomerId(), customer.getStatus(), customer.getSatisfaction());
            }
        }

        List<Order> orderList = restaurant.getActiveOrders();
        System.out.printf("Active Orders: %s\n", orderList.isEmpty() ? "None" : orderList.size());
        for (Order order : orderList) {
            System.out.printf("-Order: %s | Customer: %d | Status: %s ", order.getMenuItem().getName(), order.getCustomer().getCustomerId(), order.getStatus().name());
            if (order.getStatus() == OrderStatus.PREPARING) {
                System.out.printf("Preparing %d/%d\n", order.getCurrentPreparationUnits(), order.getRequiredPreparationUnits());
            } else {
                System.out.println();
            }
        }

    }

    private void handleArrivals() {

        if ((controller.getTurnNumber() % 3) == 1) {
            Restaurant restaurant = controller.getRestaurant();
            WaitingQueue queue = restaurant.getWaitingQueue();
            Customer newCustomer = new RegularCustomer(nextCustomerId++, restaurant.getMenuItemList(), controller.random);
            queue.addCustomer(newCustomer);
            restaurant.getCustomers().add(newCustomer);
        }
    }

    private void handleStaffActions(){
        Restaurant restaurant = controller.getRestaurant();
        restaurant.staffWork(scanner);
    }

    private void updateSatisfaction() {
        List<Customer> customerList = controller.getRestaurant().getCustomers();
        for (Customer customer : customerList) {
            CustomerStatus status = customer.getStatus();
            if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED) {
                customer.decreaseSatisfaction();

            }
        }
    }

    private void displayEndTurnStats() {
        Restaurant restaurant = controller.getRestaurant();
        System.out.printf("\nEnd of Turn: %d/%d\n", controller.getTurnNumber(), GameController.MAX_TURNS);
        System.out.printf("Total Served Count: %d\n", restaurant.getServedCount());
        List<Customer> leavingCustomers = restaurant.removeLeavingCustomers();
        System.out.printf("Unhappy departures: %s\n", leavingCustomers.isEmpty()? "N/A" : "");
        for (Customer customer: leavingCustomers){
            System.out.printf("-Customer #%d has left.\n", customer.getCustomerId());
        }
        System.out.printf("Current Revenue: $%s\n", restaurant.getRevenue() % 100 == 0 ? (restaurant.getRevenue() / 100) : String.format("%.2f", (restaurant.getRevenue() * 0.01f)));
        System.out.printf("Average Satisfaction of Paid Customers: %s\n", Double.isNaN(restaurant.getPaidAverageSatisfaction()) ? "N/A" : String.format("%.2f", restaurant.getPaidAverageSatisfaction()));
        System.out.printf("Turns used: %d\n====================\n", controller.getTurnNumber());
    }



    private void updateCustomerStatus() {
        Restaurant restaurant = controller.getRestaurant();
        for (Customer customer: restaurant.getCustomers()){
            if (customer.getStatus() == CustomerStatus.SERVED){
                customer.setStatus(CustomerStatus.EATING);
            } else if (customer.getStatus() == CustomerStatus.EATING){
                customer.setStatus(CustomerStatus.READY_TO_PAY);
                restaurant.addToPaymentQueue(customer);
            }
        }
    }
}