package restaurantrush.entity.customer;

import restaurantrush.entity.menu.MenuItem;
import restaurantrush.entity.restaurant.Table;
import restaurantrush.enums.CustomerStatus;

public abstract class Customer {
    protected int customerId;
    protected int satisfaction;
    protected CustomerStatus status;
    protected Table table;
    protected MenuItem order;
    protected int satisfaction_loss_rate;

    public Customer(int customerId, int satisfaction, int satisfaction_loss_rate) {
        this.customerId = customerId;
        this.satisfaction = satisfaction;
        this.satisfaction_loss_rate = satisfaction_loss_rate;
        this.status = CustomerStatus.WAITING;
    }

    public abstract void decreaseSatisfaction();

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public void chooseMenuItem(MenuItem item) {
        order = item;
    }

    public void leaveRestaurant() {
        status = CustomerStatus.LEFT;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }
}