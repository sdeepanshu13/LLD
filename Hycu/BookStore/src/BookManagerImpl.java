import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManagerImpl implements BookManager {
    private final Map<String, Book> inventory;

    public BookManagerImpl() {
        this.inventory = new HashMap<>();
    }

    @Override
    public synchronized void addBook(Book book) throws BookstoreException {
        if (book == null || inventory.containsKey(book.getIsbn())) {
            throw new BookstoreException("Invalid or duplicate book.");
        }
        inventory.put(book.getIsbn(), book);
    }

    @Override
    public synchronized List<Book> getAvailableBooks() {
        return new ArrayList<>(inventory.values());
    }
}
