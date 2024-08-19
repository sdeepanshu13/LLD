import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final String id;
    private final User user;
    private final List<Book> books;
    private final double totalCost;

    public Order(String id, User user, List<Book> books) throws BookstoreException {
        if (id == null || user == null || books == null || books.isEmpty()) {
            throw new BookstoreException("Invalid order details.");
        }
        this.id = id;
        this.user = user;
        this.books = new ArrayList<>(books);
        this.totalCost = calculateTotalCost();
    }

    private double calculateTotalCost() {
        return books.stream().mapToDouble(Book::getPrice).sum();
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID='" + id + '\'' +
                ", User=" + user.getName() +
                ", Books=" + books +
                ", TotalCost=" + totalCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Double.compare(order.totalCost, totalCost) == 0 &&
                Objects.equals(id, order.id) &&
                Objects.equals(user, order.user) &&
                Objects.equals(books, order.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, books, totalCost);
    }
}
