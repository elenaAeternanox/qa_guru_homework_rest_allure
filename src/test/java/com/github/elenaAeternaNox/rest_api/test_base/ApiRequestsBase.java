package com.github.elenaAeternaNox.rest_api.test_base;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.github.elenaAeternaNox.rest_api.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;

public class ApiRequestsBase {
    public static RequestSpecification reqresRequest = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    public static RequestSpecification booksShopRequest = with()
            .baseUri("https://demoqa.com")
            .filter(customLogFilter().withCustomTemplates())
            .log().all()
            .contentType("application/json")
            .accept("application/json");

    public static ResponseSpecification successResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}
