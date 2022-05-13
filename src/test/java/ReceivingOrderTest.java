import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pogo.IngredientsData;
import pogo.UserData;
import utils.DetailsUser;

import static org.hamcrest.core.IsEqual.equalTo;

public class ReceivingOrderTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);

    public String accessToken;
    private final String[] ingredient = {"61c0c5a71d1f82001bdaaa6d"};
    DetailsUser detailsUser;
    UserData userRegistration = new UserData(email, password, name);
    UserData userAuthorization = new UserData(email, password);
    IngredientsData order = new IngredientsData(ingredient);

    @Before
    public void setUp() throws InterruptedException {
        detailsUser = new DetailsUser();
        detailsUser.registration(userRegistration);
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void getOrdersAuthorizedUser() throws InterruptedException {
        accessToken = detailsUser.login(userAuthorization).extract().path("accessToken");
        detailsUser.orderCreateWithAuth(order, accessToken);
        ValidatableResponse getOrderResponse = detailsUser.getUserOrdersWithAuth(accessToken);
        getOrderResponse.assertThat().body("success", equalTo(true)).and().statusCode(200);
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Метод должен вернуть 401 с телом {'success': false}")
    public void getOrdersNotAuthorizedUser() throws InterruptedException {
        ValidatableResponse getOrderResponse = detailsUser.getUserOrdersWithNotAuth();
        getOrderResponse.assertThat().body("success", equalTo(false)).and().statusCode(401);
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
