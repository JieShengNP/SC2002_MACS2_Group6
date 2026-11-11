package restaurantrush.entity.customer;

import restaurantrush.entity.menu.MenuItem;

import java.util.List;
import java.util.Random;

public class RegularCustomer extends Customer {

    public RegularCustomer(int customerId, List<MenuItem> menuItemList, Random random) {
        super(customerId, 100, 8, menuItemList, random);
    }

    @Override
    public void decreaseSatisfaction() {
        satisfaction -= satisfaction_loss_rate;
        if (satisfaction < 0)
            satisfaction = 0;
    }
}