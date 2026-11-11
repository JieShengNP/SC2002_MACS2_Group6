package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.menu.Burger;
import restaurantrush.entity.menu.MenuItem;
import restaurantrush.entity.menu.Pasta;
import restaurantrush.entity.menu.Salad;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private final List<Table> tables;
    private final WaitingQueue waitingQueue;
    private final List<Order> activeOrders;
    private final List<Customer> customers;
    private int revenue = 0;
    private final List<MenuItem> menuItemList;
    private int nextOrderId = 1;

    public Restaurant() {
        tables = new ArrayList<>();
        waitingQueue = new WaitingQueue();
        activeOrders = new ArrayList<>();
        customers = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            tables.add(new Table(i));
        }
        menuItemList = List.of(
                new Burger(), new Pasta(), new Salad()
        );
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
        List<Order> activeOrder = new ArrayList<>();
        for (Order order: activeOrders){
            if (order.getStatus() == OrderStatus.ORDERED || order.getStatus() == OrderStatus.PREPARING || order.getStatus() == OrderStatus.READY){
                activeOrder.add(order);
            }
        }

        return activeOrder;
    }

    public List<Order> getCookableOrders(){
        List<Order> cookableOrder = new ArrayList<>();
        for (Order order: activeOrders){
            if (order.getStatus() == OrderStatus.ORDERED || order.getStatus() == OrderStatus.PREPARING){
                cookableOrder.add(order);
            }
        }
        return cookableOrder;
    }

    public void addOrder(Order order) {
        activeOrders.add(order);
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
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

    public void takeOrder(Customer customer){
        Order order = new Order(nextOrderId++, customer, customer.getMenuItem());
        activeOrders.add(order);
        customer.setStatus(CustomerStatus.ORDERED);
    }

    public void serveOrder(Order order){
        order.serve();
        Customer customer = order.getCustomer();
        customer.setStatus(CustomerStatus.SERVED);
    }

    public void cookOrder(Order order){
        order.prepare();
    }
}