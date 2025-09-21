package org.example.models;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;


public class CourierStepsAndOrderSteps {

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then();
    }
    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())

                .when()
                .delete("/api/v1/courier/" + id)
                .then();
    }
    @Step("Авторизация без пароля")
    public ValidatableResponse loginWithoutPassword(String login){
        CourierCredentials credentials = new CourierCredentials();
        credentials.setLogin(login);

        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов с лимитом")
    public ValidatableResponse getOrdersListWithLimit(int limit) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .queryParam("limit", limit)
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())

                .when()
                .delete("/api/v1/orders/track/" + track)
                .then();
    }
    @Step("Авторизация без логина")
    public ValidatableResponse loginWithoutLogin(String password) {
        CourierCredentials credentials = new CourierCredentials();
        credentials.setPassword(password);

        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
}
