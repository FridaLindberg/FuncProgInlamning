package AddOrders;

public class Shoe {

    private int id;
    private Brand brand;
    private Color color;
    private Size size;
    private int amountInStock;
    private double price;

    public int getId() {
        return id;
    }

    public Brand getBrand() {
        return brand;
    }

    public Color getColor() {
        return color;
    }

    public Size getSize() {
        return size;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public double getPrice() {
        return price;
    }

    public Shoe(int id, Brand brand, Color color, Size size, int amount, double price) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.amountInStock = amount;
        this.price = price;
    }
}
