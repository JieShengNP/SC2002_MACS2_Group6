package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.menu.MenuItem;
import restaurantrush.enums.OrderStatus;

public class Order {
    private int orderId;
    private Customer customer;
    private MenuItem menuItem;
    private OrderStatus status;
    private int requiredPreparationUnits;
    private int currentPreparationUnits = 0;

    public Order(int orderId, Customer customer, MenuItem menuItem) {
        this.orderId = orderId;
        this.customer = customer;
        this.menuItem = menuItem;
        status = OrderStatus.ORDERED;
        requiredPreparationUnits = menuItem.getPreparationUnit();
    }

    public void prepare() {
        currentPreparationUnits += 1;
        status = OrderStatus.READY;
    }

    public boolean isReady() {
        return currentPreparationUnits == requiredPreparationUnits;
    }

    public void serve() {
        status = OrderStatus.SERVED;
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void cancel() {
        // TODO: Maybe delete the order
        status = OrderStatus.CANCELLED;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getRequiredPreparationUnits() {
        return requiredPreparationUnits;
    }

    public int getCurrentPreparationUnits() {
        return currentPreparationUnits;
    }
}