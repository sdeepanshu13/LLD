import java.util.Objects;

public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final double price;
    private int quantity; // New field for the quantity

    public Book(String isbn, String title, String author, double price, int quantity) throws BookstoreException {
        if (isbn == null || title == null || author == null || price <= 0 || quantity < 0) {
            throw new BookstoreException("Invalid book details.");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity; // Initialize quantity
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws BookstoreException {
        if (quantity < 0) {
            throw new BookstoreException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + isbn + '\'' +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'' +
                ", Price=" + price +
                ", Quantity=" + quantity + // Include quantity in toString
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Double.compare(book.price, price) == 0 &&
                quantity == book.quantity &&
                isbn.equals(book.isbn) &&
                title.equals(book.title) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author, price, quantity);
    }
}
