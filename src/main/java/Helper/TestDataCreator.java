package Helper;

import Serialization.User;
import com.github.javafaker.Faker;

public class TestDataCreator {

    private static final Faker faker = new Faker();

    public static User getNewDefaultUser() {
        return new User(faker.internet().emailAddress(), faker.internet().password(), faker.funnyName().name());
    }

    public static User getNewUserWithoutPassword() {
        return new User(faker.internet().emailAddress(), "", faker.funnyName().name());
    }

    public static String getNewEmail() {
        return faker.internet().emailAddress();
    }

    public static String getNewName() {
        return faker.funnyName().name();
    }

    public static String getFakedIngredientId() {
        return faker.internet().uuid();
    }
}
