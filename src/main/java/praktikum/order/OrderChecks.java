package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderChecks {
    @Step("заказ создался успешно")
    public void checkCreated(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue(created);
    }

    @Step("заказ НЕ создался с неправильным хэшем ингредиентов")
    public void checkNotCreatedWrongHash(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("заказ НЕ создался без ингредиентов")
    public void checkNotCreatedWithoutIngredients(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("success");
        assertFalse(created);
    }

    @Step("МОЖНО посмотреть заказы пользователя авторизовавшись")
    public void checkGetUserOrderWithAuth(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue(created);
    }

    @Step("НЕЛЬЗЯ посмотреть заказы пользователя НЕ авторизовавшись")
    public void checkNotGetUserOrderWithoutAuth(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("success");
        assertFalse(created);
    }
}
