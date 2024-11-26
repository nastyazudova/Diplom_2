package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class ChangeUserDataTest {
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
    @DisplayName("можно залогиниться и поменять данные пользователя")
    public void userMakingLoggingAndChanging() {
        var user = User.random();
        client.createUser(user);


        var creds = UserCredentials.fromUser(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        bearerToken = check.checkLoggedIn(loginResponse);

        client.changeUserData(UserCredentials.fromUserWithNewData(user), bearerToken);
        creds = UserCredentials.fromUserWithNewData(user);
        loginResponse = client.logIn(creds);
        check.checkChangedEmail(UserCredentials.fromUserWithNewData(user), loginResponse);
        check.checkChangedName(UserCredentials.fromUserWithNewData(user), loginResponse);
        check.checkLoggedIn(loginResponse);
    }

    @Test
    @DisplayName("НЕ залогиниться и поменять данные пользователя")
    public void userMakingAndChanging() {
        var user = User.random();
        client.createUser(user);

        client.changeUserData(UserCredentials.fromUserWithNewData(user), null);
        var creds = UserCredentials.fromUserWithNewData(user);
        ValidatableResponse loginResponse = client.logIn(creds);
        check.checkChangedEmail(UserCredentials.fromUserWithNewData(user), loginResponse);
        check.checkChangedName(UserCredentials.fromUserWithNewData(user), loginResponse);
        check.checkLoggedIn(loginResponse);
    }

    @Test
    @DisplayName("НЕЛЬЗЯ НЕ залогиниться и поменять данные пользователя")
    public void userMakingAndChangingNot() {
        var user = User.random();
        client.createUser(user);

        var creds = UserCredentials.fromUserWithNewData(user);
        ValidatableResponse Response = client.changeUserData(UserCredentials.fromUserWithNewData(user), null);
        check.checkFailedChanged(Response);
    }

}
