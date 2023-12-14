import client.OrderClient;
import client.UserClient;
import data.OrderData;
import data.OrderGenerator;
import data.UserData;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest {

    public static final String INGREDIENT_IDS_MUST_BE_PROVIDED = "Ingredient ids must be provided";
    private OrderClient orderClient;
    private OrderData order;
    private OrderData orderWithoutIngredients;
    private  OrderData orderWithWrongIngredients;
    private String accessToken;
    private UserClient userClient;
    private UserData user;



    @Before
    public void setUp() {

        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken").toString().substring(7);
        order = OrderGenerator.getRandomOrder(3);
        orderWithoutIngredients = OrderGenerator.getRandomOrder(0);
        orderWithWrongIngredients = OrderGenerator.getOrderWithWrongIngredient();

    }
    @After
    public void cleanUp(){
        if(accessToken != null) {
            userClient.deleteUser(accessToken);

        }

    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем")
    public void authUserCanCreatedOrderTest() {
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.status",equalTo("done"));

    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем без ингредиентов")
    public void authUserCanNotCreatedOrderWithoutIngredientsTest() {
        ValidatableResponse response = orderClient.createOrder(orderWithoutIngredients, accessToken);
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message",equalTo(INGREDIENT_IDS_MUST_BE_PROVIDED));;

    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем c неверным ингредиентом")
    public void authUserCanNotCreatedOrderWithWrongIngredientsTest() {
        ValidatableResponse response = orderClient.createOrder(orderWithWrongIngredients, accessToken);
        response.assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);


    }
    @Test
    @DisplayName("Создание заказа неавторизированным пользователем")
    public void notAuthUserCanNotCreatedOrderTest() {
        ValidatableResponse response = orderClient.createOrder(order, "");
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false));

    }
}
