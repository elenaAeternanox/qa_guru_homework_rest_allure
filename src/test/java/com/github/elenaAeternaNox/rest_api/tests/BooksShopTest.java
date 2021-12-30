package com.github.elenaAeternaNox.rest_api.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.elenaAeternaNox.rest_api.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BooksShopTest {

    String userLoginData = "{\"userName\": \"alex\"," +
            "  \"password\": \"asdsad#frew_DFS2\"}";

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://demoqa.com";
    }

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
}
