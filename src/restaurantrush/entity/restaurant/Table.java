package restaurantrush.entity.restaurant;

import restaurantrush.entity.customer.Customer;

public class Table {

    private int tableNumber;
    private Customer customer;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        customer = null;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isOccupied() {
        return customer != null;
    }

    public Customer getSeatCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void removeCustomer() {
        customer = null;
    }

}