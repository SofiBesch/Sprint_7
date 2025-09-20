import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.Courier;
import org.example.models.CourierCredentials;
import org.example.models.CourierStepsAndOrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTests {
    private CourierStepsAndOrderSteps courierSteps = new CourierStepsAndOrderSteps();
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.filters(new ResponseLoggingFilter(), new ResponseLoggingFilter());
        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(12));
        courier.setPassword(RandomStringUtils.randomAlphabetic(12));
        courier.setFirstName("Courier from New Vegas");
        courierSteps.createCourier(courier)
                .statusCode(201);
    }

    //тест 1 - курьера авторизовать можно
    @DisplayName("Тестирование авторизации курьера")
    @Description("Проверка возможности авторизации курьера в системе")

    @Test
    public void shouldLoginCourierTest(){

        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );
        courierSteps
                .login(credentials)
                .statusCode(200)
                .body("id", notNullValue());
    }

    // тест 2 - ошибка при неправильном пароле
    @DisplayName("Тестирование неправильного пароля")
    @Description("Проверка возникновения ошибки при вводе неправильного пароля")
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

    // тест 3 - ошибка при несуществующем пользователе
    @DisplayName("Тестирование создания несуществующего пользователя")
    @Description("Проверка возникновения ошибки при вводе данных для несуществующего пользователя")
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

    // тест 4 - обязательные поля для логина - пароль
    @DisplayName("Тестирование обязательных полей для логина - пароль")
    @Description("Проверка возникновения ошибки при отсутствии данных в поле password")
    @Test
    public void shouldNotLoginWithoutPassword(){
        courierSteps.loginWithoutPassword(courier.getLogin())
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // тест 5 - обязательные поля для логина - логин
    @DisplayName("Тестирование обязательных полей для логина - логин")
    @Description("Проверка возникновения ошибки при отсутствии данных в поле login")
    @Test
    public void shouldNotLoginWithoutLogin() {

        courierSteps.loginWithoutLogin(courier.getPassword())
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
                courierSteps.deleteCourier(courierId)
                        .statusCode(200);
            }
        } catch (Exception e) {

        }

    }

}
