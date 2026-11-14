package restaurantrush.entity.customer;

import restaurantrush.entity.menu.MenuItem;
import restaurantrush.entity.restaurant.Table;
import restaurantrush.enums.CustomerStatus;

import java.util.List;
import java.util.Random;

public abstract class Customer {
    private final int customerId;
    private int satisfaction;
    private CustomerStatus status;
    private Table table;
    private final MenuItem menuItem;
    private final int satisfactionLossRate;

    public Customer(int customerId, int satisfaction, int satisfactionLossRate, List<MenuItem> menuItemList, Random random) {
        this.customerId = customerId;
        this.satisfaction = satisfaction;
        this.satisfactionLossRate = satisfactionLossRate;
        this.status = CustomerStatus.WAITING;

        this.menuItem = menuItemList.get(random.nextInt(menuItemList.size()));
    }

    public void updateSatisfaction() {
        if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED) {
            decreaseSatisfaction();
        }
    }

    public boolean updateServePayState() {
        if (status == CustomerStatus.SERVED) {
            status = CustomerStatus.EATING;
            return false;
        } else if (status == CustomerStatus.EATING) {
            status = CustomerStatus.READY_TO_PAY;
            return true;
        }
        return false;
    }

    public void decreaseSatisfaction() {
        satisfaction -= satisfactionLossRate;
        if (satisfaction < 0)
            satisfaction = 0;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }


}