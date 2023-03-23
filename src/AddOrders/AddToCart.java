package AddOrders;

import Resources.Repository;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AddToCart {

    Repository r = new Repository();
    final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new AddToCart();
    }

    public AddToCart() {

        boolean addMore = true;
        System.out.println("Välkommen till skobutiken! Varsågod att logga in.");

        System.out.println("Förnamn:");
        final String firstName = sc.nextLine();

        System.out.println("Efternamn:");
        final String lastName = sc.nextLine();

        Customer customer = getCustomer(firstName, lastName);
        if (customer == null) {
            System.out.println("Ingen kund med detta namn");
        } else {
            System.out.println("Lösenord:");
            final String lösen = sc.nextLine();
            if (!customer.correctPassword(lösen)) {
                System.out.println("Fel lösenord");
            } else {
                System.out.println("Vilken ort beställer du ifrån?");
                String ort = sc.nextLine();
                int newOrderId = addShoe(customer, 0, ort); //Sätts vid första beställningen
                while(addMore) {
                    System.out.println("Vill du lägga till fler produkter? (j/n)");
                    String input = sc.nextLine();
                    if (input.equalsIgnoreCase("j")) {
                        addShoe(customer, newOrderId, ort); //Skickar in aktuell beställning
                    }
                    else if(input.equalsIgnoreCase("n")){
                        addMore = false;
                        System.out.println("Tack för din beställning!");
                    }
                    else {
                        System.out.println("Skriv j eller n!");
                    }
                }
            }
        }
    }

    //Hittar kunden man skriver in
    public Customer getCustomer(String firstName, String lastName) {
        List<Customer> customers = r.finalAllCustomers.stream().filter(c -> c.getFirstName().equalsIgnoreCase(firstName)).
                filter(c -> c.getLastName().equalsIgnoreCase(lastName)).toList();
        if (customers.size() == 1) {
            return customers.get(0);
        } else return null;
    }

    //Skriver ut alla skor med märke, färg, storlek, pris
    public void printAllShoes() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        r.getAllShoes().stream().filter(s -> s.getAmountInStock()>0).sorted((p1, p2) -> ((Integer) p1.getId()).compareTo(p2.getId())).
                forEach(e -> System.out.println(e.getId() + ". Märke: " + e.getBrand().getName() + ", Färg: "
                        + e.getColor().getName() + ", Storlek: " + e.getSize().getSizeNumber() +
                        ", Pris: " + formatter.format(e.getPrice()) + " SEK"));
    }

    public int addShoe(Customer customer, int orderId, String ort) {
        int newOrderId = 0;
        System.out.println("Välj sko genom att skriva siffran som motsvarar skon");
        printAllShoes();
        try {
            int skoId = sc.nextInt();
            sc.nextLine(); //läser tom rad
            newOrderId = r.callAddToCart(customer.getId(), orderId, skoId, ort); //Nytt id vid första ordern, sen 0

        } catch (InputMismatchException e) {
            System.out.println("Skriv en siffra");
        }
        return newOrderId;

    }

}