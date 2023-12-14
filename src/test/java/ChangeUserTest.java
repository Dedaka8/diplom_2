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

public class ChangeUserTest {
    public static final String NEW_PASSWORD = "NEWpassword123";
    public static final String NOT_AUTH = "You should be authorised";
    private UserClient userClient;
    private UserData user;
    private String accessToken;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken").toString().substring(7);

    }

    @After
    public void cleanUp(){
        if(accessToken != null) {
            userClient.deleteUser(accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Изменение  email авторизованного пользователя")
    public void authUserCanChangeEmailTest() {
        user.setEmail("N"+user.getName());
        ValidatableResponse response = userClient.changeUser(user, accessToken);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email",equalTo(user.getEmail().toLowerCase()));

    }
    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void authUserCanChangeNameTest() {
        user.setName(user.getName()+"!");
        ValidatableResponse response = userClient.changeUser(user, accessToken);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.name",equalTo(user.getName()));

    }
    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void authUserCanChangePasswordTest() {
        user.setPassword(NEW_PASSWORD);
        ValidatableResponse response = userClient.changeUser(user, accessToken);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email",equalTo(user.getEmail().toLowerCase()));

        ValidatableResponse responseLogin = userClient.loginUser(UserCredentials.from(user));
        responseLogin.assertThat()
                .statusCode(SC_OK);

    }

    @Test
    @DisplayName("Изменение email неавторизованного пользователя")
    public void notAuthUserCantChangeEmailTest() {
        user.setEmail("N"+user.getName());
        ValidatableResponse response = userClient.changeUser(user,"");
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message",equalTo(NOT_AUTH));

    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    public void notAuthUserCantChangePasswordTest() {
        user.setPassword(NEW_PASSWORD);
        ValidatableResponse response = userClient.changeUser(user,"");
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message",equalTo(NOT_AUTH));
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void notAuthUserCantChangeNameTest() {
        user.setName(user.getName()+"!");
        ValidatableResponse response = userClient.changeUser(user,"");
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message",equalTo(NOT_AUTH));
    }
}



