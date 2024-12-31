package com.kaung_dev.RestaurantPOS.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/test/greet")
    public String print() {
        return "Hello";
    }

}
