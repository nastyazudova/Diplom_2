package praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

public class UserClient extends praktikum.Client {
    private static final String USER_PATH = "auth";

    @Step("залогиниться")
    public ValidatableResponse logIn(UserCredentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(USER_PATH + "/login")
                .then().log().all();
    }

    @Step("создать пользователя")
    public ValidatableResponse createUser(praktikum.user.User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then().log().all();
    }

    @Step("удалить пользователя")
    public ValidatableResponse delete(String bearerToken) {
        return spec()
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .delete(USER_PATH + "/user")
                .then().log().all();
    }

    @Step("изменить данные  пользователя")
    public ValidatableResponse changeUserData(praktikum.user.UserCredentials user, String bearerToken) {
        return spec()
                .body(user)
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .patch(USER_PATH + "/user")
                .then().log().all();
    }


}


