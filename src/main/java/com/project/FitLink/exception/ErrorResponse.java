package com.project.FitLink.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    Map<String,String> errorMessages=new HashMap<>();

    public void addError(String field, String message){
        errorMessages.put(field,message);
    }
}
