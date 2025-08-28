package com.phonedirectory.springboot.phonebook.controller;

import com.phonedirectory.springboot.phonebook.model.EmployeeModel;
import com.phonedirectory.springboot.phonebook.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee/{business_id}")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/all")
    public List<EmployeeModel> getAllEmployees(@PathVariable("business_id") String business_id){
        return service.getAllEmployees(business_id);
    }

    @GetMapping("/search/all")
    public List<EmployeeModel> getEmployeeByAll(@PathVariable("business_id") String business_id, @RequestParam String query){
        return service.getEmployeeByAll(query, business_id);
    }

    @GetMapping("/search/id")
    public List<EmployeeModel> getEmployeeById(@PathVariable("business_id") String business_id, @RequestParam String query) {
        return service.getEmployeeById(query, business_id);
    }

    @GetMapping("/search/name")
    public List<EmployeeModel> getEmployeeByXname(@PathVariable("business_id") String business_id, @RequestParam String query) {
        return service.getEmployeeByXname(query, business_id);
    }

    @GetMapping("/search/phone")
    public List<EmployeeModel> getEmployeeByPhone(@PathVariable("business_id") String business_id, @RequestParam String query) {
        return service.getEmployeeByPhone(query, business_id);
    }

    @GetMapping("/search/email")
    public List<EmployeeModel> getEmployeeByEmail(@PathVariable("business_id") String business_id, @RequestParam String query) {
        return service.getEmployeeByEmail(query, business_id);
    }

    @GetMapping("/designation/{designation}")
    public List<EmployeeModel> getEmployeeByDesignation(@PathVariable("business_id") String business_id, @PathVariable String query) {
        return service.getEmployeeByDesignation(query, business_id);
    }
}
