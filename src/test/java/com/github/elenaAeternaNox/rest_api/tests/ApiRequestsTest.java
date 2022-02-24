package com.github.elenaAeternaNox.rest_api.tests;

import com.github.elenaAeternaNox.rest_api.annotations.Layer;
import com.github.elenaAeternaNox.rest_api.annotations.Microservice;
import com.github.elenaAeternaNox.rest_api.models.reqres.Registr;
import com.github.elenaAeternaNox.rest_api.models.reqres.RegistrationData;
import com.github.elenaAeternaNox.rest_api.models.reqres.Users;
import com.github.elenaAeternaNox.rest_api.models.reqres.single_resource.SingleResource;
import com.github.elenaAeternaNox.rest_api.test_base.ApiRequestsBase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Layer("rest")
@Owner("ekomarova")
@Story("ReqresIn")
@Feature("reqres.in")
@Tag("API")
public class ApiRequestsTest extends ApiRequestsBase {

    private RegistrationData registrationData;

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Microservice("Registration")
    @DisplayName("Check API register successful")
    @Test
    void registerSuccessful() {
        registrationData = new RegistrationData();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        String expectedToken = "QpwL5tke4Pnpja7X4";
        int expectedId = 4;

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
    }

    @Microservice("Registration")
    @DisplayName("Check API register unsuccessful")
    @Test
    void registerUnsuccessful() {
        registrationData = new RegistrationData();
        registrationData.setEmail("sydney@fife");

        String expectedError = "Missing password";

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
    }

    @Microservice("Users")
    @DisplayName("Check API create user")
    @Test
    void createUser() {
        Users existUser = new Users();
        existUser.setName("morpheus");
        existUser.setJob("leader");

        String expectedName = "morpheus";
        String expectedJob = "leader";

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
    }

    @Microservice("Users")
    @DisplayName("Check API user isn't found")
    @Test
    void singleUserNotFound() {
            given()
                    .spec(reqresRequest)
                    .when()
                    .get("/users/23")
                    .then()
                    .statusCode(404)
                    .body(is("{}"));
    }

    @Microservice("Users")
    @DisplayName("Check API single resource")
    @Test
    void singleResource() {
        int expectedId = 2;
        String expectedName = "fuchsia rose";
        int expectedYear = 2001;
        String expectedColor = "#C74375";
        String expectedPantoneValue = "17-2031";
        String expectedUrl = "https://reqres.in/#support-heading";
        String expectedText = "To keep ReqRes free, contributions towards server costs are appreciated!";

            SingleResource singleResource =
                    given()
                            .spec(reqresRequest)
                            .when()
                            .get("/unknown/2")
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
    }

    @Microservice("Users")
    @DisplayName("Check API name = ~/ru/ in LIST <RESOURCE>")
    @Test
    public void checkNameInListResource() {
        given()
                .spec(reqresRequest)
                .when()
                .log().all()
                .get("/unknown")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.findAll{it.name =~/ru/}.name.flatten()",
                        hasItems("cerulean", "true red"));
    }
}
