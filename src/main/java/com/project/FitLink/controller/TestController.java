package com.project.FitLink.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Tag(name = "Protected Controller", description = "Test for Tokens")
public class TestController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> test() {
        return ResponseEntity.ok(Map.of("message", "response from protected controller"));
    }

    // this for test protected apis and will be deleted
}
