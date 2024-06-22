import Serialization.User;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static Helper.Credentials.getCredentials;
import static Helper.TestDataCreator.*;
import static java.net.HttpURLConnection.*;

public class ChangeUserInfoTest {

    private UserSteps userSteps;
    private User user;
    private String token;

    @Before
    public void before() {
        userSteps = new UserSteps();
        user = getNewDefaultUser();
    }

    @After
    public void after(){
        token = userSteps.signInUser(getCredentials(user)).extract().body().path("accessToken");
        if (token !=null) {
            Assert.assertEquals(HTTP_ACCEPTED, userSteps.deleteUser(token).extract().statusCode());
        }
    }

    @Test
    @DisplayName("Изменение поля email для авторизованного пользователя")
    @Description("Позитивный тест на изменение поля email для авторизованного пользователя")
    public void updateEmailBySignedUserCheck(){
        userSteps.createUser(user);
        token = userSteps.signInUser(getCredentials(user)).extract().body().path("accessToken");
        user.setEmail(getNewEmail());
        ValidatableResponse changeResponse = userSteps.changeUserInfo(user, token);
        Assert.assertEquals(HTTP_OK, changeResponse.extract().statusCode());
        Assert.assertTrue(changeResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Изменение поля name для авторизованного пользователя")
    @Description("Позитивный тест на изменение поля name для авторизованного пользователя")
    public void updateNameBySignedUserCheck(){
        userSteps.createUser(user);
        token = userSteps.signInUser(getCredentials(user)).extract().body().path("accessToken");
        user.setName(getNewName());
        ValidatableResponse changeResponse = userSteps.changeUserInfo(user, token);
        Assert.assertEquals(HTTP_OK, changeResponse.extract().statusCode());
        Assert.assertTrue(changeResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Изменение поля email для неавторизованного пользователя")
    @Description("Негативный тест на изменение поля email для неавторизованного пользователя")
    public void updateEmailByNotSignedUserCheck(){
        user.setEmail(getNewEmail());
        token = "";
        ValidatableResponse changeResponse = userSteps.changeUserInfo(user, token);
        Assert.assertEquals(HTTP_UNAUTHORIZED, changeResponse.extract().statusCode());
        Assert.assertFalse(changeResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Изменение поля name для неавторизованного пользователя")
    @Description("Негативный тест на изменение поля name для неавторизованного пользователя")
    public void updateNameByNotSignedUserCheck(){
        user.setName(getNewName());
        token = "";
        ValidatableResponse changeResponse = userSteps.changeUserInfo(user, token);
        Assert.assertEquals(HTTP_UNAUTHORIZED, changeResponse.extract().statusCode());
        Assert.assertFalse(changeResponse.extract().body().path("success"));
    }
}
