package AddOrders;

public class Order {


    private int id;
    private Customer customer;
    private String location;

    public Order(int id, Customer customer, String location) {
        this.id = id;
        this.customer = customer;
        this.location = location;
    }
    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getLocation() {
        return location;
    }
}
