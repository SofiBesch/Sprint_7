import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.Courier;
import org.example.models.CourierCredentials;
import org.example.models.CourierStepsAndOrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierTests {
    private CourierStepsAndOrderSteps courierSteps = new CourierStepsAndOrderSteps();
    private Courier courier;
    private Integer courierId;
    private boolean courierCreated = false;

    @Before
    public void setUp() {

        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(12));
        courier.setPassword(RandomStringUtils.randomAlphabetic(12));
    }
    //тест 1 - создание курьера
    @DisplayName("Тестирование создания курьера")
    @Description("Проверка возможности создать курьера")
    @Test
    public void shouldCreateCourierTest(){
            courierSteps
                    .createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));
        courierCreated = true;
    }

    //тест 2 - нельзя создать двух одинаковых курьеров
    @DisplayName("Тестирование создания двух одинаковых курьеров")
    @Description("Проверка возникновения ошибки при создании двух одинаковых курьеров")
    @Test
    public void shouldNotCreateDuplicateCourier(){

        courierSteps.createCourier(courier);
        courierCreated = true;

        courierSteps.createCourier(courier)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    // тест 3 - обязательные поля - без логина
    @DisplayName("Тестирование обязательных полей для создания курьера - без логина")
    @Description("Проверка возникновения ошибки при создании курьера - отсутствие данных в поле login")
    @Test
    public void shouldNotCreateCourierWithoutLogin(){
        Courier invalidCourier = new Courier();
        invalidCourier.setPassword("password");
        invalidCourier.setFirstName("Name");

        courierSteps.createCourier(invalidCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // тест 4 - обязательные поля - без пароля
    @DisplayName("Тестирование обязательных полей для создания курьера - без логина")
    @Description("Проверка возникновения ошибки при создании курьера - отсутствие данных в поле password")
    @Test
    public void shouldNotCreateCourierWithoutPassword(){
        Courier invalidCourier = new Courier();
        invalidCourier.setLogin("login");
        invalidCourier.setFirstName("Name");

        courierSteps.createCourier(invalidCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown(){
        if (courierCreated) {
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
                            .statusCode(200);;
                }
            } catch (Exception e) {

            }
        }
    }
}
