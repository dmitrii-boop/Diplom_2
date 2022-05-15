import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pogo.UserData;
import utils.DetailsUser;

import static org.hamcrest.core.IsEqual.equalTo;

public class ChangeUserDataTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);
    public String accessToken;

    private final String newName = RandomStringUtils.randomAlphabetic(6);
    private final String newEmail = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String newPassword = RandomStringUtils.randomAlphabetic(7);

    DetailsUser detailsUser;
    UserData userRegistration = new UserData(email, password, name);
    UserData userAuthorization = new UserData(email, password);
    UserData newUserRegistration = new UserData(newEmail, newPassword, newName);

    @Before
    public void setUp() {
        detailsUser = new DetailsUser();
        detailsUser.registration(userRegistration);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void changeUserDataWithAuthorization() {
        ValidatableResponse changeResponse = detailsUser.changeUserDataWithAuth(userAuthorization, newUserRegistration);
        changeResponse.assertThat().body("success",equalTo(true)).and().statusCode(200);
        detailsUser.changeUserDataWithAuth(newUserRegistration, userAuthorization);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Метод должен вернуть 401 с телом {'success': false}")
    public void changeUserDataWithNotAuthorization() {
        ValidatableResponse changeResponse = detailsUser.changeUserDataWithoutAuth(newUserRegistration);
        changeResponse.assertThat().body("success",equalTo(false)).and().statusCode(401);
    }

    @After
    public void deleteUser() {
        ValidatableResponse response = detailsUser.login(userAuthorization);
        accessToken = response.extract().path("accessToken");
        detailsUser.removal(accessToken);
    }
}





