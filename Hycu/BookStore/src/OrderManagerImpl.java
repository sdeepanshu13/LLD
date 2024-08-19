import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagerImpl implements OrderManager {
    private final Map<String, Order> orders;

    public OrderManagerImpl() {
        this.orders = new HashMap<>();
    }

    @Override
    public synchronized Order placeOrder(User user) throws BookstoreException {
        if (user == null || user.getCart().isEmpty()) {
            throw new BookstoreException("User not registered or cart is empty.");
        }

        Order order = new Order("ORD-" + (orders.size() + 1), user, user.getCart());
        orders.put(order.getId(), order);

        user.clearCart();
        return order;
    }

    @Override
    public synchronized List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }
}
