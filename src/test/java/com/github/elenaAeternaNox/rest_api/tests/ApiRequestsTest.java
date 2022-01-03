package com.github.elenaAeternaNox.rest_api.tests;

import com.github.elenaAeternaNox.rest_api.models.reqres.Registr;
import com.github.elenaAeternaNox.rest_api.models.reqres.RegistrationData;
import com.github.elenaAeternaNox.rest_api.models.reqres.Users;
import com.github.elenaAeternaNox.rest_api.models.reqres.single_resource.SingleResource;
import com.github.elenaAeternaNox.rest_api.test_base.ApiRequestsBase;
import io.restassured.RestAssured;
import org.graalvm.compiler.lir.LIRInstruction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiRequestsTest extends ApiRequestsBase {

    private RegistrationData registrationData;

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void registerSuccessful() {
        registrationData = new RegistrationData();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        String expectedToken = "QpwL5tke4Pnpja7X4";
        int expectedId = 4;

        step("Check API register successful", () -> {
            Registr registration =
                    given()
                            .spec(reqresRequest)
                            .body(registrationData)
                            .when()
                            .post("/register")
                            .then()
                            .spec(successResponseSpec)
                            .extract().as(Registr.class);

            assertEquals(expectedToken, registration.getToken());
            assertEquals(expectedId, registration.getId());
        });
    }

    @Test
    void registerUnsuccessful() {
        registrationData = new RegistrationData();
        registrationData.setEmail("sydney@fife");

        String expectedError = "Missing password";
        step("Check API register unsuccessful", () -> {
            Registr registration =
                    given()
                            .spec(reqresRequest)
                            .body(registrationData)
                            .when()
                            .post("/register")
                            .then()
                            .statusCode(400)
                            .extract().as(Registr.class);
            assertEquals(expectedError, registration.getError());
        });
    }

    @Test
    void createUser() {
        Users existUser = new Users();
        existUser.setName("morpheus");
        existUser.setJob("leader");

        String expectedName = "morpheus";
        String expectedJob = "leader";

        step("Check API create user", () -> {
            Users user =
                    given()
                            .spec(reqresRequest)
                            .body(existUser)
                            .when()
                            .post("/users")
                            .then()
                            .statusCode(201)
                            .extract().as(Users.class);

            assertEquals(expectedName, user.getName());
            assertEquals(expectedJob, user.getJob());
            assertNotNull(user.getId());
            assertNotNull(user.getCreatedAt());
        });
    }

    @Test
    void singleUserNotFound() {
        step("Check API user isn't found", () -> {
            given()
                    .spec(reqresRequest)
                    .when()
                    .get("/api/users/23")
                    .then()
                    .statusCode(404)
                    .body(is("{}"));
        });
    }

    @Test
    void singleResource() {
        int expectedId = 2;
        String expectedName = "fuchsia rose";
        int expectedYear = 2001;
        String expectedColor = "#C74375";
        String expectedPantoneValue = "17-2031";
        String expectedUrl = "https://reqres.in/#support-heading";
        String expectedText = "To keep ReqRes free, contributions towards server costs are appreciated!";
        step("Check API single resource", () -> {
            SingleResource singleResource =
                    given()
                            .spec(reqresRequest)
                            .when()
                            .get("/api/unknown/2")
                            .then()
                            .spec(successResponseSpec)
                            .extract().as(SingleResource.class);

            assertEquals(expectedId, singleResource.getData().getId());
            assertEquals(expectedName, singleResource.getData().getName());
            assertEquals(expectedYear, singleResource.getData().getYear());
            assertEquals(expectedColor, singleResource.getData().getColor());
            assertEquals(expectedPantoneValue, singleResource.getData().getPantoneValue());
            assertEquals(expectedUrl, singleResource.getSupport().getUrl());
            assertEquals(expectedText, singleResource.getSupport().getText());
        });
    }

    @Test
    public void checkContainsLastName() {
        given()
                .spec(reqresRequest)
                .when()
                .log().all()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.findAll{it.last_name =~/ds/}.last_name.flatten()",
                        hasItems("Edwards" , "Fields"));
    }
}
