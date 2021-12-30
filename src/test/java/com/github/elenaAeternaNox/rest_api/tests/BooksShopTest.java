package com.github.elenaAeternaNox.rest_api.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;

import static com.github.elenaAeternaNox.rest_api.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BooksShopTest extends ApiRequestsBase {

    String userLoginData = "{\"userName\": \"alex\"," +
            "  \"password\": \"asdsad#frew_DFS2\"}";

    @Test
    void noLogsTest() {
        step("Check API books without logs", () -> {
            given()
                    .get("/BookStore/v1/Books")
                    .then()
                    .body("books", hasSize(greaterThan(0)));
        });
    }

    @Test
    void withAllLogsTest() {
        step("Check API books with all logs", () -> {
            given()
                    .log().all()
                    .get("/BookStore/v1/Books")
                    .then()
                    .log().all()
                    .body("books", hasSize(greaterThan(0)));
        });
    }

    @Test
    void withSomeLogsTest() {
        step("Check API books with some logs", () -> {
            given()
                    .log().uri()
                    .log().body()
                    .get("/BookStore/v1/Books")
                    .then()
                    .log().body()
                    .body("books", hasSize(greaterThan(0)));
        });
    }


    @Test
    void authorizeApiTest() {
        step("Check API user's authorize", () -> {
            given()
                    .contentType("application/json")
                    .body(userLoginData)
                    .when()
                    .log().uri()
                    .log().body()
                    .post("/Account/v1/GenerateToken")
                    .then()
                    .log().body()
                    .body("status", is("Success"))
                    .body("result", is("User authorized successfully."));
        });
    }

    @Test
    void authorizeWithListenerTest() {
        step("Check API user's authorize with Listener", () -> {
            given()
                    .filter(new AllureRestAssured())
                    .contentType("application/json")
                    .accept("application/json")
                    .body(userLoginData)
                    .when()
                    .log().uri()
                    .log().body()
                    .post("/Account/v1/GenerateToken")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("status", is("Success"))
                    .body("result", is("User authorized successfully."));
        });
    }

    @Test
    void authorizeWithTemplatesTest() {
        step("Check API user's authorize with custom log filter", () -> {
            given()
                    .filter(customLogFilter().withCustomTemplates())
                    .contentType("application/json")
                    .accept("application/json")
                    .body(userLoginData)
                    .when()
                    .log().uri()
                    .log().body()
                    .post("/Account/v1/GenerateToken")
                    .then()
                    .log().body()
                    .body("status", is("Success"))
                    .body("result", is("User authorized successfully."));
        });
    }

    @Test
    void authorizeWithSpecificationTest() {
        step("Check API user's authorize with specification", () -> {
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
        });
    }
}
