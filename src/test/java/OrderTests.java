import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.example.models.CourierSteps;
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
public class OrderTests {
    private CourierSteps courierSteps = new CourierSteps();
    private Order order;
    private List<String> colors;

    public OrderTests(List<String> colors) {
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
    @Test
    public void shouldCreateOrderWithDifferentColorsTest() {
        courierSteps.createOrder(order)
                .statusCode(201)
                .body("track", notNullValue());
    }

//    Тестирование получения списка заказов
    @Test
    public void shouldGetOrdersListTest() {

        Integer track = courierSteps.createOrder(order)
                .extract().body().path("track");

        courierSteps.getOrdersList()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));

        if (track != null) {
            courierSteps.cancelOrder(track);
        }
    }

//    Тестирование получения списка заказов с лимитом
    @Test
    public void shouldGetOrdersListWithLimitTest() {
        courierSteps.getOrdersListWithLimit(5)
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", lessThanOrEqualTo(5));
    }

    @After
    public void tearDown() {

    }
}
