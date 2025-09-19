package org.example;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class CourierSteps {
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then();
    }
    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
//                .pathParams("id", courier.getId())
                .when()
                .delete("/api/v1/courier/" + id)
                .then();
    }
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
}
