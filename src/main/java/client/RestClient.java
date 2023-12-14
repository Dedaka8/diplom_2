package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    protected RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

    }

    protected RequestSpecification requestSpecificationDelete(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .build();

    }
}