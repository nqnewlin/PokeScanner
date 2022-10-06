package com.nqnewlin.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import java.util.Map;


@RestController
public class HelloWorldController {

    @GetMapping("/Hello")
    public String index() {
        return "Hello";
    }

    @GetMapping("/Greeting")
    public String greeting(@RequestParam(name="name", defaultValue = "World", required = false)String name,
                           @RequestParam(name="lastName", defaultValue = "!", required = false)String last) {
        String greeting = "Hello " + name + " " + last + "!";


        return greeting;
    }
}
