package org.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/1/bank-customers")
public class BankCustomersController {


    @GetMapping(value = "/{customerName}", produces = {"application/json"})
    public String getCustomerDetails(@PathVariable("customerName") final String bankCustomerName) {
        return "bankCustomersService.getCustomerDetail(bankCustomerName)";
    }


}
