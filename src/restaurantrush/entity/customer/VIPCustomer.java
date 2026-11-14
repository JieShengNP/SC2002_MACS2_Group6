package restaurantrush.entity.customer;

import restaurantrush.entity.menu.MenuItem;

import java.util.List;
import java.util.Random;

public class VIPCustomer extends Customer {

    public VIPCustomer(int customerId, List<MenuItem> menuItemList, Random random) {
        super(customerId, 100, 5, menuItemList, random);
    }

    @Override
    public int getPaymentPercentage() {
        return 90;
    }

    @Override
    public String getCustomerType() {
        return "VIP";
    }
}