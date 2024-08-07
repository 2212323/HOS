package com.example.demo4444.demos.web.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/myFunction")
    public String myFunction() {
        return "Hello, this is the result of myFunction!";
    }
}
