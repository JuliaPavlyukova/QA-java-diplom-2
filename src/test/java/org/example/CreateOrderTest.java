package org.example;

import api.OrderApi;
import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import steps.OrderSteps;

import java.util.List;

public class CreateOrderTest {
    private final UserApi userApi = new UserApi();
    private final OrderApi orderApi = new OrderApi();
    private final OrderSteps orderChecks = new OrderSteps();
    private String accessToken;

    private List<String> getValidIngredients() {
        return orderApi.getAvailableIngredients()
                .extract()
                .path("data._id");
    }

    @Before
    public void setUp() {
        Faker faker = new Faker();
        //mail, password, name
        User user = new User(faker.internet().emailAddress(),  faker.internet().password(), faker.name().firstName());
        Response response = userApi.createUser(user);
        accessToken = userApi.getAccessToken(response);
    }

//    Создание заказа:
//без ингредиентов;
//с неверным хешем ингредиентов.


// 1 Создание заказа:     //с авторизацией;
    @Test
    @DisplayName("Create order with authorization and valid ingredients")
    @Description("Проверка на успешное создание заказа с авторизацией и валидными ингредиентами")
    public void orderCanBeCreatedWithAuthAndValidIngredientsTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);

        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }


    //2   Создание заказа: //без авторизации;
    @Test
    @DisplayName("Create order without authorization")
    @Description("Проверка на создание заказа без авторизации")
    public void orderCanBeCreatedWithoutAuthTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);

        ValidatableResponse response = orderApi.createOrderWithoutAuth(order);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }




    //    Создание заказа: //без ингредиентов;
    @Test
    @DisplayName("Create order without ingredients")
    @Description("Проверка на невозможность создания заказа без ингредиентов")
    public void orderCannotBeCreatedWithoutIngredientsTest() {
        Order order = Order.emptyOrder();

        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreationWithoutIngredientsFails(response);
    }

//    Создание заказа: //    с неверным хешем ингредиентов.
    @Test
    @DisplayName("Create order with invalid ingredients")
    @Description("Проверка на невозможность создания заказа с невалидными ингредиентами")
    public void orderCannotBeCreatedWithInvalidIngredientsTest() {
        Order order = Order.invalidOrder();
        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        System.out.println("order " +order.getIngredients() );
        orderChecks.checkOrderCreationWithInvalidIngredientsFails(response);
    }


    // удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
