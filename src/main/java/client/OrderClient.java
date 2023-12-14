package client;
import data.OrderData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    public static final String ORDER_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "api/ingredients";


    @Step("Отправка POST запроса на создание заказа на" + ORDER_PATH)
    public ValidatableResponse createOrder(OrderData order, String accessToken){
        return given()
                .spec(requestSpecification())
                .auth()
                .oauth2(accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();

    }

    @Step("Отправка GET запроса на получение заказов пользователя на " + ORDER_PATH)
    public ValidatableResponse getOrders(String accessToken){
        return given()
                .spec(requestSpecification())
                .auth()
                .oauth2(accessToken)
                .and()
                .when()
                .get(ORDER_PATH)
                .then();

    }
    @Step("Отправка GET запроса на получение списка игредиентов на " + INGREDIENTS_PATH)
    public ValidatableResponse getIngredients(){
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(INGREDIENTS_PATH)
                .then();

    }
}