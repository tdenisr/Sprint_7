package courier;


import io.restassured.response.Response;
import models.Courier;
import models.CourierCreds;

import static io.restassured.RestAssured.given;


public class CourierClient {
    private static String CREATE_URL = "/api/v1/courier";
    private static String LOGIN_URL = "/api/v1/courier/login";
    private static String DELETE_URL = "/api/v1/courier/";
    public Response create(Courier courier){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(CREATE_URL);
    }
    public Response login(CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_URL);
    }
    public Response deleteCourier(Courier courier){
        String id = getCourierId(courier);
        return given()
                .header("Content-type", "application/json")
                .and()
                .pathParam("id", id)
                .body("{\"id\":\"" + id + "\"}")
                .when()
                .delete(DELETE_URL+ "{id}");
    }
    public String getCourierId(Courier courier){
        Response response = login(CourierCreds.credsFrom(courier));
        return response.jsonPath().getString("id");
    }
}
