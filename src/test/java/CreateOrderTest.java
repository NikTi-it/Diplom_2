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
import static Helper.TestDataCreator.getFakedIngredientId;
import static Helper.TestDataCreator.getNewDefaultUser;
import static java.net.HttpURLConnection.*;

public class CreateOrderTest {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private IngredientsSteps ingredientsSteps;
    private String token;
    private Order order;
    private List<String> ingredients;

    @Before
    public void before() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        ingredientsSteps = new IngredientsSteps();
        User user = getNewDefaultUser();
        userSteps.createUser(user);
        token = userSteps.signInUser(getCredentials(user)).extract().body().path("accessToken");
        Assert.assertNotNull(token);
        ingredients = new ArrayList<>();
    }

    @After
    public void after(){
        if (token !=null) {
            Assert.assertEquals(HTTP_ACCEPTED, userSteps.deleteUser(token).extract().statusCode());
        }
    }

    @Test
    @DisplayName("Создание заказа для авторизованного пользователя с ингридиентами")
    @Description("Позитивный тест создания заказа для авторизованного пользователя с ингридиентами")
    public void createOrderBySignedUserCheck(){
        ingredients.add(0, ingredientsSteps.getFirstIngredientId());
        ingredients.add(1, ingredientsSteps.getSecondIngredientId());
        ingredients.add(2, ingredientsSteps.getThirdIngredientId());
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrderBySignedUser(order,token);
        Assert.assertEquals(HTTP_OK, orderResponse.extract().statusCode());
        Assert.assertTrue(orderResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Создание заказа для неавторизованного пользователя с ингридиентами")
    @Description("Позитивный тест создания заказа для неавторизованного пользователя с ингридиентами")
    public void createOrderByNotSignedUserCheck(){
        ingredients.add(0, ingredientsSteps.getFirstIngredientId());
        ingredients.add(1, ingredientsSteps.getSecondIngredientId());
        ingredients.add(2, ingredientsSteps.getThirdIngredientId());
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrderByNotSignedUser(order);
        Assert.assertEquals(HTTP_OK, orderResponse.extract().statusCode());
        Assert.assertTrue(orderResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    @Description("Негативный тест создания заказа без ингридиентов")
    public void createOrderWithoutIngredientsCheck(){
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrderBySignedUser(order,token);
        Assert.assertEquals(HTTP_BAD_REQUEST, orderResponse.extract().statusCode());
        Assert.assertFalse(orderResponse.extract().body().path("success"));
    }

    @Test
    @DisplayName("Создание заказа с неверными ингридиентами")
    @Description("Негативный тест создания заказа с неверным хешем ингридиентов")
    public void createOrderWithWrongIngredientsCheck(){
        ingredients.add(0, getFakedIngredientId());
        ingredients.add(1, getFakedIngredientId());
        ingredients.add(2, getFakedIngredientId());
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrderBySignedUser(order,token);
        Assert.assertEquals(HTTP_INTERNAL_ERROR, orderResponse.extract().statusCode());
    }
}
