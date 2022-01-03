package com.github.elenaAeternaNox.rest_api.models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Register {
    private Long id;
    private String token;
    private String error;

}
