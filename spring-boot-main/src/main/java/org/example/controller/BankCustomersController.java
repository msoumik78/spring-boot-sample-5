package org.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/1/bank-customers")
public class BankCustomersController {


    @GetMapping
    public String getMessage() {
        System.out.println("Controller called");
        return "hello world";
    }


}
