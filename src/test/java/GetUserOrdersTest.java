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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class GetUserOrdersTest {

    public static final String NOT_AUTH_USER = "You should be authorised";
    private OrderClient orderClient;
    private OrderData order;
    private String accessToken;
    private UserClient userClient;
    private UserData user;



    @Before
    public void setUp() {

        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        accessToken = userClient.createUser(user).extract().path("accessToken").toString().substring(7);
        order = OrderGenerator.getRandomOrder(3);
        orderClient.createOrder(order,accessToken);

    }
    @After
    public void cleanUp(){
        if(accessToken != null) {
            userClient.deleteUser(accessToken);

        }
    }

    @Test
    @DisplayName("Получение заказов авторизированного пользователя")
    public void authUserCanGetOrdersTest() {
        ValidatableResponse response = orderClient.getOrders(accessToken);
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("orders[0].ingredients[0]",equalTo(order.getIngredients()[0]));

    }

    @Test
    @DisplayName("Получение заказов неавторизированного пользователя")
    public void authUserCanNotGetOrdersTest() {
        ValidatableResponse response = orderClient.getOrders("");
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message",equalTo(NOT_AUTH_USER));

    }
}
