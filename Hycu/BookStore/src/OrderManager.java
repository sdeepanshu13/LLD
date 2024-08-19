import java.util.List;

public interface OrderManager {
    Order placeOrder(User user) throws BookstoreException;
    List<Order> getOrders();
}
