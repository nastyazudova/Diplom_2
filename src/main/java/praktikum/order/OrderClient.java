package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


public class OrderClient extends praktikum.Client {
    private static final String ORDER_PATH = "orders";


    @Step("создать заказ c авторизацией и ингредиентами")
    public ValidatableResponse createOrderAllWright( String bearerToken, String bun, String sauce, String filling) {
        return spec()
                .body("{\"ingredients\": [\"" + bun + "\", \"" + sauce + "\", \"" + filling + "\"]}")
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("создать заказ без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients( String bearerToken) {
        return spec()
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("создать заказ без авторизации")
    public ValidatableResponse createOrderWithoutAuth(String bun, String sauce, String filling) {
        return spec()
                .body("{\"ingredients\": [\"" + bun + "\", \"" + sauce + "\", \"" + filling + "\"]}")
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("создать заказ без ингериентов и авторизации")
    public ValidatableResponse createOrderWithoutAuthAndIngredients() {
        return spec()
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("посмотреть заказы пользователя с авторизацией")
    public ValidatableResponse getUserOrderWithAuth( String bearerToken) {
        return spec()
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step("посмотреть заказы пользователя без авторизации")
    public ValidatableResponse getUserOrderWithoutAuth() {
        return spec()
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }
}
