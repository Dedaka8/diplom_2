package data;

import client.OrderClient;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import java.util.Random;
public class OrderGenerator {



    @Step("Получение случайного игредиента")
    public static String getRandomIngredient(){
        OrderClient orderClient = new OrderClient();
        JsonPath jsonPath = orderClient.getIngredients().extract().jsonPath();
        Random random = new Random();
        int i = random.nextInt(jsonPath.getInt("data.size()"));
        return jsonPath.param("i",i).getString("data[i]._id");
    }

    @Step("Создание случайного заказа")
    public static OrderData getRandomOrder(int totalIngredients){

        String[] order = new String[totalIngredients];
        for(int i=0; i < totalIngredients; i++){
            order[i] = getRandomIngredient();
        }

        return new OrderData(order);
    }

    @Step("Создание заказа с ошибочным ингредиентом")
    public static OrderData getOrderWithWrongIngredient(){

        String[] order = new String[1];
        order[0] = getRandomIngredient() + "ERROR";
        return new OrderData(order);
    }



}
