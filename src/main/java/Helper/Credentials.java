package Helper;

import Serialization.User;

public class Credentials {

    public static User getCredentials (User user) {
        return new User(user.getEmail(), user.getPassword());
    }
}
