package org.example.models;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;


public class CourierSteps {
    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then();
    }
    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")

                .when()
                .delete("/api/v1/courier/" + id)
                .then();
    }
    @Step("Авторизация без пароля")
    public ValidatableResponse loginWithoutPassword(String login){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("login", login);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(requestBody)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrdersList() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов с лимитом")
    public ValidatableResponse getOrdersListWithLimit(int limit) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .queryParam("limit", limit)
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(Map.of("track", track))
                .when()
                .put("/api/v1/orders/cancel")
                .then();
    }
}
