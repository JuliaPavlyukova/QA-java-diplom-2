package org.example;

import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Test;
import pojo.User;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
    private final UserApi userApi = new UserApi();
    private final UserSteps userSteps = new UserSteps();
    private String accessToken = null;


    //
    Faker faker = new Faker();
    //mail, password, name
    User user = new User(faker.internet().emailAddress(),  faker.internet().password(), faker.name().firstName());
   // User withoutEmail = new User("", faker.internet().password(), faker.name().firstName());
    User withoutEmail = new User(null, faker.internet().password(), faker.name().firstName());
    User withoutPassword = new User(faker.internet().emailAddress(), null, faker.name().lastName());
    User withoutName= new User(faker.internet().emailAddress(), faker.internet().password(), null);




//1 Создание пользователя: //создать уникального пользователя;
    @Test
    @DisplayName("Cоздание пользователя")
    @Description("Cоздать уникального пользователя;")
    public void createUserTest() {
        Response response = userApi.createUser(user);
        userApi.getAccessToken(response);
        userSteps.checkedStatusResponse(response, SC_OK);
        userSteps
                .checkedBodyResponseSuccessfulAuthorization(response, user
                        .getEmail(), user.getName());
    }

    //2 Создание пользователя: //создать пользователя, который уже зарегистрирован;
    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    @Description("При попытке создать пользователя, который уже зарегистрирован  ответ: 403 Forbidden. В теле вернётся информация об ошибке.")
    public void cannotCreateUserWhoIsAlreadyRegistered() {
        Response response = userApi.createUser(user);
        accessToken = userApi.getAccessToken(response);
        Response responseCreateNewUser = userApi.createUser(user);
        userSteps.checkedStatusResponse(responseCreateNewUser, SC_FORBIDDEN);
        userSteps.checkedBodyInvalidResponse(responseCreateNewUser, false, "User already exists");
    }

//Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    /// *************************
    @Test
    @DisplayName("Вход по логину зарегестрированного пользователя")
    @Description("Вход под существующим пользователем. Оьзователе и токен авторизации.")
    public void userLoginWithoutEmail() {
        Response response = userApi.createUser(withoutEmail);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }

    //Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    /// *************************
    @Test
    @DisplayName("Вход по логину зарегестрированного пользователя")
    @Description("Вход под существующим пользователем. Оьзователе и токен авторизации.")
    public void userLoginWithoutPassword() {
        Response response = userApi.createUser(withoutPassword);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }



    //Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    /// *************************
    @Test
    @DisplayName("Вход по логину зарегестрированного пользователя")
    @Description("Вход под существующим пользователем. Оьзователе и токен авторизации.")
    public void userLoginWithoutName() {
        Response response = userApi.createUser(withoutName);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }


//    Логин пользователя: //вход под существующим пользователем;
    @Test
    @DisplayName("Вход по логину зарегестрированного пользователя")
    @Description("Вход под существующим пользователем. Ответ: 200 ОК. В теле ответа вернётся информация о пользователе и токен авторизации.")
    public void userLoginTest() {
        Response response = userApi.createUser(user);
        System.out.println("user 1  " +user.getEmail() + " " + user.getPassword() + " " + user.getName());
        Response response2 = userApi.loginUser(user);
        System.out.println("user 2  " +user.getEmail() + " " + user.getPassword() + " " + user.getName());
        userSteps.checkedStatusResponse(response2, SC_OK);
        userSteps.checkedBodyResponseSuccessfulAuthorization(response2, user.getEmail(), user.getName());
    }




//    Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход по *************")
    @Description("Вход под существующим  ")
    public void userAuthorWithoutPasswordTest() {
        Response response = userApi.createUser(user);
        //emailAddress(), password(),firstName()
        User user2 = new User(user.getEmail(), "",  user.getEmail());
        System.out.println("user 2  " + user2);
        Response response2 = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(response2, SC_UNAUTHORIZED);
        System.out.println("statusCode()  " + response2.statusCode());
        userSteps.checkedBodyWithWrongField(response2);
    }

    //    Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход по *************")
    @Description("Вход под существующим  ")
    public void userAuthorWithWrongNameTest() {
        Response response = userApi.createUser(user);
        //emailAddress(), password(),firstName()
        User user2 = new User(faker.internet().emailAddress(), user.getPassword(), user.getName());
        System.out.println("user 2 =====  " + user.getName() + " " + user2.getName());
        Response response2 = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(response2, SC_UNAUTHORIZED);
        System.out.println("statusCode()  " + response2.statusCode());
        userSteps.checkedBodyWithWrongField(response2);
    }

    //    Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход по *************")
    @Description("Вход под существующим  ")
    public void userAuthorWithWrongPasswordTest() {
        Response response = userApi.createUser(user);
        User user2 = new User(user.getEmail(), faker.internet().password(), user.getName());
        System.out.println("user 2  " + user2);
        Response response2 = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(response2, SC_UNAUTHORIZED);
        System.out.println("statusCode()  " + response2.statusCode());
        userSteps.checkedBodyWithWrongField(response2);
    }


    @After
    public void deleteUser() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
