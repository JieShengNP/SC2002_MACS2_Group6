package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class WaitingQueue {

    private final List<Customer> queue;

    public WaitingQueue() {
        queue = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        queue.add(customer);
    }

    public void removeCustomer(Customer customer) {
        queue.remove(customer);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public List<Customer> getQueue() {
        return queue;
    }

}