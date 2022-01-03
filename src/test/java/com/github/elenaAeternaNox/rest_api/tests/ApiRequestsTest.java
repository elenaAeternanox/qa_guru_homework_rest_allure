package com.github.elenaAeternaNox.rest_api.tests;

import com.github.elenaAeternaNox.rest_api.models.reqres.Registr;
import com.github.elenaAeternaNox.rest_api.test_base.ApiRequestsBase;
import io.restassured.RestAssured;
import org.graalvm.compiler.nodes.graphbuilderconf.InvocationPlugins;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiRequestsTest extends ApiRequestsBase {

    private String body;

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void registerSuccessful() {
        String expectedToken = "QpwL5tke4Pnpja7X4";
        int expectedId = 4;
        body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        step("Check API register successful", () -> {
            Registr registration =
                    given()
                            .spec(reqresRequest)
                            .body(body)
                            .when()
                            .post("/register")
                            .then()
                            .spec(successResponseSpec)
                            .extract().as(Registr.class);

           assertEquals(expectedToken, registration.token);
           assertEquals(expectedId, registration.id);
        });
    }

    @Test
    void registerUnsuccessful() {
        String expectedError = "Missing password";
        body = "{ \"email\": \"sydney@fife\" }";

        step("Check API register unsuccessful", () -> {
            Registr registration =
                    given()
                            .spec(reqresRequest)
                            .body(body)
                            .when()
                            .post("/register")
                            .then()
                            .statusCode(400)
                            .extract().as(Registr.class);
           // assertEquals(expectedError, register.getError());
        });
    }

    @Test
    void createUser() {
        body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        step("Check API create user", () -> {
            given()
                    .spec(reqresRequest)
                    .body(body)
                    .when()
                    .post("/users")
                    .then()
                    .statusCode(201)
                    .body("name", is("morpheus"), "job", is("leader"), "id", notNullValue());
        });
    }

    @Test
    void singleUserNotFound() {
        step("Check API user isn't found", () -> {
            given()
                    .when()
                    .get("/api/users/23")
                    .then()
                    .statusCode(404)
                    .body(is("{}"));
        });
    }

    @Test
    void singleResource() {
        step("Check API single resource", () -> {
            given()
                    .when()
                    .get("/api/unknown/2")
                    .then()
                    .spec(successResponseSpec)
                    .body("data.id", is(2), "data.name", is("fuchsia rose"), "data.year", is(2001),
                            "data.color", is("#C74375"), "data.pantone_value", is("17-2031"),
                            "support.url", is("https://reqres.in/#support-heading"),
                            "support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
        });
    }
}
