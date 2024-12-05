package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class LoggingInUserTest {

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
    @DisplayName("пользователь может залогиниться")
    public void userCreatingAndLogging() {
        var user = User.random();
        client.createUser(user);


        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);
    }

    @Test
    @DisplayName("нельзя залогиниться без имейла")
    public void userLoggingWithoutLogin() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUserWithoutEmail(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        check.checkNotLoggedIn(loginResponse);
    }

    @Test
    @DisplayName("нельзя залогиниться без пароля")
    public void userLoggingWithoutPassword() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUserWithoutPassword(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        check.checkNotLoggedIn(loginResponse);
    }

    @Test
    @DisplayName("нельзя залогиниться с неправильным имейлом")
    public void userLoggingWithoutWithWrongLogin() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUserWithWrongEmail(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        check.checkNotLoggedInWithWrongEmailOrPassword(loginResponse);
    }

    @Test
    @DisplayName("нельзя залогиниться с неправильным паролем")
    public void userLoggingWithWrongPassword() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUserWithWrongPassword(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        check.checkNotLoggedInWithWrongEmailOrPassword(loginResponse);
    }
}
