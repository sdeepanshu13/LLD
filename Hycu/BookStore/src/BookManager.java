import java.util.List;

public interface BookManager {
    void addBook(Book book) throws BookstoreException;
    List<Book> getAvailableBooks();
}
