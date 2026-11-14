package restaurantrush.entity.customer;

import restaurantrush.entity.menu.MenuItem;

import java.util.List;
import java.util.Random;

public class CriticCustomer extends Customer {
    public CriticCustomer(int customerId, List<MenuItem> menuItemList, Random random) {
        super(customerId, 100, 12, menuItemList, random);
    }

    @Override
    public boolean isUnsatisfied() {
        return getSatisfaction() <= 50;
    }

    @Override
    public String getCustomerType() {
        return "Critic";
    }
}
