import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import pogo.UserData;
import utils.DetailsUser;

import static org.hamcrest.core.IsEqual.equalTo;

public class UserAuthorizationTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);

    public String accessToken;

    DetailsUser detailsUser;
    UserData userRegistration = new UserData(email, password, name);
    UserData userAuthorization = new UserData(email, password);

    UserData userAuthorizationWithIncorrectEmail = new UserData("Vladimir8790", password);
    UserData userAuthorizationWithIncorrectPassword = new UserData(email, "registration4759");

    @Before
    public void setUp() {
        detailsUser = new DetailsUser();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void userLogin() {
        ValidatableResponse response = detailsUser.registration(userRegistration);
        detailsUser.login(userAuthorization);
        response.assertThat().body("success",equalTo(true)).and().statusCode(200);
        accessToken = response.extract().path("accessToken");
        detailsUser.removal(accessToken);
    }

    @Test
    @DisplayName("Логин с несуществующим email")
    @Description("Метод должен вернуть 401 с телом {'success': false}")
    public void userLoginWithIncorrectEmail() {
        ValidatableResponse response = detailsUser.login(userAuthorizationWithIncorrectEmail);
        response.assertThat().body("success",equalTo(false)).and().statusCode(401);
    }

    @Test
    @DisplayName("Логин с несуществующим паролем")
    @Description("Метод должен вернуть 401 с телом {'success': false}")
    public void userLoginWithIncorrectPassword() {
        ValidatableResponse response = detailsUser.login(userAuthorizationWithIncorrectPassword);
        response.assertThat().body("success",equalTo(false)).and().statusCode(401);
    }
}
