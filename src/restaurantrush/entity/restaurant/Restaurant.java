package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;
import restaurantrush.enums.CustomerStatus;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private List<Table> tables;
    private WaitingQueue waitingQueue;
    private List<Order> activeOrders;
    private List<Customer> customers;
    private int revenue = 0;

    public Restaurant() {
        tables = new ArrayList<>();
        waitingQueue = new WaitingQueue();
        activeOrders = new ArrayList<>();
        customers = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            tables.add(new Table(i));
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public int getRevenue() {
        return revenue;
    }

    public void addRevenue(int revenue) {
        this.revenue += revenue;
    }

    public double getPaidAverageSatisfaction() {
        int totalSatisfaction = 0;
        List<Customer> paidList = getPaidCustomer();
        for (Customer customer : paidList) {
            totalSatisfaction += customer.getSatisfaction();
        }
        return (double) totalSatisfaction / paidList.size();
    }

    public List<Table> getAvailableTables() {
        List<Table> freeList = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isOccupied()) {
                freeList.add(t);
            }
        }
        return freeList;
    }

    public int getOccupiedTableCount() {
        int occupied = 0;
        for (Table t : tables) {
            if (t.isOccupied()) occupied++;
        }
        return occupied;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Customer> getPaidCustomer() {
        List<Customer> paidList = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getStatus().equals(CustomerStatus.PAID)) {
                paidList.add(customer);
            }
        }

        return paidList;
    }

    public WaitingQueue getWaitingQueue() {
        return waitingQueue;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }

    public void addOrder(Order order) {
        activeOrders.add(order);
    }

    public void seatCustomer(Customer customer) {
        for (Table t : getAvailableTables()) {
            t.setCustomer(customer);
            customer.setTable(t);
            customer.setStatus(CustomerStatus.SEATED);
            waitingQueue.removeCustomer(customer);
            break;
        }
    }
}