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

public class OrderCreationTest {

    private final String name = RandomStringUtils.randomAlphabetic(6);
    private final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(7);

    public String accessToken;

    private final String[] ingredient = {"61c0c5a71d1f82001bdaaa6d"};
    private final String[] emptyIngredient = {};
    private final String[] invalidIngredient = {"45c8c7a90d2f82001dbvvv6b"};

    DetailsUser detailsUser;
    UserData userRegistration = new UserData(email, password, name);
    UserData userAuthorization = new UserData(email, password);
    IngredientsData order = new IngredientsData(ingredient);
    IngredientsData emptyOrder = new IngredientsData(emptyIngredient);
    IngredientsData invalidOrder = new IngredientsData(invalidIngredient);

    @Before
    public void setUp() {
        detailsUser = new DetailsUser();
        detailsUser.registration(userRegistration);
    }

    @Test
    @DisplayName("Создание заказа с авторизованным пользователем")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void orderCreateWithAuthorization() {
        accessToken = detailsUser.login(userAuthorization).extract().path("accessToken");
        ValidatableResponse orderResponse = detailsUser.orderCreateWithAuth(order, accessToken);
        orderResponse.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа с неавторизованным пользователем")
    @Description("Метод должен вернуть 200 с телом {'success': true}")
    public void orderCreateWithNotAuthorization() {
        ValidatableResponse orderResponse = detailsUser.orderCreateWithNotAuth(order);
        orderResponse.assertThat().body("success",equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Метод должен вернуть 400 с телом {'success': false}")
    public void orderCreateWithNotIngredients() {
        accessToken = detailsUser.login(userAuthorization).extract().path("accessToken");
        ValidatableResponse orderResponse = detailsUser.orderCreateWithAuth(emptyOrder, accessToken);
        orderResponse.assertThat().body("success", equalTo(false)).and().statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с некорректным ингредиентом")
    @Description("Метод должен вернуть 500")
    public void orderCreateWithBadIngredients() {
        accessToken = detailsUser.login(userAuthorization).extract().path("accessToken");
        ValidatableResponse orderResponse = detailsUser.orderCreateWithAuth(invalidOrder, accessToken);
        orderResponse.statusCode(500);
    }

    @After
    public void deleteUser() {
        ValidatableResponse response = detailsUser.login(userAuthorization);
        accessToken = response.extract().path("accessToken");
        detailsUser.removal(accessToken);
    }
}
