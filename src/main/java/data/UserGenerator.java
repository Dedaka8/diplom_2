package data;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {


    @Step("Создание случайного пользователя")
    public static UserData getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new UserData(email, password, name);
    }

    @Step("Создание случайного пользователя без пароля")
    public static UserData getRandomUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
        String name = RandomStringUtils.randomAlphabetic(10);

        return new UserData(email, null, name);
    }

    @Step("Создание случайного пользователя без email")
    public static UserData getRandomUserWithoutEmail() {

        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserData(null, password, name);
    }

    @Step("Создание случайного пользователя без имени")
    public static UserData getRandomUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(10);

        return new UserData(email, password, null);
    }

}
