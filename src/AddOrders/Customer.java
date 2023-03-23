package AddOrders;

public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String password;

    //Kollar om det är rätt lösenord
    public boolean correctPassword(String p){
        return password.equals(p);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Customer(int id, String firstName, String lastName, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
