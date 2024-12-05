package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static java.net.HttpURLConnection.*;


public class OrderClient extends praktikum.Client {
    private static final String ORDER_PATH = "orders";
    private static final String INGREDIENTS_PATH = "ingredients";
    private static final String INGREDIENTS = "data._id";
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String BASE_PATH = "/api/";

    @Step("Получение списка ингредиентов")
    public static String[] getIngredients() {
        Response response = RestAssured
                .given().log().all()
                .get(BASE_URI+BASE_PATH+INGREDIENTS_PATH);

        response.then().statusCode(HTTP_OK);
        return response.jsonPath().getList(INGREDIENTS, String.class).toArray(new String[0]);
    }

    @Step("создать заказ c авторизацией и ингредиентами")
    public ValidatableResponse createOrderAllWright(String bearerToken, String[] ingredients) {
        return spec()
                .body(new Ingredients(ingredients))
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
    public ValidatableResponse createOrderWithoutAuth(String[] ingredients) {
        return spec()
                .body(new Ingredients(ingredients))
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
