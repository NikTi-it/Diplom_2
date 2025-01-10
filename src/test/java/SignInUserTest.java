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
import static java.net.HttpURLConnection.*;

public class SignInUserTest {

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
    @DisplayName("Авторизация пользователя")
    @Description("Позитивный тест на авторизацию пользователя в сервисе")
    public void userSignInCheck(){
        userSteps.createUser(user);
        ValidatableResponse signInResponse = userSteps.signInUser(getCredentials(user));
        Assert.assertEquals(HTTP_OK, signInResponse.extract().statusCode());
        Assert.assertNotNull(signInResponse.extract().path("accessToken"));
    }

    @Test
    @DisplayName("Авторизация пользователя c неверными данными")
    @Description("Негативный тест на авторизацию пользователя с неверными логином и паролем")
    public void userSignInWrongCredentialsCheck(){
        ValidatableResponse signInResponse = userSteps.signInUser(getCredentials(user));
        Assert.assertEquals(HTTP_UNAUTHORIZED, signInResponse.extract().statusCode());
    }
}
