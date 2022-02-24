package com.github.elenaAeternaNox.rest_api.tests;

import com.github.elenaAeternaNox.rest_api.annotations.Layer;
import com.github.elenaAeternaNox.rest_api.annotations.Microservice;
import com.github.elenaAeternaNox.rest_api.test_base.ApiRequestsBase;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Layer("rest")
@Owner("ekomarova")
@Story("BookShop")
@Feature("BookShopWithSpec")
public class BookShopSpecificationTest extends ApiRequestsBase {

    //Example of user's data without using a model
    String userLoginData = "{\"userName\": \"alex\"," +
            "  \"password\": \"asdsad#frew_DFS2\"}";

    @Microservice("Authorization")
    @Tag("API")
    @DisplayName("Check API user's authorize with specification")
    @Test
    void authorizeWithSpecificationTest() {
            given()
                    .spec(booksShopRequest)
                    .body(userLoginData)
                    .when()
                    .post("/Account/v1/GenerateToken")
                    .then()
                    .log().body()
                    .spec(successResponseSpec)
                    .body("status", is("Success"))
                    .body("result", is("User authorized successfully."));
    }
}
