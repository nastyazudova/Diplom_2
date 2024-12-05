package praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class GetUserOrderTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private UserChecks userChecks = new UserChecks();
    private OrderChecks orderChecks = new OrderChecks();

    private String bearerToken;

    @After
    public void deleteUser() {
        if (bearerToken != null) {
            ValidatableResponse response = userClient.delete(bearerToken);
            userChecks.deleted(response);
        }
    }

    @Test
    @DisplayName("МОЖНО посмотреть заказы пользователя авторизовавшись")
    public void getUserOrderWithAuth() {
        var user = User.random();
        userClient.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = userClient.logIn(creds);
        bearerToken = userChecks.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = orderClient.getUserOrderWithAuth(bearerToken);
        orderChecks.checkGetUserOrderWithAuth(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ посмотреть заказы пользователя НЕ авторизовавшись")
    public void getUserOrderWithoutAuth() {
        ValidatableResponse createResponse = orderClient.getUserOrderWithoutAuth();
        orderChecks.checkNotGetUserOrderWithoutAuth(createResponse);
    }
}
