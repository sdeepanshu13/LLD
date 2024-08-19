import java.util.List;

public class OnlineBookstoreSystem {
    public static void main(String[] args) {
        BookstoreFacade bookstore = new BookstoreFacade();

        try {
            // Book Management
            System.out.println("Book Management:");

            // Adding books to the bookstore inventory with quantities
            Book book1 = new Book("123", "Java", "Jos", 45.00, 10);
            Book book2 = new Book("456", "Go", "Robert", 50.00, 5);
            Book duplicateBook = new Book("123", "Java", "Jos", 45.00, 10);

            // Adding valid books
            bookstore.addBook(book1);
            bookstore.addBook(book2);

            // Display the list of available books
            List<Book> availableBooks = bookstore.getAvailableBooks();
            System.out.println("Available Books:");
            for (Book book : availableBooks) {
                System.out.println(book);
            }

            // Attempting to add a duplicate book (should throw exception)
            System.out.println("\nAttempting to add a duplicate book:");
            try {
                bookstore.addBook(duplicateBook);
            } catch (BookstoreException e) {
                System.out.println("Error adding book: " + e.getMessage());
            }

            // User Management
            System.out.println("\nUser Management:");

            // Registering new users
            User user1 = new User("U01", "Ram");
            User user2 = new User("U02", "Maddy");
            User user3 = new User("U03", "Taylor");
            User duplicateUser = new User("U01", "Ram");

            // Registering valid users
            bookstore.registerUser(user1);
            bookstore.registerUser(user2);
            bookstore.registerUser(user3);

            // Display the list of registered users
            List<User> registeredUsers = bookstore.getRegisteredUsers();
            System.out.println("Registered Users:");
            for (User user : registeredUsers) {
                System.out.println(user);
            }

            // Attempting to register a duplicate user (should throw exception)
            System.out.println("\nAttempting to register a duplicate user:");
            try {
                bookstore.registerUser(duplicateUser);
            } catch (BookstoreException e) {
                System.out.println("Error registering user: " + e.getMessage());
            }

            // Order Management
            System.out.println("\nOrder Management:");

            // Allow users to add books to their shopping cart
            try {
                user1.addToCart(book1);
                user2.addToCart(book2);
                user2.addToCart(book1);
                
                // Place an order
                Order order1 = bookstore.placeOrder(user1);
                System.out.println("Order placed: " + order1);
                System.out.println();

                Order order2 = bookstore.placeOrder(user2);
                System.out.println("Order placed: " + order2);
            } catch (BookstoreException e) {
                System.out.println("Error adding to cart : " + e.getMessage());
            }


            System.out.println();


            // Display all orders
            List<Order> orders = bookstore.getOrders();
            System.out.println("All Orders:");
            for (Order order : orders) {
                System.out.println(order);
            }

            System.out.println();

            try {
                book1.setQuantity(-1);
            } catch (BookstoreException e) {
                System.out.println("Error setting quantity to -1: " + e.getMessage());
                book1.setQuantity(0);
            }
            
            

            System.out.println();

            try {
                user3.addToCart(book1);
                Order order3 = bookstore.placeOrder(user3);
                System.out.println("Order placed: " + order3);
            } catch (BookstoreException e) {
                user3.clearCart();
                book1.setQuantity(8);
                System.out.println("Error adding to cart : " + e.getMessage());
            }
            
            System.out.println("Quantity of Book1 is back to 9");

            try {
                user3.addToCart(book1);
                Order order3 = bookstore.placeOrder(user3);
                System.out.println("Order placed: " + order3);
            } catch (BookstoreException e) {
                System.out.println("Error adding to cart : " + e.getMessage());
            }

            System.out.println();


            // Display all orders
            System.out.println("All Orders:");
            orders = bookstore.getOrders();
            for (Order order : orders) {
                System.out.println(order);
            }

            System.out.println();

            // Try to place an order with an empty cart (should throw exception)
            System.out.println("\nAttempting to place an order with an empty cart:");
            User emptyCartUser = new User("U003", "Empty Cart User");
            bookstore.registerUser(emptyCartUser);
            try {
                bookstore.placeOrder(emptyCartUser);
            } catch (BookstoreException e) {
                System.out.println("Error placing order: " + e.getMessage());
            }
            System.out.println();

            // Attempting to add null book and null user (should throw exceptions)
            System.out.println("\nAttempting to add null book:");
            try {
                bookstore.addBook(null);
            } catch (BookstoreException e) {
                System.out.println("Error adding null book: " + e.getMessage());
            }

            System.out.println("\nAttempting to register null user:");
            try {
                bookstore.registerUser(null);
            } catch (BookstoreException e) {
                System.out.println("Error registering null user: " + e.getMessage());
            }
            System.out.println();

            // Display books after placing orders to check quantity update
            System.out.println("\nAvailable Books after placing order:");
            List<Book> updatedBooks = bookstore.getAvailableBooks();
            for (Book book : updatedBooks) {
                System.out.println(book);
            }

        } catch (BookstoreException e) {
            e.printStackTrace();
        }
    }
}
