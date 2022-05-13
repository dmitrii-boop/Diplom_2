package utils;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pogo.IngredientsData;
import pogo.UserData;

import static io.restassured.RestAssured.given;

public class DetailsUser extends UrlSite {

    @Step("Регистрация пользователя")
    public ValidatableResponse registration(UserData user) {
        return given().spec(getUrlSpec()).body(user).post("api/auth/register").then();
    }

    @Step("Удаление пользователя")
    public void removal(String accessToken) {
        given().header("Authorization", accessToken).spec(getUrlSpec()).delete("api/auth/user").then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserData user) {
        return given().spec(getUrlSpec()).body(user).post("api/auth/login").then();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse changeUserDataWithAuth(UserData loginData, UserData updateData) {
        String accessToken = given().spec(getUrlSpec()).body(loginData).post("api/auth/login").then().extract().path("accessToken");
        return given().header("Authorization", accessToken)
                .spec(getUrlSpec()).body(updateData).patch("api/auth/user").then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse changeUserDataWithoutAuth(UserData updateData) {
        return given().spec(getUrlSpec()).body(updateData).patch("api/auth/user").then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse orderCreateWithAuth(IngredientsData order, String accessToken) {
        return given().header("Authorization", accessToken).spec(getUrlSpec()).body(order).post("/api/orders").then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse orderCreateWithNotAuth(IngredientsData order) {
        return given().spec(getUrlSpec()).body(order).post("/api/orders").then();
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getUserOrdersWithAuth(String accessToken) {
        return given().header("Authorization", accessToken).spec(getUrlSpec()).get("/api/orders").then();
    }

    @Step("Получение заказов неавторизованного пользователя")
    public ValidatableResponse getUserOrdersWithNotAuth() {
        return given().spec(getUrlSpec()).get("/api/orders").then();
    }


}

