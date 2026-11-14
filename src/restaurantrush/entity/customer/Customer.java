package restaurantrush.entity.customer;

import restaurantrush.entity.menu.ComboMeal;
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

        this.menuItem = chooseRandomFood(menuItemList, random);
    }

    private MenuItem chooseRandomFood(List<MenuItem> menuItemList, Random random) {
        int roll = random.nextInt(100);

        if (roll < 75) {
            return menuItemList.get(random.nextInt(menuItemList.size()));
        } else {
            MenuItem item1 = menuItemList.get(random.nextInt(menuItemList.size()));
            MenuItem item2 = menuItemList.get(random.nextInt(menuItemList.size()));
            while (item2 == item1) {
                item2 = menuItemList.get(random.nextInt(menuItemList.size()));
            }
            int price = 0;
            int preperationUnit = 0;

            price += item1.getPrice();
            price += item2.getPrice();
            price = (price * 90) / 100;

            preperationUnit += item1.getPreparationUnit();
            preperationUnit += item2.getPreparationUnit();
            String name = String.format("Combo Meal (%s + %s)", item1.getName(), item2.getName());
            return new ComboMeal(name, price, preperationUnit);
        }
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

    public int getPaymentPercentage() {
        return 100;
    }

    public boolean isUnsatisfied() {
        return satisfaction <= 0;
    }

    public abstract String getCustomerType();

    public void increaseSatisfaction(int amount){
        this.satisfaction = Math.min(100, satisfaction + amount);
    }
}