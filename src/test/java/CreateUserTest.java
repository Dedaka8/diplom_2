import client.UserClient;
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

public class CreateUserTest {
    public static final String DUPLICATION_ERROR = "User already exists";
    public static final String NOT_FULL_DATA_ERROR = "Email, password and name are required fields";
    private UserClient userClient;
    private UserData user;
    private UserData userWithoutEmail;
    private UserData userWithoutPassword;
    private UserData userWithoutName;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        userWithoutEmail = UserGenerator.getRandomUserWithoutEmail();
        userWithoutPassword = UserGenerator.getRandomUserWithoutPassword();
        userWithoutName = UserGenerator.getRandomUserWithoutName();

    }

    @After
    public void cleanUp() {
        if(accessToken != null) {
           userClient.deleteUser(accessToken.substring(7));
        }

    }


    @Test
    @DisplayName("Создание пользователя")
    public void userCanBeCreatedTest() {
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
        accessToken = response.extract().path("accessToken").toString();

    }


    @Test
    @DisplayName("Попытка создания дубля пользователя")
    public void userDuplicationCreatedTest(){

        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse duplicationResponse = userClient.createUser(user);
        duplicationResponse.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(DUPLICATION_ERROR));
    }


    @Test
    @DisplayName("Создание пользователя без пароля")
    public void userCreatedWithoutPasswordTest() {
        ValidatableResponse response = userClient.createUser(userWithoutPassword);
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(NOT_FULL_DATA_ERROR));

    }
    @Test
    @DisplayName("Создание пользователя без имени")
    public void userCreatedWithoutNameTest() {
        ValidatableResponse response = userClient.createUser(userWithoutName);
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(NOT_FULL_DATA_ERROR));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void userCreatedWithoutEmailTest() {
        ValidatableResponse response = userClient.createUser(userWithoutEmail);
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(NOT_FULL_DATA_ERROR));

    }
}


