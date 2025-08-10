package com.phonedirectory.springboot.phonebook.controller;

import com.phonedirectory.springboot.phonebook.model.EmployeeModel;
import com.phonedirectory.springboot.phonebook.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/all")
    public List<EmployeeModel> getAllEmployees(){
        return service.getAllEmployees();
    }

    @GetMapping("search/all")
    public List<EmployeeModel> getEmployeeByAll(@RequestParam String query){
        return service.getEmployeeByAll(query);
    }

    @GetMapping("/search/id")
    public List<EmployeeModel> getEmployeeById(@RequestParam String query) {
        return service.getEmployeeById(query);
    }

    @GetMapping("/search/name")
    public List<EmployeeModel> getEmployeeByXname(@RequestParam String query) {
        return service.getEmployeeByXname(query);
    }

    @GetMapping("/search/phone")
    public List<EmployeeModel> getEmployeeByPhone(@RequestParam String query) {
        return service.getEmployeeByPhone(query);
    }

    @GetMapping("/search/email")
    public List<EmployeeModel> getEmployeeByEmail(@RequestParam String query) {
        return service.getEmployeeByEmail(query);
    }

    @GetMapping("/designation/{designation}")
    public List<EmployeeModel> getEmployeeByDesignation(@PathVariable String query) {
        return service.getEmployeeByDesignation(query);
    }
}
