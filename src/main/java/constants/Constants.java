package constants;

public class Constants {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static final String DELETE_USER_REQUEST = "api/auth/user";
    public static final String CREATE_USER_REQUEST = "api/auth/register";
    public static final String LOGIN_USER_REQUEST = "api/auth/login";

    public static final String CHANGE_INFORMATION_USER_REQUEST = "api/auth/changePassword";

    // Endpoint для работы с Orders
    public static final String ORDERS_REQUEST = "api/orders";
    public static final String INGREDIENTS_REQUEST = "api/ingredients";

}
// Создание пользователя:
//создать уникального пользователя; CRATE_NEW_USER
//        создать пользователя, который уже зарегистрирован;
//создать пользователя и не заполнить одно из обязательных полей.
//Логин пользователя:
//вход под существующим пользователем;
//вход с неверным логином и паролем.
//Создание заказа:
//с авторизацией;
//без авторизации;
//с ингредиентами;
//без ингредиентов;
//с неверным хешем ингредиентов.
