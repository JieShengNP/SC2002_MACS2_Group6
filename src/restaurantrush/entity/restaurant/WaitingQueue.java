package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class WaitingQueue {

    private List<Customer> queue;

    public WaitingQueue() {
        queue = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        queue.add(customer);
    }

    public Customer getCustomer(int index) {
        return queue.get(index);
    }

    public void removeCustomer(Customer customer) {
        queue.remove(customer);
    }

    public Customer removeCustomerByIndex(int index) {
        return queue.remove(index);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public List<Customer> getQueue() {
        return queue;
    }

    public int size() {
        return queue.size();
    }
}