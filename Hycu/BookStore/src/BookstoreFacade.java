import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BookstoreFacade {
    private final ConcurrentHashMap<String, Book> books = new ConcurrentHashMap<>();
    private final List<Order> orders = new ArrayList<>();
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public void addBook(Book book) throws BookstoreException {
        if (book == null) {
            throw new BookstoreException("Cannot add null book.");
        }
        if (books.containsKey(book.getIsbn())) {
            throw new BookstoreException("Book already exists.");
        }
        books.put(book.getIsbn(), book);
    }

    public void registerUser(User user) throws BookstoreException {
        if (user == null) {
            throw new BookstoreException("Cannot register null user.");
        }
        if (users.containsKey(user.getId())) {
            throw new BookstoreException("User already registered.");
        }
        users.put(user.getId(), user);
    }

    public Order placeOrder(User user) throws BookstoreException {
        if (user == null) {
            throw new BookstoreException("Cannot place order for null user.");
        }
        if (!users.containsKey(user.getId())) {
            throw new BookstoreException("User not registered.");
        }
        if (user.getCart().isEmpty()) {
            throw new BookstoreException("Cart is empty.");
        }

        List<Book> booksInCart = user.getCart();
        for (Book book : booksInCart) {
            if (book.getQuantity() <= 0) {
                throw new BookstoreException("Book out of stock: " + book.getTitle());
            }
        }

        String orderId = "ORDER" + (orders.size() + 1);
        Order order = new Order(orderId, user, booksInCart);

        // Update book quantities
        for (Book book : booksInCart) {
            Book storedBook = books.get(book.getIsbn());
            if (storedBook != null) {
                storedBook.setQuantity(storedBook.getQuantity() - 1);
            }
        }

        orders.add(order);
        return order;
    }

    public List<Book> getAvailableBooks() {
        return new ArrayList<>(books.values());
    }

    public List<User> getRegisteredUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
