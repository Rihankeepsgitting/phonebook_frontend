package com.phonedirectory.springboot.phonebook.controller;

import com.phonedirectory.springboot.phonebook.model.BusinessModel;
import com.phonedirectory.springboot.phonebook.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @GetMapping("/all")
    public List<BusinessModel> getAllBusiness(){
        return businessService.getAllBusiness();
    }

    @GetMapping("/{business_id}")
    public List<BusinessModel> getBusiness(@PathVariable("business_id") String business_id){
        return businessService.findByBusinessId(business_id);
    }
}
