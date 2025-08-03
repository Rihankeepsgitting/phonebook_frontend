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

    @GetMapping("/search/name/")
    public List<EmployeeModel> getEmployeeByXname(@RequestParam String xname) {
        return service.getEmployeeByXname(xname);
    }

    @GetMapping("/search/id/")
    public List<EmployeeModel> getEmployeeById(@RequestParam String id) {
        return service.getEmployeeById(id);
    }
}
