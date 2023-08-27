package courier;

import courier.CourierClient;
import courier.CourierGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCreds;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Utils.randomString;

public class CourierLoginTest {
    private static String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    private final CourierClient courierClient = new CourierClient();
    Courier courier =  CourierGenerator.randomCourier();
    @Before
    public void setup(){
        RestAssured.baseURI = BASE_URI;
        courierClient.create(courier);
    }
    @Test
    @DisplayName("Вход под курьером. Возвращается ID курьера")
    public void loginCourierTest(){
        Response response = courierClient.login(CourierCreds.credsFrom(courier));
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(HttpStatus.SC_OK);

    }
    @Test
    @DisplayName("Вход без пароля")
    public void noPassReturnError(){
        CourierCreds creds = CourierCreds.credsFrom(courier);
        creds.setPassword("");
        Response response = courierClient.login(creds);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Вход без указания логина")
    public void noLoginReturnError(){
        CourierCreds creds = CourierCreds.credsFrom(courier);
        creds.setLogin("");
        Response response = courierClient.login(creds);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Попытка входа с неверынм паролем")
    public void wrongPassReturnError(){
        CourierCreds creds = CourierCreds.credsFrom(courier);
        creds.setPassword(randomString(2));
        Response response = courierClient.login(creds);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and().statusCode(HttpStatus.SC_NOT_FOUND);
    }
    @Test
    @DisplayName("Попытка входа с неверынм логином")
    public void wrongLoginReturnError(){
        CourierCreds creds = CourierCreds.credsFrom(courier);
        creds.setLogin(randomString(3));
        Response response = courierClient.login(creds);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and().statusCode(HttpStatus.SC_NOT_FOUND);
    }
    @Test
    @DisplayName("Попытка входа с неверынм паролем и логином")
    public void wrongLoginAndPassReturnError(){
        CourierCreds creds = CourierCreds.credsFrom(courier);
        creds.setLogin(randomString(3));
        creds.setPassword(randomString(3));
        Response response = courierClient.login(creds);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and().statusCode(HttpStatus.SC_NOT_FOUND);
    }
    @After
    public void tearDown(){
        courierClient.deleteCourier(courier);
    }
}
