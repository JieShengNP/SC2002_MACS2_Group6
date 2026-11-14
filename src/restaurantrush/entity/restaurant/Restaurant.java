package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;
import restaurantrush.entity.menu.Burger;
import restaurantrush.entity.menu.MenuItem;
import restaurantrush.entity.menu.Pasta;
import restaurantrush.entity.menu.Salad;
import restaurantrush.entity.staff.*;
import restaurantrush.enums.CustomerStatus;
import restaurantrush.enums.OrderStatus;

import java.util.*;

public class Restaurant {

    private final List<Table> tables;
    private final WaitingQueue waitingQueue;
    private final List<Order> activeOrders;
    private final List<Customer> customers;
    private int revenue = 0;
    private final List<MenuItem> menuItemList;
    private final Queue<Customer> paymentQueue;
    private final List<Staff> staffList;
    private int happyHourTurn = -1;
    private boolean happyHourActive = false;

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

        paymentQueue = new LinkedList<>();
        staffList = List.of(new Host(), new Waiter(), new Chef(), new Cashier());
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

    public List<Table> getOccupiedTables() {
        List<Table> occupiedTables = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied()) {
                occupiedTables.add(t);
            }
        }
        return occupiedTables;
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
        Order order = new Order(customer, customer.getMenuItem(), happyHourActive);
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

    private Order getCustomerOrder(Customer customer){
        for (Order order: activeOrders){
            if (order.getCustomer().equals(customer)){
                return order;
            }
        }
        return null;
    }

    public void takeNextPayment(){
        Customer customer = paymentQueue.peek();
        if (customer != null){
            Order order = getCustomerOrder(customer);
            if (order != null) {
                int orderRevenue = order.getMenuItem().getPrice();
                orderRevenue = (orderRevenue * customer.getPaymentPercentage() / 100);
                if (order.isHappyHour()){
                    orderRevenue = ((orderRevenue * 80) / 100);
                }
                addRevenue(orderRevenue);
                customer.setStatus(CustomerStatus.PAID);
                order.markPaid();
                Table table = customer.getTable();
                table.removeCustomer();
                customer.setTable(null);
                paymentQueue.remove();
            }
        }
    }

    public void addToPaymentQueue(Customer customer){
        if (customer.getStatus() == CustomerStatus.READY_TO_PAY){
            paymentQueue.add(customer);
        }
    }

    public List<Customer> removeLeavingCustomers(){
        List<Customer> leftList = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerStatus status = customer.getStatus();
            if ((customer.isUnsatisfied()) && (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED)) {
                if (status == CustomerStatus.WAITING) {
                    waitingQueue.removeCustomer(customer);
                } else if (status == CustomerStatus.SEATED){
                    Table table = customer.getTable();
                    table.removeCustomer();
                    customer.setTable(null);
                } else {
                    Table table = customer.getTable();
                    table.removeCustomer();
                    customer.setTable(null);
                    Order order = getCustomerOrder(customer);
                    if (order != null) {
                        order.cancel();
                    }
                }
                leftList.add(customer);
                customer.setStatus(CustomerStatus.LEFT);
            }
        }
        return leftList;
    }

    public List<Staff> getStaffList(){
        return staffList;
    }

    public List<Customer> getSeatedCustomers(){
        List<Customer> seatedList = new ArrayList<>();
        for (Customer customer : customers){
            if (customer.getStatus() == CustomerStatus.SEATED){
                seatedList.add(customer);
            }
        }
        return seatedList;
    }

    public List<Order> getReadyOrders(){
        List<Order> readyOrders = new ArrayList<>();
        for (Order order : activeOrders){
            if (order.getStatus() == OrderStatus.READY){
                readyOrders.add(order);
            }
        }
        return readyOrders;
    }

    public int getTotalLeftCustomers(){
        int count = 0;
        for (Customer customer : customers){
            if (customer.getStatus() == CustomerStatus.LEFT){
                count++;
            }
        }
        return count;
    }

    public boolean isHappyHourUsed(){
        return (happyHourTurn != -1);
    }

    private boolean isHappyHourTurn(int turnNumber){
        return turnNumber - happyHourTurn == 1 || turnNumber - happyHourTurn == 0;
    }

    public boolean isHappyHourActive(){
        return happyHourActive;
    }

    public int getHappyHourTurnLeft(int turnNumber){
        return (happyHourTurn + 1 - turnNumber);
    }

    public void activateHappyHour(int turnNumber){
        this.happyHourTurn = turnNumber;
        handleHappyHour(turnNumber);
    }

    public void handleHappyHour(int turnNumber){
        if (isHappyHourTurn(turnNumber)){
            happyHourActive = true;
            for (Customer customer : customers) {
                CustomerStatus status = customer.getStatus();
                if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED) {
                    customer.increaseSatisfaction(15);
                }
            }
        } else {
            happyHourActive = false;
        }
    }
}