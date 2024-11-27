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
    private UserClient client = new UserClient();
    private OrderClient client1 = new OrderClient();
    private UserChecks check = new UserChecks();
    private OrderChecks check1 = new OrderChecks();

    private String bearerToken;

    @After
    public void deleteUser() {
        if (bearerToken != null) {
            ValidatableResponse response = client.delete(bearerToken);
            check.deleted(response);
        }
    }

    @Test
    @DisplayName("МОЖНО посмотреть заказы пользователя авторизовавшись")
    public void getUserOrderWithAuth() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = client1.getUserOrderWithAuth(bearerToken);
        check1.checkGetUserOrderWithAuth(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ посмотреть заказы пользователя НЕ авторизовавшись")
    public void getUserOrderWithoutAuth() {
        ValidatableResponse createResponse = client1.getUserOrderWithoutAuth();
        check1.checkNotGetUserOrderWithoutAuth(createResponse);
    }
}
