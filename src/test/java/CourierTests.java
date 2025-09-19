import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.Courier;
import org.example.models.CourierCredentials;
import org.example.models.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierTests {
    private CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private Integer courierId;


    @Before
    public void setUp() {
        RestAssured.filters(new ResponseLoggingFilter(), new ResponseLoggingFilter());
        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(12));
        courier.setPassword(RandomStringUtils.randomAlphabetic(12));
    }
    //тест 1 - создание курьера

    @Test
    public void shouldCreateCourierTest(){
            courierSteps
                    .createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));
    }
    //тест 2 - курьера авторизовать можно

    @Test
    public void shouldLoginCourierTest(){
            courierSteps
                .createCourier(courier);
            CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
            );
            courierSteps
                .login(credentials)
                .statusCode(200)
                .body("id", notNullValue());
    }
    //тест 3 - нельзя создать двух одинаковых курьеров

    @Test
    public void shouldNotCreateDuplicateCourier(){
        // Первое создание
        courierSteps.createCourier(courier);

        // Второе создание с теми же данными
        courierSteps.createCourier(courier)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    // тест 4 - обязательные поля - без логина

    @Test
    public void shouldNotCreateCourierWithoutLogin(){
        Courier invalidCourier = new Courier();
        invalidCourier.setPassword("password");
        invalidCourier.setFirstName("Name");

        courierSteps.createCourier(invalidCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // тест 5 - обязательные поля - без пароля

    @Test
    public void shouldNotCreateCourierWithoutPassword(){
        Courier invalidCourier = new Courier();
        invalidCourier.setLogin("login");
        invalidCourier.setFirstName("Name");

        courierSteps.createCourier(invalidCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // тест 6 - ошибка при неправильном пароле

    @Test
    public void shouldNotLoginWithWrongPassword(){
        courierSteps.createCourier(courier);

        CourierCredentials wrongCredentials = new CourierCredentials(
                courier.getLogin(),
                "wrong_password"
        );

        courierSteps.login(wrongCredentials)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // тест 7 - ошибка при несуществующем пользователе

    @Test
    public void shouldNotLoginNonExistentUser(){
        CourierCredentials nonExistent = new CourierCredentials(
                "nonexistent",
                "password"
        );

        courierSteps.login(nonExistent)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // тест 8 - обязательные поля для логина - пароль

    @Test
    public void shouldNotLoginWithoutPassword(){
        courierSteps.loginWithoutPassword(courier.getLogin())
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @After
    public void tearDown(){
        try {
            //
            CourierCredentials credentials = new CourierCredentials(
                    courier.getLogin(),
                    courier.getPassword()
            );

            courierId = courierSteps.login(credentials)
                    .extract().body().path("id");

            if (courierId != null) {
                courierSteps.deleteCourier(courierId);
            }
        } catch (Exception e) {

        }

    }
}
