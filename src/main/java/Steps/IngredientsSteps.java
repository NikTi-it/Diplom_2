package Steps;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientsSteps extends Configuration {

    private final static String INGREDIENTS_PATH ="/ingredients";

    @Step("Получение id первого ингридиента")
    public String getFirstIngredientId() {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data[0]._id");
    }
    @Step("Получение id второго ингридиента")
    public String getSecondIngredientId() {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data[1]._id");
    }
    @Step("Получение id третьего ингридиента")
    public String getThirdIngredientId() {
        return given()
                .spec(getConfiguration())
                .log()
                .all()
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data[2]._id");
    }
}
