package com.github.elenaAeternaNox.rest_api.models.books_shop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateToken {
    private String status;
    private String result;

}
