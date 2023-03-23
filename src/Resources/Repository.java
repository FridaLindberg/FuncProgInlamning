package Resources;

import AddOrders.*;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {

    private Properties p = new Properties();
    public final List<Customer> finalAllCustomers;
    private final List<Shoe> allShoesList;
    public final List<Order> finalAllOrders;
    public final List<OrderContent> finalAllOrderContents;

    public Repository() {

        try {
            p.load(new FileInputStream("src/Resources/Users.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finalAllCustomers = getAllCustomers();
        allShoesList = getAllShoes();
        finalAllOrders = getAllOrders();
        finalAllOrderContents = getAllOrderContents();
    }

    //hämtar alla kunder från databasen
    private List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(p.getProperty("connection"),
                p.getProperty("username"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select * from kund")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("förnamn");
                String lastName = rs.getString("efternamn");
                String password = rs.getString("lösenord");
                allCustomers.add(new Customer(id, firstName, lastName, password));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allCustomers;
    }

    public List<Shoe> getAllShoes() {
        List<Shoe> allShoes = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(p.getProperty("connection"),
                p.getProperty("username"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery
                     ("select sko.id, märke.märke, färg.färg, storlek.storlek, sko.antal, sko.pris  from sko \n" +
                     "inner join märke \n" +
                     "on sko.märkeID=märke.ID \n" +
                     "inner join färg \n" +
                     "on sko.färgID=färg.ID \n" +
                     "inner join storlek\n" +
                     "on sko.storlekID=storlek.ID \n")) {

            while (rs.next()) {
                int id = rs.getInt("sko.id");
                String brand = rs.getString("märke.märke");
                String color = rs.getString("färg.färg");
                int size = rs.getInt("storlek.storlek");
                int amount = rs.getInt("sko.antal");
                int price = rs.getInt("sko.pris");
                allShoes.add(new Shoe(id, new Brand(brand), new Color(color), new Size(size), amount, price));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allShoes;
    }

    private List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(p.getProperty("connection"),
                p.getProperty("username"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select * from beställning")) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                int customerID = rs.getInt("kundID");
                String location = rs.getString("ort");
                Customer customer = finalAllCustomers.stream().filter(c -> c.getId()==customerID).toList().get(0); //GÖR TILL EN KUND
                allOrders.add(new Order(id, customer, location));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    private List<OrderContent> getAllOrderContents() {
        List<OrderContent> allOrderContents = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(p.getProperty("connection"),
                p.getProperty("username"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select * from skobeställning")) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                int shoeID = rs.getInt("skoID");
                int amount = rs.getInt("antal");
                int orderID = rs.getInt("beställningID");
                Shoe shoe = allShoesList.stream().filter(s -> s.getId()==shoeID).toList().get(0); //gör till en shoe
                Order order = finalAllOrders.stream().filter(o -> o.getId()==orderID).toList().get(0); //GÖR TILL EN order
                allOrderContents.add(new OrderContent(id, shoe, amount, order));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allOrderContents;
    }
    public int callAddToCart(int customerId, int orderId, int shoeId, String ort) {
        int newOrderId = 0;
        try (Connection con = DriverManager.getConnection(p.getProperty("connection"),
                p.getProperty("username"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall("call addToCart(?, ?, ?, ?, ?)")) {
             stmt.setInt(1, customerId);
             stmt.setInt(2, orderId);
             stmt.setInt(3, shoeId);
             stmt.setString(4, ort);
             stmt.registerOutParameter(5, Types.INTEGER);
             stmt.execute();
             newOrderId = stmt.getInt(5); //Returnar nytt orderId vid första beställningen, sen ingenting

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newOrderId;
    }
}
