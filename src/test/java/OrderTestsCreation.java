import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


@RunWith(Parameterized.class)
public class OrderTestsCreation {
    private CourierStepsAndOrderSteps courierSteps = new CourierStepsAndOrderSteps();
    private Order order;
    private List<String> colors;
    private Integer track;

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
        RestAssured.filters(new ResponseLoggingFilter(), new ResponseLoggingFilter());

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
    }

    //    Тестирование создания заказа с разными цветами
    @DisplayName("Тестирование создания заказа с разными цветами")
    @Description("Проверка возможности создания заказа с разными цветами самоката")
    @Test
    public void shouldCreateOrderWithDifferentColorsTest() {
        courierSteps.createOrder(order)
                .statusCode(201)
                .body("track", notNullValue())
                .extract().body().path("track");;
    }


    @After
    public void tearDown() {
        if (track != null) {
            courierSteps.cancelOrder(track)
                    .statusCode(200);
        }
    }
}
