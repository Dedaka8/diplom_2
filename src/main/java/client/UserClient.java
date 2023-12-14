package client;

//import data.CourierCredentials;

import data.UserCredentials;
import data.UserData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class UserClient extends RestClient {
    public static final String USER_PATH = "/api/auth/register";
    public static final String DELETE_PATH = "/api/auth/user";

    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String CHANGE_PATH = "/api/auth/user";


    @Step("Отправка POST запроса на создание пользователя на " + USER_PATH)
    public ValidatableResponse createUser(UserData user){
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(USER_PATH)
                .then();

    }

    @Step("Отправка DELETE запроса на удаление пользователя на " + DELETE_PATH)
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(requestSpecification())
                .auth()
                .oauth2(accessToken)
                .and()
                .when()
                .delete(DELETE_PATH)
                .then();

    }

    @Step("Отправка POST запроса на логирование пользователя на " + LOGIN_PATH)
    public ValidatableResponse loginUser(UserCredentials user){
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then();

    }

    @Step("Отправка PATCH запроса на изменение данных пользователя на " + CHANGE_PATH)
    public ValidatableResponse changeUser(UserData user,String accessToken){
        return given()
                .spec(requestSpecification())
                .auth()
                .oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(CHANGE_PATH)
                .then();

    }



}
