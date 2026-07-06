package com.project.FitLink.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GlobalResponse {
    Map<String,Object> apiResponse = new HashMap<>();

    public void addMessage(String key,Object value){
        apiResponse.put(key,value);
    }
}
