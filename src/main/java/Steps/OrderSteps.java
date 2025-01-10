package Steps;

import Serialization.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderSteps extends Configuration {

    private final static String ORDER_PATH ="/orders";

    @Step("Создание заказа для авторизованного пользователя")
    public ValidatableResponse createOrderBySignedUser(Order order, String token) {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .header("Authorization",token)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Создание заказа для неавторизованного пользователя")
    public ValidatableResponse createOrderByNotSignedUser(Order order) {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Получение заказов для авторизованного пользователя")
    public ValidatableResponse getOrdersBySignedUser(String token) {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .header("Authorization",token)
                .get(ORDER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Получение заказов для неавторизованного пользователя")
    public ValidatableResponse getOrdersByNotSignedUser(){
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .get(ORDER_PATH)
                .then()
                .log()
                .all();
    }
}
