package praktikum.order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class OrderCreatingTest {
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
    @DisplayName("МОЖНО создать заказ c авторизацией и правильными ингредиентами")
    public void createOrderAllWright() {
        var user = User.random();
        userClient.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = userClient.logIn(creds);
        bearerToken = userChecks.checkLoggedIn(loginResponse);

        String[] ingredients = OrderClient.getIngredients();
        ValidatableResponse createResponse = orderClient.createOrderAllWright(bearerToken, ingredients);
        orderChecks.checkCreated(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ c авторизацией и неверным хэшем ингредиентов (заменить знак в хэше)")
    public void createOrderAuthIngrWrongHashSignReplaced() {
        var user = User.random();
        userClient.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = userClient.logIn(creds);
        bearerToken = userChecks.checkLoggedIn(loginResponse);

        String[] ingredients = OrderClient.getIngredients();
        String[] wrongHashIngredients = ingredients.clone();
        for(int i = 0; i< wrongHashIngredients.length; i++) {
            wrongHashIngredients[i] = wrongHashIngredients[i].replace("0", "2");
        }

        ValidatableResponse createResponse = orderClient.createOrderAllWright(bearerToken, wrongHashIngredients);
        orderChecks.checkNotCreatedWrongHash(createResponse);
    }
    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ c авторизацией и неверным хэшем ингредиентов (прибавить число к хэшу)")
    public void createOrderAuthIngrWrongHashNumberAdded() {
        var user = User.random();
        userClient.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = userClient.logIn(creds);
        bearerToken = userChecks.checkLoggedIn(loginResponse);

        String[] ingredients = OrderClient.getIngredients();
        String[] wrongHashIngredients = ingredients.clone();
        for(int i = 0; i< wrongHashIngredients.length; i++) {
            wrongHashIngredients[i] = wrongHashIngredients[i]+"1";
        }

        ValidatableResponse createResponse = orderClient.createOrderAllWright(bearerToken, wrongHashIngredients);
        orderChecks.checkNotCreatedWrongHash(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ с авторизацией без ингредиентов")
    public void createOrderWithoutIngredients() {
        var user = User.random();
        userClient.createUser(user);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = userClient.logIn(creds);
        bearerToken = userChecks.checkLoggedIn(loginResponse);

        ValidatableResponse createResponse = orderClient.createOrderWithoutIngredients(bearerToken);
        orderChecks.checkNotCreatedWithoutIngredients(createResponse);
    }

    @Test
    @DisplayName("МОЖНО создать заказ без авторизации с правильными ингредиентами")
    public void createOrderWithoutAuth() {
        String[] ingredients = OrderClient.getIngredients();
        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(ingredients);
        orderChecks.checkCreated(createResponse);
    }


    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ без авторизации и неверным хэшем ингредиентов (заменить знак в хэше)")
    public void createOrderWithoutAuthWrongHashSignReplaced() {
        String[] ingredients = OrderClient.getIngredients();
        String[] wrongHashIngredients = ingredients.clone();
        for(int i = 0; i< wrongHashIngredients.length; i++) {
            wrongHashIngredients[i] = wrongHashIngredients[i].replace("0", "2");
        }

        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(wrongHashIngredients);
        orderChecks.checkNotCreatedWrongHash(createResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ создать заказ без авторизации и неверным хэшем ингредиентов (прибавить число к хэшу)")
    public void createOrderWithoutAuthWrongHashNumberAdded() {
        String[] ingredients = OrderClient.getIngredients();
        String[] wrongHashIngredients = ingredients.clone();
        for(int i = 0; i< wrongHashIngredients.length; i++) {
            wrongHashIngredients[i] = wrongHashIngredients[i] + "1";
        }

        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(wrongHashIngredients);
        orderChecks.checkNotCreatedWrongHash(createResponse);
    }

    @Test
    @DisplayName("создать заказ без авторизации и ингредиентов")
    public void createOrderWithoutAuthAndIngredients() {
        ValidatableResponse createResponse = orderClient.createOrderWithoutAuthAndIngredients();
        orderChecks.checkNotCreatedWithoutIngredients(createResponse);
    }
}
