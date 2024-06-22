package Steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Configuration {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String BASE_PATH = "api/";

    protected RequestSpecification getConfiguration() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .build();
    }
}
