import Serialization.User;
import Steps.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static Helper.Credentials.getCredentials;
import static Helper.TestDataCreator.getNewDefaultUser;
import static Helper.TestDataCreator.getNewUserWithoutPassword;
import static java.net.HttpURLConnection.*;

public class CreateUserTest {

    private UserSteps userSteps;
    private User user;

    @Before
    public void before() {
        userSteps = new UserSteps();
        user = getNewDefaultUser();
    }

    @After
    public void after(){
        String token = userSteps.signInUser(getCredentials(user)).extract().path("accessToken");
        if (token !=null) {
            Assert.assertEquals(HTTP_ACCEPTED, userSteps.deleteUser(token).extract().statusCode());
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Позитивный тест на регистрацию нового пользователя в сервисе")
    public void createUserCheck(){
        ValidatableResponse createResponse = userSteps.createUser(user);
        Assert.assertEquals(HTTP_OK, createResponse.extract().statusCode());
        Assert.assertNotNull(createResponse.extract().path("accessToken"));
    }

    @Test
    @DisplayName("Создание зарегистрированного ранее пользователя")
    @Description("Негативный тест на регистрацию пользователя, который уже был зарегистрирован в сервисе")
    public void createExistingUserCheck(){
        userSteps.createUser(user);
        ValidatableResponse createResponse = userSteps.createUser(user);
        Assert.assertEquals(HTTP_FORBIDDEN, createResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Негативный тест на регистрацию пользователя без обязательного поля (пароля)")
    public void createUserWithoutPasswordCheck(){
        ValidatableResponse createResponse = userSteps.createUser(getNewUserWithoutPassword());
        Assert.assertEquals(HTTP_FORBIDDEN, createResponse.extract().statusCode());
    }
}
