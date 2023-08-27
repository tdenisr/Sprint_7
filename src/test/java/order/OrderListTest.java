package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    private final static String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    OrderClient orderClient = new OrderClient();
    @Before
    public void setup(){
        RestAssured.baseURI = BASE_URI;
    }
    @Test
    @DisplayName("Получение всего списка заказов")
    public void getOrderList(){
        orderClient.getAllOrders()
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
}
