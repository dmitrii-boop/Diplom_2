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

public class UserCreationTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);
    public String accessToken;

    DetailsUser detailsUser;
    UserData userRegistration = new UserData(email, password, name);
    UserData userAuthorization = new UserData(email, password);

    @Before
    public void setUp() {
        detailsUser = new DetailsUser();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void creatingUniqueUser() throws InterruptedException {
        ValidatableResponse response = detailsUser.registration(userRegistration);
        response.assertThat().body("success",equalTo(true)).and().statusCode(200);
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Метод должен вернуть 403 с телом {'success': false}")
    public void creatingRegisteredUser() throws InterruptedException {
        detailsUser.registration(userRegistration);
        ValidatableResponse registerResponse = detailsUser.registration(userRegistration);
        registerResponse.assertThat().body("success",equalTo(false)).and().statusCode(403);
        Thread.sleep(1000);
    }

    @After
    public void deleteUser() throws InterruptedException {
        ValidatableResponse response = detailsUser.login(userAuthorization);
        accessToken = response.extract().path("accessToken");
        detailsUser.removal(accessToken);
        Thread.sleep(1000);
    }
}
