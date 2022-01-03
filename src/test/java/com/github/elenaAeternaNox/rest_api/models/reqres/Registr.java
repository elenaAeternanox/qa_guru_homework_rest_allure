package com.github.elenaAeternaNox.rest_api.models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Registr {
    public Integer id;
    public String token;
    public String error;
}
