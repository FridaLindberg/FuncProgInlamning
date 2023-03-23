package ShowReports;

import AddOrders.Customer;
import AddOrders.Order;
import AddOrders.OrderContent;
import Resources.Repository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShowReports {

    Repository r = new Repository();

    public static void main(String[] args) {
        new ShowReports();
    }

    public ShowReports() {

        ShoeSearchInterface searchColor = (oc, word) -> oc.getShoe().getColor().getName().equalsIgnoreCase(word);
        ShoeSearchInterface searchBrand = (oc, word) -> oc.getShoe().getBrand().getName().equalsIgnoreCase(word);
        ShoeSearchInterface searchSize = (oc, word) -> String.valueOf(oc.getShoe().getSize().getSizeNumber()).equalsIgnoreCase(word);

        final Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nVilken rapport vill du se?");
            System.out.println("1. Alla kunder som har handlat varor i en viss storlek, färg eller märke\n" +
                    "2. Hur många beställningar varje kund har lagt\n" +
                    "3. Hur mycket pengar varje kund har beställt för");
            final String reportChoice = sc.nextLine();

            switch (reportChoice) {
                case "1" -> {
                    System.out.println("Vill du söka på färg, märke eller storlek?");
                    final String attributeToSearchFor = sc.nextLine();
                    switch (attributeToSearchFor) {
                        case "färg" -> {
                            System.out.println("Vilken färg vill du söka på?");
                            final String colorToSearchFor = sc.nextLine();
                            searchShoe(colorToSearchFor, searchColor);
                        }
                        case "märke" -> {
                            System.out.println("Vilket märke vill du söka på?");
                            final String brandToSearchFor = sc.nextLine();
                            searchShoe(brandToSearchFor, searchBrand);
                        }
                        case "storlek" -> {
                            System.out.println("Vilken storlek vill du söka på?");
                            final String sizeToSearchFor = sc.nextLine();
                            searchShoe(sizeToSearchFor, searchSize);
                        }
                        default -> System.out.println("Det var inget alternativ");
                    }
                }
                case "2" -> {
                    System.out.println("Beställningar per kund:");
                    ordersPerCustomer();
                }
                case "3" -> {
                    System.out.println("Total summa per kund:");
                    sumPerCustomer();
                }
                default -> System.out.println("Det var inte ett alternativ");
            }
        }
    }

    public void ordersPerCustomer() {
        Map<Customer, List<Order>> customersOrders = r.finalAllOrders.stream().
                collect(Collectors.groupingBy(o -> o.getCustomer()));
        customersOrders.forEach((k, v) -> System.out.println(k.getFirstName() + " " + k.getLastName() + ": " +
                v.stream().count() + " st"));
    }

    public void sumPerCustomer() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        Map<Customer, List<OrderContent>> customerOC = r.finalAllOrderContents.stream().
                collect(Collectors.groupingBy(oc -> oc.getOrder().getCustomer()));
        customerOC.forEach((k, v) -> System.out.println(k.getFirstName() + " " + k.getLastName() + ": " + formatter.format(v.stream()
                .mapToDouble(oc -> oc.getShoe().getPrice() * oc.getAmount()).sum()) + " SEK"));
    }

    public void searchShoe(String word, ShoeSearchInterface shoeSearch) {
        r.finalAllOrderContents.stream().filter(oc -> shoeSearch.search(oc, word))
                .map(OrderContent::getOrder).map(Order::getCustomer).distinct()
                .forEach(c -> System.out.println(c.getFirstName() + " " + c.getLastName()));
    }
}
