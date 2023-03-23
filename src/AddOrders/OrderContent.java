package AddOrders;

public class OrderContent {

    private int id;
    private Shoe shoe;
    private int amount;
    private Order order;

    public OrderContent(int id, Shoe shoe, int amount, Order order) {
        this.id = id;
        this.shoe = shoe;
        this.amount = amount;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public int getAmount() {
        return amount;
    }

    public Order getOrder() {
        return order;
    }
}
