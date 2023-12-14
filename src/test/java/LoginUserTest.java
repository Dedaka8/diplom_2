import client.UserClient;
import data.UserCredentials;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class LoginUserTest {
    public static final String INCORRECT_DATA = "email or password are incorrect";

    private UserClient userClient;
    private UserData user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken").toString();

    }

    @After
    public void cleanUp(){
        if(accessToken != null) {
            userClient.deleteUser(accessToken.substring(7));

        }

    }

    @Test
    @DisplayName("Вход пользователя в систему")
    public void userCanLoggingTest(){
        ValidatableResponse responseLogin = userClient.loginUser(UserCredentials.from(user));
        responseLogin.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email",equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Попытка входа пользователя с неверным паролем")
    public void userWithErrorPasswordLoggingTest() {
        ValidatableResponse responseLogin = userClient.loginUser(UserCredentials.errorPassword(user));
        responseLogin.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo(INCORRECT_DATA));
    }

    @Test
    @DisplayName("Попытка входа пользователя с неверным email")
    public void userWithErrorEmailLoggingTest() {
        ValidatableResponse responseLogin = userClient.loginUser(UserCredentials.errorEmail(user));
        responseLogin.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo(INCORRECT_DATA));
    }

    @Test
    @DisplayName("Попытка входа пользователя ез пароля")
    public void userWithoutPasswordLoggingTest() {
        ValidatableResponse responseLogin = userClient.loginUser(UserCredentials.withOutPassword(user));
        responseLogin.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo(INCORRECT_DATA));
    }
}
