package org.example.models;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredConfig {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}
