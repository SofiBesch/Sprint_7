import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import org.example.models.CourierStepsAndOrderSteps;
import org.example.models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class OrderListTest {
    private CourierStepsAndOrderSteps courierSteps = new CourierStepsAndOrderSteps();
    private Order order;
    private Integer track;

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
                Arrays.asList("BLACK")
        );
        track = courierSteps.createOrder(order)
                .statusCode(201)
                .extract().body().path("track");
    }
    //    1  получения списка заказов
    @DisplayName("Тестирование получения списка заказов")
    @Description("Проверка возможности получения списка заказов")
    @Test
    public void shouldGetOrdersListTest() {



        courierSteps.getOrdersList()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));

    }

    //    2 получения списка заказов с лимитом
    @DisplayName("Тестирование получения списка заказов с лимитом")
    @Description("Проверка возможности получения списка заказов с лимитом")
    @Test
    public void shouldGetOrdersListWithLimitTest() {

        courierSteps.getOrdersListWithLimit(5)
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", lessThanOrEqualTo(5));
    }

    @After
    public void tearDown() {
        if (track != null) {
            courierSteps.cancelOrder(track);
        }
    }
}
