import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import pogo.UserData;
import utils.DetailsUser;

import static org.hamcrest.core.IsEqual.equalTo;

public class UserCreationNegativeTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);

    DetailsUser detailsUser;
    UserData userRegistrationWithOutEmail = new UserData("", password, name);
    UserData userRegistrationWithOutPassword = new UserData(email, "", name);
    UserData userRegistrationWithOutName = new UserData(email, password, "");

    @Before
    public void setUp() {
        detailsUser = new DetailsUser();
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Метод должен вернуть 403 с телом {'success': false}")
    public void creatingUserWithOutEmail() {
        ValidatableResponse response = detailsUser.registration(userRegistrationWithOutEmail);
        response.assertThat().body("success",equalTo(false)).and().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Метод должен вернуть 403 с телом {'success': false}")
    public void creatingUserWithOutPassword() {
        ValidatableResponse response = detailsUser.registration(userRegistrationWithOutPassword);
        response.assertThat().body("success",equalTo(false)).and().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Метод должен вернуть 403 с телом {'success': false}")
    public void creatingUserWithOutName() {
        ValidatableResponse response = detailsUser.registration(userRegistrationWithOutName);
        response.assertThat().body("success",equalTo(false)).and().statusCode(403);
    }
}
