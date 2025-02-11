package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class CreateUserTest {
    private UserClient client = new UserClient();
    private UserChecks check = new UserChecks();

    String bearerToken;

    @After
    public void deleteUser() {
        if (bearerToken != null) {
            ValidatableResponse response = client.delete(bearerToken);
            check.deleted(response);
        }
    }

    @Test
    @DisplayName("пользователя можно создать")
    public void userCreating() {
        var user = User.random();
        ValidatableResponse createResponse = client.createUser(user);
        check.checkCreated(createResponse);
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых пользователей")
    public void userTwoSame() {
        var user = new User("zastya@yandex.ru", "password", "user" );
        ValidatableResponse createResponse = client.createUser(user);
        check.checkCreated(createResponse);

        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        var user1 = new User("zastya@yandex.ru", "password", "user" );
        ValidatableResponse createResponse1 = client.createUser(user1);
        check.checkFailedSameEmail(createResponse1);

        var creds1 = UserCredentials.fromUser(user1);
        ValidatableResponse loginResponse1 = client.logIn(creds1);
        bearerToken = check.checkLoggedIn(loginResponse1);
    }

    @Test
    @DisplayName("нельзя создать пользователя без имейла")
    public void cannotCreateWithoutLogin() {
        var user = User.withoutEmail();
        ValidatableResponse createResponse = client.createUser(user);
        check.checkFailed(createResponse);
    }

    @Test
    @DisplayName("нельзя создать пользователя без пароля")
    public void cannotCreateWithoutPassword() {
        var user = User.withoutPassword();
        ValidatableResponse createResponse = client.createUser(user);
        check.checkFailed(createResponse);
    }

    @Test
    @DisplayName("нельзя создать пользователя без имени (необязательное поле)")
    public void cannotCreateWithoutName() {
        var user = User.withoutName();
        ValidatableResponse createResponse = client.createUser(user);
        check.checkFailed(createResponse);
    }
}