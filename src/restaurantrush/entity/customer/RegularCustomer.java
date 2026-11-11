package restaurantrush.entity.customer;

public class RegularCustomer extends Customer {

    public RegularCustomer(int customerId) {
        super(customerId, 100, 8);
    }

    @Override
    public void decreaseSatisfaction() {
        satisfaction -= satisfaction_loss_rate;
        if (satisfaction < 0)
            satisfaction = 0;
    }
}