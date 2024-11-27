package praktikum.order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class OrderMakingTest {
    private UserClient client = new UserClient();
    private OrderClient client1 = new OrderClient();
    private UserChecks check = new UserChecks();
    private OrderChecks check1 = new OrderChecks();

    private String bun = "61c0c5a71d1f82001bdaaa6c";
    private String sauce= "61c0c5a71d1f82001bdaaa75";
    private String filling = "61c0c5a71d1f82001bdaaa76";
    private String bearerToken;

    @After
    public void deleteUser() {
        if (bearerToken != null) {
            ValidatableResponse response = client.delete(bearerToken);
            check.deleted(response);
        }
    }

    @Test
    @DisplayName("МОЖНО создать заказ c авторизацией и правильными ингредиентами")
    public void createOrderAllWright() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = client1.createOrderAllWright(bearerToken, bun, sauce, filling);
        check1.checkCreated(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ c авторизацией и неверным хэшем ингредиентов")
    public void createOrderAuthIngrWrongHash() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = client1.createOrderAllWright(bearerToken, bun+1, sauce+1, filling+1);
        check1.checkNotCreatedWrongHash(createResponse);
    }

    @Test
    @DisplayName("создать заказ с авторизацией без ингредиентов")
    public void createOrderWithoutIngredients() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = client1.createOrderWithoutIngredients(bearerToken);
        check1.checkNotCreatedWithoutIngredients(createResponse);
    }

    @Test
    @DisplayName("МОЖНО создать заказ без авторизации с правильными ингредиентами")
    public void createOrderWithoutAuth() {
        ValidatableResponse createResponse = client1.createOrderWithoutAuth(bun, sauce, filling);
        check1.checkCreated(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ без авторизации и неверным хэшем ингредиентов")
    public void createOrderWithoutAuthWrongHash() {
        ValidatableResponse createResponse = client1.createOrderWithoutAuth(bun+1, sauce+1, filling+1);
        check1.checkNotCreatedWrongHash(createResponse);
    }

    @Test
    @DisplayName("создать заказ без авторизации и ингредиентов")
    public void createOrderWithoutAuthAndIngredients() {
        ValidatableResponse createResponse = client1.createOrderWithoutAuthAndIngredients();
        check1.checkNotCreatedWithoutIngredients(createResponse);
    }
}
