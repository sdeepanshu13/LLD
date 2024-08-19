import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagerImpl implements UserManager {
    private final Map<String, User> users;

    public UserManagerImpl() {
        this.users = new HashMap<>();
    }

    @Override
    public synchronized void registerUser(User user) throws BookstoreException {
        if (user == null || users.containsKey(user.getId())) {
            throw new BookstoreException("Invalid or duplicate user.");
        }
        users.put(user.getId(), user);
    }

    @Override
    public synchronized List<User> getRegisteredUsers() {
        return new ArrayList<>(users.values());
    }
}
