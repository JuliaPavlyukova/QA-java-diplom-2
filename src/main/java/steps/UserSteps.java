package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.startsWith;

public class UserSteps{
    @Step("Проверка статуса ответа")
    public void checkedStatusResponse(Response response, int code) {
        response.then().statusCode(code);
    }


    @Step("Проверка тела ответа невалидного запроса")
    public void checkedBodyInvalidResponse(Response response, boolean successExpected, String messageExpected) {
        response.then().assertThat()
                .body("success", is(successExpected))
                .body("message", is(messageExpected));
    }


    @Step("Проверка тела ответа невалидного запроса")
    public void checkedBodyWithoutField(Response response) {
        response.then().assertThat()
                .body("success", is(false))
                .body("message", startsWith("Email, password and name are required fields"));
    }


    @Step("Проверка тела ответа невалидного залогирования")
    public void checkedBodyWithWrongField(Response response) {
        response.then().assertThat()
                .body("success", is(false))
                .body("message", startsWith("email or password are incorrect"));
    }


    @Step("Проверка тела ответа на успешную авторизацию/создание пользователя")
    public void checkedBodyResponseSuccessfulAuthorization(Response response, String email, String name) {
        response.then().assertThat()
                .body("success", is(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(emptyOrNullString()))
                .body("user.email", is(email))
                .body("user.name", is(name));
    }
}
