import Serialization.Order;
import Serialization.User;
import Steps.IngredientsSteps;
import Steps.OrderSteps;
import Steps.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Helper.Credentials.getCredentials;
import static Helper.TestDataCreator.getNewDefaultUser;
import static java.net.HttpURLConnection.*;

public class GetOrderTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String token;
    private Order order;

    @Before
    public void before() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        IngredientsSteps ingredientsSteps = new IngredientsSteps();
        User user = getNewDefaultUser();
        userSteps.createUser(user);
        token = userSteps.signInUser(getCredentials(user)).extract().body().path("accessToken");
        Assert.assertNotNull(token);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(0, ingredientsSteps.getFirstIngredientId());
        ingredients.add(1, ingredientsSteps.getSecondIngredientId());
        ingredients.add(2, ingredientsSteps.getThirdIngredientId());
        order = new Order(ingredients);
    }

    @After
    public void after(){
        if (token !=null) {
            Assert.assertEquals(HTTP_ACCEPTED, userSteps.deleteUser(token).extract().statusCode());
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Позитивный тест получения заказов авторизованного пользователя")
    public void getOrderBySignedUserCheck(){
        orderSteps.createOrderBySignedUser(order,token);
        ValidatableResponse orderResponse = orderSteps.getOrdersBySignedUser(token);
        Assert.assertEquals(HTTP_OK, orderResponse.extract().statusCode());
        Assert.assertTrue(orderResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Позитивный тест получения заказов неавторизованного пользователя")
    public void getOrderByNotSignedUserCheck(){
        orderSteps.createOrderByNotSignedUser(order);
        ValidatableResponse orderResponse = orderSteps.getOrdersByNotSignedUser();
        Assert.assertEquals(HTTP_UNAUTHORIZED, orderResponse.extract().statusCode());
        Assert.assertFalse(orderResponse.extract().body().path("success"));
    }
}
