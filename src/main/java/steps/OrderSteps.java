package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {
    // Проверка успешного создания заказа
    @Step("Create order with ingredients")
    public void checkOrderCreatedSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }
    //Проверка ошибки при создании заказа без ингредиентов
    @Step("Create order without ingredients")
    public void checkOrderCreationWithoutIngredientsFails(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    // Проверка ошибки при создании заказа с невалидными ингредиентами
    @Step("Create order with invalid ingredients")
    public void checkOrderCreationWithInvalidIngredientsFails(ValidatableResponse response) {
        response.assertThat()
                .statusCode(500);
    }
    // Проверка получения заказов авторизованного пользователя
    @Step("Get orders for authorized user")
    public void checkGetOrdersWithAuth(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }
    // Проверка ошибки получения заказов без авторизации
    @Step("Get orders for unauthorized user")
    public void checkGetOrdersWithoutAuth(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
