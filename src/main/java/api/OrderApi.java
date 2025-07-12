package api;

import constants.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.Order;

import static api.UserApi.requestSpecification;
import static io.restassured.RestAssured.given;

public class OrderApi {

    // Метод для создания заказа с авторизацией
    @Step("Create order with authorization")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
            return requestSpecification()
                    .header("Authorization", token)
                    .body(order)
                    .when()
                    .post(Constants.ORDERS_REQUEST)
                    .then();
    }


    // Метод для создания заказа без авторизации
    @Step("Create order without authorization")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return requestSpecification()
                .body(order)
                .when()
                .post(Constants.ORDERS_REQUEST)
                .then().log().all();
    }


    // Метод для получения доступных ингредиентов
    @Step("Get available ingredients")
    public ValidatableResponse getAvailableIngredients() {
        return requestSpecification()
                .when()
                .get("/api/ingredients")
                .then();
    }


    // Метод для получения заказов пользователя
    @Step("Get orders")
    public ValidatableResponse getOrders(String token) {
        if (token != null) {
            return requestSpecification()
                    .header("Authorization", token)
                    .when()
                    .get(Constants.ORDERS_REQUEST)
                    .then();
        } else {
            return requestSpecification()
                    .when()
                    .get(Constants.ORDERS_REQUEST)
                    .then();
        }
    }
}
