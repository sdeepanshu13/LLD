import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final String id;
    private final String name;
    private final List<Book> cart;

    public User(String id, String name) {
        if (id == null || name == null) {
            throw new IllegalArgumentException("Invalid user details.");
        }
        this.id = id;
        this.name = name;
        this.cart = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized void addToCart (Book book) throws BookstoreException {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        
        if(book.getQuantity() ==0){
            throw new BookstoreException(book.getIsbn()+ " " +book.getTitle()+" " + " out of stock");
        }
        cart.add(book);
    }

    public synchronized List<Book> getCart() {
        return new ArrayList<>(cart);
    }

    public synchronized void clearCart() {
        cart.clear();
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
