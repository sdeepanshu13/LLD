import java.util.List;

public interface UserManager {
    void registerUser(User user) throws BookstoreException;
    List<User> getRegisteredUsers();
}
