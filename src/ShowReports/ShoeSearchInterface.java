package ShowReports;

import AddOrders.OrderContent;

@FunctionalInterface
public interface ShoeSearchInterface {
    boolean search(OrderContent oc, String word);
}
