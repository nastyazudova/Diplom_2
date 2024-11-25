package praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;
import java.util.Set;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;
import org.assertj.core.api.SoftAssertions;

public class UserChecks {
    @Step("создался успешно")
    public void checkCreated(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue(created);
    }

    @Step("создать не получилось(отсутствует имейл, пароль или имя)")
    public void checkFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);


        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(body.get("message"))
                .isEqualTo("Email, password and name are required fields");
        soft.assertThat(body.keySet())
                .isEqualTo(Set.of("success","message"));
        soft.assertAll();
    }

    @Step("создать не получилось, такой имейл уже есть")
    public void checkFailedSameEmail(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(body.get("message"))
                .isEqualTo("User already exists");
        soft.assertThat(body.keySet())
                .isEqualTo(Set.of("success","message"));
        soft.assertAll();

    }

    @Step("залогинился")
    public String checkLoggedIn(ValidatableResponse loginResponse) {
        String bearerToken = loginResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("accessToken");

        assertNotEquals(0, bearerToken);

        return bearerToken;
    }

    @Step("не залогинился без имейла или пароля")
    public void checkNotLoggedIn(ValidatableResponse loginResponse) {
        var body = loginResponse
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .body().as(Map.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(body.get("message"))
                .isEqualTo("email or password are incorrect");
        soft.assertThat(body.keySet())
                .isEqualTo(Set.of("success","message"));
        soft.assertAll();

    }

    @Step("не залогинился с неправильным логином или паролем")
    public void checkNotLoggedInWithWrongLoginOrPassword(ValidatableResponse loginResponse) {
        var body = loginResponse
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .body().as(Map.class);


        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(body.get("message"))
                .isEqualTo("email or password are incorrect");
        soft.assertThat(body.keySet())
                .isEqualTo(Set.of("success", "message"));
        soft.assertAll();
    }

    @Step("удалился")
    public void deleted(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_ACCEPTED)
                .extract()
                .path("success");
        assertTrue(created);
    }
}
