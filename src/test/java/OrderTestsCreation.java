import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.ValidatableResponse;
import org.example.models.CourierStepsAndOrderSteps;
import org.example.models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;



@RunWith(Parameterized.class)
public class OrderTestsCreation {
    private CourierStepsAndOrderSteps courierSteps = new CourierStepsAndOrderSteps();
    private Order order;
    private List<String> colors;
    private Integer track;
    private ValidatableResponse createOrderResponse;


    public OrderTestsCreation(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},           // один цвет BLACK
                {Arrays.asList("GREY")},            // один цвет GREY
                {Arrays.asList("BLACK", "GREY")},   // оба цвета
                {null}                              // без цвета
        });
    }

    @Before
    public void setUp() {


        order = new Order(
                "Владимир",
                "Ленин",
                "Красная площадь, 9",
                "4",
                "+79101917711",
                3,
                "2025-10-26",
                "Комментарий к заказу",
                colors
        );

        createOrderResponse = courierSteps.createOrder(order);
        track = createOrderResponse.extract().path("track");
    }

    //    Тестирование создания заказа с разными цветами
    @DisplayName("Тестирование создания заказа с разными цветами")
    @Description("Проверка возможности создания заказа с разными цветами самоката")
    @Test
    public void shouldCreateOrderWithDifferentColorsTest() {
        createOrderResponse
                .statusCode(201)
                .body("track", notNullValue());
    }


    @After
    public void tearDown() {
        if (track != null) {
            courierSteps.cancelOrder(track);
        }
    }
}
