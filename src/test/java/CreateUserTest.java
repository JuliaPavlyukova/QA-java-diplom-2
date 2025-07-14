import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
    private final UserApi userApi = new UserApi();
    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private User user;
    private Response userResponse;
    private User withoutEmail;
    private User withoutPassword;
    private User withoutName;
    private Faker faker;

    @Before
    public void setUp() {
        faker = new Faker();
        //mail, password, name
        user = new User(faker.internet().emailAddress(), faker.internet().password(6, 6), faker.name().firstName());
        userResponse = userApi.createUser(user);
        accessToken = userApi.getAccessToken(userResponse);
        withoutEmail = new User(null, faker.internet().password(), faker.name().firstName());
        withoutPassword = new User(faker.internet().emailAddress(), null, faker.name().lastName());
        withoutName = new User(faker.internet().emailAddress(), faker.internet().password(), null);
    }


    //1 Создание пользователя: //создать уникального пользователя;
    @Test
    @DisplayName("Cоздание пользователя")
    @Description("Cоздать уникального пользователя;")
    public void createUserTest() {
        userSteps.checkedStatusResponse(userResponse, SC_OK);
        userSteps.checkedBodyResponseSuccessfulAuthorization(userResponse, user.getEmail(), user.getName());
    }


    //2 Создание пользователя: //создать пользователя, который уже зарегистрирован;
    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    @Description("При попытке создать пользователя, который уже зарегистрирован  ответ: 403 Forbidden. В теле вернётся информация об ошибке.")
    public void cannotCreateUserWhoIsAlreadyRegistered() {
        Response responseCreateNewUser = userApi.createUser(user);
        userSteps.checkedStatusResponse(responseCreateNewUser, SC_FORBIDDEN);
        userSteps.checkedBodyInvalidResponse(responseCreateNewUser, false, "User already exists");
    }


    //Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    @Test
    @DisplayName("Нельзя создать пользователя без логина")
    @Description("При попытке создать пользователя без логина ошибка")
    public void userLoginWithoutEmail() {
        Response response = userApi.createUser(withoutEmail);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }


    //Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    @Test
    @DisplayName("Нельзя создать пользователя без пароля")
    @Description("При попытке создать пользователя без пароля ошибка")
    public void userLoginWithoutPassword() {
        Response response = userApi.createUser(withoutPassword);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }


    //  Создание пользователя: //создать пользователя и не заполнить одно из обязательных полей.
    @Test
    @DisplayName("Нельзя создать пользователя без имени")
    @Description("ВПри попытке создать пользователя без имени ошибка")
    public void userLoginWithoutName() {
        Response response = userApi.createUser(withoutName);
        userSteps.checkedStatusResponse(response, SC_FORBIDDEN);
        userSteps.checkedBodyWithoutField(response);
    }


    //Логин пользователя: //вход под существующим пользователем;
    @Test
    @DisplayName("Вход по логину зарегестрированного пользователя")
    @Description("Вход под существующим пользователем. Ответ: 200 ОК. В теле ответа вернётся информация о пользователе и токен авторизации.")
    public void userLoginTest() {
        Response loginUserResponse = userApi.loginUser(user);
        userSteps.checkedStatusResponse(loginUserResponse, SC_OK);
        userSteps.checkedBodyResponseSuccessfulAuthorization(loginUserResponse, user.getEmail(), user.getName());
    }


    //Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход с пустым паролем")
    @Description("Вход под существующим пользователем с пустым паролем")
    public void userAuthorWithoutPasswordTest() {
        User user2 = new User(user.getEmail(), "", user.getEmail());
        Response loginUserResponse = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(loginUserResponse, SC_UNAUTHORIZED);
        userSteps.checkedBodyWithWrongField(loginUserResponse);
    }


    //Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход с неверным логином")
    @Description("Вход под существующим пользователем с неверным логином")
    public void userAuthorWithWrongNameTest() {

        User user2 = new User(faker.internet().emailAddress(), user.getPassword(), user.getName());
        Response loginUserResponse = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(loginUserResponse, SC_UNAUTHORIZED);
        userSteps.checkedBodyWithWrongField(loginUserResponse);
    }


    //Логин пользователя: // вход с неверным логином и паролем.
    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Вход под существующим пользователем с неверным паролем")
    public void userAuthorWithWrongPasswordTest() {
        User user2 = new User(user.getEmail(), faker.internet().password(), user.getName());
        Response loginUserResponse = userApi.loginUser(user2);
        userSteps.checkedStatusResponse(loginUserResponse, SC_UNAUTHORIZED);
        userSteps.checkedBodyWithWrongField(loginUserResponse);
    }


    @After
    public void deleteUser() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
