package data;

import io.qameta.allure.Step;

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Step("Получение данных о пользователе")
    public static UserCredentials from(UserData user){

        return new UserCredentials(user.getEmail(),user.getPassword());
    }
    @Step("Получение данных о пользователе c неверным паролем")
    public static UserCredentials errorPassword(UserData user){

        return new UserCredentials(user.getEmail(),user.getPassword()+"!");
    }
    @Step("Получение данных о пользователе c неверным email")
    public static UserCredentials errorEmail(UserData user){

        return new UserCredentials(user.getEmail()+"!",user.getPassword());
    }

    @Step("Получение данных о пользователе без пароля")
    public static UserCredentials withOutPassword(UserData user){

        return new UserCredentials(user.getEmail(),null);
    }
}
