package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.User;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;

public class UserApi {

    @Step("Before создания нового пользователя")
    public static RequestSpecification requestSpecification() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Создание нового пользователя")
    public  Response createUser(User user) {
        return (Response) requestSpecification()
                .body(user)
                .when()
                .post(CREATE_USER_REQUEST);
    }

    @Step("Авторизация ранее созданного пользователя")
    public Response loginUser(User user) {
        return requestSpecification()
                .body(user)
                .post(LOGIN_USER_REQUEST);

    }

    @Step("Авторизация ранее созданного пользователя")
    public Response loginWithoutPassword(User user) {
        return requestSpecification()
                .body(user.getEmail())
                .body(user.getName())
                .when()
                .post(LOGIN_USER_REQUEST);
    }


    @Step("Получение accessToken")
    public String getAccessToken(Response response) {
        return response.jsonPath().getString("accessToken");
    }


// Метод для удаления пользователя
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return requestSpecification()
                .header("Authorization",  accessToken)
                .when()
                .delete(DELETE_USER_REQUEST)
                .then();
    }
}
